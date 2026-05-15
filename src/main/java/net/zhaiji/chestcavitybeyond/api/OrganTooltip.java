package net.zhaiji.chestcavitybeyond.api;

import net.zhaiji.chestcavitybeyond.api.function.OrganTooltipConsumer;
import net.zhaiji.chestcavitybeyond.api.function.TooltipSectionFunction;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;

import java.util.List;
import java.util.function.Function;

/**
 * 器官工具提示构建管线
 * <p>
 * 构建顺序：Tags → afterTags → Attributes → afterAttributes → Description → afterDescription
 * → ShiftHint → afterShiftHint → PassiveEffect → afterPassiveEffect → ActiveSkill → afterActiveSkill
 * </p>
 */
public class OrganTooltip {
    private static final TooltipSectionFunction EMPTY_SECTION = (data, index, stack, keyContext, context, tooltipComponents, tooltipFlag) -> List.of();

    public final TooltipSectionFunction tagsSection;
    public final TooltipSectionFunction afterTags;
    public final TooltipSectionFunction attributesSection;
    public final TooltipSectionFunction afterAttributes;
    public final TooltipSectionFunction descriptionSection;
    public final TooltipSectionFunction afterDescription;
    public final TooltipSectionFunction shiftHintSection;
    public final TooltipSectionFunction afterShiftHint;
    public final TooltipSectionFunction passiveEffectSection;
    public final TooltipSectionFunction afterPassiveEffect;
    public final TooltipSectionFunction activeSkillSection;
    public final TooltipSectionFunction afterActiveSkill;

    private final Function<OrganTooltip, OrganTooltipConsumer> consumer;

    private OrganTooltip(Builder builder) {
        this.tagsSection = builder.tagsSection;
        this.afterTags = builder.afterTags;
        this.attributesSection = builder.attributesSection;
        this.afterAttributes = builder.afterAttributes;
        this.descriptionSection = builder.descriptionSection;
        this.afterDescription = builder.afterDescription;
        this.shiftHintSection = builder.shiftHintSection;
        this.afterShiftHint = builder.afterShiftHint;
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
        private TooltipSectionFunction attributesSection = TooltipUtil::addOrganAttributeTooltip;
        private TooltipSectionFunction descriptionSection = TooltipUtil::descriptionSection;
        private TooltipSectionFunction shiftHintSection = TooltipUtil::shiftHintSection;
        private TooltipSectionFunction passiveEffectSection = TooltipUtil::passiveEffectSection;
        private TooltipSectionFunction activeSkillSection = TooltipUtil::activeSkillSection;

        private TooltipSectionFunction afterTags = EMPTY_SECTION;
        private TooltipSectionFunction afterAttributes = EMPTY_SECTION;
        private TooltipSectionFunction afterDescription = EMPTY_SECTION;
        private TooltipSectionFunction afterShiftHint = EMPTY_SECTION;
        private TooltipSectionFunction afterPassiveEffect = EMPTY_SECTION;
        private TooltipSectionFunction afterActiveSkill = EMPTY_SECTION;

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

        public Builder attributes(TooltipSectionFunction section) {
            this.attributesSection = section;
            return this;
        }

        public Builder afterAttributes(TooltipSectionFunction section) {
            this.afterAttributes = section;
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

        public Builder shiftHint(TooltipSectionFunction section) {
            this.shiftHintSection = section;
            return this;
        }

        public Builder afterShiftHint(TooltipSectionFunction section) {
            this.afterShiftHint = section;
            return this;
        }

        public Builder passiveEffect(TooltipSectionFunction section) {
            this.passiveEffectSection = section;
            return this;
        }

        public Builder afterPassiveEffect(TooltipSectionFunction section) {
            this.afterPassiveEffect = section;
            return this;
        }

        public Builder activeSkill(TooltipSectionFunction section) {
            this.activeSkillSection = section;
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
         * 构建管线并返回 OrganTooltipConsumer
         */
        public OrganTooltipConsumer build() {
            return new OrganTooltip(this).consumer();
        }
    }
}
