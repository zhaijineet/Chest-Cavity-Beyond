package net.zhaiji.chestcavitybeyond.api.goal;

/**
 * 目标需求枚举，用于 Goal 评估时过滤
 */
public enum GoalSkillTargetRequirement {
    /**
     * 不需要目标（大部分技能）
     */
    NONE,
    /**
     * 需要解析到实体目标（激光/潜影子弹等）
     */
    HAS_ENTITY_TARGET,
    /**
     * 需要解析到方块目标（吃草/吃泥土等）
     */
    HAS_BLOCK_TARGET
}
