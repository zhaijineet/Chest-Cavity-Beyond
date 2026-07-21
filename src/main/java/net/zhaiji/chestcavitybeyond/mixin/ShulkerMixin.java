package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.register.InitItem;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Shulker.class)
public abstract class ShulkerMixin extends AbstractGolem {
    protected ShulkerMixin(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * 没有末影属性不允许传送
     */
    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/Shulker;isPassenger()Z"
            )
    )
    public boolean chestCavityBeyond$tick(boolean original) {
        // 由于最终值要取反，所以此处需要判断小于等于0
        return original || getAttributeValue(InitAttribute.ENDER) <= 0;
    }

    /**
     * 没有末影属性不允许传送
     */
    @ModifyExpressionValue(
            method = "teleportSomewhere",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/Shulker;isAlive()Z"
            )
    )
    public boolean chestCavityBeyond$teleportSomewhere(boolean original) {
        return original && getAttributeValue(InitAttribute.ENDER) > 0;
    }

    @Mixin(Shulker.ShulkerAttackGoal.class)
    public abstract static class ShulkerAttackGoalMixin extends Goal {
        @Shadow
        @Final
        Shulker this$0;

        /**
         * 没有潜隐脾脏禁止发射子弹
         */
        @Inject(
                method = "tick",
                at = @At("HEAD"),
                cancellable = true
        )
        public void chestCavityBeyond$tick(CallbackInfo ci) {
            if (!ChestCavityUtil.getData(this$0).hasOrgan(InitItem.SHULKER_SPLEEN.get())) {
                ci.cancel();
            }
        }
    }
}
