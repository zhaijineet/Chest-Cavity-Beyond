package net.zhaiji.chestcavitybeyond.api;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ChestCavityType {
    private final String type;

    private final NonNullList<Item> organs = NonNullList.withSize(27, Items.AIR);

    private final Map<EntityType<?>, Map<Holder<Attribute>, Double>> defaultAttributes = new HashMap<>();

    private final Map<EntityType<?>, Map<Holder<Attribute>, AttributeModifier>> defaultModifiers = new HashMap<>();

    public ChestCavityType(String type) {
        this.type = type;
    }

    /**
     * 计算胸腔类型属性默认值
     *
     * @param entityType 实体类型
     * @param modifiers  修饰符集合
     */
    private static void calculateValue(EntityType<? extends LivingEntity> entityType, Holder<Attribute> attribute, Collection<AttributeModifier> modifiers, Map<Holder<Attribute>, Double> defaultMap, Map<Holder<Attribute>, AttributeModifier> modifierMap) {
        double value = 0;
        double baseValue = 0;
        boolean hasAttribute = false;
        if (DefaultAttributes.hasSupplier(entityType)) {
            AttributeSupplier attributeSupplier = DefaultAttributes.getSupplier(entityType);
            if (attributeSupplier.hasAttribute(attribute)) {
                hasAttribute = true;
                value = baseValue = attributeSupplier.getValue(attribute);
            }
        }
        if (!hasAttribute) {
            defaultMap.put(attribute, 0D);
        }
        for (AttributeModifier modifier : modifiers) {
            if (modifier.operation() == AttributeModifier.Operation.ADD_VALUE) {
                value += modifier.amount();
            }
        }
        double copyValue = value;
        for (AttributeModifier modifier : modifiers) {
            if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE) {
                value += copyValue * modifier.amount();
            }
        }
        for (AttributeModifier modifier : modifiers) {
            if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                value *= 1 + modifier.amount();
            }
        }
        defaultMap.put(attribute, value);
        // 当属性不是本模组添加的属性时，给予默认属性调整修饰符
        if (!attribute.getKey().location().getNamespace().equals(ChestCavityBeyond.MOD_ID)) {
            modifierMap.put(attribute, OrganAttributeUtil.createAddValueModifier("default", baseValue - value));
        }
    }

    public String getType() {
        return type;
    }

    public NonNullList<Item> getOrgans() {
        return organs;
    }

    /**
     * 为胸腔类型设置默认器官
     *
     * @param index 小于27
     * @param organ 器官
     * @return 胸腔类型
     */
    public ChestCavityType setOrgan(int index, Item organ) {
        organs.set(index, organ);
        return this;
    }

    /**
     * 为胸腔类型设置第一排默认器官
     *
     * @param index 0 ~ 8
     * @param organ 器官
     * @return 胸腔类型
     */
    public ChestCavityType setFirstRow(int index, Item organ) {
        return setOrgan(index, organ);
    }

    /**
     * 为胸腔类型设置第二排默认器官
     *
     * @param index 0 ~ 8
     * @param organ 器官
     * @return 胸腔类型
     */
    public ChestCavityType setSecondRow(int index, Item organ) {
        return setOrgan(index + 9, organ);
    }

    /**
     * 为胸腔类型设置第三排默认器官
     *
     * @param index 0 ~ 8
     * @param organ 器官
     * @return 胸腔类型
     */
    public ChestCavityType setThirdRow(int index, Item organ) {
        return setOrgan(index + 18, organ);
    }

    /**
     * 为每个属于这个胸腔类型的实体类型计算默认胸腔属性
     *
     * @param entityType 实体类型
     * @return 胸腔类型
     */
    public ChestCavityType builder(EntityType<? extends LivingEntity> entityType) {
        Multimap<Holder<Attribute>, AttributeModifier> modifierMultimap = HashMultimap.create();
        Map<Holder<Attribute>, Double> attributeMap = new HashMap<>();
        Map<Holder<Attribute>, AttributeModifier> modifierMap = new HashMap<>();
        for (int i = 0; i < organs.size(); i++) {
            ItemStack organ = organs.get(i).getDefaultInstance();
            modifierMultimap.putAll(
                    ChestCavityUtil.getOrganCap(organ)
                            .getAttributeModifiers(new ChestCavitySlotContext(null, null, ChestCavityUtil.getSlotId(i), i, organ))
            );
        }
        for (Holder<Attribute> attribute : modifierMultimap.keySet()) {
            calculateValue(entityType, attribute, modifierMultimap.get(attribute), attributeMap, modifierMap);
        }
        defaultAttributes.put(entityType, attributeMap);
        defaultModifiers.put(entityType, modifierMap);
        return this;
    }

    /**
     * 获取实体类型的默认胸腔属性
     *
     * @param entityType 实体类型
     * @return 默认胸腔属性
     */
    public Map<Holder<Attribute>, Double> getDefaultAttributes(EntityType<?> entityType) {
        return defaultAttributes.get(entityType);
    }

    /**
     * 获取实体类型的默认属性调整修饰符
     *
     * @param entityType 实体类型
     * @return 默认属性调整修饰符
     */
    public Map<Holder<Attribute>, AttributeModifier> getDefaultModifier(EntityType<?> entityType) {
        return defaultModifiers.get(entityType);
    }
}
