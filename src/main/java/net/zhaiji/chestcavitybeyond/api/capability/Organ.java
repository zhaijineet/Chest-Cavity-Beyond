package net.zhaiji.chestcavitybeyond.api.capability;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.api.function.OrganTooltipConsumer;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Organ implements IOrgan {
    private final BiConsumer<ResourceLocation, Multimap<Holder<Attribute>, AttributeModifier>> organModifierConsumer;
    private final OrganTooltipConsumer organTooltipConsumer;
    private final Consumer<ChestCavitySlotContext> organTickConsumer;
    private final Consumer<ChestCavitySlotContext> organAddedConsumer;
    private final Consumer<ChestCavitySlotContext> organRemovedConsumer;
    private final boolean hasSkill;
    private final Consumer<ChestCavitySlotContext> organSkillConsumer;

    public Organ(BiConsumer<ResourceLocation, Multimap<Holder<Attribute>, AttributeModifier>> organModifierConsumer, OrganTooltipConsumer organTooltipConsumer, Consumer<ChestCavitySlotContext> organTickConsumer, Consumer<ChestCavitySlotContext> organAddedConsumer, Consumer<ChestCavitySlotContext> organRemovedConsumer, boolean hasSkill, Consumer<ChestCavitySlotContext> organSkillConsumer) {
        this.organModifierConsumer = organModifierConsumer;
        this.organTooltipConsumer = organTooltipConsumer;
        this.organTickConsumer = organTickConsumer;
        this.organAddedConsumer = organAddedConsumer;
        this.organRemovedConsumer = organRemovedConsumer;
        this.hasSkill = hasSkill;
        this.organSkillConsumer = organSkillConsumer;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(ChestCavitySlotContext context) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = LinkedHashMultimap.create();
        organModifierConsumer.accept(context.id(), modifiers);
        return modifiers;
    }

    @Override
    public void organTooltip(ChestCavityData data, ItemStack stack, TooltipsKeyContext keyContext, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        organTooltipConsumer.accept(data, stack, keyContext, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public void tick(ChestCavitySlotContext context) {
        organTickConsumer.accept(context);
    }

    @Override
    public void organAdded(ChestCavitySlotContext context) {
        organAddedConsumer.accept(context);
    }

    @Override
    public void organRemoved(ChestCavitySlotContext context) {
        organRemovedConsumer.accept(context);
    }

    @Override
    public boolean hasSkill() {
        return hasSkill;
    }

    @Override
    public void organSkill(ChestCavitySlotContext context) {
        organSkillConsumer.accept(context);
    }
}
