package net.zhaiji.chestcavitybeyond.api.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.api.function.TaskDeserializer;
import net.zhaiji.chestcavitybeyond.manager.ChestCavityTypeManager;
import net.zhaiji.chestcavitybeyond.manager.TaskManager;

/**
 * 注册胸腔事件
 */
public class ChestCavityRegisterEvent extends Event implements IModBusEvent {
    /**
     * 注册胸腔类型
     */
    public ChestCavityType registerType(String name) {
        return ChestCavityTypeManager.register(name);
    }

    /**
     * 为实体注册胸腔类型
     */
    public void registerEntity(EntityType<? extends LivingEntity> entityType, ChestCavityType chestCavityType) {
        ChestCavityTypeManager.registerEntity(entityType, chestCavityType);
    }

    /**
     * 注册可序列化的task类型
     *
     * @param type         Task类型
     * @param deserializer 反序列化器
     */
    public void registerTask(ResourceLocation type, TaskDeserializer deserializer) {
        TaskManager.registerTask(type, deserializer);
    }
}
