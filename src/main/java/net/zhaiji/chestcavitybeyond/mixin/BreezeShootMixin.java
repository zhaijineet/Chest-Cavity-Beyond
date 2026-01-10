package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.breeze.Shoot;
import net.zhaiji.chestcavitybeyond.register.InitItem;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Shoot.class)
public abstract class BreezeShootMixin extends Behavior<Breeze> {
    public BreezeShootMixin(Map<MemoryModuleType<?>, MemoryStatus> entryCondition) {
        super(entryCondition);
    }

    /**
     * 没有核心不允许发射风弹
     */
    @Inject(
            method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/monster/breeze/Breeze;J)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void chestCavityBeyond$tick(ServerLevel level, Breeze owner, long gameTime, CallbackInfo ci) {
        if (!ChestCavityUtil.getData(owner).hasOrgan(InitItem.BREEZE_CORE.get())) {
            ci.cancel();
        }
    }
}
