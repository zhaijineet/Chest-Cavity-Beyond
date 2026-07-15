package net.zhaiji.chestcavitybeyond.compat.jei;

import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public final class JeiEntityRecipeSlotRenderer implements IIngredientRenderer<JeiEntityIngredient> {
    private final IDrawableStatic slotBackground;
    private double mouseX = ChestCavityPageScrollWidget.ENTITY_SLOT_SIZE / 2.0;
    private double mouseY = ChestCavityPageScrollWidget.ENTITY_SLOT_SIZE / 2.0;

    public JeiEntityRecipeSlotRenderer(IDrawableStatic slotBackground) {
        this.slotBackground = slotBackground;
    }

    public void setMousePosition(double mouseX, double mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public void clearMousePosition() {
        mouseX = ChestCavityPageScrollWidget.ENTITY_SLOT_SIZE / 2.0;
        mouseY = ChestCavityPageScrollWidget.ENTITY_SLOT_SIZE / 2.0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, JeiEntityIngredient ingredient) {
        JeiEntityCache.getOrCreate(ingredient.entityType())
            .ifPresentOrElse(
                livingEntity -> JeiEntitySlotRenderer.render(
                    livingEntity,
                    0,
                    0,
                    ChestCavityPageScrollWidget.ENTITY_SLOT_SIZE,
                    ChestCavityPageScrollWidget.ENTITY_RENDER_MARGIN,
                    guiGraphics,
                    mouseX,
                    mouseY
                ),
                () -> renderFallback(guiGraphics, ingredient)
            );
    }

    private void renderFallback(GuiGraphics guiGraphics, JeiEntityIngredient ingredient) {
        int slotSize = ChestCavityPageScrollWidget.ENTITY_SLOT_SIZE;
        int itemSlotSize = ChestCavityPageScrollWidget.SLOT_SIZE;
        int backgroundOffset = (slotSize - itemSlotSize) / 2 - 1;
        slotBackground.draw(guiGraphics, backgroundOffset, backgroundOffset);

        Component entityName = ingredient.entityType().getDescription();
        Font font = Minecraft.getInstance().font;
        int textWidth = font.width(entityName);
        int textX = (slotSize - textWidth) / 2;
        int textY = (slotSize - font.lineHeight) / 2;
        guiGraphics.drawString(font, entityName, textX, textY, 0xFFFFFF, false);
    }

    @Override
    public List<Component> getTooltip(JeiEntityIngredient ingredient, TooltipFlag tooltipFlag) {
        return JeiEntityIngredientRenderer.createTooltip(ingredient);
    }

    @Override
    public int getWidth() {
        return ChestCavityPageScrollWidget.ENTITY_SLOT_SIZE;
    }

    @Override
    public int getHeight() {
        return ChestCavityPageScrollWidget.ENTITY_SLOT_SIZE;
    }
}
