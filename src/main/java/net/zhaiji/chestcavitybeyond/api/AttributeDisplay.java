package net.zhaiji.chestcavitybeyond.api;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.zhaiji.chestcavitybeyond.manager.AttributeDisplayManager;

import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 属性显示信息
 * <p>
 * 通过 Builder 模式构造，定义属性在查询指令中的显示行为。
 * 构建时自动注册到 {@link AttributeDisplayManager}。
 * </p>
 * 示例用法：
 * <pre>
 * {@code
 * AttributeDisplay.builder(InitAttribute.HEALTH)
 *     .priority(40)
 *     .build();
 *
 * // 百分比属性（自动检测 PercentageAttribute）
 * AttributeDisplay.builder(myPercentageAttr)
 *     .build();
 *
 * // 附属模组自定义属性
 * AttributeDisplay.builder(myAttribute)
 *     .priority(10)
 *     .hideValue(0)
 *     .build();
 *
 * // 自定义隐藏条件（如值 ≤ 0 时隐藏）
 * AttributeDisplay.builder(myAttribute)
 *     .hideWhen(v -> v <= 0)
 *     .build();
 * }
 * </pre>
 */
public class AttributeDisplay {
    private final Holder<Attribute> attribute;
    private final String descriptionKey;
    private final int priority;
    private final boolean percentageDisplay;
    private final DoublePredicate hidePredicate;
    private final Supplier<Component> descriptionOverride;
    private final Function<LivingEntity, Component> valueEffect;

    private AttributeDisplay(
        Holder<Attribute> attribute,
        String descriptionKey,
        int priority,
        boolean percentageDisplay,
        DoublePredicate hidePredicate,
        Supplier<Component> descriptionOverride,
        Function<LivingEntity, Component> valueEffect
    ) {
        this.attribute = attribute;
        this.descriptionKey = descriptionKey;
        this.priority = priority;
        this.percentageDisplay = percentageDisplay;
        this.hidePredicate = hidePredicate;
        this.descriptionOverride = descriptionOverride;
        this.valueEffect = valueEffect;
    }

    /**
     * 创建属性显示信息的 Builder
     *
     * @param attribute 属性 Holder
     */
    public static Builder builder(Holder<Attribute> attribute) {
        return new Builder(attribute);
    }

    public Holder<Attribute> attribute() {
        return attribute;
    }

    public String getDescriptionKey(int i) {
        return descriptionKey + "." + i;
    }

    public int priority() {
        return priority;
    }

    /**
     * 是否按百分比格式显示
     * <p>
     * 启用时将原始值乘以 scaleFactor（100）并追加 {@code %} 后缀。
     * </p>
     */
    public boolean percentageDisplay() {
        return percentageDisplay;
    }

    /**
     * 隐藏判定谓词
     * <p>
     * 当谓词对当前属性值返回 true 时，跳过不显示。
     * 返回 null 表示不启用隐藏（永不跳过）。
     * </p>
     */
    public DoublePredicate hidePredicate() {
        return hidePredicate;
    }

    /**
     * 动态描述覆盖
     * <p>
     * 将直接使用此 Supplier 返回的组件，而非静态翻译键。
     * 适用于需要在运行时动态读取配置值等场景。
     * </p>
     */
    public Supplier<Component> descriptionOverride() {
        return descriptionOverride;
    }

    /**
     * 实时效果描述
     * <p>
     * 接收目标实体，返回基于当前属性值计算后的动态效果描述组件。
     * 用于在属性显示时展示实际生效的数值（如 "增加8点最大生命值"）。
     * 返回 null 表示该属性无实时效果描述。
     * </p>
     */
    public Function<LivingEntity, Component> valueEffect() {
        return valueEffect;
    }

    public static class Builder {
        private final Holder<Attribute> attribute;
        private final String descriptionKey;
        private int priority;
        private Boolean percentageDisplay = null;  // null=自动推断, true/false=手动强制
        private DoublePredicate hidePredicate = null; // null=不隐藏
        private Supplier<Component> descriptionOverride = null;
        private Function<LivingEntity, Component> valueEffect = null;

        private Builder(Holder<Attribute> attribute) {
            this.attribute = attribute;
            this.descriptionKey = attribute.value().getDescriptionId() + ".description";
        }

        /**
         * 设置显示优先级
         *
         * @param priority 值越高越靠前显示。默认 0
         */
        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        /**
         * 强制设置是否按百分比显示。
         * <p>
         * 不调用此方法时，自动检测：若属性是 {@link PercentageAttribute} 则启用，
         * 否则不启用。
         * </p>
         *
         * @param percentageDisplay 是否按百分比格式显示
         */
        public Builder percentageDisplay(boolean percentageDisplay) {
            this.percentageDisplay = percentageDisplay;
            return this;
        }

        /**
         * 设置隐藏值。
         * <p>
         * 当属性的原始值等于此值时，跳过不显示。
         * 不调用此方法或 {@link #hideWhen(DoublePredicate)} 时，默认不隐藏。
         * </p>
         *
         * @param hideValue 不显示的值
         */
        public Builder hideValue(double hideValue) {
            this.hidePredicate = v -> v == hideValue;
            return this;
        }

        /**
         * 设置隐藏判定谓词。
         * <p>
         * 当谓词对当前属性值返回 true 时，跳过不显示。
         * 不调用此方法或 {@link #hideValue(double)} 时，默认不隐藏。
         * </p>
         *
         * @param predicate 隐藏判定谓词，传入 null 表示不隐藏
         */
        public Builder hideWhen(DoublePredicate predicate) {
            this.hidePredicate = predicate;
            return this;
        }

        /**
         * 设置动态描述覆盖。
         * <p>
         * 设置后，{@link net.zhaiji.chestcavitybeyond.util.TooltipUtil#buildAttributeDescription}
         * 将使用此 Supplier 生成描述组件，忽略静态翻译键。
         * 适用于需要在运行时动态读取配置值等场景。
         * </p>
         *
         * @param descriptionOverride 描述组件的 Supplier，传入 null 表示使用静态翻译
         */
        public Builder descriptionOverride(Supplier<Component> descriptionOverride) {
            this.descriptionOverride = descriptionOverride;
            return this;
        }

        /**
         * 设置实时效果描述。
         * <p>
         * 接收目标实体，返回基于当前属性值计算后的动态效果描述组件。
         * 用于在属性显示时展示实际生效的数值（如 "增加8点最大生命值"）。
         * </p>
         *
         * @param valueEffect 实时效果描述函数，传入 null 表示无实时效果
         */
        public Builder valueEffect(Function<LivingEntity, Component> valueEffect) {
            this.valueEffect = valueEffect;
            return this;
        }

        /**
         * 构建并注册属性显示信息
         */
        public AttributeDisplay build() {
            // 自动推断 percentageDisplay
            boolean isPercentage = percentageDisplay != null ? percentageDisplay : attribute.value() instanceof PercentageAttribute;

            AttributeDisplay display = new AttributeDisplay(
                attribute,
                descriptionKey,
                priority,
                isPercentage,
                hidePredicate,
                descriptionOverride,
                valueEffect
            );
            AttributeDisplayManager.register(display);
            return display;
        }
    }
}
