package net.zhaiji.chestcavitybeyond.compat.jei;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.zhaiji.chestcavitybeyond.api.capability.Organ;

import java.util.List;

/**
 * JEI 兼容入口，仅当 JEI 加载时初始化，避免主类直接依赖 JEI 类。
 */
public class JeiCompat {
    public static void init(IEventBus modBus, IEventBus gameBus) {
        modBusListener(modBus);
        gameBusListener(gameBus);
    }

    public static void modBusListener(IEventBus modBus) {
    }

    public static void gameBusListener(IEventBus gameBus) {
        gameBus.addListener(JeiCompat::handleLevelEventUnload);
    }

    /**
     * 尝试为 JEI 胸腔页面处理 tooltip，不在 JEI 页面悬停时返回 false。
     */
    public static boolean handlerTooltip(
        Organ organ,
        ItemStack stack,
        Item.TooltipContext context,
        List<Component> tooltip,
        TooltipFlag flags
    ) {
        return JeiOrganTooltipCache.handlerTooltip(organ, stack, context, tooltip, flags);
    }

    /**
     * 世界卸载时清理虚拟实体缓存与 tooltip 缓存，避免引用旧世界的幽灵实体。
     */
    private static void handleLevelEventUnload(LevelEvent.Unload event) {
        JeiEntityCache.clear();
        JeiOrganTooltipCache.clear();
    }
}
