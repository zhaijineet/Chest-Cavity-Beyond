package net.zhaiji.chestcavitybeyond.api.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.capability.Organ;
import net.zhaiji.chestcavitybeyond.api.function.GoalSkillFunction;
import net.zhaiji.chestcavitybeyond.api.function.GoalSkillWeightFunction;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToIntFunction;

/**
 * 技能元数据，声明器官技能如何被 Goal 使用
 */
public final class GoalSkillMetadata {
    /**
     * 空技能元数据
     */
    public static final GoalSkillMetadata EMPTY = new GoalSkillMetadata(
        GoalSkillIntent.NONE,
        GoalSkillTargetRequirement.NONE,
        mob -> false,
        (mob, target) -> 0,
        (mob, target) -> 0,
        null,
        mob -> true,
        null, null, null,
        false,
        false,
        (goalCtx, slotCtx) -> false
    );

    private final GoalSkillIntent intent;
    private final GoalSkillTargetRequirement targetRequirement;
    private final Predicate<Mob> canUse;
    private final ToDoubleBiFunction<Mob, LivingEntity> entityRangeProvider;
    private final ToDoubleBiFunction<Mob, BlockPos> blockRangeProvider;
    private final GoalSkillWeightFunction weightOverride;
    private final Predicate<Mob> entityFilter;
    private final @Nullable Function<Mob, LivingEntity> entityTargetResolver;
    private final @Nullable Function<Mob, BlockPos> blockTargetResolver;
    private final @Nullable ToIntFunction<ChestCavitySlotContext> cooldownProvider;
    private final boolean requireLineOfSight;
    private final boolean useMemoryFallback;
    private final GoalSkillFunction useSkill;

    public GoalSkillMetadata(
        GoalSkillIntent intent,
        GoalSkillTargetRequirement targetRequirement,
        Predicate<Mob> canUse,
        ToDoubleBiFunction<Mob, LivingEntity> entityRangeProvider,
        ToDoubleBiFunction<Mob, BlockPos> blockRangeProvider,
        @Nullable GoalSkillWeightFunction weightOverride,
        Predicate<Mob> entityFilter,
        @Nullable Function<Mob, LivingEntity> entityTargetResolver,
        @Nullable Function<Mob, BlockPos> blockTargetResolver,
        @Nullable ToIntFunction<ChestCavitySlotContext> cooldownProvider,
        boolean requireLineOfSight,
        boolean useMemoryFallback,
        GoalSkillFunction useSkill
    ) {
        this.intent = intent;
        this.targetRequirement = targetRequirement;
        this.canUse = canUse;
        this.entityRangeProvider = entityRangeProvider;
        this.blockRangeProvider = blockRangeProvider;
        this.weightOverride = weightOverride;
        this.entityFilter = entityFilter;
        this.entityTargetResolver = entityTargetResolver;
        this.blockTargetResolver = blockTargetResolver;
        this.cooldownProvider = cooldownProvider;
        this.requireLineOfSight = requireLineOfSight;
        this.useMemoryFallback = useMemoryFallback;
        this.useSkill = useSkill;
    }

    /**
     * 默认实体距离：使用 Goal 敌人检测范围配置值
     */
    public static double defaultRange(Mob mob, LivingEntity target) {
        return ChestCavityBeyondConfig.goalSkillEnemyDetectRange;
    }

    /**
     * 方向型攻击：投射物/射线等，不需要显式目标实体
     */
    public static Builder attack(GoalSkillFunction useSkill) {
        return new Builder(GoalSkillIntent.ATTACK, useSkill)
            .targetRequirement(GoalSkillTargetRequirement.NONE)
            .requireLineOfSight(true);
    }

    /**
     * 需要目标实体的攻击：潜影子弹等
     */
    public static Builder targetedAttack(ToDoubleBiFunction<Mob, LivingEntity> range, GoalSkillFunction useSkill) {
        return new Builder(GoalSkillIntent.ATTACK, useSkill)
            .targetRequirement(GoalSkillTargetRequirement.HAS_ENTITY_TARGET)
            .entityRange(range)
            .requireLineOfSight(true);
    }

    /**
     * 自身 AoE 攻击：自爆等
     */
    public static Builder aoeAttack(GoalSkillFunction useSkill) {
        return new Builder(GoalSkillIntent.ATTACK_AOE, useSkill)
            .targetRequirement(GoalSkillTargetRequirement.NONE);
    }

