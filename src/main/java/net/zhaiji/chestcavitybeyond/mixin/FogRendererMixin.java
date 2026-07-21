package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.material.FogType;
import net.zhaiji.chestcavitybeyond.util.MixinUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    /**
     * 免疫熔岩伤害时，将熔岩迷雾起始距离替换为正常空气雾值，大幅改善视野
     */
    @WrapOperation(
        method = "setupFog",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"
        )
    )
    private static void chestCavityBeyond$setupFog$start(
        float fogStart,
        Operation<Void> original,
        @Local(argsOnly = true) Camera camera,
        @Local(argsOnly = true, ordinal = 0) float farPlaneDistance
    ) {
        if (camera.getFluidInCamera() == FogType.LAVA
            && MixinUtil.shouldClearLavaFog(camera.getEntity())) {
            original.call(farPlaneDistance * 0.05F);
        } else {
            original.call(fogStart);
        }
    }

    /**
     * 免疫熔岩伤害时，将熔岩迷雾结束距离替换为正常空气雾值，大幅改善视野
     */
    @WrapOperation(
        method = "setupFog",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V"
        )
    )
    private static void chestCavityBeyond$setupFog$end(
        float fogEnd,
        Operation<Void> original,
        @Local(argsOnly = true) Camera camera,
        @Local(argsOnly = true, ordinal = 0) float farPlaneDistance
    ) {
        if (camera.getFluidInCamera() == FogType.LAVA
            && MixinUtil.shouldClearLavaFog(camera.getEntity())) {
            original.call(Math.min(farPlaneDistance, 192.0F) * 0.5F);
        } else {
            original.call(fogEnd);
        }
    }
}
