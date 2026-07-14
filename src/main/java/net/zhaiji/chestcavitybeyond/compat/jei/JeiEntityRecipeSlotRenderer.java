package net.zhaiji.chestcavitybeyond.compat.jei;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public final class JeiEntityRecipeSlotRenderer implements IIngredientRenderer<JeiEntityIngredient> {
    public static final JeiEntityRecipeSlotRenderer INSTANCE = new JeiEntityRecipeSlotRenderer();

    private static final int SLOT_SIZE = 35;

    private JeiEntityRecipeSlotRenderer() {
    }

    @Override
    public void render(GuiGraphics guiGraphics, JeiEntityIngredient ingredient) {
    }

    @Override
    public List<Component> getTooltip(JeiEntityIngredient ingredient, TooltipFlag tooltipFlag) {
        return List.of(ingredient.entityType().getDescription());
    }

    @Override
    public int getWidth() {
        return SLOT_SIZE;
    }

    @Override
    public int getHeight() {
        return SLOT_SIZE;
    }
}
