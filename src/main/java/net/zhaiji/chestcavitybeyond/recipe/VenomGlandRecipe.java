package net.zhaiji.chestcavitybeyond.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.register.InitItem;
import net.zhaiji.chestcavitybeyond.register.InitRecipe;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;

public class VenomGlandRecipe extends CustomRecipe {
    public VenomGlandRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int venomGland = 0;
        int potion = 0;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(InitItem.VENOM_GLAND.get())) {
                venomGland++;
            } else {
                PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
                if (potionContents != null && potionContents.getAllEffects() != null) {
                    potion++;
                }
            }
            if (venomGland > 1 || potion > 1) return false;
        }
        return venomGland == 1 && potion == 1;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack venomGland = ItemStack.EMPTY;
        ItemStack potion = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(InitItem.VENOM_GLAND.get())) {
                venomGland = stack;
            } else if (stack.has(DataComponents.POTION_CONTENTS)) {
                potion = stack;
            }
        }
        return ChestCavityUtil.attachPotionContents(venomGland.copy(), potion.get(DataComponents.POTION_CONTENTS).getAllEffects());
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < nonnulllist.size(); i++) {
            ItemStack item = input.getItem(i);
            if (!item.is(InitItem.VENOM_GLAND.get())) {
                // 真有你的mojang，自己写了craftRemaining和usingConvertsTo
                // 结果tmd不用，在PotionItem的finishUsingItem方法里直接new ItemStack(Items.GLASS_BOTTLE)
                // 我没招了
                nonnulllist.set(i, Items.GLASS_BOTTLE.getDefaultInstance());
            }
        }
        return nonnulllist;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return InitRecipe.VENOM_GLAND.get();
    }
}
