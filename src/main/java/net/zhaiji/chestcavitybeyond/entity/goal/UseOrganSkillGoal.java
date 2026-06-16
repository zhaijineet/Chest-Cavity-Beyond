package net.zhaiji.chestcavitybeyond.entity.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.capability.IOrgan;
import net.zhaiji.chestcavitybeyond.api.function.GoalSkillWeightFunction;
import net.zhaiji.chestcavitybeyond.api.goal.GoalCombatContext;
import net.zhaiji.chestcavitybeyond.api.goal.GoalSkillIntent;
import net.zhaiji.chestcavitybeyond.api.goal.GoalSkillMetadata;
import net.zhaiji.chestcavitybeyond.api.goal.GoalSkillTargetRequirement;
import net.zhaiji.chestcavitybeyond.api.goal.GoalSkillTargetResolver;
import net.zhaiji.chestcavitybeyond.api.goal.SkillCacheEntry;
import net.zhaiji.chestcavitybeyond.api.goal.WeightedCandidate;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.OrganSkillUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * 统一 Goal 器官技能决策 Goal
 * <p>
 * 每 N tick 评估胸腔中所有带 {@link GoalSkillMetadata} 的器官，
 * 根据战场态势动态计算权重，加权随机选择一个技能执行。
 * </p>
 */
public class UseOrganSkillGoal extends Goal {
    private final Mob mob;
    /**
     * 静态候选缓存：仅在器官变化时重建
     */
    private final List<SkillCacheEntry> cachedCandidates = new ArrayList<>();
    /**
     * start() 重试候选列表：canUse() 中通过过滤且权重 > 0 的候选（排除首选）
     */
    private final List<SkillCacheEntry> availableCandidates = new ArrayList<>();
    /**
     * 评估触发偏移量：分散各 Mob 的评估时机，避免集中判断
     */
    private int evalOffset;
    /**
     * 上次使用的评估间隔配置值，用于检测配置变更后重建随机偏移
     */
    private int lastEvalInterval = -1;

    private GoalSkillIntent lastUsedIntent = GoalSkillIntent.NONE;
    @Nullable
    private SkillCacheEntry selectedCandidate = null;

    /**
     * 目标记忆：resolver 无目标时尝试用上次记住的目标
     */
    @Nullable
    private LivingEntity rememberedTarget;

    private int rememberedTargetTick;

    /**
     * 缓存的器官变更计数
     */
    private int cachedChangeCount = -1;

    public UseOrganSkillGoal(Mob mob) {
        this.mob = mob;
        initEvalOffset(resolveInterval());
    }

    /**
     * 解析当前配置的评估间隔，强制为偶数。
     * <p>
     * Mob.serverAiStep() 仅在 (tickCount + entityId) 为偶数时完整 tick（调用 canUse），
     * 因此 interval 必须为偶数，否则奇数 interval 会导致匹配 tick 的奇偶性交替，
     * 实际评估间隔翻倍（配置值 × 2）。
     * </p>
     *
     * @return 调整后的偶数间隔
     */
    private int resolveInterval() {
        int interval = Math.max(2, ChestCavityBeyondConfig.goalSkillEvalInterval);
        if (interval % 2 != 0) interval++;
        return interval;
    }

    /**
     * 初始化评估触发偏移量（基于当前配置间隔随机化）
     *
     * @param interval 已解析的偶数评估间隔
     */
    private void initEvalOffset(int interval) {
        evalOffset = mob.getRandom().nextInt(interval);
        // evalOffset 必须与 entityId 同奇偶，确保 modulo 命中时恰好是 canUse 被调用的 tick
        if (evalOffset % 2 != mob.getId() % 2) {
            evalOffset = (evalOffset + 1) % interval;
        }
        lastEvalInterval = interval;
    }

