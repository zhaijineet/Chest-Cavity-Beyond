package net.zhaiji.chestcavitybeyond.compat;

import dev.ryanhcode.sable.Sable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.zhaiji.chestcavitybeyond.util.TeleportUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * 与 Sable 物理结构模组交互的兼容工具。
 */
public class SableCompat {
    /**
     * 命中 sable 结构时，在 plot 空间计算传送目标位置，最后投影到近处返回。
     *
     * @param from           玩家视线起点（近处）
     * @param plotTargetPos  clip 返回的 plot 空间目标
     * @param blockHitResult clip 结果（plot 空间命中点）
     * @param lookAngle      玩家视线方向
     * @param ender          传送距离
     */
    public static Optional<Vec3> teleportSable(
        Level level,
        Vec3 from,
        BlockPos plotTargetPos,
        BlockHitResult blockHitResult,
        Vec3 lookAngle,
        double ender
    ) {
        // 近墙判断：用投影后坐标比较距离
        Vec3 projectedHit = Sable.HELPER.projectOutOfSubLevel(
            level,
            blockHitResult.getBlockPos().getCenter()
        );
        if (from.distanceToSqr(projectedHit) < 9) {
            BlockPos wallResult = wallPenetration(level, blockHitResult, lookAngle, ender);
            if (wallResult != null) {
                plotTargetPos = wallResult;
            }
        }

        // 在 plot 空间检查站立位置
        Optional<Double> floorHeight = TeleportUtil.isTeleportPositionClear(level, plotTargetPos.below());
        if (floorHeight.isEmpty()) {
            return Optional.empty();
        }

        // 投影到近处
        Vec3 projected = Sable.HELPER.projectOutOfSubLevel(level, plotTargetPos.getCenter());
        return Optional.of(projected.add(0.5, floorHeight.get(), 0.5));
    }

    /**
     * 在 plot 空间执行穿墙遍历，找到第一个可站立位置。
     *
     * @return plot 空间新目标，若失败返回 null（调用方保留原目标）
     */
    @Nullable
    private static BlockPos wallPenetration(Level level, BlockHitResult blockHitResult, Vec3 lookAngle, double ender) {
        Vec3 hit = blockHitResult.getLocation();
        Vec3 traverseFrom = hit.add(lookAngle.scale(0.01));
        Vec3 plotTo = hit.add(lookAngle.scale(ender));

        BlockPos failPos = new BlockPos(0, Integer.MAX_VALUE, 0);
        boolean aimingUp = lookAngle.y > 0.5;

        BlockPos result = BlockGetter.traverseBlocks(traverseFrom, plotTo,
            new ClipContext(traverseFrom, plotTo, ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE, CollisionContext.empty()
            ),
            (ctx, pos) -> {
                if (!aimingUp) {
                    BlockPos below = TeleportUtil.traversalCheck(level, pos.below());
                    if (below != null) return below;
                }
                return TeleportUtil.traversalCheck(level, pos);
            },
            ctx -> failPos
        );

        if (result != failPos) {
            if (TeleportUtil.traversalCheck(level, result.below()) != null) {
                result = result.below();
            }
            return result.immutable();
        }
        return null;
    }
}
