package net.zhaiji.chestcavitybeyond.builder;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.capability.IOrgan;
import net.zhaiji.chestcavitybeyond.api.capability.Organ;
import net.zhaiji.chestcavitybeyond.api.function.AttackConsumer;
import net.zhaiji.chestcavitybeyond.api.function.HurtConsumer;
import net.zhaiji.chestcavitybeyond.api.function.IncomingDamageConsumer;
import net.zhaiji.chestcavitybeyond.api.function.OrganTooltipConsumer;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 器官工厂类
 */
public class OrganBuilder {
    public static final Map<Item, Organ> ORGAN_REGISTRY = new HashMap<>();

    public static final IOrgan EMPTY_ORGAN = new IOrgan() {
    };
    private static final BiConsumer<ResourceLocation, Multimap<Holder<Attribute>, AttributeModifier>> EMPTY_MODIFIER = (id, modifiers) -> {
    };
    private static final OrganTooltipConsumer EMPTY_TOOLTIP = (data, stack, keyContext, context, tooltipComponents, tooltipFlag) -> {
    };
    private static final Consumer<ChestCavitySlotContext> EMPTY_CONSUMER = context -> {
    };
    private static final AttackConsumer EMPTY_ATTACK = (context, target, source, damageContainer) -> {
    };
    private static final HurtConsumer EMPTY_HURT = (context, source, damageContainer) -> {
    };
    private static final IncomingDamageConsumer EMPTY_INCOMING_DAMAGE = (context, source, damageContainer) -> {
    };

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

    public static class Builder {
        private final Item.Properties properties = new Item.Properties().stacksTo(1);
        private Item item;
        private BiConsumer<ResourceLocation, Multimap<Holder<Attribute>, AttributeModifier>> organModifierConsumer = EMPTY_MODIFIER;
        private OrganTooltipConsumer descriptionTooltipConsumer = EMPTY_TOOLTIP;
        private OrganTooltipConsumer attributeTooltipConsumer = null;
        private OrganTooltipConsumer skillTooltipConsumer = EMPTY_TOOLTIP;
        private Consumer<ChestCavitySlotContext> organTickConsumer = EMPTY_CONSUMER;
        private Consumer<ChestCavitySlotContext> organAddedConsumer = EMPTY_CONSUMER;
        private Consumer<ChestCavitySlotContext> organRemovedConsumer = EMPTY_CONSUMER;
        private boolean hasSkill = false;
        private Consumer<ChestCavitySlotContext> organSkillConsumer = EMPTY_CONSUMER;
        private int cooldownTicks = 0;
        private AttackConsumer attackConsumer = EMPTY_ATTACK;
        private HurtConsumer hurtConsumer = EMPTY_HURT;
        private IncomingDamageConsumer incomingDamageConsumer = EMPTY_INCOMING_DAMAGE;

        private Builder() {
        }

        private Builder(Item item) {
            this.item = item;
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
        public Builder modifier(BiConsumer<ResourceLocation, Multimap<Holder<Attribute>, AttributeModifier>> organModifierConsumer) {
            this.organModifierConsumer = organModifierConsumer;
            return this;
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
         * 设置器官拥有者受到伤害前触发器
         */
        public Builder incomingDamage(IncomingDamageConsumer incomingDamageConsumer) {
            this.incomingDamageConsumer = incomingDamageConsumer;
            return this;
        }

        /**
         * 构建
         */
        public Item build() {
            if (item == null) {
                item = new Item(properties);
            }
            ORGAN_REGISTRY.put(
                    item,
                    new Organ(
                            organModifierConsumer,
                            descriptionTooltipConsumer,
                            attributeTooltipConsumer,
                            skillTooltipConsumer,
                            organTickConsumer,
                            organAddedConsumer,
                            organRemovedConsumer,
                            hasSkill,
                            organSkillConsumer,
                            cooldownTicks,
                            attackConsumer,
                            hurtConsumer,
                            incomingDamageConsumer
                    )
            );
            return item;
        }
    }
}
