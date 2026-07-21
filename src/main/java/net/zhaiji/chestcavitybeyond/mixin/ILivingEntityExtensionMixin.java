package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import net.neoforged.neoforge.fluids.FluidType;
import net.zhaiji.chestcavitybeyond.util.MixinUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ILivingEntityExtension.class)
public interface ILivingEntityExtensionMixin {
    @Shadow
    LivingEntity self();

    /**
     * 熔岩中跳跃时按 LAVA_SWIM_SPEED 的平方根增强向上脉冲，替代原版固定 0.04
     */
    @Inject(
        method = "jumpInFluid(Lnet/neoforged/neoforge/fluids/FluidType;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void chestCavityBeyond$jumpInFluid(FluidType type, CallbackInfo ci) {
        if (MixinUtil.applyLavaJumpImpulse(self(), type)) {
            ci.cancel();
        }
    }

    /**
     * 熔岩中下沉时按 LAVA_SWIM_SPEED 的平方根增强向下脉冲，替代原版固定 -0.04
     */
    @Inject(
        method = "sinkInFluid(Lnet/neoforged/neoforge/fluids/FluidType;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void chestCavityBeyond$sinkInFluid(FluidType type, CallbackInfo ci) {
        if (MixinUtil.applyLavaSinkImpulse(self(), type)) {
            ci.cancel();
        }
    }

    /**
     * 免疫熔岩伤害时允许在熔岩中游泳（含游泳姿态与下沉），打通原版因 canSwim(false) 被阻断的调用链
     */
    @Inject(
        method = "canSwimInFluidType(Lnet/neoforged/neoforge/fluids/FluidType;)Z",
        at = @At("RETURN"),
        cancellable = true
    )
    private void chestCavityBeyond$canSwimInFluidType(FluidType type, CallbackInfoReturnable<Boolean> cir) {
        if (MixinUtil.canSwimInLava(self(), type, cir.getReturnValue())) {
            cir.setReturnValue(true);
        }
    }
}
