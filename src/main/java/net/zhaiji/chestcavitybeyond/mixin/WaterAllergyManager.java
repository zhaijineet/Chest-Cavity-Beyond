package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Strider;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
        value = {
                LivingEntity.class,
                Blaze.class,
                EnderMan.class,
                SnowGolem.class,
                Strider.class
        }
)
public class WaterAllergyManager {
    /**
     * @see LivingEntityMixin#chestCavityBeyond$aiStep
     * <p>
     * 为了兼容性其他mod，将原本的isSensitiveToWater逻辑一并修改
     * </p>
     */
    @Inject(
            method = "isSensitiveToWater",
            at = @At("HEAD"),
            cancellable = true
    )
    public void chestCavityBeyond$isSensitiveToWater(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(OrganAttributeUtil.isWaterAllergy((LivingEntity) (Object) this));
    }
}
