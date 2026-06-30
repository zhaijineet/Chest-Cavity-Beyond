package net.zhaiji.chestcavitybeyond.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;

/**
 * 器官更换事件
 * 当胸腔中的器官被更换时触发此事件
 */
public class OrganChangeEvent extends Event {
    private final ChestCavityData data;
    private final int index;
    private final ItemStack oldStack;
    private final ItemStack newStack;

    public OrganChangeEvent(ChestCavityData data, int index, ItemStack oldStack, ItemStack newStack) {
        this.data = data;
        this.index = index;
        this.oldStack = oldStack;
        this.newStack = newStack;
    }

    /**
     * 获取胸腔数据
     */
    public ChestCavityData getData() {
        return data;
    }

    /**
     * 获取拥有胸腔的实体
     */
    public LivingEntity getEntity() {
        return data.getOwner();
    }

    /**
     * 获取槽位索引
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取被移除的器官物品
     */
    public ItemStack getOldStack() {
        return oldStack;
    }

    /**
     * 获取新添加的器官物品
     */
    public ItemStack getNewStack() {
        return newStack;
    }
}
