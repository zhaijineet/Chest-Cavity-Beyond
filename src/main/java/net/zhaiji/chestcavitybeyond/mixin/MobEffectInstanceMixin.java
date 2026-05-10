package net.zhaiji.chestcavitybeyond.mixin;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.zhaiji.chestcavitybeyond.mixinapi.ILivingEntity;
import net.zhaiji.chestcavitybeyond.mixinapi.IMobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin implements IMobEffectInstance {
    @Shadow
    private int duration;
    @Shadow
    private int amplifier;

    @Shadow
    public abstract Holder<MobEffect> getEffect();

    @Shadow
    public abstract int mapDuration(Int2IntFunction mapper);

    @Unique
    @Override
    public boolean isHarmful() {
        return getEffect().value().getCategory() == MobEffectCategory.HARMFUL;
    }

    @Unique
    @Override
    public void setDuration(Int2IntFunction mapper, @Nullable LivingEntity entity) {
        duration = mapDuration(mapper);
        chestCavityBeyond$syncEffect(entity);
    }

    @Override
    public void setAmplifier(int amplifier, @Nullable LivingEntity entity) {
        this.amplifier = Math.clamp(amplifier, 0, 255);
        chestCavityBeyond$syncEffect(entity);
    }

    @Unique
    private void chestCavityBeyond$syncEffect(@Nullable LivingEntity entity) {
        if (entity != null) {
            ((ILivingEntity) entity).chestCavityBeyond$onEffectUpdated((MobEffectInstance) (Object) this, true, null);
        }
    }
}
