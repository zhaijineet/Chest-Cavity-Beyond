package net.zhaiji.chestcavitybeyond.manager;

import net.minecraft.world.item.Item;
import net.zhaiji.chestcavitybeyond.api.capability.Organ;

import java.util.HashMap;
import java.util.Map;

public class OrganManager {
    public static final Map<Item, Organ> ORGAN_REGISTRY = new HashMap<>();

    /**
     * 获取器官注册表
     *
     * @return 器官注册表
     */
    public static Map<Item, Organ> getRegistry() {
        return ORGAN_REGISTRY;
    }
}