    @Override
    public boolean canUse() {
        if (!ChestCavityBeyondConfig.enableMobGoalSkill) return false;
        if (mob.level().isClientSide()) return false;

        // 检测评估间隔配置变更，变更时重建随机偏移
        int interval = resolveInterval();
        if (interval != lastEvalInterval) {
            initEvalOffset(interval);
        }

        // 间隔触发：modulo + 随机偏移分散所有 Mob 的评估时间，避免同步抖动
        if (mob.tickCount % interval != evalOffset) {
            cleanupRememberedTarget();
            return false;
        }

        ChestCavityData data = ChestCavityUtil.getData(mob);

        // 仅在器官变更计数变化时重建静态候选缓存
        if (cachedChangeCount != data.getOrganChangeCount()) {
            rebuildCache(data);
        }

        // 没有 Goal 技能器官的 Mob 直接返回
        if (cachedCandidates.isEmpty()) {
            cleanupRememberedTarget();
            selectedCandidate = null;
            return false;
        }

        // 预计算不变字段
        double selfHealthPercent = mob.getHealth() / Math.max(0.01, mob.getMaxHealth());
        int nearbyEnemyCount = countNearbyEnemies();

        // 默认目标 + 默认上下文
        LivingEntity defaultTarget = resolveTargetWithMemory(GoalSkillTargetResolver.DEFAULT_ENTITY_RESOLVER);
        GoalCombatContext defaultContext = buildCombatContext(defaultTarget, null, selfHealthPercent, nearbyEnemyCount);

        // 动态过滤 + 权重计算
        List<WeightedCandidate> weightedCandidates = new ArrayList<>();
        for (SkillCacheEntry candidate : cachedCandidates) {
            GoalSkillMetadata metadata = candidate.metadata();
            if (OrganSkillUtil.hasCooldown(mob, candidate.stack())) continue;
            if (!metadata.getCanUse().test(mob)) continue;

            // 按目标需求分支过滤 + 确定战斗上下文
            GoalCombatContext combatContext;
            switch (metadata.getTargetRequirement()) {
                case HAS_ENTITY_TARGET -> {
                    // 无自定义 resolver 时直接复用循环前预计算的 defaultTarget，避免重复执行 resolveTargetWithMemory
                    LivingEntity effectiveTarget;
                    if (metadata.getEntityTargetResolver() != null) {
                        effectiveTarget = resolveEntityTarget(metadata);
                    } else {
                        effectiveTarget = defaultTarget;
                    }
                    if (effectiveTarget == null) continue;
                    // 射程过滤
                    double entityRange = metadata.getEntityRangeProvider().applyAsDouble(mob, effectiveTarget);
                    if (entityRange > 0 && mob.distanceTo(effectiveTarget) > entityRange) continue;
                    // 视线过滤（复用 context 已计算的 LOS 避免重复检测）
                    if (metadata.isRequireLineOfSight()) {
                        boolean hasLos = effectiveTarget == defaultTarget
                                         ? defaultContext.hasLineOfSight()
                                         : mob.getSensing().hasLineOfSight(effectiveTarget);
                        if (!hasLos) continue;
                    }
                    // 用 effectiveTarget 构建 per-candidate context（相同引用则复用 defaultContext，避免重算）
                    combatContext = effectiveTarget == defaultTarget
                                    ? defaultContext
                                    : buildCombatContext(effectiveTarget, null, selfHealthPercent, nearbyEnemyCount);
                }
                case HAS_BLOCK_TARGET -> {
                    BlockPos blockTarget = null;
                    Function<Mob, BlockPos> blockResolver = metadata.getBlockTargetResolver();
                    if (blockResolver != null) {
                        blockTarget = blockResolver.apply(mob);
                        if (blockTarget == null) continue;
                        double blockRange = metadata.getBlockRangeProvider().applyAsDouble(mob, blockTarget);
                        if (blockRange > 0 && !mob.blockPosition().closerThan(blockTarget, blockRange)) continue;
                    }
                    combatContext = buildCombatContext(defaultTarget, blockTarget, selfHealthPercent, nearbyEnemyCount);
                }
                default -> combatContext = defaultContext;
            }

            double weight = calculateWeight(metadata, combatContext);
            if (weight > 0) {
                weightedCandidates.add(new WeightedCandidate(candidate, weight));
            }
        }
        if (weightedCandidates.isEmpty()) {
            selectedCandidate = null;
            return false;
        }

        selectedCandidate = weightedRandomSelect(weightedCandidates);
        if (selectedCandidate == null) return false;
        // 按权重降序保存候选（排除首选）用于 start() 重试，使重试优先尝试次优候选，权重相同时保持原顺序
        weightedCandidates.sort((a, b) -> Double.compare(b.weight(), a.weight()));
        availableCandidates.clear();
        for (WeightedCandidate weightedCandidate : weightedCandidates) {
            if (weightedCandidate.candidate() == selectedCandidate) continue;
            availableCandidates.add(weightedCandidate.candidate());
        }
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        // 预计算不变字段，避免重试链重复调用
        double selfHealthPercent = mob.getHealth() / Math.max(0.01, mob.getMaxHealth());
        int nearbyEnemyCount = countNearbyEnemies();
        ChestCavityData data = ChestCavityUtil.getData(mob);

        // 先尝试首选候选（加权随机选出）
        if (selectedCandidate != null && tryUseCandidate(selectedCandidate, data, selfHealthPercent, nearbyEnemyCount)) {
            selectedCandidate = null;
            availableCandidates.clear();
            return;
        }
        // 首选失败（或为 null），遍历其余候选重试
        for (SkillCacheEntry candidate : availableCandidates) {
            if (tryUseCandidate(candidate, data, selfHealthPercent, nearbyEnemyCount)) {
                break;
            }
        }
        selectedCandidate = null;
        availableCandidates.clear();
    }

