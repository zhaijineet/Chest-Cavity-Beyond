package net.zhaiji.chestcavitybeyond.api.capability;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.function.AttackConsumer;
import net.zhaiji.chestcavitybeyond.api.function.OrganTooltipConsumer;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 器官工厂类
 */
public class OrganFactory {
    public static final Map<Item, Organ> ORGAN_REGISTRY = new HashMap<>();

    public static final IOrgan EMPTY_ORGAN = new IOrgan() {
    };
    private static final BiConsumer<ResourceLocation, Multimap<Holder<Attribute>, AttributeModifier>> EMPTY_MODIFIER = (id, modifiers) -> {
    };
    private static final Consumer<ChestCavitySlotContext> EMPTY_CONSUMER = context -> {
    };
    private static final AttackConsumer EMPTY_ATTACK =  (context, target, source, damageContainer)-> {
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
        private OrganTooltipConsumer organTooltipConsumer = TooltipUtil::addOrganTooltip;
        private Consumer<ChestCavitySlotContext> organTickConsumer = EMPTY_CONSUMER;
        private Consumer<ChestCavitySlotContext> organAddedConsumer = EMPTY_CONSUMER;
        private Consumer<ChestCavitySlotContext> organRemovedConsumer = EMPTY_CONSUMER;
        private boolean hasSkill = false;
        private Consumer<ChestCavitySlotContext> organSkillConsumer = EMPTY_CONSUMER;
        private AttackConsumer attackConsumer = EMPTY_ATTACK;

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
         * 设置器官工具提示
         */
        public Builder tooltips(OrganTooltipConsumer organTooltipConsumer) {
            this.organTooltipConsumer = organTooltipConsumer;
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
         * 设置器官拥有者攻击
         */
        public Builder attack(AttackConsumer attackConsumer) {
            this.attackConsumer = attackConsumer;
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
                            organTooltipConsumer,
                            organTickConsumer,
                            organAddedConsumer,
                            organRemovedConsumer,
                            hasSkill,
                            organSkillConsumer,
                            attackConsumer
                    )
            );
            return item;
        }
    }
}
