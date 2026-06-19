package net.zhaiji.chestcavitybeyond.mixin.appleskin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.zhaiji.chestcavitybeyond.compat.appleskin.AppleSkinCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import squeek.appleskin.helpers.FoodHelper;

/**
 * 替换 AppleSkin 血量估算以反映新陈代谢和耐力。
 */
@Pseudo
@Mixin(FoodHelper.class)
public class FoodHelperMixin {
    @Inject(
        method = "getEstimatedHealthIncrement(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/food/FoodProperties;)F",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void chestCavityBeyond$getEstimatedHealthIncrement(
        Player player,
        FoodProperties foodProperties,
        CallbackInfoReturnable<Float> callbackInfoReturnable
    ) {
        callbackInfoReturnable.setReturnValue(AppleSkinCompat.estimateHealthIncrement(player, foodProperties));
    }
}
