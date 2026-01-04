package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.event.CommonEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CaveSpider.class)
public abstract class CaveSpiderMixin extends Spider {
    public CaveSpiderMixin(EntityType<? extends Spider> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @see CommonEventHandler#handlerLivingDamageEvent$Pre organ.attack
     * 更改了机制，所有难度统一会施加效果
     */
    @Inject(
            method = "doHurtTarget",
            at = @At("HEAD"),
            cancellable = true
    )
    public void chestCavityBeyond$doHurtTarget(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(super.doHurtTarget(entity));
    }
}
