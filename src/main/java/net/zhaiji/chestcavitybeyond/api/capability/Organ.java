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
import net.zhaiji.chestcavitybeyond.api.OrganInteractContext;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.api.function.AttackConsumer;
import net.zhaiji.chestcavitybeyond.api.function.HealConsumer;
import net.zhaiji.chestcavitybeyond.api.function.HurtConsumer;
import net.zhaiji.chestcavitybeyond.api.function.IncomingDamageConsumer;
import net.zhaiji.chestcavitybeyond.api.function.OrganInteractConsumer;
import net.zhaiji.chestcavitybeyond.api.function.OrganModifierConsumer;
import net.zhaiji.chestcavitybeyond.api.function.OrganSkillFunction;
import net.zhaiji.chestcavitybeyond.api.function.OrganTooltipConsumer;
import net.zhaiji.chestcavitybeyond.api.function.OtherOrganChangeConsumer;
import net.zhaiji.chestcavitybeyond.api.goal.GoalSkillMetadata;
import net.zhaiji.chestcavitybeyond.manager.OrganManager;
import net.zhaiji.chestcavitybeyond.register.InitEnchantment;
import net.zhaiji.chestcavitybeyond.util.EnchantmentUtil;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;
import net.zhaiji.chestcavitybeyond.util.OrganSkillUtil;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class Organ {
    /**
     * 无行为的空器官单例，用于表示物品不具备器官能力
     */
    public static final Organ EMPTY = new Organ();

    private static final OrganModifierConsumer EMPTY_MODIFIER = (context, modifiers) -> {
    };
    private static final OrganTooltipConsumer EMPTY_TOOLTIP = (slotContext, keyContext, context, tooltipComponents, tooltipFlag) -> {
    };
    private static final Consumer<ChestCavitySlotContext> EMPTY_CONSUMER = context -> {
    };
    private static final ToIntFunction<ChestCavitySlotContext> EMPTY_COOLDOWN = context -> 0;
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
    private static final OrganSkillFunction EMPTY_SKILL = context -> false;
    private static final OrganInteractConsumer EMPTY_INTERACT = (context, interactContext) -> {
    };

    private final List<AttributeEntry> attributeEntries;
    private final OrganModifierConsumer organModifierConsumer;
    private final OrganTooltipConsumer tooltipConsumer;
    private final Consumer<ChestCavitySlotContext> organTickConsumer;
    private final Consumer<ChestCavitySlotContext> organAddedConsumer;
    private final Consumer<ChestCavitySlotContext> organRemovedConsumer;
    private final OtherOrganChangeConsumer otherOrganChangeConsumer;
    private final boolean refreshOnOrganChange;
    private final boolean hasSkill;
    private final OrganSkillFunction organSkillFunction;
    private final ToIntFunction<ChestCavitySlotContext> cooldownTicksFunction;
    private final Consumer<ChestCavitySlotContext> organSkillOnCooldownConsumer;
    private final AttackConsumer attackConsumer;
    private final HurtConsumer hurtConsumer;
    private final HealConsumer healConsumer;
    private final IncomingDamageConsumer incomingDamageConsumer;
    private final Consumer<ChestCavitySlotContext> chestCavityOpenConsumer;
    private final Consumer<ChestCavitySlotContext> chestCavityCloseConsumer;
    private final OrganInteractConsumer interactConsumer;
    private final GoalSkillMetadata goalSkillMetadata;

    private Organ(AbstractBuilder<?> builder) {
        this.attributeEntries = builder.attributeEntries;
        this.organModifierConsumer = builder.organModifierConsumer;
        this.tooltipConsumer = builder.tooltipConsumer;
        this.organTickConsumer = builder.organTickConsumer;
        this.organAddedConsumer = builder.organAddedConsumer;
        this.organRemovedConsumer = builder.organRemovedConsumer;
        this.otherOrganChangeConsumer = builder.otherOrganChangeConsumer;
        this.refreshOnOrganChange = builder.refreshOnOrganChange;
        this.hasSkill = builder.hasSkill;
        this.organSkillFunction = builder.organSkillFunction;
        this.cooldownTicksFunction = builder.cooldownTicksFunction;
        this.organSkillOnCooldownConsumer = builder.organSkillOnCooldownConsumer;
        this.attackConsumer = builder.attackConsumer;
        this.hurtConsumer = builder.hurtConsumer;
        this.healConsumer = builder.healConsumer;
        this.incomingDamageConsumer = builder.incomingDamageConsumer;
        this.chestCavityOpenConsumer = builder.chestCavityOpenConsumer;
        this.chestCavityCloseConsumer = builder.chestCavityCloseConsumer;
        this.interactConsumer = builder.interactConsumer;
        this.goalSkillMetadata = builder.goalSkillMetadata;
    }

    private Organ() {
        this.attributeEntries = List.of();
        this.organModifierConsumer = EMPTY_MODIFIER;
        this.tooltipConsumer = EMPTY_TOOLTIP;
        this.organTickConsumer = EMPTY_CONSUMER;
        this.organAddedConsumer = EMPTY_CONSUMER;
        this.organRemovedConsumer = EMPTY_CONSUMER;
        this.otherOrganChangeConsumer = EMPTY_OTHER_ORGAN_CHANGE;
        this.refreshOnOrganChange = false;
        this.hasSkill = false;
        this.organSkillFunction = EMPTY_SKILL;
        this.cooldownTicksFunction = EMPTY_COOLDOWN;
        this.organSkillOnCooldownConsumer = EMPTY_CONSUMER;
        this.attackConsumer = EMPTY_ATTACK;
        this.hurtConsumer = EMPTY_HURT;
        this.healConsumer = EMPTY_HEAL;
        this.incomingDamageConsumer = EMPTY_INCOMING_DAMAGE;
        this.chestCavityOpenConsumer = EMPTY_CHEST_CAVITY_OPEN;
        this.chestCavityCloseConsumer = EMPTY_CHEST_CAVITY_CLOSE;
        this.interactConsumer = EMPTY_INTERACT;
        this.goalSkillMetadata = GoalSkillMetadata.EMPTY;
    }

    /**
     * 新建构建器。
     *
     * @return 构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 使用已有物品构造构建器。
     *
     * @param item 禁止注册时使用的物品实例
     * @return 构建器
     */
    public static Builder builder(Item item) {
        return new Builder(item);
    }

    /**
     * 使用物品工厂构造构建器。
     *
     * @param itemFunction 物品工厂
     * @return 构建器
     */
    public static Builder builder(Function<Item.Properties, Item> itemFunction) {
        return new Builder(itemFunction);
    }

    /**
     * 包装指定物品当前注册的器官。
     * <p>
     * 从 {@link OrganManager#getRegistry()} 中读取 {@code item} 对应的原始 {@link Organ}，
     * 并创建一个只包含器官行为覆盖项的构建器。未被覆盖的属性和回调都会继承原器官；
     * 调用 {@link AbstractBuilder#build()} 会把新 {@link Organ} 自动写回同一个物品的注册表项，
     * 只替换 Organ，不创建或替换 Item。
     * </p>
     *
     * @param item 已注册器官能力的物品
     * @return 包装器官构建器
     * @throws IllegalArgumentException 当指定物品没有注册 {@link Organ} 时抛出
     */
    public static WrapperBuilder wrap(Item item) {
        Objects.requireNonNull(item, "item");
        Organ organ = OrganManager.getRegistry().get(item);
        if (organ == null) {
            throw new IllegalArgumentException("Cannot wrap unregistered organ item: " + item);
        }
        return new WrapperBuilder(item, organ);
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(ChestCavitySlotContext context) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = LinkedHashMultimap.create();
        for (AttributeEntry entry : attributeEntries) {
            AttributeModifier modifier = OrganAttributeUtil.createModifier(context.id(), entry.value(), entry.operation());
            modifiers.put(entry.attribute(), modifier);
        }
        if (context.data() != null && context.entity() != null) {
            organModifierConsumer.accept(context, modifiers);
        }
        // 原始回归附魔效果
        if (
            context.entity() != null
            && EnchantmentUtil.getEnchantmentLevel(context.entity().level(), context.stack(), InitEnchantment.PRIMAL_REVERSION) > 0
        ) {
            Multimap<Holder<Attribute>, AttributeModifier> scaled = LinkedHashMultimap.create();
            for (Map.Entry<Holder<Attribute>, AttributeModifier> entry : modifiers.entries()) {
                AttributeModifier original = entry.getValue();
                AttributeModifier scaledModifier = new AttributeModifier(
                    original.id(), original.amount() * 1.5, original.operation()
                );
                scaled.put(entry.getKey(), scaledModifier);
            }
            return scaled;
        }
        return modifiers;
    }

    public void organTooltip(
        ChestCavitySlotContext slotContext,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        OrganTooltipConsumer consumer = tooltipConsumer;
        if (consumer == null) {
            consumer = TooltipUtil.DEFAULT_TOOLTIP;
        }
        consumer.accept(slotContext, keyContext, context, tooltipComponents, tooltipFlag);
    }

    public void tick(ChestCavitySlotContext context) {
        organTickConsumer.accept(context);
    }

    public void organAdded(ChestCavitySlotContext context) {
        organAddedConsumer.accept(context);
    }

    public void organRemoved(ChestCavitySlotContext context) {
        organRemovedConsumer.accept(context);
    }

    public void otherOrganChange(ChestCavitySlotContext context, int changedIndex, ItemStack oldStack, ItemStack newStack) {
        if (refreshOnOrganChange) {
            OrganAttributeUtil.updateSlotOrganAttribute(context);
        }
        otherOrganChangeConsumer.accept(context, changedIndex, oldStack, newStack);
    }

    public boolean shouldRefreshOnOrganChange() {
        return refreshOnOrganChange;
    }

    public boolean hasSkill() {
        return hasSkill;
    }

    public void organSkill(ChestCavitySlotContext context) {
        if (OrganSkillUtil.hasCooldown(context.entity(), context.stack())) {
            organSkillOnCooldownConsumer.accept(context);
            return;
        }
        if (organSkillFunction.apply(context)) {
            int cooldown = cooldownTicksFunction.applyAsInt(context);
            if (cooldown > 0) {
                OrganSkillUtil.addCooldown(context.entity(), context.stack(), cooldown);
            }
        }
    }

    public void incomingDamage(ChestCavitySlotContext context, LivingIncomingDamageEvent event) {
        incomingDamageConsumer.accept(context, event);
    }

    public void attack(ChestCavitySlotContext context, LivingEntity target, DamageSource source, DamageContainer damageContainer) {
        attackConsumer.accept(context, target, source, damageContainer);
    }

    public void hurt(ChestCavitySlotContext context, DamageSource source, DamageContainer damageContainer) {
        hurtConsumer.accept(context, source, damageContainer);
    }

    public void heal(ChestCavitySlotContext context, LivingHealEvent event) {
        healConsumer.accept(context, event);
    }

    public int getCooldownTicks(ChestCavitySlotContext context) {
        return cooldownTicksFunction.applyAsInt(context);
    }

    public void chestCavityOpen(ChestCavitySlotContext context) {
        chestCavityOpenConsumer.accept(context);
    }

    public void chestCavityClose(ChestCavitySlotContext context) {
        chestCavityCloseConsumer.accept(context);
    }

    public void interact(ChestCavitySlotContext context, OrganInteractContext interactContext) {
        interactConsumer.accept(context, interactContext);
    }

    public GoalSkillMetadata getGoalSkillMetadata() {
        return goalSkillMetadata;
    }

    /**
     * 器官行为构建基类。
     * <p>
     * 包含所有器官行为字段和配置方法（setter 返回 {@link AbstractBuilder}），
     * 以及统一的 {@link #build()} 入口。
     * </p>
     */
    public abstract static class AbstractBuilder<T extends AbstractBuilder<T>> {
        private final List<AttributeEntry> attributeEntries = new ArrayList<>();
        private OrganModifierConsumer organModifierConsumer = EMPTY_MODIFIER;
        private boolean refreshOnOrganChange = false;
        private OrganTooltipConsumer tooltipConsumer = null;
        private Consumer<ChestCavitySlotContext> organTickConsumer = EMPTY_CONSUMER;
        private Consumer<ChestCavitySlotContext> organAddedConsumer = EMPTY_CONSUMER;
        private Consumer<ChestCavitySlotContext> organRemovedConsumer = EMPTY_CONSUMER;
        private OtherOrganChangeConsumer otherOrganChangeConsumer = EMPTY_OTHER_ORGAN_CHANGE;
        private boolean hasSkill = false;
        private OrganSkillFunction organSkillFunction = EMPTY_SKILL;
        private ToIntFunction<ChestCavitySlotContext> cooldownTicksFunction = EMPTY_COOLDOWN;
        private Consumer<ChestCavitySlotContext> organSkillOnCooldownConsumer = EMPTY_CONSUMER;
        private AttackConsumer attackConsumer = EMPTY_ATTACK;
        private HurtConsumer hurtConsumer = EMPTY_HURT;
        private HealConsumer healConsumer = EMPTY_HEAL;
        private IncomingDamageConsumer incomingDamageConsumer = EMPTY_INCOMING_DAMAGE;
        private Consumer<ChestCavitySlotContext> chestCavityOpenConsumer = EMPTY_CHEST_CAVITY_OPEN;
        private Consumer<ChestCavitySlotContext> chestCavityCloseConsumer = EMPTY_CHEST_CAVITY_CLOSE;
        private OrganInteractConsumer interactConsumer = EMPTY_INTERACT;
        private GoalSkillMetadata goalSkillMetadata = GoalSkillMetadata.EMPTY;

        protected AbstractBuilder() {
        }

        /**
         * 将给定 Organ 的全部配置复制到此构建器中。
         */
        protected void copyFrom(Organ organ) {
            attributeEntries.clear();
            attributeEntries.addAll(organ.attributeEntries);
            organModifierConsumer = organ.organModifierConsumer;
            refreshOnOrganChange = organ.refreshOnOrganChange;
            tooltipConsumer = organ.tooltipConsumer;
            organTickConsumer = organ.organTickConsumer;
            organAddedConsumer = organ.organAddedConsumer;
            organRemovedConsumer = organ.organRemovedConsumer;
            otherOrganChangeConsumer = organ.otherOrganChangeConsumer;
            hasSkill = organ.hasSkill;
            organSkillFunction = organ.organSkillFunction;
            cooldownTicksFunction = organ.cooldownTicksFunction;
            organSkillOnCooldownConsumer = organ.organSkillOnCooldownConsumer;
            attackConsumer = organ.attackConsumer;
            hurtConsumer = organ.hurtConsumer;
            healConsumer = organ.healConsumer;
            incomingDamageConsumer = organ.incomingDamageConsumer;
            chestCavityOpenConsumer = organ.chestCavityOpenConsumer;
            chestCavityCloseConsumer = organ.chestCavityCloseConsumer;
            interactConsumer = organ.interactConsumer;
            goalSkillMetadata = organ.goalSkillMetadata;
        }

        /**
         * 子类返回自身，用于泛型自引用链式调用。
         */
        protected abstract T self();

        /**
         * 子类必须提供要注册到的物品实例。
         * <p>
         * Builder 返回新创建的 Item，WrapperBuilder 返回已存在的 Item。
         * </p>
         */
        protected abstract Item resolveItem();

        public T tooltip(OrganTooltipConsumer tooltipConsumer) {
            this.tooltipConsumer = Objects.requireNonNull(tooltipConsumer, "tooltipConsumer");
            return self();
        }

        public T modifier(OrganModifierConsumer organModifierConsumer) {
            this.organModifierConsumer = Objects.requireNonNull(organModifierConsumer, "organModifierConsumer");
            return self();
        }

        public T refreshOnOrganChange() {
            this.refreshOnOrganChange = true;
            return self();
        }

        public T clearRefreshOnOrganChange() {
            this.refreshOnOrganChange = false;
            return self();
        }

        public T removeAttribute(Holder<Attribute> attribute) {
            attributeEntries.removeIf(entry -> Objects.equals(entry.attribute(), attribute));
            return self();
        }

        public T clearAttributes() {
            attributeEntries.clear();
            return self();
        }

        public T addAttribute(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
            Objects.requireNonNull(attribute, "attribute");
            Objects.requireNonNull(operation, "operation");
            attributeEntries.add(new AttributeEntry(attribute, value, operation));
            return self();
        }

        public T addValueAttribute(Holder<Attribute> attribute, double value) {
            return addAttribute(attribute, value, AttributeModifier.Operation.ADD_VALUE);
        }

        public T baseMultipliedAttribute(Holder<Attribute> attribute, double value) {
            return addAttribute(attribute, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        }

        public T totalMultipliedAttribute(Holder<Attribute> attribute, double value) {
            return addAttribute(attribute, value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        }

        public T tick(Consumer<ChestCavitySlotContext> organTickConsumer) {
            this.organTickConsumer = Objects.requireNonNull(organTickConsumer, "organTickConsumer");
            return self();
        }

        public T added(Consumer<ChestCavitySlotContext> organAddedConsumer) {
            this.organAddedConsumer = Objects.requireNonNull(organAddedConsumer, "organAddedConsumer");
            return self();
        }

        public T removed(Consumer<ChestCavitySlotContext> organRemovedConsumer) {
            this.organRemovedConsumer = Objects.requireNonNull(organRemovedConsumer, "organRemovedConsumer");
            return self();
        }

        public T otherChange(OtherOrganChangeConsumer otherOrganChangeConsumer) {
            this.otherOrganChangeConsumer = Objects.requireNonNull(otherOrganChangeConsumer, "otherOrganChangeConsumer");
            return self();
        }

        public T skill(OrganSkillFunction organSkillFunction) {
            hasSkill = true;
            this.organSkillFunction = Objects.requireNonNull(organSkillFunction, "organSkillFunction");
            return self();
        }

        public T clearSkill() {
            hasSkill = false;
            organSkillFunction = EMPTY_SKILL;
            cooldownTicksFunction = EMPTY_COOLDOWN;
            organSkillOnCooldownConsumer = EMPTY_CONSUMER;
            return self();
        }

        public T cooldown(int cooldownTicks) {
            this.cooldownTicksFunction = context -> cooldownTicks;
            return self();
        }

        public T cooldown(ToIntFunction<ChestCavitySlotContext> cooldownTicksFunction) {
            this.cooldownTicksFunction = Objects.requireNonNull(cooldownTicksFunction, "cooldownTicksFunction");
            return self();
        }

        public T skillOnCooldown(Consumer<ChestCavitySlotContext> organSkillOnCooldownConsumer) {
            this.organSkillOnCooldownConsumer = Objects.requireNonNull(organSkillOnCooldownConsumer, "organSkillOnCooldownConsumer");
            return self();
        }

        /**
         * 声明器官技能可被 Goal 自动使用
         *
         * @param metadata 技能元数据
         */
        public T goalSkill(GoalSkillMetadata metadata) {
            this.goalSkillMetadata = Objects.requireNonNull(metadata, "goalSkillMetadata");
            return self();
        }

        /**
         * 清除 Goal 技能元数据
         */
        public T clearGoalSkill() {
            goalSkillMetadata = GoalSkillMetadata.EMPTY;
            return self();
        }

        public T attack(AttackConsumer attackConsumer) {
            this.attackConsumer = Objects.requireNonNull(attackConsumer, "attackConsumer");
            return self();
        }

        public T hurt(HurtConsumer hurtConsumer) {
            this.hurtConsumer = Objects.requireNonNull(hurtConsumer, "hurtConsumer");
            return self();
        }

        public T heal(HealConsumer healConsumer) {
            this.healConsumer = Objects.requireNonNull(healConsumer, "healConsumer");
            return self();
        }

        public T incomingDamage(IncomingDamageConsumer incomingDamageConsumer) {
            this.incomingDamageConsumer = Objects.requireNonNull(incomingDamageConsumer, "incomingDamageConsumer");
            return self();
        }

        public T chestCavityOpen(Consumer<ChestCavitySlotContext> chestCavityOpenConsumer) {
            this.chestCavityOpenConsumer = Objects.requireNonNull(chestCavityOpenConsumer, "chestCavityOpenConsumer");
            return self();
        }

        public T chestCavityClose(Consumer<ChestCavitySlotContext> chestCavityCloseConsumer) {
            this.chestCavityCloseConsumer = Objects.requireNonNull(chestCavityCloseConsumer, "chestCavityCloseConsumer");
            return self();
        }

        public T interact(OrganInteractConsumer interactConsumer) {
            this.interactConsumer = Objects.requireNonNull(interactConsumer, "interactConsumer");
            return self();
        }

        /**
         * 构建并注册 Organ，返回对应的 Item。
         * <p>
         * Builder：创建新 Item → 注册 Organ → 返回新 Item。
         * </p>
         * <p>
         * WrapperBuilder：使用已有 Item → 替换 Organ → 返回该 Item。
         * </p>
         */
        public Item build() {
            Item item = resolveItem();
            OrganManager.getRegistry().put(item, new Organ(this));
            return item;
        }
    }

    // 物品级构建器：创建新 Item + 注册 Organ
    public static class Builder extends AbstractBuilder<Builder> {
        private final Item.Properties properties = new Item.Properties().stacksTo(1);
        private Function<Item.Properties, Item> itemFunction = null;

        public Builder() {
        }

        public Builder(Item item) {
            itemFunction = properties -> item;
        }

        public Builder(Function<Item.Properties, Item> itemFunction) {
            this.itemFunction = itemFunction;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        protected Item resolveItem() {
            if (itemFunction == null) {
                return new Item(properties);
            }
            return itemFunction.apply(properties);
        }

        /**
         * 设置器官物品属性。
         */
        public Builder properties(Consumer<Item.Properties> itemPropertiesConsumer) {
            Objects.requireNonNull(itemPropertiesConsumer, "itemPropertiesConsumer");
            itemPropertiesConsumer.accept(properties);
            return this;
        }
    }

    /**
     * 基于已有器官创建局部覆盖版本的构建器。
     * <pre>
     * 该构建器只处理 Organ 侧行为：属性、tooltip、回调、技能和冷却等。
     * 它不会创建、替换或修改 Item。
     * 调用 {@link #build()} 时会创建新 {@link Organ} 并写回 {@link OrganManager} 中当前 item 对应的注册表项。
     * </pre>
     */
    public static class WrapperBuilder extends AbstractBuilder<WrapperBuilder> {
        private final Item item;

        private WrapperBuilder(Item item, Organ source) {
            this.item = item;
            copyFrom(source);
        }

        @Override
        protected WrapperBuilder self() {
            return this;
        }

        @Override
        protected Item resolveItem() {
            return item;
        }
    }
}