    /**
     * 防御/逃跑：传送
     */
    public static Builder defense(GoalSkillFunction useSkill) {
        return new Builder(GoalSkillIntent.DEFENSE, useSkill)
            .targetRequirement(GoalSkillTargetRequirement.NONE);
    }

    /**
     * 恢复类：回血
     */
    public static Builder recovery(GoalSkillFunction useSkill) {
        return new Builder(GoalSkillIntent.RECOVERY, useSkill)
            .targetRequirement(GoalSkillTargetRequirement.NONE);
    }

    /**
     * 方块交互：吃草/吃泥土
     */
    public static Builder blockInteract(GoalSkillFunction useSkill) {
        return new Builder(GoalSkillIntent.BLOCK_INTERACT, useSkill)
            .targetRequirement(GoalSkillTargetRequirement.HAS_BLOCK_TARGET)
            .blockRange((mob, target) -> 2.0);
    }

    /**
     * 是否为空技能元数据
     *
     * @return 如果此对象为 EMPTY 返回 true
     */
    public boolean isEmpty() {
        return this == EMPTY;
    }

    public GoalSkillIntent getIntent() {
        return intent;
    }

    public GoalSkillTargetRequirement getTargetRequirement() {
        return targetRequirement;
    }

    public Predicate<Mob> getCanUse() {
        return canUse;
    }

    public ToDoubleBiFunction<Mob, LivingEntity> getEntityRangeProvider() {
        return entityRangeProvider;
    }

    public ToDoubleBiFunction<Mob, BlockPos> getBlockRangeProvider() {
        return blockRangeProvider;
    }

    public @Nullable GoalSkillWeightFunction getWeightOverride() {
        return weightOverride;
    }

    public Predicate<Mob> getEntityFilter() {
        return entityFilter;
    }

    public @Nullable Function<Mob, LivingEntity> getEntityTargetResolver() {
        return entityTargetResolver;
    }

    public @Nullable Function<Mob, BlockPos> getBlockTargetResolver() {
        return blockTargetResolver;
    }

    public @Nullable ToIntFunction<ChestCavitySlotContext> getCooldownProvider() {
        return cooldownProvider;
    }

    /**
     * 是否要求实体能直接看到目标（两者间无方块遮挡）才能使用此技能。
     *
     * @return 为 true 时，目标被遮挡则该技能会被跳过
     */
    public boolean isRequireLineOfSight() {
        return requireLineOfSight;
    }

    public boolean isUseMemoryFallback() {
        return useMemoryFallback;
    }

    public GoalSkillFunction getUseSkill() {
        return useSkill;
    }

    public static class Builder {
        private final GoalSkillIntent intent;
        private final GoalSkillFunction useSkill;
        private GoalSkillTargetRequirement targetRequirement = GoalSkillTargetRequirement.NONE;
        private Predicate<Mob> canUse = mob -> true;
        private ToDoubleBiFunction<Mob, LivingEntity> entityRangeProvider = GoalSkillMetadata::defaultRange;
        private ToDoubleBiFunction<Mob, BlockPos> blockRangeProvider = (mob, target) -> 0;
        private GoalSkillWeightFunction weightOverride = null;
        private Predicate<Mob> entityFilter = mob -> true;
        private @Nullable Function<Mob, LivingEntity> entityTargetResolver = null;
        private @Nullable Function<Mob, BlockPos> blockTargetResolver = null;
        private @Nullable ToIntFunction<ChestCavitySlotContext> cooldownProvider = null;
        private boolean requireLineOfSight = false;
        private boolean useMemoryFallback = false;

        Builder(GoalSkillIntent intent, GoalSkillFunction useSkill) {
            this.intent = intent;
            this.useSkill = useSkill;
        }

        public Builder targetRequirement(GoalSkillTargetRequirement requirement) {
            this.targetRequirement = requirement;
            return this;
        }

        public Builder canUse(Predicate<Mob> predicate) {
            this.canUse = predicate;
            return this;
        }

        /**
         * 设置实体目标射程。仅对 {@link GoalSkillTargetRequirement#HAS_ENTITY_TARGET} 生效，
         * 返回 0 或负数表示不检查距离。
         *
         * @param entityRangeProvider 实体目标射程函数
         */
        public Builder entityRange(ToDoubleBiFunction<Mob, LivingEntity> entityRangeProvider) {
            this.entityRangeProvider = entityRangeProvider;
            return this;
        }

