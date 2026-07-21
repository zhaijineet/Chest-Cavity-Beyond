package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnderDragon.class)
public abstract class EnderDragonMixin extends Mob {
    public EnderDragonMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Definition(id = "tickCount", field = "Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;tickCount:I")
    @Expression("this.tickCount % 10 == 0")
    @ModifyExpressionValue(
            method = "checkCrystals",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    public boolean chestCavityBeyond$checkCrystals(boolean original) {
        // 没有结晶属性禁止回血
        return original && getAttributeValue(InitAttribute.CRYSTALLIZATION) > 0;
    }
}
