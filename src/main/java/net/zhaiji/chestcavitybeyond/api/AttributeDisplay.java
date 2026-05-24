package net.zhaiji.chestcavitybeyond.api;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.zhaiji.chestcavitybeyond.manager.AttributeDisplayManager;

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
 * // 百分比属性（自动检测 PercentageAttribute，默认隐藏 1.0）
 * AttributeDisplay.builder(myPercentageAttr)
 *     .build();
 *
 * // 附属模组自定义属性
 * AttributeDisplay.builder(myAttribute)
 *     .priority(10)
 *     .hideValue(1)
 *     .build();
 * }
 * </pre>
 */
public class AttributeDisplay {
    private final Holder<Attribute> attribute;
    private final String descriptionKey;
    private final int priority;
    private final boolean percentageDisplay;
    private final double hideValue;

    private AttributeDisplay(
        Holder<Attribute> attribute,
        String descriptionKey,
        int priority,
        boolean percentageDisplay,
        double hideValue
    ) {
        this.attribute = attribute;
        this.descriptionKey = descriptionKey;
        this.priority = priority;
        this.percentageDisplay = percentageDisplay;
        this.hideValue = hideValue;
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
     * 隐藏值
     * <p>
     * 当属性的原始值等于此值时，跳过不显示。
     * {@link Double#NaN} 表示不启用（永不跳过）。
     * </p>
     */
    public double hideValue() {
        return hideValue;
    }

    public static class Builder {
        private final Holder<Attribute> attribute;
        private final String descriptionKey;
        private int priority;
        private Boolean percentageDisplay = null;  // null=自动推断, true/false=手动强制
        private Double hideValue = null;           // null=自动推断, 其他=手动设置（含0）

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
         * 当属性的原始值等于此值时，跳过不显示（通用逻辑，适用于所有属性类型）。
         * 设为 {@link Double#NaN} 可禁用隐藏（永不跳过）。
         * 不调用此方法时，自动推断：百分比属性默认 {@code 1.0}（=100%），其他属性默认 {@code 0}。
         * </p>
         *
         * @param hideValue 不显示的值
         */
        public Builder hideValue(double hideValue) {
            this.hideValue = hideValue;
            return this;
        }

        /**
         * 构建并注册属性显示信息
         */
        public AttributeDisplay build() {
            // 自动推断 percentageDisplay
            boolean isPercentage = percentageDisplay != null ? percentageDisplay : attribute.value() instanceof PercentageAttribute;

            // 自动推断 hideValue：null → 百分比 1.0，非百分比 0；非 null → 直接使用
            double hideVal = hideValue != null ? hideValue : (isPercentage ? 1.0 : 0);

            AttributeDisplay display = new AttributeDisplay(
                attribute,
                descriptionKey,
                priority,
                isPercentage,
                hideVal
            );
            AttributeDisplayManager.register(display);
            return display;
        }
    }
}
