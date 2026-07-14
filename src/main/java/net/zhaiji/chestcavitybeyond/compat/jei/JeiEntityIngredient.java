package net.zhaiji.chestcavitybeyond.compat.jei;

import com.mojang.serialization.Codec;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;

import java.util.Objects;

public record JeiEntityIngredient(ResourceLocation entityTypeResourceLocation) {
    public static final IIngredientType<JeiEntityIngredient> TYPE = new IIngredientType<>() {
        @Override
        public Class<? extends JeiEntityIngredient> getIngredientClass() {
            return JeiEntityIngredient.class;
        }

        @Override
        public String getUid() {
            return ChestCavityBeyond.MOD_ID + ":entity";
        }
    };

    public static final Codec<JeiEntityIngredient> CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec().xmap(
        JeiEntityIngredient::new,
        JeiEntityIngredient::entityType
    );

    public JeiEntityIngredient {
        Objects.requireNonNull(entityTypeResourceLocation, "entityTypeResourceLocation");
        if (!BuiltInRegistries.ENTITY_TYPE.containsKey(entityTypeResourceLocation)) {
            throw new IllegalArgumentException("Unknown entity type: " + entityTypeResourceLocation);
        }
    }

    public JeiEntityIngredient(EntityType<?> entityType) {
        this(getEntityTypeResourceLocation(entityType));
    }

    private static ResourceLocation getEntityTypeResourceLocation(EntityType<?> entityType) {
        Objects.requireNonNull(entityType, "entityType");
        return BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType)
            .orElseThrow(() -> new IllegalArgumentException("Unregistered entity type: " + entityType))
            .location();
    }

    public EntityType<?> entityType() {
        return BuiltInRegistries.ENTITY_TYPE.getOptional(entityTypeResourceLocation)
            .orElseThrow(() -> new IllegalStateException("Unknown entity type: " + entityTypeResourceLocation));
    }
}
