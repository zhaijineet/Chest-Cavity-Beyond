package net.zhaiji.chestcavitybeyond.api;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
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
 *     .build();
 *
 * // 附属模组自定义属性
 * AttributeDisplay.builder(myAttribute)
 *     .priority(10)
 *     .showWhenZero(true)
 *     .build();
 * }
 * </pre>
 */
public class AttributeDisplay {
    private final Holder<Attribute> attribute;
    private final String descriptionKey;
    private final boolean showWhenZero;
    private final int priority;

    private AttributeDisplay(Builder builder) {
        this.attribute = builder.attribute;
        this.descriptionKey = builder.descriptionKey;
        this.showWhenZero = builder.showWhenZero;
        this.priority = builder.priority;
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

    public boolean showWhenZero() {
        return showWhenZero;
    }

    public int priority() {
        return priority;
    }

    public static class Builder {
        private final Holder<Attribute> attribute;
        private final String descriptionKey;
        private boolean showWhenZero = false;
        private int priority;

        private Builder(Holder<Attribute> attribute) {
            this.attribute = attribute;
            this.descriptionKey = attribute.value().getDescriptionId() + ".description";
        }

        /**
         * 设置值为0时是否显示
         *
         * @param showWhenZero 默认 false（不显示）
         */
        public Builder showWhenZero(boolean showWhenZero) {
            this.showWhenZero = showWhenZero;
            return this;
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
         * 构建并注册属性显示信息
         */
        public AttributeDisplay build() {
            AttributeDisplay display = new AttributeDisplay(this);
            AttributeDisplayManager.register(display);
            return display;
        }
    }
}
