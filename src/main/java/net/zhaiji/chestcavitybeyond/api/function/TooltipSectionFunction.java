package net.zhaiji.chestcavitybeyond.api.function;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;

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
     * @param data              胸腔数据
     * @param index             器官在胸腔中的槽位索引，若未在胸腔中则为 -1
     * @param stack             器官物品
     * @param keyContext        工具提示按键上下文
     * @param context           工具提示上下文
     * @param tooltipComponents 当前已渲染的工具提示组件列表
     * @param tooltipFlag       工具提示标识符
     * @return 要插入的工具提示行列表
     */
    List<Component> apply(
        ChestCavityData data,
        int index,
        ItemStack stack,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    );
}
