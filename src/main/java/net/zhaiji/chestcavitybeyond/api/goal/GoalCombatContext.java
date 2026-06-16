package net.zhaiji.chestcavitybeyond.api.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

/**
 * 战斗态势快照，由 Goal 构建一次，同时用于权重计算和技能执行
 *
 * @param target              解析到的实体目标（可能为 null）
 * @param blockTarget         解析到的方块目标（可能为 null）
 * @param distanceToTarget    到目标距离（无目标时 -1）
 * @param hasLineOfSight      是否有视线
 * @param selfHealthPercent   自身血量百分比 0~1
 * @param targetHealthPercent 目标血量百分比 0~1（无目标时 0）
 * @param nearbyEnemyCount    周围威胁实体数量
 * @param lastUsedIntent      上次使用的 intent（防重复）
 */
public record GoalCombatContext(
    @Nullable LivingEntity target,
    @Nullable BlockPos blockTarget,
    double distanceToTarget,
    boolean hasLineOfSight,
    double selfHealthPercent,
    double targetHealthPercent,
    int nearbyEnemyCount,
    GoalSkillIntent lastUsedIntent
) {
}
