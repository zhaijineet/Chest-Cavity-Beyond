package net.zhaiji.chestcavitybeyond.api.capability;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Organ implements IOrgan {
    private final BiConsumer<ResourceLocation, Multimap<Holder<Attribute>, AttributeModifier>> organModifierConsumer;
    private final Consumer<ChestCavitySlotContext> organTickConsumer;
    private final Consumer<ChestCavitySlotContext> organAddedConsumer;
    private final Consumer<ChestCavitySlotContext> organRemovedConsumer;

    public Organ(BiConsumer<ResourceLocation, Multimap<Holder<Attribute>, AttributeModifier>> organModifierConsumer, Consumer<ChestCavitySlotContext> organTickConsumer, Consumer<ChestCavitySlotContext> organAddedConsumer, Consumer<ChestCavitySlotContext> organRemovedConsumer) {
        this.organModifierConsumer = organModifierConsumer;
        this.organTickConsumer = organTickConsumer;
        this.organAddedConsumer = organAddedConsumer;
        this.organRemovedConsumer = organRemovedConsumer;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(ChestCavitySlotContext context) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();
        organModifierConsumer.accept(context.id(), modifiers);
        return modifiers;
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
}
