package net.zhaiji.chestcavitybeyond.api;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class ChestCavityType {
    private final NonNullList<Item> organs = NonNullList.withSize(54, Items.AIR);

    private final Map<EntityType<?>, Map<Holder<Attribute>, Double>> defaultAttributes = new HashMap<>();

    private final Map<EntityType<?>, Map<Holder<Attribute>, AttributeModifier>> defaultModifiers = new HashMap<>();

    private final Map<Item, List<AttributeBonus>> attributeBonuses = new HashMap<>();

    private final List<AttributeBonus> typeDefaultBonuses = new ArrayList<>();

    private final Map<Predicate<ChestCavitySlotContext>, Function<ChestCavitySlotContext, ItemStack>> conversionMap = new HashMap<>();

    private ChestCavitySize size = ChestCavitySize.ROW_3;

    private boolean needBreath = true;

    private BiFunction<Player, LivingEntity, Boolean> canOpen = (player, entity) -> true;

    private String unopenableMessage;

    /**
     * 计算胸腔类型属性默认值
     *
     * @param entityType 实体类型
     * @param modifiers  修饰符集合
     */
    private static void calculateValue(
        EntityType<? extends LivingEntity> entityType,
        Holder<Attribute> attribute,
        Collection<AttributeModifier> modifiers,
        Map<Holder<Attribute>, Double> defaultMap,
        Map<Holder<Attribute>, AttributeModifier> modifierMap
    ) {
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

    public NonNullList<Item> getOrgans() {
        return organs;
    }

    /**
     * 获取胸腔容量大小
     */
    public ChestCavitySize getSize() {
        return size;
    }

    /**
     * 设置胸腔容量大小
     */
    public ChestCavityType setSize(ChestCavitySize size) {
        this.size = size;
        return this;
    }

    public ChestCavityType set3RowSize() {
        return setSize(ChestCavitySize.ROW_3);
    }

    public ChestCavityType set4RowSize() {
        return setSize(ChestCavitySize.ROW_4);
    }

    public ChestCavityType set5RowSize() {
        return setSize(ChestCavitySize.ROW_5);
    }

    public ChestCavityType set6RowsSize() {
        return setSize(ChestCavitySize.ROW_6);
    }

    /**
     * 复制目标胸腔类型
     *
     * @param copyTarget 要复制属性的胸腔类型
     * @return 当前胸腔类型
     */
    public ChestCavityType copyWith(ChestCavityType copyTarget) {
        for (int i = 0; i < organs.size(); i++) {
            organs.set(i, copyTarget.organs.get(i));
        }
        attributeBonuses.putAll(copyTarget.attributeBonuses);
        typeDefaultBonuses.addAll(copyTarget.typeDefaultBonuses);
        needBreath = copyTarget.needBreath;
        canOpen = copyTarget.canOpen;
        unopenableMessage = copyTarget.unopenableMessage;
        size = copyTarget.size;
        conversionMap.putAll(copyTarget.conversionMap);
        return this;
    }

    /**
     * 为胸腔类型设置默认器官
     *
     * @param index 0 ~ 53
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
     * 获取是否需要呼吸
     *
     * @return 是否需要呼吸
     */
    public boolean getNeedBreath() {
        return needBreath;
    }

    /**
     * 设置是否需要呼吸
     *
     * @param needBreath 是否需要呼吸
     * @return 胸腔类型
     */
    public ChestCavityType setNeedBreath(boolean needBreath) {
        this.needBreath = needBreath;
        return this;
    }

    /**
     * 获取物品的属性加成列表
     *
     * @param item 物品
     * @return 属性加成列表，如果没有则返回空列表
     */
    public List<AttributeBonus> getAttributeBonuses(Item item) {
        return attributeBonuses.getOrDefault(item, List.of());
    }

    /**
     * 为特定器官添加额外属性加成
     *
     * @param organ      提供加成的物品
     * @param attribute  要修改的属性
     * @param bonusValue 加成数值
     * @param operation  应用方式
     * @return 胸腔类型
     */
    public ChestCavityType addAttributeBonus(
        Item organ,
        Holder<Attribute> attribute,
        double bonusValue,
        AttributeModifier.Operation operation
    ) {
        attributeBonuses.computeIfAbsent(organ, item -> new ArrayList<>())
            .add(new AttributeBonus(attribute, bonusValue, operation));
        return this;
    }

    /**
     * 清空所有器官添加的额外属性加成
     *
     * @return 胸腔类型
     */
    public ChestCavityType clearAttributeBonuses() {
        attributeBonuses.clear();
        return this;
    }

    /**
     * 批量添加简单加成
     *
     * @param item       提供加成的物品
     * @param attributes 属性到加成数值的映射
     * @return 胸腔类型
     */
    public ChestCavityType addValueBonuses(Item item, Map<Holder<Attribute>, Double> attributes) {
        attributes.forEach((attribute, value) ->
            addAttributeBonus(item, attribute, value, AttributeModifier.Operation.ADD_VALUE)
        );
        return this;
    }

    /**
     * 为胸腔类型添加默认属性加成
     * 这些加成将应用于所有拥有此胸腔类型的实体，独立于器官系统
     *
     * @param attribute  要修改的属性
     * @param bonusValue 加成数值
     * @param operation  应用方式
     * @return 胸腔类型
     */
    public ChestCavityType addTypeDefaultBonus(Holder<Attribute> attribute, double bonusValue, AttributeModifier.Operation operation) {
        typeDefaultBonuses.add(new AttributeBonus(attribute, bonusValue, operation));
        return this;
    }

    /**
     * 批量添加简单的加值加成
     *
     * @param attributes 属性到加成数值的映射
     * @return 胸腔类型
     */
    public ChestCavityType addTypeValueBonuses(Map<Holder<Attribute>, Double> attributes) {
        attributes.forEach((attribute, value) ->
            addTypeDefaultBonus(attribute, value, AttributeModifier.Operation.ADD_VALUE)
        );
        return this;
    }

    /**
     * 获取胸腔类型的默认属性加成
     *
     * @return 默认属性加成列表
     */
    public List<AttributeBonus> getTypeDefaultBonuses() {
        return Collections.unmodifiableList(typeDefaultBonuses);
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

        // 收集器官的所有修饰符
        for (int i = 0; i < size.getSlots(); i++) {
            Item organItem = organs.get(i);
            if (organItem == Items.AIR) continue;
            ItemStack organ = organItem.getDefaultInstance();
            ResourceLocation slotId = ChestCavityUtil.getSlotId(i);
            // 器官默认属性
            modifierMultimap.putAll(
                ChestCavityUtil.getOrganCap(organ)
                    .getAttributeModifiers(new ChestCavitySlotContext(null, null, slotId, i, organ))
            );
            // 器官补偿属性
            if (attributeBonuses.containsKey(organItem)) {
                for (AttributeBonus bonus : attributeBonuses.get(organItem)) {
                    modifierMultimap.put(bonus.attribute(), bonus.create(slotId));
                }
            }
        }

        // 添加胸腔类型默认加成
        for (AttributeBonus bonus : typeDefaultBonuses) {
            modifierMultimap.put(bonus.attribute(), bonus.create(ChestCavityBeyond.of("type_default")));
        }

        // 计算最终值
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

    /**
     * 获取能否开胸
     */
    public boolean canOpen(Player player, LivingEntity entity) {
        return canOpen.apply(player, entity);
    }

    /**
     * 设置能否开胸
     */
    public ChestCavityType setCanOpen(BiFunction<Player, LivingEntity, Boolean> unopenable) {
        this.canOpen = unopenable;
        return this;
    }

    /**
     * 设置能否开胸
     */
    public ChestCavityType setCanOpen(boolean canOpen) {
        this.canOpen = (player, entity) -> canOpen;
        return this;
    }

    /**
     * 获取不可开胸消息键
     */
    public String getUnopenableMessage() {
        return unopenableMessage;
    }

    /**
     * 设置不可开胸时的消息提示（语言键）
     *
     * @param messageKey 翻译键，如 "message.chestcavitybeyond.boss_undying"
     */
    public ChestCavityType setUnopenableMessage(String messageKey) {
        this.unopenableMessage = messageKey;
        return this;
    }

    /**
     * 注册器官转换映射（简单映射，精确物品匹配）
     *
     * @param from 原始器官
     * @param to   转换后的器官
     * @return 胸腔类型
     */
    public ChestCavityType addConversion(Item from, Item to) {
        conversionMap.put(ctx -> ctx.stack().is(from), ctx -> to.getDefaultInstance());
        return this;
    }

    /**
     * 注册器官转换映射（携带 NBT/组件的 ItemStack，精确物品匹配）
     *
     * @param from 原始器官
     * @param to   转换后的器官物品栈（保留 NBT/组件）
     * @return 胸腔类型
     */
    public ChestCavityType addConversion(Item from, ItemStack to) {
        conversionMap.put(ctx -> ctx.stack().is(from), ctx -> to.copy());
        return this;
    }

    /**
     * 注册器官转换映射（条件匹配 + 简单目标）
     *
     * @param condition 匹配条件（首个命中即用）
     * @param to        转换后的器官
     * @return 胸腔类型
     */
    public ChestCavityType addConversion(Predicate<ChestCavitySlotContext> condition, Item to) {
        conversionMap.put(condition, ctx -> to.getDefaultInstance());
        return this;
    }

    /**
     * 注册器官转换映射（条件匹配 + 指定 ItemStack）
     *
     * @param condition 匹配条件（首个命中即用）
     * @param to        转换后的器官物品栈（保留 NBT/组件）
     * @return 胸腔类型
     */
    public ChestCavityType addConversion(Predicate<ChestCavitySlotContext> condition, ItemStack to) {
        conversionMap.put(condition, ctx -> to.copy());
        return this;
    }

    /**
     * 注册器官转换映射（条件匹配 + 完整回调）
     * <p>
     * 回调接收 {@link ChestCavitySlotContext}，返回要放入槽位的新 ItemStack：
     * - 返回 {@code context.stack()} → 保持原器官不变
     * - 返回其他 ItemStack → 替换
     * - 返回 {@link ItemStack#EMPTY} → 清除槽位
     * </p>
     *
     * @param condition 匹配条件（首个命中即用）
     * @param converter 转换函数
     * @return 胸腔类型
     */
    public ChestCavityType addConversion(
        Predicate<ChestCavitySlotContext> condition,
        Function<ChestCavitySlotContext, ItemStack> converter
    ) {
        conversionMap.put(condition, converter);
        return this;
    }

    /**
     * 获取器官转换结果（按注册顺序匹配，首个命中即用）
     *
     * @param context 当前槽位的上下文
     * @return 转换后的 ItemStack，无匹配则返回 {@code context.stack()}（保持不变）
     */
    public ItemStack getConversionResult(ChestCavitySlotContext context) {
        for (Map.Entry<Predicate<ChestCavitySlotContext>, Function<ChestCavitySlotContext, ItemStack>> entry : conversionMap.entrySet()) {
            if (entry.getKey().test(context)) {
                return entry.getValue().apply(context);
            }
        }
        return context.stack();
    }

    /**
     * 清空所有器官转换规则
     *
     * @return 胸腔类型
     */
    public ChestCavityType clearConversions() {
        conversionMap.clear();
        return this;
    }
}
