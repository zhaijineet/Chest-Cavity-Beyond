package net.zhaiji.chestcavitybeyond.manager;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.register.InitDamageType;

public class DamageSourceManager {
    public static DamageSource organLoss(Level level) {
        return new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(InitDamageType.ORGAN_LOSS)
        );
    }
}
