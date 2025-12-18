package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.phys.EntityHitResult;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Snowball.class)
public class SnowballMixin {
    /**
     * 根据水过敏属性设置伤害
     */
    @ModifyVariable(
            method = "onHitEntity",
            at = @At("STORE")
    )
    public int chestCavityBeyond$onHitEntity(int i, @Local(argsOnly = true) EntityHitResult result) {
        boolean canHurt = false;
        if (result.getEntity() instanceof LivingEntity entity) {
            canHurt = ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.WATER_ALLERGY) > 0;
        }
        return canHurt ? 3 : 0;
    }
}
