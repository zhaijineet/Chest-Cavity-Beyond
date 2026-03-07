package net.zhaiji.chestcavitybeyond.api.function;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.zhaiji.chestcavitybeyond.api.task.ISerializableTask;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;

/**
 * Task反序列化器函数式接口
 * 参考AttackConsumer、HurtConsumer的设计，不使用泛型
 */
@FunctionalInterface
public interface TaskDeserializer {
    /**
     * 从NBT反序列化task
     *
     * @param data     胸腔数据
     * @param provider HolderLookup.Provider
     * @param nbt      NBT数据
     * @return 反序列化的task实例
     */
    ISerializableTask deserialize(ChestCavityData data, HolderLookup.Provider provider, CompoundTag nbt);
}
