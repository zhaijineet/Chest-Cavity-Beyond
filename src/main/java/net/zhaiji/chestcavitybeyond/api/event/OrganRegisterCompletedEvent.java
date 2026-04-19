package net.zhaiji.chestcavitybeyond.api.event;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.zhaiji.chestcavitybeyond.api.capability.Organ;
import net.zhaiji.chestcavitybeyond.manager.OrganManager;

import java.util.Map;

/**
 * 器官注册完成事件
 * <p>
 * 在 {@code OrganRegisterEvent} 之后触发，供外部模组替换或删除已有器官。
 * </p>
 */
public class OrganRegisterCompletedEvent extends Event implements IModBusEvent {
    /**
     * 替换物品的器官能力
     *
     * @param item  物品
     * @param organ 器官
     */
    public void replaceOrgan(Item item, Organ organ) {
        OrganManager.getRegistry().put(item, organ);
    }

    /**
     * 删除物品的器官能力
     *
     * @param item 物品
     */
    public void removeOrgan(Item item) {
        OrganManager.getRegistry().remove(item);
    }

    /**
     * 获取器官注册表
     *
     * @return 器官注册表
     */
    public Map<Item, Organ> getOrganRegistry() {
        return OrganManager.getRegistry();
    }
}
