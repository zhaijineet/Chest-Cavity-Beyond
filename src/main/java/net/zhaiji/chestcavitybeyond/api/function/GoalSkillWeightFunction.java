package net.zhaiji.chestcavitybeyond.api.function;

import net.minecraft.world.entity.Mob;
import net.zhaiji.chestcavitybeyond.api.goal.GoalCombatContext;

/**
 * 权重覆盖函数，用于自定义技能的 Goal 权重计算
 */
@FunctionalInterface
public interface GoalSkillWeightFunction {
    /**
     * 计算此技能在当前态势下的权重
     *
     * @param mob     实体
     * @param goalCombatContext 战斗态势快照
     * @return 权重值（≤0 表示不可用）
     */
    double calculate(Mob mob, GoalCombatContext goalCombatContext);
}
