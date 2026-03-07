package net.zhaiji.chestcavitybeyond.manager;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.zhaiji.chestcavitybeyond.api.function.TaskDeserializer;
import net.zhaiji.chestcavitybeyond.api.task.ISerializableTask;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;

import java.util.HashMap;
import java.util.Map;

/**
 * Task类型注册管理器
 */
public class TaskManager {
    private static final Map<ResourceLocation, TaskDeserializer> TASK_REGISTRY = new HashMap<>();

    /**
     * 注册task类型
     *
     * @param type         Task类型
     * @param deserializer 反序列化器
     */
    public static void registerTask(ResourceLocation type, TaskDeserializer deserializer) {
        TASK_REGISTRY.put(type, deserializer);
    }

    /**
     * 反序列化task
     *
     * @param type     Task类型
     * @param provider HolderLookup.Provider
     * @param nbt      NBT数据
     * @return 反序列化的task实例，如果类型未注册则返回null
     */
    public static ISerializableTask deserializeTask(ChestCavityData data,ResourceLocation type, HolderLookup.Provider provider, CompoundTag nbt) {
        TaskDeserializer deserializer = TASK_REGISTRY.get(type);
        return deserializer != null ? deserializer.deserialize(data, provider, nbt) : null;
    }
}
