package net.zhaiji.chestcavitybeyond.api.function;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;

import java.util.List;

/**
 * 用于添加器官工具提示
 */
@FunctionalInterface
public interface OrganTooltipConsumer {
    /**
     * @param data              胸腔数据
     * @param index             器官在胸腔中的槽位索引，若未在胸腔中则为 -1
     * @param stack             器官物品
     * @param keyContext        工具提示按键上下文
     * @param context           工具提示上下文
     * @param tooltipComponents 工具提示组件列表
     * @param tooltipFlag       工具提示标识符
     */
    void accept(ChestCavityData data, int index, ItemStack stack, TooltipsKeyContext keyContext, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag);
}
