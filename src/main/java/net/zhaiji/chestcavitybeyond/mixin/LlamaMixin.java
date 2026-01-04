package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.register.InitItem;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Llama.class)
public abstract class LlamaMixin extends AbstractChestedHorse {
    public LlamaMixin(EntityType<? extends AbstractChestedHorse> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * 是否可以吐口水
     */
    @WrapWithCondition(
            method = "performRangedAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/horse/Llama;spit(Lnet/minecraft/world/entity/LivingEntity;)V"
            )
    )
    public boolean chestCavityBeyond$performRangedAttack(Llama instance, LivingEntity target) {
        return ChestCavityUtil.getData(this).hasOrgan(InitItem.LLAMA_LUNG.get());
    }
}
