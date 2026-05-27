package net.zhaiji.chestcavitybeyond.api.task;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * 器官冷却任务（仅用于非玩家实体）
 * <p>
 * 存储冷却的 Item 和剩余冷却时间（tick），每 tick 递减。
 * 归零后 canRemove 返回 true，自动从任务队列中移除。
 * </p>
 */
public class OrganCooldownTask implements IChestCavityTask {
    private final Item item;
    private int remainingTicks;

    public OrganCooldownTask(Item item, int cooldownTicks) {
        this.item = item;
        this.remainingTicks = cooldownTicks;
    }

    public Item getItem() {
        return item;
    }

    public int getRemainingTicks() {
        return remainingTicks;
    }

    /**
     * 刷新冷却时间
     */
    public void setOrRefresh(int cooldownTicks) {
        remainingTicks = cooldownTicks;
    }

    /**
     * 判断此冷却任务是否匹配指定物品
     */
    public boolean isOnCooldown(Item item) {
        return this.item == item;
    }

    /**
     * 判断此冷却任务是否匹配指定物品栈
     */
    public boolean isOnCooldown(ItemStack stack) {
        return item == stack.getItem();
    }

    @Override
    public void tick(LivingEntity entity) {
        remainingTicks--;
    }

    @Override
    public boolean canRemove(LivingEntity entity) {
        return remainingTicks <= 0;
    }
}