    /**
     * 解析目标（实体/方块）→ null 守卫 → 执行技能 → 成功时设置冷却/刷新记忆/更新 lastUsedIntent。
     *
     * @param candidate 要尝试的候选
     * @param data      胸腔数据（由 start() 预计算传入，避免重试链重复查询）
     * @return 技能是否执行成功
     */
    private boolean tryUseCandidate(SkillCacheEntry candidate, ChestCavityData data, double selfHealthPercent, int nearbyEnemyCount) {
        GoalSkillMetadata metadata = candidate.metadata();

        // 解析实体目标
        LivingEntity entityTarget = resolveEntityTarget(metadata);

        // 目标 null 守卫：HAS_ENTITY_TARGET 技能在目标最终为 null 时跳过执行，避免下游 NPE
        if (entityTarget == null && metadata.getTargetRequirement() == GoalSkillTargetRequirement.HAS_ENTITY_TARGET) {
            return false;
        }

        // 解析方块目标
        BlockPos blockTarget = metadata.getBlockTargetResolver() != null
                               ? metadata.getBlockTargetResolver().apply(mob)
                               : null;

        GoalCombatContext ctx = buildCombatContext(entityTarget, blockTarget, selfHealthPercent, nearbyEnemyCount);

        ChestCavitySlotContext slotContext = ChestCavityUtil.createContext(data, candidate.slot(), candidate.stack());

        boolean success = metadata.getUseSkill().useSkill(ctx, slotContext);
        if (!success) return false;

        // 技能成功且有目标 → 刷新记忆，被动生物的 lastHurtByMob 过期后，靠持续攻击保持记忆不超时
        if (entityTarget != null && entityTarget.isAlive() && !entityTarget.isRemoved()) {
            rememberedTarget = entityTarget;
            rememberedTargetTick = mob.tickCount;
        }
        int cooldown;
        if (metadata.getCooldownProvider() != null) {
            cooldown = metadata.getCooldownProvider().applyAsInt(slotContext);
        } else {
            cooldown = ChestCavityUtil.getOrganCap(candidate.stack()).getCooldownTicks(slotContext);
        }
        if (cooldown > 0) {
            OrganSkillUtil.addCooldown(mob, candidate.stack(), cooldown);
        }
        // lastUsedIntent 只在成功时更新，避免失败的技能也触发重复惩罚
        lastUsedIntent = metadata.getIntent();
        return true;
    }

