package net.zhaiji.chestcavitybeyond.compat.jei;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * 为器官物品 tooltip 提供当前胸腔类型与真实槽位索引
 */
public class JeiOrganSlotRenderer implements IIngredientRenderer<ItemStack> {
    private final IIngredientRenderer<ItemStack> itemStackRenderer;
    private final ChestCavityTypeDisplay display;
    private final int organIndex;

    public JeiOrganSlotRenderer(
        IIngredientRenderer<ItemStack> itemStackRenderer,
        ChestCavityTypeDisplay display,
        int organIndex
    ) {
        this.itemStackRenderer = itemStackRenderer;
        this.display = display;
        this.organIndex = organIndex;
    }

    @Override
    public void render(GuiGraphics guiGraphics, ItemStack itemStack) {
        itemStackRenderer.render(guiGraphics, itemStack);
    }

    @Override
    public List<Component> getTooltip(ItemStack itemStack, TooltipFlag tooltipFlag) {
        JeiOrganTooltipContext.prepare(display, organIndex, itemStack);
        List<Component> tooltip = itemStackRenderer.getTooltip(itemStack, tooltipFlag);
        JeiOrganTooltipContext.clear();
        return tooltip;
    }

    @Override
    public Font getFontRenderer(Minecraft minecraft, ItemStack itemStack) {
        return itemStackRenderer.getFontRenderer(minecraft, itemStack);
    }

    @Override
    public int getWidth() {
        return itemStackRenderer.getWidth();
    }

    @Override
    public int getHeight() {
        return itemStackRenderer.getHeight();
    }
}
