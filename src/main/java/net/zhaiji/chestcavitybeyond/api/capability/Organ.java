package net.zhaiji.chestcavitybeyond.api.capability;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.zhaiji.chestcavitybeyond.api.AttributeEntry;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.api.function.AttackConsumer;
import net.zhaiji.chestcavitybeyond.api.function.HurtConsumer;
import net.zhaiji.chestcavitybeyond.api.function.IncomingDamageConsumer;
import net.zhaiji.chestcavitybeyond.api.function.OrganModifierConsumer;
import net.zhaiji.chestcavitybeyond.api.function.OrganTooltipConsumer;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;
import net.zhaiji.chestcavitybeyond.util.OrganSkillUtil;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;

import java.util.List;
import java.util.function.Consumer;

public class Organ implements IOrgan {
    private final List<AttributeEntry> attributeEntries;
    private final OrganModifierConsumer organModifierConsumer;
    private final OrganTooltipConsumer descriptionTooltipConsumer;
    private final OrganTooltipConsumer attributeTooltipConsumer;
    private final OrganTooltipConsumer skillTooltipConsumer;
    private final Consumer<ChestCavitySlotContext> organTickConsumer;
    private final Consumer<ChestCavitySlotContext> organAddedConsumer;
    private final Consumer<ChestCavitySlotContext> organRemovedConsumer;
    private final boolean hasSkill;
    private final Consumer<ChestCavitySlotContext> organSkillConsumer;
    private final int cooldownTicks;
    private final AttackConsumer attackConsumer;
    private final HurtConsumer hurtConsumer;
    private final IncomingDamageConsumer incomingDamageConsumer;
    private final Consumer<ChestCavitySlotContext> chestCavityOpenConsumer;
    private final Consumer<ChestCavitySlotContext> chestCavityCloseConsumer;

    public Organ(
        List<AttributeEntry> attributeEntries,
        OrganModifierConsumer organModifierConsumer,
        OrganTooltipConsumer descriptionTooltipConsumer,
        OrganTooltipConsumer attributeTooltipConsumer,
        OrganTooltipConsumer skillTooltipConsumer,
        Consumer<ChestCavitySlotContext> organTickConsumer,
        Consumer<ChestCavitySlotContext> organAddedConsumer,
        Consumer<ChestCavitySlotContext> organRemovedConsumer,
        boolean hasSkill,
        Consumer<ChestCavitySlotContext> organSkillConsumer,
        int cooldownTicks,
        AttackConsumer attackConsumer,
        HurtConsumer hurtConsumer,
        IncomingDamageConsumer incomingDamageConsumer,
        Consumer<ChestCavitySlotContext> chestCavityOpenConsumer,
        Consumer<ChestCavitySlotContext> chestCavityCloseConsumer
    ) {
        this.attributeEntries = attributeEntries;
        this.organModifierConsumer = organModifierConsumer;
        this.descriptionTooltipConsumer = descriptionTooltipConsumer;
        this.attributeTooltipConsumer = attributeTooltipConsumer;
        this.skillTooltipConsumer = skillTooltipConsumer;
        this.organTickConsumer = organTickConsumer;
        this.organAddedConsumer = organAddedConsumer;
        this.organRemovedConsumer = organRemovedConsumer;
        this.hasSkill = hasSkill;
        this.organSkillConsumer = organSkillConsumer;
        this.cooldownTicks = cooldownTicks;
        this.attackConsumer = attackConsumer;
        this.hurtConsumer = hurtConsumer;
        this.incomingDamageConsumer = incomingDamageConsumer;
        this.chestCavityOpenConsumer = chestCavityOpenConsumer;
        this.chestCavityCloseConsumer = chestCavityCloseConsumer;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(ChestCavitySlotContext context) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = LinkedHashMultimap.create();
        for (AttributeEntry entry : attributeEntries) {
            AttributeModifier modifier = OrganAttributeUtil.createModifier(context.id(), entry.value(), entry.operation());
            modifiers.put(entry.attribute(), modifier);
        }
        if (context.data() != null && context.entity() != null) {
            organModifierConsumer.accept(context, modifiers);
        }
//        // 防止在 ChestCavityType.builder() 构建时因 context.data() 为 null 而抛出异常
//        try {
//            organModifierConsumer.accept(context, modifiers);
//        } catch (NullPointerException e) {
//            // 静默忽略，因为构建时可能没有实体数据
//        }
        return modifiers;
    }

    @Override
    public void descriptionTooltip(
        ChestCavityData data,
        ItemStack stack,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        descriptionTooltipConsumer.accept(data, stack, keyContext, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public void attributeTooltip(
        ChestCavityData data,
        ItemStack stack,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        OrganTooltipConsumer consumer = attributeTooltipConsumer;
        if (consumer == null) {
            consumer = TooltipUtil.DEFAULT_ATTRIBUTE_TOOLTIP;
        }
        consumer.accept(data, stack, keyContext, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public void skillTooltip(
        ChestCavityData data,
        ItemStack stack,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        skillTooltipConsumer.accept(data, stack, keyContext, context, tooltipComponents, tooltipFlag);
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
        if (cooldownTicks > 0 && OrganSkillUtil.hasCooldown(context.entity(), context.stack())) {
            return;
        }
        organSkillConsumer.accept(context);
        if (cooldownTicks > 0) {
            OrganSkillUtil.addCooldown(context.entity(), context.stack(), cooldownTicks);
        }
    }

    @Override
    public void incomingDamage(ChestCavitySlotContext context, LivingIncomingDamageEvent event) {
        incomingDamageConsumer.accept(context, event);
    }

    @Override
    public void attack(ChestCavitySlotContext context, LivingEntity target, DamageSource source, DamageContainer damageContainer) {
        attackConsumer.accept(context, target, source, damageContainer);
    }

    @Override
    public void hurt(ChestCavitySlotContext context, DamageSource source, DamageContainer damageContainer) {
        hurtConsumer.accept(context, source, damageContainer);
    }

    @Override
    public int getCooldownTicks() {
        return cooldownTicks;
    }

    @Override
    public void chestCavityOpen(ChestCavitySlotContext context) {
        chestCavityOpenConsumer.accept(context);
    }

    @Override
    public void chestCavityClose(ChestCavitySlotContext context) {
        chestCavityCloseConsumer.accept(context);
    }
}
