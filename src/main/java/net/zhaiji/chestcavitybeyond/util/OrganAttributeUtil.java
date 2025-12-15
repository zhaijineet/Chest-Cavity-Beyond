package net.zhaiji.chestcavitybeyond.util;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;

import java.util.Collection;

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
     * 更新器官属性修饰符
     *
     * @param data     胸腔数据
     * @param entity   目标实体
     * @param oldStack 旧器官
     * @param newStack 新器官
     */
    public static void updateOrganAttributeModifier(ChestCavityData data, LivingEntity entity, int index, ItemStack oldStack, ItemStack newStack) {
        if (oldStack.equals(newStack)) return;
        Multimap<Holder<Attribute>, AttributeModifier> removeModifiers = ChestCavityUtil.getAttributeModifiers(ChestCavityUtil.createContext(data, entity, index, oldStack));
        Multimap<Holder<Attribute>, AttributeModifier> addModifiers = ChestCavityUtil.getAttributeModifiers(ChestCavityUtil.createContext(data, entity, index, newStack));
        if (!removeModifiers.isEmpty()) {
            for (Holder<Attribute> attribute : removeModifiers.keySet()) {
                removeModifier(entity, attribute, removeModifiers.get(attribute));
            }
        }
        if (!addModifiers.isEmpty()) {
            for (Holder<Attribute> attribute : addModifiers.keySet()) {
                addModifier(entity, attribute, addModifiers.get(attribute));
            }
        }
        updateHealth(data, entity);
        updateNerves(data, entity);
        updateStrength(data, entity);
        updateSpeed(data, entity);
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
    public static void modifyModifier(LivingEntity entity, Holder<Attribute> attribute, Collection<AttributeModifier> modifiers, boolean isAdd) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            if (isAdd) {
                modifiers.forEach(instance::removeModifier);
            } else {
                modifiers.forEach(instance::addOrUpdateTransientModifier);
            }
        }
    }

    /**
     * 更新健康附带的属性（最大生命值）
     *
     * @param data   胸腔数据
     * @param entity 目标实体
     */
    public static void updateHealth(ChestCavityData data, LivingEntity entity) {
        double health = data.getDifferenceValue(InitAttribute.HEALTH);
        updateAttributeModifier(entity, Attributes.MAX_HEALTH, createAddValueModifier("health", health * 2));
    }

    /**
     * 更新神经效率附带的属性（移动速度，攻击速度）
     *
     * @param data   胸腔数据
     * @param entity 目标实体
     */
    public static void updateNerves(ChestCavityData data, LivingEntity entity) {
        double nerves = data.getDifferenceValue(InitAttribute.NERVES);
        double factor = MathUtil.getLog10Scale(nerves);
        // 通过nerves属性的差值计算最终应用的数值
        double value = nerves >= 0 ? factor : -factor;
        // 移速做负值特殊处理，当前神经效率小于等于0就不允许移动了
        // 本来应该都不许的，但想了一下还是算了
        double moveValue = data.getCurrentValue(InitAttribute.NERVES) <= 0 ? -1 : nerves / 10;
        // 使用最终乘算
        updateAttributeModifier(entity, Attributes.MOVEMENT_SPEED, createMultipliedTotalModifier("nerves_move", moveValue));
        updateAttributeModifier(entity, Attributes.ATTACK_SPEED, createMultipliedBaseModifier("attack_speed", value));
    }

    /**
     * 更新力量附带的属性（攻击力）
     *
     * @param data   胸腔数据
     * @param entity 目标实体
     */
    public static void updateStrength(ChestCavityData data, LivingEntity entity) {
        double strength = data.getDifferenceValue(InitAttribute.STRENGTH);
        double factor = strength != 0 ? MathUtil.getDirectScale(strength) : 0;
        updateAttributeModifier(entity, Attributes.ATTACK_DAMAGE, createAddValueModifier("strength", strength >= 0 ? factor : -factor));
    }

    /**
     * 更新神经效率附带的属性（移动速度）
     *
     * @param data   胸腔数据
     * @param entity 目标实体
     */
    public static void updateSpeed(ChestCavityData data, LivingEntity entity) {
        double speed = data.getDifferenceValue(InitAttribute.SPEED);
        double factor = MathUtil.getLog10Scale(speed) / 10;
        updateAttributeModifier(entity, Attributes.MOVEMENT_SPEED, createMultipliedBaseModifier("speed", speed >= 0 ? factor : -factor));
    }
}
