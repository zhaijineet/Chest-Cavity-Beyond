package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.zhaiji.chestcavitybeyond.util.MixinUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Strider.class)
public abstract class StriderMixin extends Animal {
    public StriderMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * 将熔岩站立逻辑交由 MixinUtil 的 LAVA_WALK 判断处理
     */
    @Inject(
        method = "canStandOnFluid",
        at = @At("HEAD"),
        cancellable = true
    )
    public void chestCavityBeyond$canStandOnFluid(FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(MixinUtil.canStandOnLava(fluidState, this));
    }

    /**
     * 取消原版浮力逻辑，交由 LivingEntityMixin 的 aiStep 注入处理
     */
    @Inject(
        method = "floatStrider",
        at = @At("HEAD"),
        cancellable = true
    )
    public void chestCavityBeyond$floatStrider(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(
        method = "checkFallDamage",
        at = @At("HEAD"),
        cancellable = true
    )
    public void chestCavityBeyond$checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos, CallbackInfo ci) {
        super.checkFallDamage(y, onGround, state, pos);
        ci.cancel();
    }
}
