package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.register.InitItem;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowGolem.class)
public abstract class SnowGolemMixin extends AbstractGolem {
    protected SnowGolemMixin(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * 没有雪之心就禁止发射雪球
     */
    @Inject(
            method = "performRangedAttack",
            at = @At("HEAD"),
            cancellable = true
    )
    public void chestCavityBeyond$performRangedAttack(LivingEntity target, float distanceFactor, CallbackInfo ci) {
        if (!ChestCavityUtil.getData(this).hasOrgan(InitItem.SNOW_CORE.get())) {
            ci.cancel();
        }
    }
}
