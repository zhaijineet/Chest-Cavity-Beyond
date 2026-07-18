package net.zhaiji.chestcavitybeyond.client.event;

import net.neoforged.bus.api.IEventBus;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondClientConfig;
import net.zhaiji.chestcavitybeyond.compat.CompatManager;
import net.zhaiji.chestcavitybeyond.compat.jei.JeiCompat;

public class ClientEventManager {
    public static void init(IEventBus modBus, IEventBus gameBus) {
        ClientEventManager.modBusListener(modBus);
        ClientEventManager.gameBusListener(gameBus);
        if (CompatManager.JEI_LOADED) {
            JeiCompat.init(modBus, gameBus);
        }
    }

    public static void modBusListener(IEventBus modBus) {
        modBus.addListener(ClientEventHandler::handlerRegisterMenuScreensEvent);
        modBus.addListener(ClientEventHandler::handlerRegisterKeyMappingsEvent);
        modBus.addListener(ClientEventHandler::handlerEntityRenderersEvent$RegisterRenderers);
        modBus.addListener(ClientEventHandler::handlerRegisterGuiLayersEvent);
        modBus.addListener(ChestCavityBeyondClientConfig::handlerModConfigEvent);
    }

    public static void gameBusListener(IEventBus gameBus) {
        gameBus.addListener(ClientEventHandler::handlerRenderLevelStageEvent);
        gameBus.addListener(ClientEventHandler::handlerItemTooltipEvent);
        gameBus.addListener(ClientEventHandler::handlerInputEvent$MouseButton$Pre);
        gameBus.addListener(ClientEventHandler::handlerInputEvent$Key);
        gameBus.addListener(ClientEventHandler::handlerRenderGuiLayerEvent$Pre);
        gameBus.addListener(ClientEventHandler::handlerMovementInputUpdateEvent);
    }
}
