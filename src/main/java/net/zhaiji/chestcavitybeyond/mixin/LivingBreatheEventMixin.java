package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingBreatheEvent.class)
public class LivingBreatheEventMixin {
    @Shadow
    private int consumeAirAmount;

    @Shadow
    private int refillAirAmount;

    @Inject(
        method = "<init>",
        at = @At("RETURN")
    )
    public void chestCavityBeyond$init(
        LivingEntity entity,
        boolean canBreathe,
        int consumeAirAmount,
        int refillAirAmount,
        CallbackInfo ci
    ) {
        this.consumeAirAmount = consumeAirAmount;
        this.refillAirAmount = refillAirAmount;
    }
}
