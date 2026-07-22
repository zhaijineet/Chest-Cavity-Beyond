package net.zhaiji.chestcavitybeyond;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.zhaiji.chestcavitybeyond.client.event.ClientEventManager;

@Mod(value = ChestCavityBeyond.MOD_ID, dist = Dist.CLIENT)
public class ChestCavityBeyondClient {
    public ChestCavityBeyondClient(IEventBus modEventBus, ModContainer modContainer) {
        // 注册客户端配置
        modContainer.registerConfig(ModConfig.Type.CLIENT, ChestCavityBeyondClientConfig.SPEC);

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        // 事件注册管理
        ClientEventManager.init(modEventBus, NeoForge.EVENT_BUS);
    }
}
