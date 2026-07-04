package net.zhaiji.chestcavitybeyond.api.function;

import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;

/**
 * 器官拥有者受伤结算后
 */
@FunctionalInterface
public interface AfterHurtConsumer {
    /**
     * @param context 胸腔槽位上下文
     * @param event   受伤后事件（不可变快照，含最终伤害、格挡、盾牌损耗、减伤明细等）
     */
    void accept(ChestCavitySlotContext context, LivingDamageEvent.Post event);
}
