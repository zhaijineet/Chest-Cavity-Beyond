package net.zhaiji.chestcavitybeyond.compat;

import net.minecraft.resources.ResourceLocation;
import net.zhaiji.chestcavitybeyond.manager.ChestCavityTypeManager;

/**
 * 第三方模组胸腔类型兼容注册管理器
 */
public class CompatChestCavityManager {
    /**
     * 注册所有第三方模组的胸腔类型兼容
     */
    public static void register() {
        // Ribbits
        ChestCavityTypeManager.registerEntity(
            ResourceLocation.fromNamespaceAndPath("ribbits", "ribbit"),
            ChestCavityTypeManager.FROG
        );
    }
}
