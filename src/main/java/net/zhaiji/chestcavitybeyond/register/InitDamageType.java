package net.zhaiji.chestcavitybeyond.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.builder.DamageTypeBuilder;

public class InitDamageType {
    public static final ResourceKey<DamageType> ORGAN_LOSS = ResourceKey.create(Registries.DAMAGE_TYPE, ChestCavityBeyond.of("organ_loss"));

    public static final ResourceKey<DamageType> OPEN_CHEST = ResourceKey.create(Registries.DAMAGE_TYPE, ChestCavityBeyond.of("open_chest"));

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(ORGAN_LOSS, DamageTypeBuilder.builder(ORGAN_LOSS).build());
        context.register(OPEN_CHEST, DamageTypeBuilder.builder(OPEN_CHEST).build());
    }
}
