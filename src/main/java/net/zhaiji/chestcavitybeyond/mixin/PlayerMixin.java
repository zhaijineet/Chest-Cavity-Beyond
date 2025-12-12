package net.zhaiji.chestcavitybeyond.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.mixinapi.IFoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow
    protected FoodData foodData;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    public void chestCavityBeyond$init(Level level, BlockPos pos, float yRot, GameProfile gameProfile, CallbackInfo ci) {
        ((IFoodData) foodData).setPlayer((Player) (Object) this);
    }
}
