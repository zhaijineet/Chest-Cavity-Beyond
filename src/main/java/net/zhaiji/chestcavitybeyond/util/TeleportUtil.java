package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * 本工具类的方法基于EnderIO模组的TravelHandler类实现
 * <p>
 * 原始代码使用Unlicense许可证发布
 * </P>
 * 原始代码位置：com.enderio.enderio.content.travel.TravelHandler
 *
 * @author EnderIO开发团队
 * @author zhaijineet
 */
public class TeleportUtil {
    /**
     * @see OrganSkillUtil#randomTeleport
     */
    public static boolean randomTeleport(LivingEntity entity, double ender) {
        Level level = entity.level();
        if (level.isClientSide() || !entity.isAlive() || ender <= 0) return false;
        double distance = MathUtil.getDirectScale(ender);
        double x = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * distance;
        double y = entity.getY() + (entity.getRandom().nextInt((int) distance) - distance / 2);
        double z = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * distance;
        BlockPos.MutableBlockPos mutableblockpos = new BlockPos.MutableBlockPos(x, y, z);
        while (mutableblockpos.getY() > level.getMinBuildHeight() && !level.getBlockState(mutableblockpos).blocksMotion()) {
            mutableblockpos.move(Direction.DOWN);
        }
        BlockState blockstate = level.getBlockState(mutableblockpos);
        boolean flag = blockstate.blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            Optional<Vec3> eventPos = teleportEvent(entity, new Vec3(x, y, z));
            if (eventPos.isPresent()) {
                Vec3 targetPos = eventPos.get();
                Vec3 vec3 = entity.position();
                boolean flag2 = entity.randomTeleport(targetPos.x(), targetPos.y(), targetPos.z(), false);
                if (flag2) onTeleportComplete(entity, level, vec3);
                return flag2;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 主动传送
     */
    public static void teleport(LivingEntity entity, double ender) {
        Level level = entity.level();
        Optional<Vec3> pos = teleportPosition(level, entity, ender);
        if (pos.isPresent()) {
            if (entity instanceof ServerPlayer serverPlayer) {
                Optional<Vec3> eventPos = teleportEvent(entity, pos.get());
                if (eventPos.isPresent()) {
                    Vec3 targetPos = eventPos.get();
                    entity.teleportTo(targetPos.x(), targetPos.y(), targetPos.z());
                    onTeleportComplete(entity, level, targetPos);
                    serverPlayer.connection.resetPosition();
                    entity.resetFallDistance();
                    if (entity.isInWall()) {
                        // 当玩家卡进墙里的时候，改变成游泳动作，预防受伤
                        entity.setPose(Pose.SWIMMING);
                    }
                } else {
                    level.playSound(null, entity.blockPosition(), SoundEvents.DISPENSER_FAIL, entity.getSoundSource());
                }
            }
        }
    }

    /**
     * 传送位置
     */
    private static Optional<Vec3> teleportPosition(Level level, LivingEntity entity, double ender) {
        @Nullable
        BlockPos targetPos = null;
        double floorHeight = 0;

        Vec3 lookAngle = entity.getLookAngle().normalize();
        Vec3 from = entity.getEyePosition();
        Vec3 to = from.add(lookAngle.scale(ender));
        ClipContext clipContext = new ClipContext(
                from,
                to,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                CollisionContext.empty()
        );
        BlockHitResult blockHitResult = level.clip(clipContext);

        if (blockHitResult.getType() == HitResult.Type.MISS) {
            // 视线没有阻挡，返回目标位置
            targetPos = blockHitResult.getBlockPos();
        } else if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            Direction direction = blockHitResult.getDirection();
            if (direction == Direction.UP) {
                // 命中位置为方块上方，返回原定位置
                // 会传送到方块内部，后续通过isTeleportClear调整高度
                // 这个设计依赖于isTeleportClear能正确处理完整方块的高度
                targetPos = blockHitResult.getBlockPos();
            } else if (direction == Direction.DOWN) {
                // 命中位置为方块下方，返回原定位置向下偏移玩家高度，确保不会卡入墙中
                targetPos = blockHitResult.getBlockPos().below((int) Math.ceil(entity.getBbHeight()));
            } else {
                // 命中位置为水平方向，偏移位置到相邻区域
                targetPos = blockHitResult.getBlockPos().offset(direction.getStepX(), 0, direction.getStepZ());
                if (level.getBlockState(targetPos).getCollisionShape(level, targetPos).isEmpty()) {
                    // 如果偏移位置碰撞箱为空，则向下寻找一格
                    targetPos = targetPos.below();
                }
            }
        }
        // 如果目标距离很近，尝试穿墙传送
        if (from.distanceToSqr(blockHitResult.getLocation()) < 9) {
            // 添加少量偏移，确保不会从原位置开始检测
            Vec3 traverseFrom = blockHitResult.getLocation().add(lookAngle.scale(0.01));
            // 由于无法从失败条件返回null，使用一个无效位置作为标记
            BlockPos failPosition = new BlockPos(0, Integer.MAX_VALUE, 0);
            boolean aimingUp = lookAngle.y > 0.5;
            BlockPos newTarget = BlockGetter.traverseBlocks(traverseFrom, to, clipContext,
                    (traverseContext, traversePos) -> {
                        // 首先检查下方，因为玩家更可能想传送到那里
                        if (!aimingUp) {
                            BlockPos checkBelow = traversalCheck(level, traversePos.below());
                            if (checkBelow != null) {
                                return checkBelow;
                            }
                        }
                        return traversalCheck(level, traversePos);
                    }, (failContext) -> failPosition);
            if (newTarget != failPosition) {
                // 多检查一次下方是否为可以传送，防止顶头
                if (traversalCheck(level, newTarget.below()) != null) {
                    newTarget = newTarget.below();
                }
                targetPos = newTarget.immutable();
            }
        }
        if (targetPos != null) {
            Optional<Double> ground = isTeleportPositionClear(level, targetPos.below());
            if (ground.isPresent()) {
                floorHeight = ground.get();
            } else {
                targetPos = null;
            }
        }
        if (targetPos == null
            /* 我需要近距离传送，因为我认为音效和粒子是一个很有用的提醒方式
            || entity.blockPosition().distManhattan(targetPos) < 2*/) {
            return Optional.empty();
        }
        return Optional.of(targetPos.getBottomCenter().add(0, floorHeight, 0));
    }

    /**
     * 是否可以传送
     */
    @Nullable
    private static BlockPos traversalCheck(Level level, BlockPos traversePos) {
        BlockState blockState = level.getBlockState(traversePos);
        var collision = blockState.getCollisionShape(level, traversePos);
        if (collision.isEmpty() && isTeleportPositionClear(level, traversePos.below()).isPresent()) {
            return traversePos;
        }
        return null;
    }

    /**
     * 检查指定位置是否可以传送，并计算玩家应站立的高度
     * <p>
     * 此方法可以将玩家从方块中推上去
     * </P>
     */
    private static Optional<Double> isTeleportPositionClear(Level level, BlockPos target) {
        if (level.isOutsideBuildHeight(target)) {
            return Optional.empty();
        }
        BlockPos above = target.above();
        double height = level.getBlockState(above).getCollisionShape(level, above).max(Direction.Axis.Y);
        if (height <= 0.2d) {
            return Optional.of(Math.max(height, 0));
        }
        above = above.above();
        boolean noCollisionAbove = level.getBlockState(above).getCollisionShape(level, above).isEmpty();
        if (noCollisionAbove) {
            return Optional.of(Math.max(height, 0));
        }
        return Optional.empty();
    }

    /**
     * 传送事件
     */
    private static Optional<Vec3> teleportEvent(LivingEntity entity, Vec3 target) {
        EntityTeleportEvent.EnderEntity event = EventHooks.onEnderTeleport(entity, target.x(), target.y(), target.z());
        if (NeoForge.EVENT_BUS.post(event).isCanceled()) {
            return Optional.empty();
        }
        return Optional.of(new Vec3(event.getTargetX(), event.getTargetY(), event.getTargetZ()));
    }

    /**
     * 传送完成
     * <p>
     * 粒子效果和音效
     * </P>
     */
    private static void onTeleportComplete(LivingEntity entity, Level level, Vec3 pos) {
        level.gameEvent(GameEvent.TELEPORT, pos, GameEvent.Context.of(entity));
        level.broadcastEntityEvent(entity, (byte) 46);
        if (!entity.isSilent()) {
            level.playSound(null, entity.xo, entity.yo, entity.zo, SoundEvents.ENDERMAN_TELEPORT, entity.getSoundSource());
            entity.playSound(SoundEvents.ENDERMAN_TELEPORT);
        }
    }
}
