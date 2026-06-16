package net.zhaiji.chestcavitybeyond.api.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Goal 技能目标解析器（实体 + 方块）
 */
public class GoalSkillTargetResolver {
    /**
     * 默认实体目标解析策略，mob.getTarget() → mob.getLastHurtByMob() → null
     * <p>
     * 有主生物（{@link OwnableEntity}）会自动排除主人作为目标，
     * 避免主人误伤时（{@code getLastHurtByMob} 返回主人）被当作攻击目标。
     * </p>
     */
    public static Function<Mob, @Nullable LivingEntity> DEFAULT_ENTITY_RESOLVER = mob -> {
        LivingEntity target = mob.getTarget();
        if (target == null) {
            target = mob.getLastHurtByMob();
        }
        if (target == null || !target.isAlive() || target.isRemoved()) {
            return null;
        }
        if (isOwnerTarget(mob, target)) {
            return null;
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

    /**
     * 判断目标是否为该实体的主人
     * <p>
     * 有主生物（{@link OwnableEntity}，如狼、猫、马等）不应将主人作为技能目标，
     * 因为主人可能是误伤（{@code getLastHurtByMob} 会等于主人）。
     * </p>
     * 此判断仅作用于默认 resolver 路径，不影响自定义 resolver。
     * 自定义 resolver 的结果由技能自身决定，某些技能（如恢复/防护）可能需要以主人为目标。
     *
     * @param mob    技能使用者
     * @param target 待检测的目标，可为 null
     * @return 若 mob 为有主生物且 target 是其主人则返回 true
     */
    public static boolean isOwnerTarget(Mob mob, @Nullable LivingEntity target) {
        if (target == null || !(mob instanceof OwnableEntity ownable)) return false;
        UUID ownerUuid = ownable.getOwnerUUID();
        return ownerUuid != null && ownerUuid.equals(target.getUUID());
    }
}
