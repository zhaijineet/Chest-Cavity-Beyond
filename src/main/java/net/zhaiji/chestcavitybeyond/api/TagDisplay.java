package net.zhaiji.chestcavitybeyond.api;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.zhaiji.chestcavitybeyond.manager.ItemTagManager;

/**
 * Tag 显示信息
 * <p>
 * 通过 Builder 模式构造，定义物品 Tag 的显示颜色和优先级。
 * 构建时自动注册到 {@link ItemTagManager}。
 * </p>
 * 示例用法：
 * <pre>
 * {@code
 * TagDisplay.builder(MyTags.MY_TAG)
 *     .color(0xFFFF5555)
 *     .priority(50)
 *     .build();
 *
 * Component line = ItemTagManager.getTagsLine(stack);
 * }
 * </pre>
 */
public class TagDisplay {
    private final TagKey<Item> tag;
    private final int color;
    private final int priority;

    private TagDisplay(Builder builder) {
        this.tag = builder.tag;
        this.color = builder.color;
        this.priority = builder.priority;
    }

    /**
     * 创建 Tag 显示信息的 Builder
     */
    public static Builder builder(TagKey<Item> tag) {
        return new Builder(tag);
    }

    public TagKey<Item> tag() {
        return tag;
    }

    public int color() {
        return color;
    }

    public int priority() {
        return priority;
    }

    public static class Builder {
        private TagKey<Item> tag;
        private int color = 0xFFFFFFFF;
        private int priority;

        private Builder(TagKey<Item> tag) {
            this.tag = tag;
        }

        /**
         * 设置文本颜色
         *
         * @param color 文本颜色（ARGB 格式，如 0xFF55FFFF）
         */
        public Builder color(int color) {
            this.color = color;
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
         * 构建并注册 Tag 显示信息
         */
        public TagDisplay build() {
            TagDisplay display = new TagDisplay(this);
            ItemTagManager.register(display);
            return display;
        }
    }
}
