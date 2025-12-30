package net.zhaiji.chestcavitybeyond.register;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.effect.FurnacePowerEffect;

public class InitEffect {
    public static final DeferredRegister<MobEffect> EFFECT = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, ChestCavityBeyond.MOD_ID);

    public static final Holder<MobEffect> FURNACE_POWER = EFFECT.register(
            "furnace_power",
            FurnacePowerEffect::new
    );
}
