package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WitherSkull.class)
public abstract class WitherSkullMixin extends AbstractHurtingProjectile {
    public WitherSkullMixin(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyArg(
            method = "onHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;)Lnet/minecraft/world/level/Explosion;"
            ),
            index = 6
    )
    public Level.ExplosionInteraction chestCavityBeyond$onHit(Level.ExplosionInteraction explosionInteraction) {
        // 玩家造成的爆炸不会炸掉方块
        return getOwner() instanceof Player ? Level.ExplosionInteraction.NONE : explosionInteraction;
    }
}
