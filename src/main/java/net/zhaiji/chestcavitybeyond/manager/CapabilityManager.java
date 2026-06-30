package net.zhaiji.chestcavitybeyond.manager;

import net.neoforged.neoforge.capabilities.ItemCapability;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.capability.Organ;

public class CapabilityManager {
    /**
     * 器官Capability
     */
    public static final ItemCapability<Organ, Void> ORGAN = ItemCapability.createVoid(ChestCavityBeyond.of("organ"), Organ.class);
}