    /**
     * 外部事件（Mob 受伤）主动刷新目标记忆
     *
     * @param target    攻击者
     * @param tickCount 当前 tick
     */
    public void refreshTargetMemory(LivingEntity target, int tickCount) {
        if (target != null && target.isAlive() && !target.isRemoved() && !GoalSkillTargetResolver.isOwnerTarget(mob, target)) {
            this.rememberedTarget = target;
            this.rememberedTargetTick = tickCount;
        }
    }

    /**
     * 清理过期的目标记忆（独立于目标解析调用）
     */
    private void cleanupRememberedTarget() {
        if (rememberedTarget == null) return;
        if (
            !rememberedTarget.isAlive()
            || rememberedTarget.isRemoved()
            || mob.tickCount - rememberedTargetTick > ChestCavityBeyondConfig.goalSkillTargetMemoryTicks) {
            rememberedTarget = null;
        }
    }

    /**
     * 带记忆的目标解析：resolver 有有效目标时刷新记忆，无目标时尝试用记忆 fallback
     *
     * @param resolver 即时目标解析策略（通常为 {@link GoalSkillTargetResolver#DEFAULT_ENTITY_RESOLVER}）
     * @return 解析到的目标实体，null 表示无目标
     */
    private @Nullable LivingEntity resolveTargetWithMemory(Function<Mob, @Nullable LivingEntity> resolver) {
        LivingEntity immediate = resolver.apply(mob);

        if (immediate != null && immediate.isAlive() && !immediate.isRemoved()) {
            // resolver 有有效目标 → 刷新记忆
            rememberedTarget = immediate;
            rememberedTargetTick = mob.tickCount;
            return immediate;
        }

        // resolver 无目标 → 尝试记忆 fallback
        cleanupRememberedTarget();
        // 可能为 null
        return rememberedTarget;
    }

    /**
     * 解析技能的有效实体目标。
     *
     * @param metadata 技能元数据
     * @return 有效目标实体，null 表示无目标
     */
    private @Nullable LivingEntity resolveEntityTarget(GoalSkillMetadata metadata) {
        Function<Mob, @Nullable LivingEntity> customResolver = metadata.getEntityTargetResolver();
        if (customResolver != null) {
            LivingEntity custom = customResolver.apply(mob);
            if (custom != null && custom.isAlive() && !custom.isRemoved()) {
                return custom;
            }
            // resolver 返回 null 或无效实体 → 根据 useMemoryFallback 决定是否回退
            if (!metadata.isUseMemoryFallback()) return null;
        }
        // 默认 resolver 或 fallback：经过记忆层
        return resolveTargetWithMemory(GoalSkillTargetResolver.DEFAULT_ENTITY_RESOLVER);
    }

    /**
     * 重建静态候选缓存
     */
    private void rebuildCache(ChestCavityData data) {
        cachedCandidates.clear();
        for (int i = 0; i < data.getSlots(); i++) {
            ItemStack stack = data.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            IOrgan organ = ChestCavityUtil.getOrganCap(stack);
            GoalSkillMetadata metadata = organ.getGoalSkillMetadata();
            if (metadata.isEmpty()) continue;
            // entityFilter 在构建缓存时就过滤（只依赖 mob 类型，不依赖运行时状态）
            if (!metadata.getEntityFilter().test(mob)) continue;
            cachedCandidates.add(new SkillCacheEntry(i, stack, metadata));
        }
        cachedChangeCount = data.getOrganChangeCount();
    }

    private GoalCombatContext buildCombatContext(@Nullable LivingEntity target, @Nullable BlockPos blockTarget, double selfHealthPercent, int nearbyEnemyCount) {
        double distance = target != null ? mob.distanceTo(target) : -1;
        boolean hasLineOfSight = target != null && mob.getSensing().hasLineOfSight(target);
        double targetHealthPercent = target != null ? target.getHealth() / Math.max(0.01, target.getMaxHealth()) : 0;

        return new GoalCombatContext(
            target,
            blockTarget,
            distance,
            hasLineOfSight,
            selfHealthPercent,
            targetHealthPercent,
            nearbyEnemyCount,
            lastUsedIntent
        );
    }

