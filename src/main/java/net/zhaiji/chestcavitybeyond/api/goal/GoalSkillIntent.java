package net.zhaiji.chestcavitybeyond.api.goal;

/**
 * 技能意图分类，决定 Goal 权重计算公式
 */
public enum GoalSkillIntent {
    /**
     * 无技能意图，用于空技能元数据和初始状态
     */
    NONE,
    /**
     * 方向型攻击（投射物/射线/方向AoE）：火球、雪球、凋零头、音爆、龙息弹…
     */
    ATTACK,
    /**
     * 自身为中心范围攻击：自爆
     */
    ATTACK_AOE,
    /**
     * 防御/逃跑：传送
     */
    DEFENSE,
    /**
     * 恢复/清除：回血
     */
    RECOVERY,
    /**
     * 方块交互：吃草、吃泥土
     */
    BLOCK_INTERACT
}
