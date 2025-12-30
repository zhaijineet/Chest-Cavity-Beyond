package net.zhaiji.chestcavitybeyond.api.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.manager.ChestCavityManager;

/**
 * 注册胸腔事件
 */
public class RegisterChestCavityEvent extends Event {
    /**
     * 注册胸腔类型
     */
    public ChestCavityType register(String name) {
        return ChestCavityManager.register(name);
    }

    /**
     * 为实体注册胸腔类型
     */
    public void registerEntity(EntityType<? extends LivingEntity> entityType, ChestCavityType chestCavityType) {
        ChestCavityManager.registerEntity(entityType, chestCavityType);
    }
}
