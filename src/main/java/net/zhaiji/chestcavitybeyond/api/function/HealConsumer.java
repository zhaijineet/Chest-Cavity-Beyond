package net.zhaiji.chestcavitybeyond.api.function;

import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;

/**
 * 器官拥有者被治疗
 */
@FunctionalInterface
public interface HealConsumer {
    /**
     * @param context 胸腔槽位上下文
     * @param event   治疗事件
     */
    void accept(ChestCavitySlotContext context, LivingHealEvent event);
}
