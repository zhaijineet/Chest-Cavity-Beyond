package net.zhaiji.chestcavitybeyond.register;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.entity.ThrownCobweb;

import java.util.function.Supplier;

public class InitEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ChestCavityBeyond.MOD_ID);

    public static final Supplier<EntityType<ThrownCobweb>> THROWN_COBWEB = register(
            "thrown_cobweb",
            EntityType.Builder.<ThrownCobweb>of(ThrownCobweb::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
    );

    public static <T extends Entity> Supplier<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return ENTITY_TYPE.register(name, () -> builder.build(name));
    }
}
