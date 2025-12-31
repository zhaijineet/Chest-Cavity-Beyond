package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.event.CommonEventHandler;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WitherSkeleton.class)
public class WitherSkeletonMixin extends Monster {
    public WitherSkeletonMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @see CommonEventHandler#handlerLivingDamageEvent$Pre
     */
    @Redirect(
            method = "doHurtTarget",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    public boolean chestCavityBeyond$doHurtTarget(LivingEntity instance, MobEffectInstance flag, Entity entity) {
        return false;
    }

    /**
     * @see CommonEventHandler#handlerMobEffectEvent$Applicable
     */
    @Inject(
            method = "canBeAffected",
            at = @At("HEAD"),
            cancellable = true
    )
    public void chestCavityBeyond$doHurtTarget(MobEffectInstance potioneffect, CallbackInfoReturnable<Boolean> cir) {
        if (ChestCavityUtil.getData(this).getCurrentValue(InitAttribute.WITHERED) <= 0) {
            cir.setReturnValue(super.canBeAffected(potioneffect));
        }
    }
}
