package net.zhaiji.chestcavitybeyond.compat.jei;

import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.api.capability.Organ;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;

import java.util.List;

/**
 * JEI 兼容入口，仅当 JEI 加载时调用
 */
public class JeiCompat {
    /**
     * 尝试为 JEI 胸腔页面处理 tooltip，不在 JEI 页面悬停时返回 false
     */
    public static boolean handlerTooltip(
        Organ organ,
        ItemStack itemStack,
        Item.TooltipContext context,
        List<Component> tooltip,
        TooltipFlag flags
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!minecraft.isSameThread() || !(minecraft.screen instanceof IRecipesGui)) return false;
        return JeiOrganTooltipContext.consume(
            itemStack,
            target -> {
                ChestCavityData data = JeiChestCavityPreviewCache.get(target.display());
                organ.organTooltip(
                    ChestCavityUtil.createContext(data, target.organIndex(), itemStack),
                    new TooltipsKeyContext(Screen.hasShiftDown(), Screen.hasControlDown()),
                    context,
                    tooltip,
                    flags
                );
            }
        );
    }
}
