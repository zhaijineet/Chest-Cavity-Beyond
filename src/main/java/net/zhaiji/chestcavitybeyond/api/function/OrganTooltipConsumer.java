package net.zhaiji.chestcavitybeyond.api.function;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;

import java.util.List;

/**
 * 用于添加器官工具提示
 */
@FunctionalInterface
public interface OrganTooltipConsumer {
    /**
     * @param slotContext       胸腔槽位上下文
     * @param keyContext        工具提示按键上下文
     * @param context           工具提示上下文
     * @param tooltipComponents 工具提示组件列表
     * @param tooltipFlag       工具提示标识符
     */
    void accept(
        ChestCavitySlotContext slotContext,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    );
}
