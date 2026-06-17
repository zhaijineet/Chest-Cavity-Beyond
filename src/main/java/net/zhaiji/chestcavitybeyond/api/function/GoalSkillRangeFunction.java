package net.zhaiji.chestcavitybeyond.api.function;

import net.minecraft.world.entity.Mob;
import net.zhaiji.chestcavitybeyond.api.goal.SkillCacheEntry;

/**
 * Goal 技能射程函数，用于计算实体/方块目标的射程
 *
 * @param <T> 目标类型（LivingEntity 或 BlockPos）
 */
@FunctionalInterface
public interface GoalSkillRangeFunction<T> {
    /**
     * 计算技能对目标的射程
     *
     * @param mob        实体
     * @param target     目标（实体或方块坐标）
     * @param skillEntry 当前选中的技能缓存条目（含槽位、物品栈、技能元数据）
     * @return 射程值（≤0 表示不检查距离）
     */
    double apply(Mob mob, T target, SkillCacheEntry skillEntry);
}
