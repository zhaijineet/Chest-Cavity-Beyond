package net.zhaiji.chestcavitybeyond.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;

public class FurnacePowerEffect extends MobEffect {
    public FurnacePowerEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF8C00);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide() && livingEntity instanceof Player player) {
            player.getFoodData().eat(1, 0.5F);
        }
        return ChestCavityUtil.getData(livingEntity).getCurrentValue(InitAttribute.FURNACE_POWER) > 0;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % (200 / amplifier) == 0;
    }
}