    private int countNearbyEnemies() {
        double range = ChestCavityBeyondConfig.goalSkillEnemyDetectRange;
        AABB searchBox = mob.getBoundingBox().inflate(range);
        return mob.level().getEntitiesOfClass(
            LivingEntity.class,
            searchBox,
            livingEntity -> livingEntity != mob && livingEntity.isAlive()
                 && (
                     livingEntity == mob.getTarget()
                     || livingEntity == mob.getLastHurtByMob()
                     || livingEntity == rememberedTarget
                     || (livingEntity instanceof Mob m && m.getTarget() == mob)
                 )
        ).size();
    }

    @Nullable
    private SkillCacheEntry weightedRandomSelect(List<WeightedCandidate> weightedCandidates) {
        NavigableMap<Double, SkillCacheEntry> weightedMap = new TreeMap<>();
        double totalWeight = 0;

        for (WeightedCandidate wc : weightedCandidates) {
            totalWeight += wc.weight();
            weightedMap.put(totalWeight, wc.candidate());
        }

        if (totalWeight <= 0) return null;
        double roll = mob.getRandom().nextDouble() * totalWeight;
        Map.Entry<Double, SkillCacheEntry> entry = weightedMap.ceilingEntry(roll);
        return entry != null ? entry.getValue() : null;
    }

    private double calculateWeight(GoalSkillMetadata metadata, GoalCombatContext goalCombatContext) {
        GoalSkillWeightFunction weightOverride = metadata.getWeightOverride();
        if (weightOverride != null) {
            return Math.max(0, weightOverride.calculate(mob, goalCombatContext));
        }

        boolean hasThreat = goalCombatContext.target() != null || goalCombatContext.nearbyEnemyCount() > 0;

        switch (metadata.getIntent()) {
            case ATTACK:
                if (!hasThreat) return 0;
                return 80
                       + (goalCombatContext.hasLineOfSight() ? 20 : -40)
                       + (goalCombatContext.target() != null ? (1 - goalCombatContext.targetHealthPercent()) * 40 : 0)
                       + (goalCombatContext.lastUsedIntent() == GoalSkillIntent.ATTACK ? -30 : 0);

            case ATTACK_AOE:
                if (goalCombatContext.nearbyEnemyCount() == 0) return 0;
                return 60
                       + goalCombatContext.nearbyEnemyCount() * 15
                       + (goalCombatContext.target() != null && goalCombatContext.distanceToTarget() < 4 ? 20 : 0)
                       + (goalCombatContext.lastUsedIntent() == GoalSkillIntent.ATTACK_AOE ? -40 : 0);

            case DEFENSE:
                if (!hasThreat) return 0;
                double healthFactor = 1 - goalCombatContext.selfHealthPercent();
                return 1
                       + healthFactor * healthFactor * healthFactor * 200
                       + healthFactor * (goalCombatContext.target() != null ? goalCombatContext.targetHealthPercent() * 30 : 0)
                       + (goalCombatContext.lastUsedIntent() == GoalSkillIntent.DEFENSE ? -50 : 0);

            case RECOVERY:
                // 血量越低惩罚越小（低血量时即使危险也该恢复）
                return -15
                       + (1 - goalCombatContext.selfHealthPercent()) * 200
                       + (goalCombatContext.nearbyEnemyCount() > 0 && goalCombatContext.target() != null && goalCombatContext.distanceToTarget() < 5
                          ? -goalCombatContext.selfHealthPercent() * 50 : 0)
                       + (goalCombatContext.lastUsedIntent() == GoalSkillIntent.RECOVERY ? -50 : 0);

            case BLOCK_INTERACT:
                return hasThreat ? 2 : 10;

            case NONE:
            default:
                return 0;
        }
    }
}
