package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Blaze.class)
public abstract class BlazeMixin extends Monster {
    public BlazeMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Mixin(Blaze.BlazeAttackGoal.class)
    public abstract static class BlazeAttackGoalMixin extends Goal {
        @Shadow
        @Final
        private Blaze blaze;

        @Shadow
        private int attackStep;

        @Definition(id = "attackStep", field = "Lnet/minecraft/world/entity/monster/Blaze$BlazeAttackGoal;attackStep:I")
        @Expression("this.attackStep <= 4")
        @ModifyExpressionValue(
                method = "tick",
                at = @At("MIXINEXTRAS:EXPRESSION")
        )
        public boolean chestCavityBeyond$tick(boolean original) {
            // 根据呕火属性，更改烈焰人可以发射的火球数量
            return attackStep <= 1 + Math.ceil(blaze.getAttributeValue(InitAttribute.VOMIT_FIREBALL));
        }
    }
}
