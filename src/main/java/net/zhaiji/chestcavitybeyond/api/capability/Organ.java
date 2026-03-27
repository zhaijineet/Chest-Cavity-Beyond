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
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.zhaiji.chestcavitybeyond.api.AttributeEntry;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.api.function.AttackConsumer;
import net.zhaiji.chestcavitybeyond.api.function.HealConsumer;
import net.zhaiji.chestcavitybeyond.api.function.HurtConsumer;
import net.zhaiji.chestcavitybeyond.api.function.IncomingDamageConsumer;
import net.zhaiji.chestcavitybeyond.api.function.OrganModifierConsumer;
import net.zhaiji.chestcavitybeyond.api.function.OrganTooltipConsumer;
import net.zhaiji.chestcavitybeyond.api.function.OtherOrganChangeConsumer;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.manager.OrganManager;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;
import net.zhaiji.chestcavitybeyond.util.OrganSkillUtil;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Organ implements IOrgan {
    private final List<AttributeEntry> attributeEntries;
    private final OrganModifierConsumer organModifierConsumer;
    private final OrganTooltipConsumer descriptionTooltipConsumer;
    private final OrganTooltipConsumer attributeTooltipConsumer;
    private final OrganTooltipConsumer skillTooltipConsumer;
    private final Consumer<ChestCavitySlotContext> organTickConsumer;
    private final Consumer<ChestCavitySlotContext> organAddedConsumer;
    private final Consumer<ChestCavitySlotContext> organRemovedConsumer;
    private final OtherOrganChangeConsumer otherOrganChangeConsumer;
    private final boolean hasSkill;
    private final Consumer<ChestCavitySlotContext> organSkillConsumer;
    private final int cooldownTicks;
    private final Consumer<ChestCavitySlotContext> organSkillOnCooldownConsumer;
    private final AttackConsumer attackConsumer;
    private final HurtConsumer hurtConsumer;
    private final HealConsumer healConsumer;
    private final IncomingDamageConsumer incomingDamageConsumer;
    private final Consumer<ChestCavitySlotContext> chestCavityOpenConsumer;
    private final Consumer<ChestCavitySlotContext> chestCavityCloseConsumer;

    private Organ(Builder builder) {
        this.attributeEntries = builder.attributeEntries;
        this.organModifierConsumer = builder.organModifierConsumer;
        this.descriptionTooltipConsumer = builder.descriptionTooltipConsumer;
        this.attributeTooltipConsumer = builder.attributeTooltipConsumer;
        this.skillTooltipConsumer = builder.skillTooltipConsumer;
        this.organTickConsumer = builder.organTickConsumer;
        this.organAddedConsumer = builder.organAddedConsumer;
        this.organRemovedConsumer = builder.organRemovedConsumer;
        this.otherOrganChangeConsumer = builder.otherOrganChangeConsumer;
        this.hasSkill = builder.hasSkill;
        this.organSkillConsumer = builder.organSkillConsumer;
        this.cooldownTicks = builder.cooldownTicks;
        this.organSkillOnCooldownConsumer = builder.organSkillOnCooldownConsumer;
        this.attackConsumer = builder.attackConsumer;
        this.hurtConsumer = builder.hurtConsumer;
        this.healConsumer = builder.healConsumer;
        this.incomingDamageConsumer = builder.incomingDamageConsumer;
        this.chestCavityOpenConsumer = builder.chestCavityOpenConsumer;
        this.chestCavityCloseConsumer = builder.chestCavityCloseConsumer;
    }

    /**
     * 新建构建器
     *
     * @return 构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Item item) {
        return new Builder(item);
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
    public void otherOrganChange(ChestCavitySlotContext context, int changedIndex, ItemStack oldStack, ItemStack newStack) {
        otherOrganChangeConsumer.accept(context, changedIndex, oldStack, newStack);
    }

    @Override
    public boolean hasSkill() {
        return hasSkill;
    }

    @Override
    public void organSkill(ChestCavitySlotContext context) {
        if (cooldownTicks > 0 && OrganSkillUtil.hasCooldown(context.entity(), context.stack())) {
            organSkillOnCooldownConsumer.accept(context);
            return;
        }
        organSkillConsumer.accept(context);
        if (cooldownTicks > 0) {
            OrganSkillUtil.addCooldown(context.entity(), context.stack(), cooldownTicks);
        }
    }

    @Override
    public void organSkillOnCooldown(ChestCavitySlotContext context) {
        organSkillOnCooldownConsumer.accept(context);
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
    public void heal(ChestCavitySlotContext context, LivingHealEvent event) {
        healConsumer.accept(context, event);
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

    public static class Builder {
        private static final OrganModifierConsumer EMPTY_MODIFIER = (context, modifiers) -> {
        };
        private static final OrganTooltipConsumer EMPTY_TOOLTIP = (data, stack, keyContext, context, tooltipComponents, tooltipFlag) -> {
        };
        private static final Consumer<ChestCavitySlotContext> EMPTY_CONSUMER = context -> {
        };
        private static final AttackConsumer EMPTY_ATTACK = (context, target, source, damageContainer) -> {
        };
        private static final HurtConsumer EMPTY_HURT = (context, source, damageContainer) -> {
        };
        private static final HealConsumer EMPTY_HEAL = (context, event) -> {
        };
        private static final IncomingDamageConsumer EMPTY_INCOMING_DAMAGE = (context, event) -> {
        };
        private static final Consumer<ChestCavitySlotContext> EMPTY_CHEST_CAVITY_OPEN = context -> {
        };
        private static final Consumer<ChestCavitySlotContext> EMPTY_CHEST_CAVITY_CLOSE = context -> {
        };
        private static final OtherOrganChangeConsumer EMPTY_OTHER_ORGAN_CHANGE = (context, changedIndex, oldStack, newStack) -> {
        };
        private final Item.Properties properties = new Item.Properties().stacksTo(1);
        private final List<AttributeEntry> attributeEntries = new ArrayList<>();
        private Function<Item.Properties, Item> itemFunction = null;
        private OrganModifierConsumer organModifierConsumer = EMPTY_MODIFIER;
        private OrganTooltipConsumer descriptionTooltipConsumer = EMPTY_TOOLTIP;
        private OrganTooltipConsumer attributeTooltipConsumer = null;
        private OrganTooltipConsumer skillTooltipConsumer = EMPTY_TOOLTIP;
        private Consumer<ChestCavitySlotContext> organTickConsumer = EMPTY_CONSUMER;
        private Consumer<ChestCavitySlotContext> organAddedConsumer = EMPTY_CONSUMER;
        private Consumer<ChestCavitySlotContext> organRemovedConsumer = EMPTY_CONSUMER;
        private OtherOrganChangeConsumer otherOrganChangeConsumer = EMPTY_OTHER_ORGAN_CHANGE;
        private boolean hasSkill = false;
        private Consumer<ChestCavitySlotContext> organSkillConsumer = EMPTY_CONSUMER;
        private int cooldownTicks = 0;
        private Consumer<ChestCavitySlotContext> organSkillOnCooldownConsumer = EMPTY_CONSUMER;
        private AttackConsumer attackConsumer = EMPTY_ATTACK;
        private HurtConsumer hurtConsumer = EMPTY_HURT;
        private HealConsumer healConsumer = EMPTY_HEAL;
        private IncomingDamageConsumer incomingDamageConsumer = EMPTY_INCOMING_DAMAGE;
        private Consumer<ChestCavitySlotContext> chestCavityOpenConsumer = EMPTY_CHEST_CAVITY_OPEN;
        private Consumer<ChestCavitySlotContext> chestCavityCloseConsumer = EMPTY_CHEST_CAVITY_CLOSE;

        public Builder() {
        }

        public Builder(Item item) {
            itemFunction = properties -> item;
        }

        public Builder(Function<Item.Properties, Item> itemFunction) {
            this.itemFunction = itemFunction;
        }

        /**
         * 设置器官物品属性
         */
        public Builder properties(Consumer<Item.Properties> itemPropertiesConsumer) {
            itemPropertiesConsumer.accept(properties);
            return this;
        }

        /**
         * 设置器官描述工具提示
         */
        public Builder descriptionTooltip(OrganTooltipConsumer descriptionTooltipConsumer) {
            this.descriptionTooltipConsumer = descriptionTooltipConsumer;
            return this;
        }

        /**
         * 设置器官工具提示
         */
        public Builder attributeTooltip(OrganTooltipConsumer attributeTooltipConsumer) {
            this.attributeTooltipConsumer = attributeTooltipConsumer;
            return this;
        }

        /**
         * 设置技能描述工具提示
         */
        public Builder skillTooltip(OrganTooltipConsumer skillTooltipConsumer) {
            this.skillTooltipConsumer = skillTooltipConsumer;
            return this;
        }

        /**
         * 设置器官提供的属性修饰符
         */
        public Builder modifier(OrganModifierConsumer organModifierConsumer) {
            this.organModifierConsumer = organModifierConsumer;
            return this;
        }

        /**
         * 添加属性修饰符
         *
         * @param attribute 属性
         * @param value     值
         * @param operation 操作类型
         * @return 构建器
         */
        public Builder addAttribute(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
            attributeEntries.add(new AttributeEntry(attribute, value, operation));
            return this;
        }

        /**
         * 添加基础值加算属性修饰符
         *
         * @param attribute 属性
         * @param value     值
         * @return 构建器
         */
        public Builder addValueAttribute(Holder<Attribute> attribute, double value) {
            return addAttribute(attribute, value, AttributeModifier.Operation.ADD_VALUE);
        }

        /**
         * 添加基础值乘算属性修饰符
         *
         * @param attribute 属性
         * @param value     值
         * @return 构建器
         */
        public Builder baseMultipliedAttribute(Holder<Attribute> attribute, double value) {
            return addAttribute(attribute, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        }

        /**
         * 添加最终乘算属性修饰符
         *
         * @param attribute 属性
         * @param value     值
         * @return 构建器
         */
        public Builder totalMultipliedAttribute(Holder<Attribute> attribute, double value) {
            return addAttribute(attribute, value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        }

        /**
         * 设置器官tick触发器
         */
        public Builder tick(Consumer<ChestCavitySlotContext> organTickConsumer) {
            this.organTickConsumer = organTickConsumer;
            return this;
        }

        /**
         * 设置器官移植触发器
         */
        public Builder added(Consumer<ChestCavitySlotContext> organAddedConsumer) {
            this.organAddedConsumer = organAddedConsumer;
            return this;
        }

        /**
         * 设置器官摘除触发器
         */
        public Builder removed(Consumer<ChestCavitySlotContext> organRemovedConsumer) {
            this.organRemovedConsumer = organRemovedConsumer;
            return this;
        }

        /**
         * 设置其他器官变化时的回调
         */
        public Builder otherChange(OtherOrganChangeConsumer otherOrganChangeConsumer) {
            this.otherOrganChangeConsumer = otherOrganChangeConsumer;
            return this;
        }

        /**
         * 设置器官技能
         */
        public Builder skill(Consumer<ChestCavitySlotContext> organSkillConsumer) {
            hasSkill = true;
            this.organSkillConsumer = organSkillConsumer;
            return this;
        }

        /**
         * 设置器官技能冷却时间（tick）
         */
        public Builder cooldown(int cooldownTicks) {
            this.cooldownTicks = cooldownTicks;
            return this;
        }

        /**
         * 设置器官技能冷却时的回调
         */
        public Builder skillOnCooldown(Consumer<ChestCavitySlotContext> organSkillOnCooldownConsumer) {
            this.organSkillOnCooldownConsumer = organSkillOnCooldownConsumer;
            return this;
        }

        /**
         * 设置器官拥有者攻击触发器
         */
        public Builder attack(AttackConsumer attackConsumer) {
            this.attackConsumer = attackConsumer;
            return this;
        }

        /**
         * 设置器官拥有者受伤触发器
         */
        public Builder hurt(HurtConsumer hurtConsumer) {
            this.hurtConsumer = hurtConsumer;
            return this;
        }

        /**
         * 设置器官拥有者被治疗触发器
         */
        public Builder heal(HealConsumer healConsumer) {
            this.healConsumer = healConsumer;
            return this;
        }

        /**
         * 设置器官拥有者受到伤害前触发器
         */
        public Builder incomingDamage(IncomingDamageConsumer incomingDamageConsumer) {
            this.incomingDamageConsumer = incomingDamageConsumer;
            return this;
        }

        /**
         * 设置胸腔打开触发器
         */
        public Builder chestCavityOpen(Consumer<ChestCavitySlotContext> chestCavityOpenConsumer) {
            this.chestCavityOpenConsumer = chestCavityOpenConsumer;
            return this;
        }

        /**
         * 设置胸腔关闭触发器
         */
        public Builder chestCavityClose(Consumer<ChestCavitySlotContext> chestCavityCloseConsumer) {
            this.chestCavityCloseConsumer = chestCavityCloseConsumer;
            return this;
        }

        /**
         * 构建
         */
        public Item build() {
            Item item;
            if (itemFunction == null) {
                item = new Item(properties);
            } else {
                item = itemFunction.apply(properties);
            }
            OrganManager.getRegistry().put(item, new Organ(this));
            return item;
        }
    }
}
