package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LargeFireball.class)
public abstract class LargeFireballMixin extends Fireball {
    public LargeFireballMixin(EntityType<? extends Fireball> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyArgs(
            method = "onHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;)Lnet/minecraft/world/level/Explosion;"
            )
    )
    public void chestCavityBeyond$onHit(Args args) {
        if (getOwner() instanceof Player) {
            // 玩家造成的爆炸不会生成火焰和炸掉方块
            args.set(5, false);
            args.set(6, Level.ExplosionInteraction.NONE);
        }
    }
}
