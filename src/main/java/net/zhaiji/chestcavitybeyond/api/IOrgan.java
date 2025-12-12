package net.zhaiji.chestcavitybeyond.api;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/**
 * 器官
 */
public interface IOrgan {
    /**
     * @return 器官提供的属性
     */
    default Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers() {
        return HashMultimap.create();
    }
}
