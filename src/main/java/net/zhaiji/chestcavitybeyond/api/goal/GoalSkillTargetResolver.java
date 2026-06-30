package net.zhaiji.chestcavitybeyond.api.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;
import net.zhaiji.chestcavitybeyond.util.EntityRelationUtil;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Goal 技能目标解析器（实体 + 方块）
 */
public class GoalSkillTargetResolver {
    /**
     * 默认实体目标解析策略
     * <p>
     * 解析顺序：先取 mob 主动选择的 target，无效时回退到被动受伤来源 lastHurtByMob。
     * 两条路径统一经 {@link EntityRelationUtil#shouldAoeDamage} 过滤，
     * 同时应用永远友善过滤（自身 / 主人 / 同主宠物）与宠物型 Mob 反击配置。
     * </p>
     * <p>
     * 这样可根治原版 HurtByTargetGoal 在受伤后直接 setTarget 攻击者，
     * 绕过反击配置导致的宠物 / 女仆连锁反击问题。
     * </p>
     * <p>
     * 自定义 resolver 不受此过滤影响，某些技能（如恢复 / 防护）可在自身 resolver 中以主人为目标。
     * </p>
     */
    public static Function<Mob, @Nullable LivingEntity> DEFAULT_ENTITY_RESOLVER = mob -> {
        LivingEntity target = mob.getTarget();
        // 主动 target 路径：经 shouldAoeDamage 过滤（永远友善 + 宠物型 Mob 反击配置）
        if (target != null) {
            if (!target.isAlive() || target.isRemoved()) {
                target = null;
            } else if (!EntityRelationUtil.shouldAoeDamage(mob, target)) {
                target = null;
            }
        }
        // 被动反击路径：lastHurtByMob 才是"被攻击后反击"的来源
        if (target == null) {
            target = mob.getLastHurtByMob();
            if (target == null || !target.isAlive() || target.isRemoved()) {
                return null;
            }
            if (!EntityRelationUtil.shouldAoeDamage(mob, target)) {
                return null;
            }
        }
        return target;
    };

    /**
     * 默认方块目标解析策略：实体脚所在层与脚底层各 3×3 范围搜索匹配 filter 的方块
     * <p>
     * 脚所在层（dy=0）覆盖草丛等可穿过方块，脚底层（dy=-1）覆盖地面方块。
     * </p>
     */
    public static BiFunction<Mob, Predicate<BlockState>, @Nullable BlockPos> DEFAULT_BLOCK_RESOLVER = (mob, filter) -> {
        BlockPos origin = mob.blockPosition();
        for (int dy = -1; dy <= 0; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos pos = origin.offset(dx, dy, dz);
                    BlockState state = mob.level().getBlockState(pos);
                    if (filter.test(state)) {
                        return pos;
                    }
                }
            }
        }
        return null;
    };
}
