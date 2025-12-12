package net.zhaiji.chestcavitybeyond.client.event;

import net.neoforged.bus.api.IEventBus;

public class ClientEventManager {
    public static void init(IEventBus modEventBus, IEventBus gameBus) {
        ClientEventManager.modBusListener(modEventBus);
        ClientEventManager.gameBusListener(gameBus);
    }

    public static void modBusListener(IEventBus modEventBus) {
        modEventBus.addListener(ClientEventHandler::handlerRegisterMenuScreensEvent);
    }

    public static void gameBusListener(IEventBus gameBus) {
    }
}
