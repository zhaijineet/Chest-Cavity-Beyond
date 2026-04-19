package net.zhaiji.chestcavitybeyond.api.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.manager.ChestCavityTypeManager;

import java.util.Map;

public class ChestCavityRegisterCompletedEvent extends Event implements IModBusEvent {
    /**
     * 获取胸腔类型注册表
     *
     * @return 胸腔类型注册表
     */
    public Map<String, ChestCavityType> getChestCavityTypeRegistry() {
        return ChestCavityTypeManager.CHEST_CAVITY_TYPE_REGISTRY;
    }

    /**
     * 获取实体-胸腔类型映射
     *
     * @return 实体-胸腔类型映射
     */
    public Map<EntityType<? extends LivingEntity>, ChestCavityType> getEntityChestCavityTypeMap() {
        return ChestCavityTypeManager.ENTITY_CHEST_CAVITY_TYPE_MAP;
    }

    /**
     * 替换实体关联的胸腔类型
     *
     * @param entityType      实体类型
     * @param chestCavityType 胸腔类型
     */
    public void replaceEntityChestCavityType(EntityType<? extends LivingEntity> entityType, ChestCavityType chestCavityType) {
        ChestCavityTypeManager.ENTITY_CHEST_CAVITY_TYPE_MAP.put(entityType, chestCavityType);
    }

    /**
     * 删除实体与胸腔类型的关联
     *
     * @param entityType 实体类型
     */
    public void removeEntityChestCavityType(EntityType<? extends LivingEntity> entityType) {
        ChestCavityTypeManager.ENTITY_CHEST_CAVITY_TYPE_MAP.remove(entityType);
    }
}
