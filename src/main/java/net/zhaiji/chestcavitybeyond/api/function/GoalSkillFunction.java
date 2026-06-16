package net.zhaiji.chestcavitybeyond.api.function;

import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.goal.GoalCombatContext;

/**
 * Goal 技能使用函数
 */
@FunctionalInterface
public interface GoalSkillFunction {
    /**
     * Goal 技能使用
     *
     * @param ctx         战斗态势上下文（含解析到的实体/方块目标）
     * @param slotContext 胸腔槽位上下文
     * @return true 表示使用成功需加冷却，false 表示未使用不加冷却
     */
    boolean useSkill(GoalCombatContext ctx, ChestCavitySlotContext slotContext);
}
