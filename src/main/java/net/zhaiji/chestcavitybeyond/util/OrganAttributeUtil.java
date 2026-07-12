package net.zhaiji.chestcavitybeyond.util;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
import net.zhaiji.chestcavitybeyond.api.AttributeBonus;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganAttributeUtil {
    /**
     * 创建属性修饰符
     *
     * @param id        标识符
     * @param value     值
     * @param operation 计算方式
     * @return 修饰符
     */
    public static AttributeModifier createModifier(ResourceLocation id, double value, AttributeModifier.Operation operation) {
        return new AttributeModifier(id, value, operation);
    }

    /**
     * 创建加算属性修饰符
     *
     * @param name  标识符名
     * @param value 值
     * @return 修饰符
     */
    public static AttributeModifier createAddValueModifier(String name, double value) {
        return createModifier(ChestCavityBeyond.of(name), value, AttributeModifier.Operation.ADD_VALUE);
    }

    /**
     * 创建加算属性修饰符
     *
     * @param id    标识符
     * @param value 值
     * @return 修饰符
     */
    public static AttributeModifier createAddValueModifier(ResourceLocation id, double value) {
        return createModifier(id, value, AttributeModifier.Operation.ADD_VALUE);
    }

    /**
     * 创建基础值乘算修饰符
     *
     * @param name  标识符名
     * @param value 值
     * @return 修饰符
     */
    public static AttributeModifier createMultipliedBaseModifier(String name, double value) {
        return createModifier(ChestCavityBeyond.of(name), value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    /**
     * 创建基础值乘算修饰符
     *
     * @param id    标识符
     * @param value 值
     * @return 修饰符
     */
    public static AttributeModifier createMultipliedBaseModifier(ResourceLocation id, double value) {
        return createModifier(id, value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    /**
     * 创建最终乘算修饰符
     *
     * @param name  标识符名
     * @param value 值
     * @return 修饰符
     */
    public static AttributeModifier createMultipliedTotalModifier(String name, double value) {
        return createModifier(ChestCavityBeyond.of(name), value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    /**
     * 创建最终乘算修饰符
     *
     * @param id    标识符
     * @param value 值
     * @return 修饰符
     */
    public static AttributeModifier createMultipliedTotalModifier(ResourceLocation id, double value) {
        return createModifier(id, value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    /**
     * 添加或替换属性修饰符
     *
     * @param entity    目标实体
     * @param attribute 属性
     * @param modifier  修饰符
     */
    public static void updateAttributeModifier(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            instance.addOrReplacePermanentModifier(modifier);
        }
    }

    /**
     * 更新器官属性
     *
     * @param context 胸腔槽位上下文
     */
    public static void updateSlotOrganAttribute(ChestCavitySlotContext context) {
        ItemStack organ = context.stack();
        updateOrganAttributeModifier(context.data(), context.index(), organ, organ);
    }

    /**
     * 对所有声明了动态属性的器官做多轮刷新，直到 modifier 计算结果不再变化或超出最大刷新轮次。
     *
     * @param data 胸腔数据
     */
    public static void refreshAllDynamicAttributes(ChestCavityData data) {
        List<Integer> refreshSlots = new ArrayList<>();
        for (int i = 0; i < data.getSlots(); i++) {
            ItemStack stack = data.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (ChestCavityUtil.getOrganCap(stack).shouldRefreshDynamicAttribute()) {
                refreshSlots.add(i);
            }
        }

        if (refreshSlots.isEmpty()) return;

        for (int round = 0; round < refreshSlots.size(); round++) {
            List<Multimap<Holder<Attribute>, AttributeModifier>> modifiersBeforeRound = new ArrayList<>();
            for (int slotIndex : refreshSlots) {
                ItemStack stack = data.getStackInSlot(slotIndex);
                ChestCavitySlotContext context = ChestCavityUtil.createContext(data, slotIndex, stack);
                modifiersBeforeRound.add(ChestCavityUtil.getAttributeModifiers(context));
            }

            for (int slotIndex : refreshSlots) {
                ItemStack stack = data.getStackInSlot(slotIndex);
                ChestCavitySlotContext context = ChestCavityUtil.createContext(data, slotIndex, stack);
                updateSlotOrganAttribute(context);
            }

            boolean changed = false;
            for (int j = 0; j < refreshSlots.size(); j++) {
                int slotIndex = refreshSlots.get(j);
                ItemStack stack = data.getStackInSlot(slotIndex);
                ChestCavitySlotContext context = ChestCavityUtil.createContext(data, slotIndex, stack);
                Multimap<Holder<Attribute>, AttributeModifier> after = ChestCavityUtil.getAttributeModifiers(context);
                if (!modifiersApproximatelyEqual(modifiersBeforeRound.get(j), after)) {
                    changed = true;
                    break;
                }
            }

            if (!changed) break;
        }
    }

    /**
     * 比较两个 modifier 集合是否近似相等
     */
    private static boolean modifierCollectionsApproximatelyEqual(
        Collection<AttributeModifier> before,
        Collection<AttributeModifier> after
    ) {
        if (before.size() != after.size()) return false;
        Map<ResourceLocation, AttributeModifier> afterById = new HashMap<>();
        for (AttributeModifier modifier : after) {
            afterById.put(modifier.id(), modifier);
        }
        for (AttributeModifier beforeModifier : before) {
            AttributeModifier afterModifier = afterById.get(beforeModifier.id());
            if (afterModifier == null) return false;
            if (beforeModifier.operation() != afterModifier.operation()) return false;
            if (Math.abs(beforeModifier.amount() - afterModifier.amount()) > 1e-6) return false;
        }
        return true;
    }

    /**
     * 比较两个 modifier Multimap 是否近似相等
     */
    private static boolean modifiersApproximatelyEqual(
        Multimap<Holder<Attribute>, AttributeModifier> before,
        Multimap<Holder<Attribute>, AttributeModifier> after
    ) {
        if (!before.keySet().equals(after.keySet())) return false;
        for (Holder<Attribute> key : before.keySet()) {
            if (!modifierCollectionsApproximatelyEqual(before.get(key), after.get(key))) return false;
        }
        return true;
    }

    /**
     * 更新器官属性修饰符
     *
     * @param data     胸腔数据
     * @param index    器官槽位索引
     * @param oldStack 旧器官
     * @param newStack 新器官
     */
    public static void updateOrganAttributeModifier(
        ChestCavityData data,
        int index,
        ItemStack oldStack,
        ItemStack newStack
    ) {
        LivingEntity entity = data.getOwner();
        Multimap<Holder<Attribute>, AttributeModifier> removeModifiers = ChestCavityUtil.getAttributeModifiers(
            ChestCavityUtil.createContext(
                data,
                index,
                oldStack
            )
        );
        Multimap<Holder<Attribute>, AttributeModifier> addModifiers = ChestCavityUtil.getAttributeModifiers(
            ChestCavityUtil.createContext(
                data,
                index,
                newStack
            )
        );
        ChestCavityType type = data.getType();
        ResourceLocation slotId = ChestCavityUtil.getSlotId(index);
        // 移除旧器官加成
        for (Holder<Attribute> attribute : removeModifiers.keySet()) {
            removeModifier(entity, attribute, removeModifiers.get(attribute));
        }
        for (AttributeBonus bonus : type.getAttributeBonuses(oldStack.getItem())) {
            removeModifier(entity, bonus.attribute(), Collections.singleton(bonus.create(slotId)));
        }
        // 添加新器官加成
        for (Holder<Attribute> attribute : addModifiers.keySet()) {
            addModifier(entity, attribute, addModifiers.get(attribute));
        }
        for (AttributeBonus bonus : type.getAttributeBonuses(newStack.getItem())) {
            addModifier(entity, bonus.attribute(), Collections.singleton(bonus.create(slotId)));
        }
    }

    /**
     * 删除修饰符
     *
     * @param entity          目标实体
     * @param attribute       修饰符
     * @param removeModifiers 需要删除的修饰符
     */
    public static void removeModifier(LivingEntity entity, Holder<Attribute> attribute, Collection<AttributeModifier> removeModifiers) {
        modifyModifier(entity, attribute, removeModifiers, false);
    }

    /**
     * 添加修饰符
     *
     * @param entity       目标实体
     * @param attribute    修饰符
     * @param addModifiers 需要添加的修饰符
     */
    public static void addModifier(LivingEntity entity, Holder<Attribute> attribute, Collection<AttributeModifier> addModifiers) {
        modifyModifier(entity, attribute, addModifiers, true);
    }

    /**
     * 修改修饰符
     *
     * @param entity    目标实体
     * @param attribute 修饰符
     * @param modifiers 需要添加的修饰符
     */
    public static void modifyModifier(
        LivingEntity entity,
        Holder<Attribute> attribute,
        Collection<AttributeModifier> modifiers,
        boolean isAdd
    ) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            if (isAdd) {
                modifiers.forEach(instance::addOrReplacePermanentModifier);
            } else {
                modifiers.forEach(instance::removeModifier);
            }
        }
    }

    /**
     * 根据模组属性分发调用对应的原版属性更新方法
     *
     * @param data      胸腔数据
     * @param attribute 发生变化的模组属性
     */
    public static void updateDerivedAttribute(ChestCavityData data, Holder<Attribute> attribute) {
        if (attribute.equals(InitAttribute.HEALTH)) updateHealth(data);
        else if (attribute.equals(InitAttribute.NERVES)) updateNerves(data);
        else if (attribute.equals(InitAttribute.STRENGTH)) updateStrength(data);
        else if (attribute.equals(InitAttribute.SPEED)) updateSpeed(data);
        else if (attribute.equals(InitAttribute.LEAPING)) updateLeaping(data);
    }

    /**
     * 检测目标是否水过敏
     *
     * @param entity 目标实体
     * @return 是否水过敏
     */
    public static boolean isWaterAllergy(LivingEntity entity) {
        return ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.WATER_ALLERGY) > 0;
    }

    /**
     * 更新胸腔类型的默认属性调整修饰符
     */
    public static void updateDefaultModifier(ChestCavityData data) {
        LivingEntity entity = data.getOwner();
        for (Map.Entry<Holder<Attribute>, List<AttributeModifier>> entry : data.getType().getDefaultModifier(entity.getType()).entrySet()) {
            for (AttributeModifier modifier : entry.getValue()) {
                updateAttributeModifier(entity, entry.getKey(), modifier);
            }
        }
    }

    /**
     * 更新健康附带的属性（最大生命值）
     */
    public static void updateHealth(ChestCavityData data) {
        LivingEntity entity = data.getOwner();
        double health = data.getDifferenceValue(InitAttribute.HEALTH);
        updateAttributeModifier(entity, Attributes.MAX_HEALTH, createAddValueModifier("health", health * 2));
    }

    /**
     * 更新神经效率附带的属性（移动速度，攻击速度）
     */
    public static void updateNerves(ChestCavityData data) {
        LivingEntity entity = data.getOwner();
        double nerves = data.getDifferenceValue(InitAttribute.NERVES);
        double factor = MathUtil.getLog10Scale(nerves);
        // 通过nerves属性的差值计算最终应用的数值
        double value = nerves >= 0 ? factor : -factor;
        // 移速做负值特殊处理，当前神经效率小于等于0就不允许移动了
        // 本来应该都不许的，但想了一下还是算了
        double moveValue = data.getCurrentValue(InitAttribute.NERVES) <= 0 ? -1 : nerves / 10;
        // 使用最终乘算
        updateAttributeModifier(entity, Attributes.ATTACK_SPEED, createMultipliedBaseModifier("attack_speed", value));
        updateAttributeModifier(entity, Attributes.MOVEMENT_SPEED, createMultipliedTotalModifier("nerves_move", moveValue));
    }

    /**
     * 更新力量附带的属性（攻击力）
     */
    public static void updateStrength(ChestCavityData data) {
        LivingEntity entity = data.getOwner();
        double strength = data.getDifferenceValue(InitAttribute.STRENGTH);
        double factor = MathUtil.getLog10Scale(strength);
        updateAttributeModifier(
            entity,
            Attributes.ATTACK_DAMAGE,
            createMultipliedBaseModifier("strength", strength >= 0 ? factor : -factor)
        );
    }

    /**
     * 更新速度附带的属性（移动速度）
     */
    public static void updateSpeed(ChestCavityData data) {
        LivingEntity entity = data.getOwner();
        double speed = data.getDifferenceValue(InitAttribute.SPEED);
        double factor = MathUtil.getLog10Scale(speed) / 2;
        if (speed == 0) {
            factor = 0;
        }
        updateAttributeModifier(
            entity,
            Attributes.MOVEMENT_SPEED,
            createMultipliedBaseModifier("speed", speed >= 0 ? factor : -factor)
        );
    }

    /**
     * 更新跳跃附带的属性（跳跃力度，安全掉落距离）
     */
    public static void updateLeaping(ChestCavityData data) {
        LivingEntity entity = data.getOwner();
        double leaping = data.getDifferenceValue(InitAttribute.LEAPING);
        double jumpStrengthFactor = MathUtil.getLog10Scale(leaping);
        updateAttributeModifier(
            entity,
            Attributes.JUMP_STRENGTH,
            leaping >= 0
            ? createAddValueModifier("leaping", jumpStrengthFactor)
            : createMultipliedBaseModifier("leaping", -jumpStrengthFactor)
        );
        // 获取更新后的实际跳跃力，计算跳跃高度，按原版比例设置安全摔落距离
        double currentJumpStr = entity.getAttributeValue(Attributes.JUMP_STRENGTH);
        double jumpHeight = calcJumpHeight(currentJumpStr);
        // 安全掉落距离按原版物理效果比例: SafeFall / JumpH = 3.0 / 1.25 = 2.4，然后减去原本的3点数值
        double safeFallTarget = (jumpHeight * 2.4) - 3;
        updateAttributeModifier(
            entity,
            Attributes.SAFE_FALL_DISTANCE,
            createAddValueModifier("leaping", safeFallTarget)
        );
    }

    /**
     * 更新胸腔容量附带的实体尺寸副作用
     * 3排无副作用，每增加一排 scale 增加 0.25
     */
    public static void updateScale(ChestCavityData data) {
        LivingEntity entity = data.getOwner();
        double bonus = 0;
        if (ChestCavityBeyondConfig.enableChestCavityScaleSideEffect) {
            int currentRows = data.getSize().getRows();
            int defaultRows = data.getType().getSize().getRows();
            int extraRows = currentRows - defaultRows;
            bonus = 0.25 * extraRows;
        }
        updateAttributeModifier(entity, Attributes.SCALE, createMultipliedBaseModifier("chest_cavity_scale", bonus));
    }

    /**
     * 判断火焰抗性是否足以完全免疫当前火焰伤害
     * <p>
     * 阶梯式免疫：
     * <ul>
     *   <li>≥ fireImmunityHotFloor：免疫热方块（岩浆块/营火）伤害</li>
     *   <li>≥ fireImmunityFire：免疫火焰/燃烧伤害</li>
     *   <li>≥ fireImmunityLava：免疫岩浆伤害</li>
     * </ul>
     * </p>
     *
     * @param fireResistance 火焰抗性差异值
     * @param source         伤害源
     * @return 是否完全免疫
     */
    public static boolean isFireImmune(double fireResistance, DamageSource source) {
        if (!source.is(DamageTypeTags.IS_FIRE) || fireResistance < ChestCavityBeyondConfig.fireImmunityHotFloor) {
            return false;
        }
        // 岩浆 → 需要 ≥ fireImmunityLava
        if (source.is(DamageTypes.LAVA)) {
            return fireResistance >= ChestCavityBeyondConfig.fireImmunityLava;
        }
        // 热地板/营火 → 需要 ≥ fireImmunityHotFloor
        if (source.is(DamageTypes.HOT_FLOOR) || source.is(DamageTypes.CAMPFIRE)) {
            return true;
        }
        // 其他火焰伤害（in_fire, on_fire, fireball 等）→ 需要 ≥ fireImmunityFire
        return fireResistance >= ChestCavityBeyondConfig.fireImmunityFire;
    }

    /**
     * 判断冰霜抗性是否足以完全免疫当前冻结伤害
     *
     * @param frostResistance 冰霜抗性差异值
     * @param source          伤害源
     * @return 是否完全免疫
     */
    public static boolean isFrostImmune(double frostResistance, DamageSource source) {
        return source.is(DamageTypeTags.IS_FREEZING) && frostResistance >= ChestCavityBeyondConfig.frostImmunity;
    }

    /**
     * 根据跳跃力计算跳跃高度
     * 模拟 Minecraft 跳跃物理: velocity = (vy - 0.08) * 0.98
     *
     * @param jumpStrength 跳跃力
     * @return 跳跃高度
     */
    public static double calcJumpHeight(double jumpStrength) {
        double velocity = jumpStrength;
        double height = 0;
        while (velocity > 0) {
            height += velocity;
            velocity = (velocity - 0.08) * 0.98;
        }
        return height;
    }
}
