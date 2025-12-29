package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class CreeperMixin extends Monster {
    public CreeperMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/Creeper;isIgnited()Z"
            )
    )
    public boolean chestCavityBeyond$tick(boolean original) {
        // 保险起见，没有对应属性，就算点燃了也不能碰撞
        return original && getAttribute(InitAttribute.EXPLOSIVE).getValue() > 0;
    }

    @ModifyExpressionValue(
            method = "mobInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"
            )
    )
    public boolean chestCavityBeyond$mobInteract(boolean original) {
        // 没有对应属性不许点燃
        return original && getAttribute(InitAttribute.EXPLOSIVE).getValue() > 0;
    }

    @Mixin(SwellGoal.class)
    public abstract static class SwellGoalMixin {
        @Shadow
        @Final
        private Creeper creeper;

        @Inject(
                method = "tick",
                at = @At("HEAD"),
                cancellable = true
        )
        public void chestCavityBeyond$tick(CallbackInfo ci) {
            // 没有对应属性不许膨胀
            if (creeper.getAttribute(InitAttribute.EXPLOSIVE).getValue() <= 0) {
                creeper.setSwellDir(-1);
                ci.cancel();
            }
        }
    }
}
