package net.zhaiji.chestcavitybeyond.compat.jei;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public final class JeiEntityIngredientRenderer implements IIngredientRenderer<JeiEntityIngredient> {
    public static final JeiEntityIngredientRenderer INSTANCE = new JeiEntityIngredientRenderer();

    private static final int RENDER_SIZE = 16;
    private static final int RENDER_MARGIN = 1;
    private static final double FIXED_MOUSE_POSITION = RENDER_SIZE / 2.0;

    private JeiEntityIngredientRenderer() {
    }

    @Override
    public void render(GuiGraphics guiGraphics, JeiEntityIngredient ingredient) {
        JeiEntityCache.getOrCreate(ingredient.entityType()).ifPresent(livingEntity -> JeiEntitySlotRenderer.render(
            livingEntity,
            0,
            0,
            RENDER_SIZE,
            RENDER_MARGIN,
            guiGraphics,
            FIXED_MOUSE_POSITION,
            FIXED_MOUSE_POSITION
        ));
    }

    public static List<Component> createTooltip(JeiEntityIngredient ingredient) {
        return List.of(ingredient.entityType().getDescription());
    }

    @Override
    public List<Component> getTooltip(JeiEntityIngredient ingredient, TooltipFlag tooltipFlag) {
        return createTooltip(ingredient);
    }
}
