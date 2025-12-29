package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.event.CommonEventHandler;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnderMan.class)
public abstract class EnderManMixin extends Monster {
    public EnderManMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * 改变传送条件，检测是否能够传送
     * TODO 随机传送距离应该受末影属性影响
     */
    @ModifyExpressionValue(
            method = "customServerAiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;isDay()Z"
            )
    )
    public boolean chestCavityBeyond$customServerAiStep(boolean original) {
        return original && ChestCavityUtil.getData(this).getCurrentValue(InitAttribute.ENDER) > 0;
    }

    /**
     * @author zhaijineet
     * @reason 换回父类逻辑，传送处理交给事件{@link CommonEventHandler#handlerLivingIncomingDamageEvent}
     */
    @Overwrite
    public boolean hurt(DamageSource source, float amount) {
        return super.hurt(source, amount);
    }

    @Mixin(EnderMan.EndermanLookForPlayerGoal.class)
    public abstract static class EndermanLookForPlayerGoalMixin {
        @Shadow
        @Final
        private EnderMan enderman;

        @ModifyExpressionValue(
                method = "tick",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/world/entity/monster/EnderMan;isPassenger()Z"
                )
        )
        public boolean chestCavityBeyond$tick(boolean original) {
            return original && ChestCavityUtil.getData(enderman).getCurrentValue(InitAttribute.ENDER) > 0;
        }
    }
}
