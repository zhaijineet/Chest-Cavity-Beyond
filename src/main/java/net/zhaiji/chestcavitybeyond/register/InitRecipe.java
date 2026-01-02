package net.zhaiji.chestcavitybeyond.register;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.recipe.VenomGlandRecipe;

import java.util.function.Supplier;

public class InitRecipe {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ChestCavityBeyond.MOD_ID);

    public static final Supplier<RecipeSerializer<VenomGlandRecipe>> VENOM_GLAND = RECIPE_SERIALIZERS.register(
            "venom_gland",
            () -> new SimpleCraftingRecipeSerializer<>(VenomGlandRecipe::new)
    );
}
