package net.zhaiji.chestcavitybeyond.api.goal;

/**
 * 带权重的技能缓存条目，用于 Goal 评估时缓存权重计算结果
 *
 * @param candidate 技能缓存条目
 * @param weight    权重值（> 0）
 */
public record WeightedCandidate(SkillCacheEntry candidate, double weight) {
}
