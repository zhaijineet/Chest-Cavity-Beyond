package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.zhaiji.chestcavitybeyond.util.MixinUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {
    /**
     * 免疫火焰伤害时隐藏屏幕火焰覆盖层，避免视觉与实际免疫状态不一致
     */
    @ModifyExpressionValue(
        method = "renderScreenEffect",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;isOnFire()Z"
        )
    )
    private static boolean chestCavityBeyond$renderScreenEffect(boolean original) {
        if (original && MixinUtil.shouldHideFireOverlay(Minecraft.getInstance().player)) {
            return false;
        }
        return original;
    }
}
