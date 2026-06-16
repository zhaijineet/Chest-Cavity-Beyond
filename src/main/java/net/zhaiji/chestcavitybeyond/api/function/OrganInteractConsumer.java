package net.zhaiji.chestcavitybeyond.api.function;

import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.OrganInteractContext;

/**
 * 器官被动交互回调
 */
@FunctionalInterface
public interface OrganInteractConsumer {
    /**
     * @param context         胸腔槽位上下文
     * @param interactContext 交互上下文
     */
    void accept(ChestCavitySlotContext context, OrganInteractContext interactContext);
}
