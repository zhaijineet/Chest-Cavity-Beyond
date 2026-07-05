package net.zhaiji.chestcavitybeyond.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;

/**
 * 器官技能因冷却被阻止事件
 */
public class OrganSkillOnCooldownEvent extends Event {
    private final ChestCavityData data;
    private final int index;
    private final ItemStack stack;

    public OrganSkillOnCooldownEvent(ChestCavityData data, int index, ItemStack stack) {
        this.data = data;
        this.index = index;
        this.stack = stack;
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
     * 获取器官物品
     */
    public ItemStack getStack() {
        return stack;
    }
}
