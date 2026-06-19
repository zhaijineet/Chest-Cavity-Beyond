package net.zhaiji.chestcavitybeyond;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.zhaiji.chestcavitybeyond.event.CommonEventManager;
import net.zhaiji.chestcavitybeyond.register.InitLootModifier;
import net.zhaiji.chestcavitybeyond.register.InitAttachmentType;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.register.InitCreativeModeTab;
import net.zhaiji.chestcavitybeyond.register.InitEffect;
import net.zhaiji.chestcavitybeyond.register.InitEntityType;
import net.zhaiji.chestcavitybeyond.register.InitItem;
import net.zhaiji.chestcavitybeyond.register.InitMenuType;
import net.zhaiji.chestcavitybeyond.register.InitRecipe;

@Mod(ChestCavityBeyond.MOD_ID)
public class ChestCavityBeyond {
    public static final String MOD_ID = "chestcavitybeyond";

    public ChestCavityBeyond(IEventBus modEventBus, ModContainer modContainer) {
        // 配置文件
        modContainer.registerConfig(ModConfig.Type.COMMON, ChestCavityBeyondConfig.SPEC);

        // 注册
        InitItem.ITEM.register(modEventBus);
        InitEntityType.ENTITY_TYPE.register(modEventBus);
        InitCreativeModeTab.CREATIVE_MODE_TAB.register(modEventBus);
        InitAttribute.ATTRIBUTE.register(modEventBus);
        InitAttachmentType.ATTACHMENT_TYPE.register(modEventBus);
        InitMenuType.MENU_TYPE.register(modEventBus);
        InitEffect.EFFECT.register(modEventBus);
        InitRecipe.RECIPE_SERIALIZERS.register(modEventBus);
        InitLootModifier.LOOT_MODIFIER.register(modEventBus);

        // 事件注册管理
        CommonEventManager.init(modEventBus, NeoForge.EVENT_BUS);
    }

    /**
     * 获取模组标识符
     *
     * @param name 名字
     * @return 标识符
     */
    public static ResourceLocation of(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}
