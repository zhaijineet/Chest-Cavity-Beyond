package net.zhaiji.chestcavitybeyond.client.event;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.zhaiji.chestcavitybeyond.client.screen.ChestCavityScreen;
import net.zhaiji.chestcavitybeyond.register.InitMenuType;

public class ClientEventHandler {
    public static void handlerRegisterMenuScreensEvent(RegisterMenuScreensEvent event) {
        event.register(InitMenuType.CHEST_CAVITY.get(), ChestCavityScreen::new);
    }
}
