package net.zhaiji.chestcavitybeyond.api.function;

import net.minecraft.world.entity.Mob;
import net.zhaiji.chestcavitybeyond.api.goal.GoalCombatContext;
import net.zhaiji.chestcavitybeyond.api.goal.SkillCacheEntry;

/**
 * 权重覆盖函数，用于自定义技能的 Goal 权重计算
 */
@FunctionalInterface
public interface GoalSkillWeightFunction {
    /**
     * 计算此技能在当前态势下的权重
     *
     * @param mob           实体
     * @param combatContext 战斗态势快照
     * @param skillEntry    当前选中的技能缓存条目
     * @return 权重值（≤0 表示不可用）
     */
    double calculate(Mob mob, GoalCombatContext combatContext, SkillCacheEntry skillEntry);
}
