package net.zhaiji.chestcavitybeyond.api;

import net.zhaiji.chestcavitybeyond.api.function.OrganTooltipConsumer;
import net.zhaiji.chestcavitybeyond.api.function.TooltipSectionFunction;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;

import java.util.List;
import java.util.function.Function;

/**
 * 器官工具提示构建管线
 * <p>
 * 构建顺序：Tags → afterTags → Description → afterDescription → Attributes → afterAttributes
 * → Hint → afterHint → PassiveEffect → afterPassiveEffect → ActiveSkill → afterActiveSkill
 * </p>
 */
public class OrganTooltip {
    private static final TooltipSectionFunction EMPTY_SECTION = (slotContext, keyContext, context, tooltipComponents, tooltipFlag) -> List.of();

    public final TooltipSectionFunction tagsSection;
    public final TooltipSectionFunction afterTags;
    public final TooltipSectionFunction descriptionSection;
    public final TooltipSectionFunction afterDescription;
    public final TooltipSectionFunction attributesSection;
    public final TooltipSectionFunction afterAttributes;
    public final TooltipSectionFunction hintSection;
    public final TooltipSectionFunction afterHint;
    public final TooltipSectionFunction passiveEffectSection;
    public final TooltipSectionFunction afterPassiveEffect;
    public final TooltipSectionFunction activeSkillSection;
    public final TooltipSectionFunction afterActiveSkill;

    private final Function<OrganTooltip, OrganTooltipConsumer> consumer;

    private OrganTooltip(Builder builder) {
        this.tagsSection = builder.tagsSection;
        this.afterTags = builder.afterTags;
        this.descriptionSection = builder.descriptionSection;
        this.afterDescription = builder.afterDescription;
        this.attributesSection = builder.attributesSection;
        this.afterAttributes = builder.afterAttributes;
        this.hintSection = builder.hintSection;
        this.afterHint = builder.afterHint;
        this.passiveEffectSection = builder.passiveEffectSection;
        this.afterPassiveEffect = builder.afterPassiveEffect;
        this.activeSkillSection = builder.activeSkillSection;
        this.afterActiveSkill = builder.afterActiveSkill;
        this.consumer = builder.consumer;
    }

    /**
     * 创建 Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 将管线构建为 OrganTooltipConsumer
     */
    public OrganTooltipConsumer consumer() {
        return consumer.apply(this);
    }

    public static class Builder {
        private TooltipSectionFunction tagsSection = TooltipUtil::tagsSection;
        private TooltipSectionFunction descriptionSection = TooltipUtil::descriptionSection;
        private TooltipSectionFunction attributesSection = TooltipUtil::organAttributeTooltip;
        private TooltipSectionFunction hintSection;
        private TooltipSectionFunction passiveEffectSection = TooltipUtil::passiveEffectSection;
        private TooltipSectionFunction activeSkillSection = TooltipUtil::activeSkillSection;

        private TooltipSectionFunction afterTags = EMPTY_SECTION;
        private TooltipSectionFunction afterDescription = EMPTY_SECTION;
        private TooltipSectionFunction afterAttributes = EMPTY_SECTION;
        private TooltipSectionFunction afterHint = EMPTY_SECTION;
        private TooltipSectionFunction afterPassiveEffect = EMPTY_SECTION;
        private TooltipSectionFunction afterActiveSkill = EMPTY_SECTION;

        private boolean hasDynamicPassiveEffect = false;
        private boolean hasDynamicActiveSkill = false;
        private boolean hasCustomHint = false;

        private Function<OrganTooltip, OrganTooltipConsumer> consumer = TooltipUtil::buildConsumer;

        private Builder() {
        }

        public Builder tags(TooltipSectionFunction section) {
            this.tagsSection = section;
            return this;
        }

        public Builder afterTags(TooltipSectionFunction section) {
            this.afterTags = section;
            return this;
        }

        public Builder description(TooltipSectionFunction section) {
            this.descriptionSection = section;
            return this;
        }

        public Builder afterDescription(TooltipSectionFunction section) {
            this.afterDescription = section;
            return this;
        }

        public Builder attributes(TooltipSectionFunction section) {
            this.attributesSection = section;
            return this;
        }

        public Builder afterAttributes(TooltipSectionFunction section) {
            this.afterAttributes = section;
            return this;
        }

        public Builder hint(TooltipSectionFunction section) {
            this.hintSection = section;
            this.hasCustomHint = true;
            return this;
        }

        public Builder afterHint(TooltipSectionFunction section) {
            this.afterHint = section;
            return this;
        }

        public Builder passiveEffect(TooltipSectionFunction section) {
            this.passiveEffectSection = section;
            this.hasDynamicPassiveEffect = false;
            return this;
        }

        public Builder afterPassiveEffect(TooltipSectionFunction section) {
            this.afterPassiveEffect = section;
            return this;
        }

        public Builder activeSkill(TooltipSectionFunction section) {
            this.activeSkillSection = section;
            this.hasDynamicActiveSkill = false;
            return this;
        }

        public Builder afterActiveSkill(TooltipSectionFunction section) {
            this.afterActiveSkill = section;
            return this;
        }

        public Builder consumer(Function<OrganTooltip, OrganTooltipConsumer> factory) {
            this.consumer = factory;
            return this;
        }

        /**
         * 使用动态数值渲染被动效果段落，并自动开启 Ctrl 公式提示
         */
        public Builder dynamicPassiveEffect(Function<ChestCavitySlotContext, DynamicValues> valuesFunction) {
            this.passiveEffectSection = TooltipUtil.dynamicPassiveEffect(valuesFunction);
            this.hasDynamicPassiveEffect = true;
            return this;
        }

        /**
         * 使用动态数值渲染主动技能段落，并自动开启 Ctrl 公式提示
         */
        public Builder dynamicActiveSkill(Function<ChestCavitySlotContext, DynamicValues> valuesFunction) {
            this.activeSkillSection = TooltipUtil.dynamicActiveSkill(valuesFunction);
            this.hasDynamicActiveSkill = true;
            return this;
        }

        /**
         * 构建管线并返回 OrganTooltipConsumer
         */
        public OrganTooltipConsumer build() {
            if (!this.hasCustomHint) {
                boolean hasDynamicTooltipValues = this.hasDynamicPassiveEffect || this.hasDynamicActiveSkill;
                this.hintSection = TooltipUtil.hintSection(hasDynamicTooltipValues);
            }
            return new OrganTooltip(this).consumer();
        }
    }
}
