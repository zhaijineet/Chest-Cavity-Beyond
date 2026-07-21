package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.HashSet;
import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ILivingEntity, ILivingEntityExtension {
    @Unique
    private final Set<Holder<Attribute>> dirtyDerivedAttributes = new HashSet<>();

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    protected abstract void onEffectUpdated(MobEffectInstance effectInstance, boolean forced, @Nullable Entity entity);

    /**
     * 根据熔岩游泳速度属性提升熔岩中的移动速度，对齐水中 SWIM_SPEED 的应用方式
     */
    @ModifyArg(
        method = "travel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V",
            ordinal = 1
        ),
        index = 0
    )
    public float chestCavityBeyond$travel(float original) {
        return MixinUtil.applyLavaSwimSpeed(self(), original);
    }

    /**
     * 浅熔岩水平衰减对齐水的疾跑机制：疾跑时提升水平衰减，垂直衰减保持 0.8 不变
     */
    @ModifyArgs(
        method = "travel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/phys/Vec3;multiply(DDD)Lnet/minecraft/world/phys/Vec3;",
            ordinal = 1
        )
    )
    public void chestCavityBeyond$travel$multiply(Args args) {
        double decay = MixinUtil.getLavaHorizontalDecay(self());
        args.set(0, decay);
        args.set(2, decay);
    }

    /**
     * 深熔岩水平衰减对齐浅熔岩疾跑机制，垂直衰减保持原版 0.5 不变
     */
    @WrapOperation(
        method = "travel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;",
            ordinal = 0
        )
    )
    public Vec3 chestCavityBeyond$travel$scale(Vec3 instance, double scaleFactor, Operation<Vec3> original) {
        double decay = MixinUtil.getLavaHorizontalDecay(self());
        return instance.multiply(decay, 0.5F, decay);
    }

    /**
     * 按 LAVA_SWIM_SPEED 属性值减缓熔岩额外的下沉重力，对齐水（水无额外重力）
     */
    @ModifyArg(
        method = "travel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;",
            ordinal = 0
        ),
        index = 1
    )
    public double chestCavityBeyond$travel$add(double original) {
        return MixinUtil.modifyLavaExtraGravity(self(), original);
    }

    /**
     * 拥有熔岩行者属性且非潜行时，允许实体站在熔岩表面
     */
    @Inject(
        method = "canStandOnFluid(Lnet/minecraft/world/level/material/FluidState;)Z",
        at = @At("RETURN"),
        cancellable = true
    )
    public void chestCavityBeyond$canStandOnFluid(FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        if (MixinUtil.canStandOnLava(fluidState, self())) {
            cir.setReturnValue(true);
        }
    }

    /**
     * 对拥有熔岩行者属性的实体，完整执行原版 Strider.checkFallDamage 的等价逻辑（checkInsideBlocks + 熔岩坠落重置）
     */
    @Inject(
        method = "checkFallDamage",
        at = @At("HEAD"),
        cancellable = true
    )
    public void chestCavityBeyond$checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos, CallbackInfo ci) {
        if (MixinUtil.handleLavaFall(self(), state)) {
            ci.cancel();
        }
    }

    /**
     * 拥有熔岩行者属性且非潜行时，在熔岩中提供浮力，对齐原版炽足兽的 floatStrider 逻辑
     */
    @Inject(
        method = "aiStep",
        at = @At("RETURN")
    )
    public void chestCavityBeyond$aiStep$floatOnLava(CallbackInfo ci) {
        MixinUtil.floatOnLava(self());
    }

    /**
     * 控制熔岩分支的进入条件，分两种情况：
     * onGround 时强制返回 true 阻止进入熔岩分支，避免原版"熔岩中撞墙向上游"机制误触发 dY=0.3 弹跳；
     * 非 onGround 时强制返回 false 使熔岩分支正常执行，LAVA_SWIM_SPEED 属性正常应用
     */
    @ModifyExpressionValue(
        method = "travel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;canStandOnFluid(Lnet/minecraft/world/level/material/FluidState;)Z",
            ordinal = 1
        )
    )
    public boolean chestCavityBeyond$travel$canStandOnFluid(boolean original) {
        LivingEntity self = self();
        if (MixinUtil.hasLavaWalkActive(self)) {
            return self.onGround();
        }
        return original;
    }

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
        return OrganAttributeUtil.isWaterAllergy(self());
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
        MixinUtil.applyLaunchEffect(self(), source);
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
        ChestCavityData data = ChestCavityUtil.getData(self());
        for (Holder<Attribute> attr : dirtyDerivedAttributes) {
            OrganAttributeUtil.updateDerivedAttribute(data, attr);
        }
        dirtyDerivedAttributes.clear();
    }
}
