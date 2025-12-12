package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.world.entity.animal.WaterAnimal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterAnimal.class)
public abstract class WaterAnimalMixin {
    @Inject(
            method = "handleAirSupply",
            at = @At("HEAD"),
            cancellable = true
    )
    public void chestCavityBeyond$handleAirSupply(int airSupply, CallbackInfo ci) {
        ci.cancel();
    }
}
