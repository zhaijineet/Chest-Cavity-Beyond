package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.mixinapi.IFoodData;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow
    protected FoodData foodData;

    public PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    public void chestCavityBeyond$init(Level level, BlockPos pos, float yRot, GameProfile gameProfile, CallbackInfo ci) {
        ((IFoodData) foodData).setChestCavityData(ChestCavityUtil.getData(this));
    }

    @Inject(
            method = "eat",
            at = @At("HEAD")
    )
    public void chestCavityBeyond$eat(Level level, ItemStack food, FoodProperties foodProperties, CallbackInfoReturnable<ItemStack> cir) {
        ((IFoodData) foodData).setFood(food);
    }

    @ModifyExpressionValue(
            method = "updatePlayerPose",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;isShiftKeyDown()Z"
            )
    )
    public boolean chestCavityBeyond$updatePlayerPose(boolean original) {
        // 如果重力小于等于0，且不在地面，不会蹲下
        return original && !(getBlockStateOn().isAir() && getAttributeValue(Attributes.GRAVITY) <= 0);
    }
}
