package net.zhaiji.chestcavitybeyond.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.zhaiji.chestcavitybeyond.register.InitEntityType;

public class ThrownCobweb extends ThrowableItemProjectile {
    private boolean isTrigger = false;

    public ThrownCobweb(EntityType<ThrownCobweb> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownCobweb(double x, double y, double z, Level level) {
        super(InitEntityType.THROWN_COBWEB.get(), x, y, z, level);
    }

    public ThrownCobweb(LivingEntity shooter, Level level) {
        super(InitEntityType.THROWN_COBWEB.get(), shooter, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide() && isInWaterOrBubble()) {
            // 在水中删除自身
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide()) {
            if (!isTrigger) {
                // 没触发时，原地生成蛛网掉落物
                spawnAtLocation(getItem());
            }
            // 击中就删除自身
            this.discard();
        }
    }

    /**
     * 击中实体时，在其脚下放置蜘蛛网
     */
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide()) {
            BlockPos pos = result.getEntity().getOnPos().above();
            BlockState state = level().getBlockState(pos);
            if (state.isAir() && getDefaultItem() instanceof BlockItem item) {
                level().setBlockAndUpdate(pos, item.getBlock().defaultBlockState());
                isTrigger = true;
            }
        }
    }

    /**
     * 击中方块时，在其击中面放置蜘蛛网
     */
    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!level().isClientSide()) {
            BlockPos pos = result.getBlockPos().offset(result.getDirection().getNormal());
            if (level().getBlockState(pos).isAir() && getDefaultItem() instanceof BlockItem item) {
                level().setBlockAndUpdate(pos, item.getBlock().defaultBlockState());
                isTrigger = true;
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return Items.COBWEB;
    }
}
