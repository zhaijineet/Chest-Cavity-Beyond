package net.zhaiji.chestcavitybeyond.event;

import net.neoforged.bus.api.IEventBus;
import net.zhaiji.chestcavitybeyond.network.PacketManager;

public class CommonEventManager {
    public static void init(IEventBus modEventBus, IEventBus gameBus) {
        CommonEventManager.modBusListener(modEventBus);
        CommonEventManager.gameBusListener(gameBus);
    }

    public static void modBusListener(IEventBus modEventBus) {
        modEventBus.addListener(CommonEventHandler::handlerRegisterCapabilitiesEvent);
        modEventBus.addListener(CommonEventHandler::handlerFMLLoadCompleteEvent);
        modEventBus.addListener(CommonEventHandler::handlerEntityAttributeModificationEvent);
        modEventBus.addListener(PacketManager::handlerRegisterPayloadHandlersEvent);
    }

    public static void gameBusListener(IEventBus gameBus) {
        gameBus.addListener(CommonEventHandler::handlerEntityJoinLevelEvent);
        gameBus.addListener(CommonEventHandler::handlerPlayerEvent$PlayerRespawnEvent);
        gameBus.addListener(CommonEventHandler::handlerMobEffectEvent$Applicable);
        gameBus.addListener(CommonEventHandler::handlerLivingIncomingDamageEvent);
        gameBus.addListener(CommonEventHandler::handlerLivingDamageEvent$Pre);
        gameBus.addListener(CommonEventHandler::handlerPlayerEvent$BreakSpeed);
        gameBus.addListener(CommonEventHandler::handlerEntityTickEvent$Post);
    }
}
