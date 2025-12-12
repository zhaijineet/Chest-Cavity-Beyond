package net.zhaiji.chestcavitybeyond.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.zhaiji.chestcavitybeyond.api.IOrgan;
import net.zhaiji.chestcavitybeyond.util.ChestCavityClientUtil;

import java.util.List;

public class BaseOrganItem extends Item implements IOrgan {
    private final Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();

    /**
     * 是否可以修改属性
     * <p>
     * 注册完成后请使用{@link #builder()}方法将这个属性设为false
     * </P>
     */
    private boolean canModifyAttribute = true;

    public BaseOrganItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        ChestCavityClientUtil.addOrganTooltips(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers() {
        return modifiers;
    }

    public BaseOrganItem addAttributeModifier(Holder<Attribute> attribute, AttributeModifier modifier) {
        if (canModifyAttribute) {
            modifiers.put(attribute, modifier);
        } else {
            // 在注册完器官后，调用这个方法会抛出异常
            throw new IllegalStateException("Cannot modify attributes after registration is complete. Call this method only during registration.");
        }
        return this;
    }

    public BaseOrganItem builder() {
        canModifyAttribute = false;
        return this;
    }
}
