package net.zhaiji.chestcavitybeyond.compat;

import net.neoforged.fml.ModList;

/**
 * 管理可选模组兼容，防止未安装的模组类被触发加载。
 */
public class CompatManager {
    public static final boolean SABLE_LOADED = ModList.get().isLoaded("sable");
    public static final boolean APPLESKIN_LOADED = ModList.get().isLoaded("appleskin");
    public static final boolean JEI_LOADED = ModList.get().isLoaded("jei");
}
