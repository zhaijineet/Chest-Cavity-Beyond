package net.zhaiji.chestcavitybeyond.event;

import net.neoforged.bus.api.IEventBus;

public class CommonEventManager {
    public static void init(IEventBus modEventBus, IEventBus gameBus) {
        CommonEventManager.modBusListener(modEventBus);
        CommonEventManager.gameBusListener(gameBus);
    }

    public static void modBusListener(IEventBus modEventBus) {
        modEventBus.addListener(CommonEventHandler::handlerEntityAttributeModificationEvent);
    }

    public static void gameBusListener(IEventBus gameBus) {
        gameBus.addListener(CommonEventHandler::handlerEntityJoinLevelEvent);
        gameBus.addListener(CommonEventHandler::handlerMobEffectEvent$Added);
        gameBus.addListener(CommonEventHandler::handlerLivingDamageEvent$Pre);
        gameBus.addListener(CommonEventHandler::handlerEntityTickEvent$Post);
    }
}
