package net.zhaiji.chestcavitybeyond.api.function;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;

import java.util.List;

/**
 * 工具提示段落渲染器
 * <p>
 * 与 {@link OrganTooltipConsumer} 参数相同，区别是返回要插入的行列表。
 * 框架负责将返回的行按正确顺序插入到管线中。
 * </p>
 */
@FunctionalInterface
public interface TooltipSectionFunction {
    /**
     * 渲染工具提示段落
     *
     * @param slotContext       胸腔槽位上下文
     * @param keyContext        工具提示按键上下文
     * @param context           工具提示上下文
     * @param tooltipComponents 当前已渲染的工具提示组件列表
     * @param tooltipFlag       工具提示标识符
     * @return 要插入的工具提示行列表
     */
    List<Component> apply(
        ChestCavitySlotContext slotContext,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    );
}