        /**
         * 设置方块目标射程。仅对 {@link GoalSkillTargetRequirement#HAS_BLOCK_TARGET} 生效，
         * 返回 0 或负数表示不检查距离。
         *
         * @param blockRangeProvider 方块目标射程函数
         */
        public Builder blockRange(ToDoubleBiFunction<Mob, BlockPos> blockRangeProvider) {
            this.blockRangeProvider = blockRangeProvider;
            return this;
        }

        public Builder weightOverride(GoalSkillWeightFunction weightOverride) {
            this.weightOverride = weightOverride;
            return this;
        }

        /**
         * 设置实体排除回调，返回 false 的实体会跳过此技能
         * <p>
         * 如：烈焰人不该使用烈焰棒的技能
         * </p>
         * <p>
         * <strong>注意</strong>：此谓词仅在器官变更时执行（缓存重建阶段），
         * 因此不可依赖运行时状态（血量/目标/位置等），只能依赖实体类型等稳定属性。
         * 如需运行时过滤，请使用 {@link #canUse(Predicate)}。
         * </p>
         *
         * @param entityFilter 实体过滤谓词
         */
        public Builder entityFilter(Predicate<Mob> entityFilter) {
            this.entityFilter = entityFilter;
            return this;
        }

        /**
         * 设置自定义实体目标解析，覆盖 {@link GoalSkillTargetResolver#DEFAULT_ENTITY_RESOLVER}
         *
         * @param entityTargetResolver 实体目标解析函数，返回 null 表示无目标
         */
        public Builder entityTargetResolver(Function<Mob, LivingEntity> entityTargetResolver) {
            this.entityTargetResolver = entityTargetResolver;
            return this;
        }

        /**
         * 设置自定义方块目标解析
         *
         * @param blockTargetResolver 方块目标解析函数，返回 null 表示未找到
         */
        public Builder blockTargetResolver(Function<Mob, BlockPos> blockTargetResolver) {
            this.blockTargetResolver = blockTargetResolver;
            return this;
        }

        /**
         * 设置 Goal 专用冷却时间，覆盖 {@link Organ#getCooldownTicks}
         *
         * @param cooldownTicks 冷却时间（tick）
         */
        public Builder cooldown(int cooldownTicks) {
            this.cooldownProvider = context -> cooldownTicks;
            return this;
        }

        /**
         * 设置 Goal 专用动态冷却时间，覆盖 {@link Organ#getCooldownTicks}
         *
         * @param cooldownProvider 冷却时间函数，接收槽位上下文
         */
        public Builder cooldown(ToIntFunction<ChestCavitySlotContext> cooldownProvider) {
            this.cooldownProvider = Objects.requireNonNull(cooldownProvider);
            return this;
        }

        /**
         * 设置是否要求实体能直接看到目标（两者间无方块遮挡）才能使用此技能。
         * <p>
         * 为 true 时，Goal 会通过 {@link net.minecraft.world.entity.ai.sensing.Sensing#hasLineOfSight}
         * 检测实体与目标之间是否存在遮挡，被挡住则跳过此技能。
         * </p>
         * 默认值取决于工厂方法：{@link #attack} / {@link #targetedAttack} 为 true，
         * 其余为 false。可手动覆盖。
         * <p>
         * 仅对 {@link GoalSkillTargetRequirement#HAS_ENTITY_TARGET} 生效。
         * </p>
         *
         * @param requireLineOfSight 是否要求实体与目标间视线无遮挡
         */
        public Builder requireLineOfSight(boolean requireLineOfSight) {
            this.requireLineOfSight = requireLineOfSight;
            return this;
        }

        /**
         * 设置自定义实体目标解析器返回 null 时，是否回退到默认解析 + 目标记忆。
         * <p>
         * 默认 false：自定义 resolver 返回 null 即为最终结果（当前行为）。
         * 设为 true 时，自定义 resolver 返回 null 后会继续走默认 resolver + 记忆层（fallback 到上次记住的目标）。
         * </p>
         *
         * @param useMemoryFallback 是否启用记忆兜底
         */
        public Builder useMemoryFallback(boolean useMemoryFallback) {
            this.useMemoryFallback = useMemoryFallback;
            return this;
        }

        public GoalSkillMetadata build() {
            return new GoalSkillMetadata(
                intent,
                targetRequirement,
                canUse,
                entityRangeProvider,
                blockRangeProvider,
                weightOverride,
                entityFilter,
                entityTargetResolver,
                blockTargetResolver,
                cooldownProvider,
                requireLineOfSight,
                useMemoryFallback,
                useSkill
            );
        }
    }
}
