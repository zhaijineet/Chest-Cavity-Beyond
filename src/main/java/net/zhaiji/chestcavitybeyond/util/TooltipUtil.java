package net.zhaiji.chestcavitybeyond.util;

import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondClientConfig;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.OrganTooltip;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.api.function.OrganTooltipConsumer;
import net.zhaiji.chestcavitybeyond.api.function.TooltipSectionFunction;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.manager.ItemTagManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class TooltipUtil {
    public static final String PREFIX = "organ." + ChestCavityBeyond.MOD_ID + ".";

    /**
     * 默认的器官工具提示回调
     */
    public static OrganTooltipConsumer DEFAULT_TOOLTIP = OrganTooltip.builder().build();

    /**
     * 判断翻译键是否存在
     */
    public static boolean hasTranslation(String key) {
        return Language.getInstance().has(key);
    }

    /**
     * 添加详细文本行（.0~.N 索引格式）
     */
    public static void addDetailedLines(List<Component> lines, ItemStack stack, String type) {
        String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        int i = 0;
        String key;
        String baseKey = PREFIX + path + "." + type + ".";
        while (hasTranslation(key = baseKey + i)) {
            lines.add(Component.literal(" • ").append(Component.translatable(key)));
            i++;
        }
    }

    /**
     * 简略模式优先使用 .simple.0~.N 索引键，不存在则回退到详细文本
     */
    public static List<Component> addSimpleOrDetailedLines(ItemStack stack, String type, boolean detailed) {
        List<Component> result = new ArrayList<>();
        if (!detailed) {
            String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
            String baseKey = PREFIX + path + "." + type + ".simple.";
            int i = 0;
            String key;
            while (hasTranslation(key = baseKey + i)) {
                result.add(Component.literal(" • ").append(Component.translatable(key)));
                i++;
            }
            if (!result.isEmpty()) return result;
        }
        addDetailedLines(result, stack, type);
        return result;
    }

    /**
     * 判断是否为详细模式
     */
    public static boolean isDetailedMode(TooltipsKeyContext keyContext) {
        return ChestCavityBeyondClientConfig.detailedTooltips || keyContext.isKeyShiftDown();
    }

    /**
     * 创建段落执行函数，捕获 7 个渲染参数。
     * 返回的函数只需传入段落和行列表即可将段落输出追加到行列表。
     */
    public static BiConsumer<TooltipSectionFunction, List<Component>> sectionApply(
        ChestCavityData data,
        int index,
        ItemStack stack,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        return (section, lines) -> lines.addAll(section.apply(data, index, stack, keyContext, context, tooltipComponents, tooltipFlag));
    }

    /**
     * 将 OrganTooltip 管线构建为 OrganTooltipConsumer
     */
    public static OrganTooltipConsumer buildConsumer(OrganTooltip organTooltip) {
        return (data, index, stack, keyContext, context, tooltipComponents, tooltipFlag) -> {
            BiConsumer<TooltipSectionFunction, List<Component>> apply = sectionApply(
                data,
                index,
                stack,
                keyContext,
                context,
                tooltipComponents,
                tooltipFlag
            );
            List<Component> lines = new ArrayList<>();

            // 1. Tags
            apply.accept(organTooltip.tagsSection, lines);
            apply.accept(organTooltip.afterTags, lines);

            // 2. Attributes
            apply.accept(organTooltip.attributesSection, lines);
            apply.accept(organTooltip.afterAttributes, lines);

            // 3. Description（无标题，纯文字）
            apply.accept(organTooltip.descriptionSection, lines);
            apply.accept(organTooltip.afterDescription, lines);

            // 4. ShiftHint
            apply.accept(organTooltip.shiftHintSection, lines);
            apply.accept(organTooltip.afterShiftHint, lines);

            String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();

            // 5. PassiveEffect
            if (hasTranslation(PREFIX + path + ".passive_effect.0")) {
                lines.add(Component.translatable(PREFIX + "tooltip.header.passive_effect").withStyle(ChatFormatting.GRAY));
                apply.accept(organTooltip.passiveEffectSection, lines);
            }
            apply.accept(organTooltip.afterPassiveEffect, lines);

            // 6. ActiveSkill
            if (hasTranslation(PREFIX + path + ".active_skill.0")) {
                lines.add(Component.translatable(PREFIX + "tooltip.header.active_skill").withStyle(ChatFormatting.YELLOW));
                apply.accept(organTooltip.activeSkillSection, lines);
            }
            apply.accept(organTooltip.afterActiveSkill, lines);

            // 最终插入
            if (!lines.isEmpty()) {
                simpleTooltipAdd(tooltipComponents, lines);
            }
        };
    }

    /**
     * 为器官属性工具提示
     */
    public static List<Component> addOrganAttributeTooltip(
        ChestCavityData data,
        int index,
        ItemStack stack,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        List<Component> result = new ArrayList<>();
        Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers = ChestCavityUtil.getAttributeModifiers(
            new ChestCavitySlotContext(data, data.getOwner(), ChestCavityUtil.getSlotId(index), index, stack)
        );
        if (attributeModifiers.isEmpty()) return result;
        attributeModifiers.forEach((attribute, modifier) -> {
            double value = modifier.amount();
            if (modifier.operation() != AttributeModifier.Operation.ADD_VALUE || attribute.value() instanceof PercentageAttribute) {
                value *= 100;
            }
            if (value == (int) value) {
                int i = (int) value;
                String string = i > 0 ? "+" + i : String.valueOf(i);
                if (modifier.operation() == AttributeModifier.Operation.ADD_VALUE && attribute.value() instanceof PercentageAttribute) {
                    string += "%";
                }
                result.add(
                    Component.translatable(
                        PREFIX + "attribute.tooltips_" + modifier.operation().ordinal(),
                        string,
                        Component.translatable(attribute.value().getDescriptionId())
                    )
                );
            } else {
                String string = value > 0 ? "+" + formatAttributeValue(value) : formatAttributeValue(value);
                if (modifier.operation() == AttributeModifier.Operation.ADD_VALUE && attribute.value() instanceof PercentageAttribute) {
                    string += "%";
                }
                result.add(
                    Component.translatable(
                        PREFIX + "attribute.tooltips_" + modifier.operation().ordinal(),
                        string,
                        Component.translatable(attribute.value().getDescriptionId())
                    )
                );
            }
        });
        return result;
    }

    /**
     * 简单工具提示添加
     *
     * @param total 总工具提示
     * @param add   需要添加的工具提示
     */
    public static void simpleTooltipAdd(List<Component> total, List<Component> add) {
        for (int i = 0; i < add.size(); i++) {
            total.add(i + 1, add.get(i));
        }
    }

    /**
     * 格式化属性修饰符的数值显示
     * <p>
     * 整数：直接显示（如 "2", "+3"）
     * 浮点数：保留最多3位小数，去除末尾多余的0（如 "0.5", "1.25", "1.5" 而非 "1.500"）
     * </p>
     */
    private static String formatAttributeValue(double value) {
        // 四舍五入到3位小数，消除浮点精度偏差
        double rounded = Math.round(value * 1000.0) / 1000.0;
        if (rounded == (int) rounded) {
            return String.valueOf((int) rounded);
        }
        return String.valueOf(rounded);
    }

    /**
     * Tags 段落回调
     */
    public static List<Component> tagsSection(
        ChestCavityData data,
        int index,
        ItemStack stack,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        Component tagsLine = ItemTagManager.getTagsLine(stack);
        return !tagsLine.getString().isEmpty() ? List.of(tagsLine) : List.of();
    }

    /**
     * Description 段落回调
     */
    public static List<Component> descriptionSection(
        ChestCavityData data,
        int index,
        ItemStack stack,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        return addSimpleOrDetailedLines(stack, "description", isDetailedMode(keyContext));
    }

    /**
     * ShiftHint 段落回调
     */
    public static List<Component> shiftHintSection(
        ChestCavityData data,
        int index,
        ItemStack stack,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        if (ChestCavityBeyondClientConfig.detailedTooltips) return List.of();
        String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        String base = PREFIX + path;
        // 条件1：必须有被动效果或主动技能
        boolean hasPassiveOrSkill = hasTranslation(base + ".passive_effect.0") || hasTranslation(base + ".active_skill.0");
        if (!hasPassiveOrSkill) return List.of();
        // 条件2：必须有简略显示文本
        boolean hasSimple = hasTranslation(base + ".passive_effect.simple.0") || hasTranslation(base + ".active_skill.simple.0");
        if (!hasSimple) return List.of();
        return List.of(
            Component.empty()
                .append(Component.translatable(PREFIX + "tooltip.hint.0").withStyle(ChatFormatting.GRAY))
                .append(Component.translatable(PREFIX + "tooltip.hint.1")
                    .withStyle(keyContext.isKeyShiftDown() ? ChatFormatting.YELLOW : ChatFormatting.DARK_GRAY))
                .append(Component.translatable(PREFIX + "tooltip.hint.2").withStyle(ChatFormatting.GRAY))
        );
    }

    /**
     * PassiveEffect 段落回调
     */
    public static List<Component> passiveEffectSection(
        ChestCavityData data,
        int index,
        ItemStack stack,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        return addSimpleOrDetailedLines(stack, "passive_effect", isDetailedMode(keyContext));
    }

    /**
     * ActiveSkill 段落回调（含冷却时间显示）
     */
    public static List<Component> activeSkillSection(
        ChestCavityData data,
        int index,
        ItemStack stack,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        boolean detailed = isDetailedMode(keyContext);
        List<Component> lines = new ArrayList<>(addSimpleOrDetailedLines(stack, "active_skill", detailed));
        if (detailed) {
            lines.addAll(addCooldownLine(data, index, stack));
        }
        return lines;
    }

    /**
     * 根据器官的冷却生成冷却时间提示行
     */
    public static List<Component> addCooldownLine(ChestCavityData data, int index, ItemStack stack) {
        int cooldownTicks = ChestCavityUtil.getOrganCap(stack)
            .getCooldownTicks(ChestCavityUtil.createContext(data, index, stack));
        if (cooldownTicks > 0) {
            double seconds = Math.round(cooldownTicks / 20.0 * 10.0) / 10.0;
            String formatted = seconds == (int) seconds ? String.valueOf((int) seconds) : String.valueOf(seconds);
            return List.of(Component.literal(" • ").append(Component.translatable(PREFIX + "tooltip.cooldown", formatted)));
        }
        return List.of();
    }
}
