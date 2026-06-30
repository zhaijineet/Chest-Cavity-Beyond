package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.mixinapi.ILivingEntity;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.MixinUtil;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ILivingEntity {
    @Unique
    private final Set<Holder<Attribute>> dirtyDerivedAttributes = new HashSet<>();

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    protected abstract void onEffectUpdated(MobEffectInstance effectInstance, boolean forced, @Nullable Entity entity);

    /**
     * 根据属性返回生物是否水过敏
     */
    @ModifyExpressionValue(
        method = "aiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;isSensitiveToWater()Z"
        )
    )
    public boolean chestCavityBeyond$aiStep(boolean original) {
        return OrganAttributeUtil.isWaterAllergy((LivingEntity) (Object) this);
    }

    /**
     * 应用发射效果
     */
    @Inject(
        method = "hurt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V",
            shift = At.Shift.AFTER
        )
    )
    public void chestCavityBeyond$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        MixinUtil.applyLaunchEffect((LivingEntity) (Object) this, source);
    }


    /**
     * 如果有腐食消化，则不会附加食物的中毒和饥饿
     */
    @WrapOperation(
        method = "addEatEffect",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"
        )
    )
    public boolean chestCavityBeyond$addEatEffect(LivingEntity instance, MobEffectInstance effectInstance, Operation<Boolean> original) {
        if (MixinUtil.shouldAddEatEffect(instance, effectInstance)) {
            return original.call(instance, effectInstance);
        } else {
            return false;
        }
    }

    @Override
    public void chestCavityBeyond$onEffectUpdated(MobEffectInstance effectInstance, boolean forced, @Nullable Entity entity) {
        onEffectUpdated(effectInstance, forced, entity);
    }

    /**
     * 记录发生变更的模组派生属性
     */
    @Inject(
        method = "onAttributeUpdated",
        at = @At("HEAD")
    )
    public void chestCavityBeyond$onAttributeUpdated(Holder<Attribute> attribute, CallbackInfo ci) {
        if (attribute.equals(InitAttribute.HEALTH)
            || attribute.equals(InitAttribute.NERVES)
            || attribute.equals(InitAttribute.STRENGTH)
            || attribute.equals(InitAttribute.SPEED)
            || attribute.equals(InitAttribute.LEAPING)) {
            dirtyDerivedAttributes.add(attribute);
        }
    }

    /**
     * 在 refreshDirtyAttributes 的 set.clear() 之后安全更新原版派生属性
     */
    @Inject(
        method = "refreshDirtyAttributes",
        at = @At("RETURN")
    )
    public void chestCavityBeyond$refreshDirtyAttributes(CallbackInfo ci) {
        if (dirtyDerivedAttributes.isEmpty()) return;
        ChestCavityData data = ChestCavityUtil.getData((LivingEntity) (Object) this);
        for (Holder<Attribute> attr : dirtyDerivedAttributes) {
            OrganAttributeUtil.updateDerivedAttribute(data, attr);
        }
        dirtyDerivedAttributes.clear();
    }
}
