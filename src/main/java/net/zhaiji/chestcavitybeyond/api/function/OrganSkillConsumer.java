package net.zhaiji.chestcavitybeyond.api.function;

import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;

@FunctionalInterface
public interface OrganSkillConsumer {
    /**
     * 器官技能回调
     *
     * @param context 胸腔槽位上下文
     * @return true 表示技能成功执行需要添加冷却，false 表示技能未执行不添加冷却
     */
    boolean apply(ChestCavitySlotContext context);
}
