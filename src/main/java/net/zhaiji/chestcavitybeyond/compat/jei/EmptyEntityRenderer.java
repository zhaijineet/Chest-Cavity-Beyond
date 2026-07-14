package net.zhaiji.chestcavitybeyond.compat.jei;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * 空渲染器：不绘制任何内容（实体模型由 widget 接管），返回整格尺寸使 slot rect 扩大，
 * 让有对应物品的实体（如刷怪蛋）悬停时由 JEI 自动绘制的高亮覆盖整个 35×35 格子；
 * 无对应物品的实体（如玩家）因 getSlotUnderMouse 返回 empty，由 widget 手动补全高亮。
 */
public class EmptyEntityRenderer implements IIngredientRenderer<ItemStack> {
    public static final EmptyEntityRenderer INSTANCE = new EmptyEntityRenderer();

    @Override
    public void render(GuiGraphics guiGraphics, ItemStack ingredient) {
    }

    @Override
    public List<Component> getTooltip(ItemStack ingredient, TooltipFlag tooltipFlag) {
        return List.of();
    }

    @Override
    public int getWidth() {
        return ChestCavityPageScrollWidget.ENTITY_CELL_SIZE;
    }

    @Override
    public int getHeight() {
        return ChestCavityPageScrollWidget.ENTITY_CELL_SIZE;
    }
}
