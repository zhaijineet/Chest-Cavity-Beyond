package net.zhaiji.chestcavitybeyond.api.capability;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;

/**
 * 器官
 */
public interface IOrgan {
    /**
     * 获取器官提供的属性
     *
     * @param context 胸腔槽位上下文
     * @return 器官提供的属性
     */
    default Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(ChestCavitySlotContext context) {
        return HashMultimap.create();
    }

    /**
     * 器官tick
     *
     * @param context 胸腔槽位上下文
     */
    default void tick(ChestCavitySlotContext context) {
    }

    /**
     * 器官被移植进胸腔时调用
     *
     * @param context 胸腔槽位上下文
     */
    default void organAdded(ChestCavitySlotContext context) {
    }

    /**
     * 器官从胸腔中摘除时调用
     *
     * @param context 胸腔槽位上下文
     */
    default void organRemoved(ChestCavitySlotContext context) {
    }
}
