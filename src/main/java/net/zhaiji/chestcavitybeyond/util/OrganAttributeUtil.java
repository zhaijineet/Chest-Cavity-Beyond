package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;

import java.util.Collection;

public class OrganAttributeUtil {
    /**
     * 创建属性修饰符
     *
     * @param value     值
     * @param operation 计算方式
     * @return 修饰符
     */
    public static AttributeModifier createModifier(double value, AttributeModifier.Operation operation) {
        return new AttributeModifier(ChestCavityBeyond.of(operation.toString().toLowerCase()), value, operation);
    }

    /**
     * 创建加算属性修饰符
     *
     * @param value 值
     * @return 修饰符
     */
    public static AttributeModifier createAddValueModifier(double value) {
        return createModifier(value, AttributeModifier.Operation.ADD_VALUE);
    }

    /**
     * 创建基础值乘算修饰符
     *
     * @param value 值
     * @return 修饰符
     */
    public static AttributeModifier createMultipliedBaseModifier(double value) {
        return createModifier(value, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    /**
     * 创建最终乘算修饰符
     *
     * @param value 值
     * @return 修饰符
     */
    public static AttributeModifier createMultipliedTotalModifier(double value) {
        return createModifier(value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    /**
     * 删除修饰符
     * <p>
     * 实际上只删除了这些修饰符的值
     * </P>
     *
     * @param entity          目标实体
     * @param attribute       修饰符
     * @param removeModifiers 需要删除的修饰符
     * @param operation       计算方式
     */
    public static void removeModifier(LivingEntity entity, Holder<Attribute> attribute, Collection<AttributeModifier> removeModifiers, AttributeModifier.Operation operation) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            AttributeModifier modifier = instance.getModifier(ChestCavityBeyond.of(operation.toString().toLowerCase()));
            if (modifier == null) {
                modifier = OrganAttributeUtil.createModifier(0, operation);
            }
            double newValue = modifier.amount();
            for (AttributeModifier removeModifier : removeModifiers) {
                if (removeModifier.operation() == operation) {
                    newValue -= removeModifier.amount();
                }
            }
            instance.addOrReplacePermanentModifier(OrganAttributeUtil.createModifier(newValue, operation));
        }
    }

    /**
     * 添加修饰符
     * <p>
     * 实际上只添加了这些修饰符的值
     * </P>
     *
     * @param entity      目标实体
     * @param attribute   修饰符
     * @param addModifier 需要添加的修饰符
     * @param operation   计算方式
     */
    public static void addModifier(LivingEntity entity, Holder<Attribute> attribute, Collection<AttributeModifier> addModifier, AttributeModifier.Operation operation) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            AttributeModifier modifier = instance.getModifier(ChestCavityBeyond.of(operation.toString().toLowerCase()));
            if (modifier == null) {
                modifier = OrganAttributeUtil.createModifier(0, operation);
            }
            double newValue = modifier.amount();
            for (AttributeModifier removeModifier : addModifier) {
                if (removeModifier.operation() == operation) {
                    newValue += removeModifier.amount();
                }
            }
            instance.addOrReplacePermanentModifier(OrganAttributeUtil.createModifier(newValue, operation));
        }
    }

    /**
     * 更新神经效率附带的属性（移动速度，攻击速度，挖掘效率）
     *
     * @param entity 目标实体
     */
    public static void updateNerves(LivingEntity entity) {
        ChestCavityData data = ChestCavityUtil.getData(entity);
        double nerves = data.getDifferenceValue(InitAttribute.NERVES);
        double factor = MathUtil.getLog1pScale(nerves);
        // 通过nerves属性的差值计算最终应用的数值
        double value = nerves >= 0 ? factor : -factor;
        // 移速做负值特殊处理，当前神经效率小于等于0就不允许移动了
        // 本来应该都不许的，但想了一下还是算了
        double moveValue = data.getCurrentValue(InitAttribute.NERVES) <= 0 ? -1 : value;
        // 使用最终乘算
        data.updateAttribute(Attributes.MOVEMENT_SPEED, createMultipliedTotalModifier(moveValue));
        data.updateAttribute(Attributes.ATTACK_SPEED, createMultipliedTotalModifier(value));
        data.updateAttribute(Attributes.MINING_EFFICIENCY, createMultipliedTotalModifier(value));
    }

    /**
     * 更新健康附带的属性（最大生命值）
     *
     * @param entity 目标实体
     */
    public static void updateHealth(LivingEntity entity) {
        ChestCavityData data = ChestCavityUtil.getData(entity);
        double health = data.getDifferenceValue(InitAttribute.HEALTH);
        data.updateAttribute(Attributes.MAX_HEALTH, createAddValueModifier(health));
    }
}
