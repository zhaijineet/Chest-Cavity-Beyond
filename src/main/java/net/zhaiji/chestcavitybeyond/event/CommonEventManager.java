package net.zhaiji.chestcavitybeyond.event;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
import net.zhaiji.chestcavitybeyond.network.PacketManager;

public class CommonEventManager {
    public static void init(IEventBus modBus, IEventBus gameBus) {
        CommonEventManager.modBusListener(modBus);
        CommonEventManager.gameBusListener(gameBus);
    }

    public static void modBusListener(IEventBus modBus) {
        modBus.addListener(ChestCavityBeyondConfig::handlerModConfigEvent);
        modBus.addListener(CommonEventHandler::handlerRegisterCapabilitiesEvent);
        modBus.addListener(CommonEventHandler::handlerFMLCommonSetupEvent);
        modBus.addListener(CommonEventHandler::handlerFMLLoadCompleteEvent);
        modBus.addListener(CommonEventHandler::handlerEntityAttributeModificationEvent);
        modBus.addListener(PacketManager::handlerRegisterPayloadHandlersEvent);
    }

    public static void gameBusListener(IEventBus gameBus) {
        gameBus.addListener(CommonEventHandler::handlerEntityJoinLevelEvent);
        gameBus.addListener(CommonEventHandler::handlerPlayerEvent$PlayerRespawnEvent);
        gameBus.addListener(CommonEventHandler::handlerPlayerInteractEvent$EntityInteract);
        gameBus.addListener(CommonEventHandler::handlerMobEffectEvent$Applicable);
        gameBus.addListener(CommonEventHandler::handlerLivingIncomingDamageEvent);
        gameBus.addListener(CommonEventHandler::handlerLivingHealEvent);
        gameBus.addListener(CommonEventHandler::handlerLivingDamageEvent$Pre);
        gameBus.addListener(CommonEventHandler::handlerLivingDeathEvent);
        gameBus.addListener(EventPriority.HIGH, CommonEventHandler::handlerLivingConversionEvent$Post$Clean);
        gameBus.addListener(EventPriority.LOW, CommonEventHandler::handlerLivingConversionEvent$Post);
        gameBus.addListener(CommonEventHandler::handlerPlayerEvent$BreakSpeed);
        gameBus.addListener(CommonEventHandler::handlerEntityTickEvent$Post);
        gameBus.addListener(CommonEventHandler::handlerRegisterCommandsEvent);
    }
}
