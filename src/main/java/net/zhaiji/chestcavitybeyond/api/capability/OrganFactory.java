package net.zhaiji.chestcavitybeyond.api.capability;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.function.OrganTooltipsConsumer;
import net.zhaiji.chestcavitybeyond.item.OrganItem;
import net.zhaiji.chestcavitybeyond.util.ChestCavityClientUtil;

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
    private static final Consumer<ChestCavitySlotContext> EMPTY_CONSUMER = context -> {
    };
    private static final BiConsumer<ResourceLocation, Multimap<Holder<Attribute>, AttributeModifier>> EMPTY_MODIFIER = (id, modifiers) -> {
    };

    /**
     * 新建构建器
     *
     * @return 构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Item.Properties properties;
        private OrganTooltipsConsumer organTooltipsConsumer;
        private BiConsumer<ResourceLocation, Multimap<Holder<Attribute>, AttributeModifier>> organModifierConsumer;
        private Consumer<ChestCavitySlotContext> organTickConsumer;
        private Consumer<ChestCavitySlotContext> organAddedConsumer;
        private Consumer<ChestCavitySlotContext> organRemovedConsumer;

        private Builder() {
            this.properties = new Item.Properties().stacksTo(1);
            this.organTooltipsConsumer = ChestCavityClientUtil::addOrganTooltips;
            this.organModifierConsumer = EMPTY_MODIFIER;
            this.organTickConsumer = EMPTY_CONSUMER;
            this.organAddedConsumer = EMPTY_CONSUMER;
            this.organRemovedConsumer = EMPTY_CONSUMER;
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
        public Builder tooltips(OrganTooltipsConsumer organTooltipsConsumer) {
            this.organTooltipsConsumer = organTooltipsConsumer;
            return this;
        }

        /**
         * 设置器官提供的属性修饰符
         */
        public Builder organModifier(BiConsumer<ResourceLocation, Multimap<Holder<Attribute>, AttributeModifier>> organModifierConsumer) {
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
         * 构建
         */
        public OrganItem build() {
            OrganItem organItem = new OrganItem(properties, organTooltipsConsumer);
            ORGAN_REGISTRY.put(organItem, new Organ(organModifierConsumer, organTickConsumer, organAddedConsumer, organRemovedConsumer));
            return organItem;
        }
    }
}
