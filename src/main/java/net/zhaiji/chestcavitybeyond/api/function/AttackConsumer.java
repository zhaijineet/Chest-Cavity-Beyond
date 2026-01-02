package net.zhaiji.chestcavitybeyond.api.function;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;

/**
 * 器官拥有者攻击
 */
@FunctionalInterface
public interface AttackConsumer {
    /**
     * @param context         胸腔槽位上下文
     * @param target          攻击目标
     * @param source          伤害源
     * @param damageContainer 伤害容器
     */
    void accept(ChestCavitySlotContext context, LivingEntity target, DamageSource source, DamageContainer damageContainer);
}
