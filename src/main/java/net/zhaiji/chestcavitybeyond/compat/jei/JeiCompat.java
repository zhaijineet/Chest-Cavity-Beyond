package net.zhaiji.chestcavitybeyond.compat.jei;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.zhaiji.chestcavitybeyond.api.capability.Organ;

import java.util.List;

public class JeiCompat {
    public static void init(IEventBus modBus, IEventBus gameBus) {
        modBusListener(modBus);
        gameBusListener(gameBus);
    }

    public static void modBusListener(IEventBus modBus) {
    }

    public static void gameBusListener(IEventBus gameBus) {
        gameBus.addListener(JeiCompat::handlerLoggingOutEvent);
    }

    public static boolean handleTooltip(
        Organ organ,
        ItemStack stack,
        Item.TooltipContext context,
        List<Component> tooltip,
        TooltipFlag flags
    ) {
        return ChestCavityPageScrollWidget.handleJeiTooltip(organ, stack, context, tooltip, flags);
    }

    private static void handlerLoggingOutEvent(ClientPlayerNetworkEvent.LoggingOut event) {
        ChestCavityPageScrollWidget.invalidate();
    }
}
