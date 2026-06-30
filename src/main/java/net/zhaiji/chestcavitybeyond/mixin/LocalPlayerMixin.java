package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Inject(
        method = "isCrouching",
        at = @At("HEAD"),
        cancellable = true
    )
    public void chestCavityBeyond$isCrouching(CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer player = (LocalPlayer) (Object) this;

        if (!player.onGround() && player.getAttributeValue(Attributes.GRAVITY) <= 0) {
            cir.setReturnValue(false);
        }
    }
}