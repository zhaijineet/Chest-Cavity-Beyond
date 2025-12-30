package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(IronGolem.class)
public abstract class IronGolemMixin extends AbstractGolem {
    public IronGolemMixin(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * 根据{@link InitAttribute#IRON_REPAIR}属性，判断是否可以回复生命值
     */
    @ModifyExpressionValue(
            method = "mobInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    public boolean chestCavityBeyond$mobInteract(boolean original) {
        return original && ChestCavityUtil.getData(this).getCurrentValue(InitAttribute.IRON_REPAIR) > 0;
    }

    /**
     * 根据{@link InitAttribute#IRON_REPAIR}属性，修改回复生命值
     */
    @Redirect(
            method = "mobInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/IronGolem;heal(F)V"
            )
    )
    public void chestCavityBeyond$modifyHeal(IronGolem instance, float v) {
        instance.heal((float) (2.5 * ChestCavityUtil.getData(instance).getCurrentValue(InitAttribute.IRON_REPAIR)));
    }


    /**
     * 此方法的实现转到{@link LivingEntityMixin#chestCavityBeyond$hurt}
     */
    @WrapWithCondition(
            method = "doHurtTarget",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
            )
    )
    public boolean chestCavityBeyond$doHurtTarget(Entity instance, Vec3 deltaMovement) {
        return false;
    }
}
