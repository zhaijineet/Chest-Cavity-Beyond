package net.zhaiji.chestcavitybeyond.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.DifferenceIngredient;
import net.neoforged.neoforge.common.crafting.IntersectionIngredient;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.manager.ItemTagManager;
import net.zhaiji.chestcavitybeyond.recipe.AlchemistGlandRecipe;
import net.zhaiji.chestcavitybeyond.recipe.VenomGlandRecipe;
import net.zhaiji.chestcavitybeyond.register.InitItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public RecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        super.buildRecipes(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, InitItem.CHEST_OPENER.get())
                .pattern(" AB")
                .pattern("  C")
                .define('A', Items.LEVER)
                .define('B', Items.IRON_INGOT)
                .define('C', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, InitItem.BIOLOGICAL_ANALYZER.get())
                .pattern("AG ")
                .pattern("IIR")
                .pattern(" S ")
                .define('A', Items.GLASS)
                .define('G', Items.EMERALD)
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(recipeOutput);

        SpecialRecipeBuilder.special(VenomGlandRecipe::new).save(recipeOutput, ChestCavityBeyond.of("venom_gland_recipe"));

        SpecialRecipeBuilder.special(AlchemistGlandRecipe::new).save(recipeOutput, ChestCavityBeyond.of("alchemist_gland_recipe"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLAZE_ROD)
                .requires(InitItem.ACTIVE_BLAZE_ROD.get())
                .unlockedBy(getHasName(InitItem.ACTIVE_BLAZE_ROD.get()), has(InitItem.ACTIVE_BLAZE_ROD.get()))
                .save(recipeOutput, ChestCavityBeyond.of(getItemName(Items.BLAZE_ROD)));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BREEZE_ROD)
                .requires(InitItem.ACTIVE_BREEZE_ROD.get())
                .unlockedBy(getHasName(InitItem.ACTIVE_BREEZE_ROD.get()), has(InitItem.ACTIVE_BREEZE_ROD.get()))
                .save(recipeOutput, ChestCavityBeyond.of(getItemName(Items.BREEZE_ROD)));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, InitItem.SLIME_STOMACH.get())
                .pattern(" A ")
                .pattern("CBC")
                .pattern(" A ")
                .define('A', InitItem.SLIME_CORE.get())
                .define('B', InitItem.STOMACH.get())
                .define('C', Items.SLIME_BALL)
                .unlockedBy(getHasName(InitItem.SLIME_CORE.get()), has(InitItem.SLIME_CORE.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, InitItem.MAGMA_STOMACH.get())
                .pattern(" A ")
                .pattern("CBC")
                .pattern(" A ")
                .define('A', InitItem.MAGMA_CUBE_CORE.get())
                .define('B', InitItem.STOMACH.get())
                .define('C', Items.MAGMA_CREAM)
                .unlockedBy(getHasName(InitItem.MAGMA_CUBE_CORE.get()), has(InitItem.MAGMA_CUBE_CORE.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SLIME_BALL, 2)
                .requires(InitItem.SLIME_CORE.get())
                .unlockedBy(getHasName(InitItem.SLIME_CORE.get()), has(InitItem.SLIME_CORE.get()))
                .save(recipeOutput, ChestCavityBeyond.of(getItemName(Items.SLIME_BALL)));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.MAGMA_CREAM, 2)
                .requires(InitItem.MAGMA_CUBE_CORE.get())
                .unlockedBy(getHasName(InitItem.MAGMA_CUBE_CORE.get()), has(InitItem.MAGMA_CUBE_CORE.get()))
                .save(recipeOutput, ChestCavityBeyond.of(getItemName(Items.MAGMA_CREAM)));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 8)
                .requires(
                        new Ingredient(
                                new IntersectionIngredient(
                                        List.of(
                                                Ingredient.of(ItemTagManager.SPINE),
                                                Ingredient.of(ItemTagManager.BONE)
                                        )
                                )
                        )
                )
                .unlockedBy("has_spine", has(ItemTagManager.SPINE))
                .save(recipeOutput, ChestCavityBeyond.of("bone_meal_spine"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 2)
                .requires(
                        new Ingredient(
                                new IntersectionIngredient(
                                        List.of(
                                                Ingredient.of(ItemTagManager.RIB),
                                                Ingredient.of(ItemTagManager.BONE)
                                        )
                                )
                        )
                )
                .unlockedBy("has_rib", has(ItemTagManager.RIB))
                .save(recipeOutput, ChestCavityBeyond.of("bone_meal_rib"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ROTTEN_FLESH)
                .requires(
                        new Ingredient(
                                new DifferenceIngredient(
                                        Ingredient.of(ItemTagManager.ROTTEN),
                                        Ingredient.of(ItemTagManager.BONE)
                                )
                        )
                )
                .unlockedBy("has_rotten", has(ItemTagManager.ROTTEN))
                .save(recipeOutput, ChestCavityBeyond.of("rotten"));

        ItemStack stack = Items.IRON_NUGGET.getDefaultInstance();
        stack.setCount(8);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ItemTagManager.IRON), RecipeCategory.MISC, stack, 0.1F, 200)
                .unlockedBy("has_iron_organ", has(ItemTagManager.IRON))
                .save(recipeOutput, ChestCavityBeyond.of("iron_organ"));
    }
}
