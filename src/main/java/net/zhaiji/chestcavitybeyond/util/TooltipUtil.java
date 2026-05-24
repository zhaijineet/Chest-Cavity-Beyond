package net.zhaiji.chestcavitybeyond.util;

import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondClientConfig;
import net.zhaiji.chestcavitybeyond.api.AttributeDisplay;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.OrganTooltip;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.api.function.OrganTooltipConsumer;
import net.zhaiji.chestcavitybeyond.api.function.TooltipSectionFunction;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.manager.AttributeDisplayManager;
import net.zhaiji.chestcavitybeyond.manager.ItemTagManager;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

public class TooltipUtil {
    public static final String PREFIX = "organ." + ChestCavityBeyond.MOD_ID + ".";
    /**
     * 格式化属性修饰符的数值显示
     * <p>
     * 整数：直接显示（如 "2", "+3"）
     * 浮点数：保留最多3位小数，去除末尾多余的0（如 "0.5", "1.25", "1.5" 而非 "1.500"）
     * </p>
     */
    private static final ThreadLocal<DecimalFormat> DECIMAL_FORMAT = ThreadLocal.withInitial(() -> {
        DecimalFormat df = new DecimalFormat("0.###", DecimalFormatSymbols.getInstance(Locale.US));
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df;
    });

    // 普通空格会导致自动换行时，此前缀被错误的分割为独自占据一行，改为使用\u00A0不换行空格
    public static String DEFAULT_PREFIX = "\u00A0•\u00A0";

    /**
     * 默认的器官工具提示回调
     */
    public static OrganTooltipConsumer DEFAULT_TOOLTIP = OrganTooltip.builder().build();

    /**
     * 获取动态前缀 + path 的组合，方便直接拼接 type
     */
    public static String getBaseKey(ItemStack stack) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return "organ." + key.getNamespace() + "." + key.getPath();
    }

    /**
     * 判断翻译键是否存在
     */
    public static boolean hasTranslation(String key) {
        return Language.getInstance().has(key);
    }

    /**
     * 添加详细文本行（.0~.N 索引格式）
     *
     * @param lines  行列表
     * @param stack  物品栈
     * @param type   段落类型（如 "description"、"passive_effect"）
     * @param prefix 行首前缀（如 " • "）
     */
    public static void addDetailedLines(List<Component> lines, ItemStack stack, String type, String prefix) {
        int i = 0;
        String key;
        String baseKey = getBaseKey(stack) + "." + type + ".";
        while (hasTranslation(key = baseKey + i)) {
            lines.add(Component.literal(prefix).append(Component.translatable(key)));
            i++;
        }
    }

    /**
     * 简略模式优先使用 .simple.0~.N 索引键，不存在则回退到详细文本
     *
     * @param stack    物品栈
     * @param type     段落类型（如 "passive_effect"、"active_skill"）
     * @param detailed 是否为详细模式
     * @param prefix   行首前缀（如 " • "）
     */
    public static List<Component> addSimpleOrDetailedLines(ItemStack stack, String type, boolean detailed, String prefix) {
        List<Component> result = new ArrayList<>();
        if (!detailed) {
            addDetailedLines(result, stack, type + ".simple", prefix);
            if (!result.isEmpty()) return result;
        }
        addDetailedLines(result, stack, type, prefix);
        return result;
    }

    /**
     * 判断是否为详细模式
     */
    public static boolean isDetailedMode(TooltipsKeyContext keyContext) {
        return ChestCavityBeyondClientConfig.detailedTooltips || keyContext.isKeyShiftDown();
    }

    /**
     * 将 OrganTooltip 管线构建为 OrganTooltipConsumer
     */
    public static OrganTooltipConsumer buildConsumer(OrganTooltip organTooltip) {
        return (data, index, stack, keyContext, context, tooltipComponents, tooltipFlag) -> {
            BiConsumer<TooltipSectionFunction, List<Component>> apply = (section, lines) -> lines.addAll(section.apply(
                data,
                index,
                stack,
                keyContext,
                context,
                tooltipComponents,
                tooltipFlag
            ));
            List<Component> lines = new ArrayList<>();

            // 1. Tags
            apply.accept(organTooltip.tagsSection, lines);
            apply.accept(organTooltip.afterTags, lines);

            // 2. Description
            apply.accept(organTooltip.descriptionSection, lines);
            apply.accept(organTooltip.afterDescription, lines);

            // 3. Attributes
            apply.accept(organTooltip.attributesSection, lines);
            apply.accept(organTooltip.afterAttributes, lines);

            // 4. ShiftHint
            apply.accept(organTooltip.shiftHintSection, lines);
            apply.accept(organTooltip.afterShiftHint, lines);

            String base = getBaseKey(stack);

            // 5. PassiveEffect
            if (hasTranslation(base + ".passive_effect.0")) {
                lines.add(Component.translatable(PREFIX + "tooltip.header.passive_effect").withStyle(ChatFormatting.GRAY));
                apply.accept(organTooltip.passiveEffectSection, lines);
            }
            apply.accept(organTooltip.afterPassiveEffect, lines);

            // 6. ActiveSkill
            if (hasTranslation(base + ".active_skill.0")) {
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
    public static List<Component> organAttributeTooltip(
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
            boolean isPercentage = isPercentageAttribute(attribute);
            boolean isAddValue = modifier.operation() == AttributeModifier.Operation.ADD_VALUE;
            double value = modifier.amount();
            if (!isAddValue || isPercentage) {
                value *= 100;
            }
            String string = (value > 0 ? " +" : " ") + formatAttributeValue(value);
            if (isAddValue && isPercentage) {
                string += "%";
            }
            result.add(
                Component.translatable(
                    PREFIX + "attribute.tooltips_" + modifier.operation().ordinal(),
                    string,
                    Component.translatable(attribute.value().getDescriptionId())
                )
            );
        });
        return result;
    }

    /**
     * 简单工具提示添加（从索引1开始插入）
     *
     * @param total 总工具提示
     * @param add   需要添加的工具提示
     */
    public static void simpleTooltipAdd(List<Component> total, List<Component> add) {
        // 一般来说不会传入空列表，但万一呢？
        if (total.isEmpty()) {
            total.addAll(add);
        } else {
            total.addAll(1, add);
        }
    }

    public static String formatAttributeValue(double value) {
        return DECIMAL_FORMAT.get().format(value);
    }

    /**
     * 构建属性描述的悬停文本（多行）
     * <p>
     * 使用索引翻译键模式 {@code <descriptionKey>.0} ~ {@code <descriptionKey>.N}，
     * 行间以换行分隔。
     * 无描述行时显示"暂无详细描述"。
     * </p>
     *
     * @param attributeDisplay 属性显示信息
     * @return 悬停文本组件
     */
    public static Component buildAttributeDescription(AttributeDisplay attributeDisplay) {
        MutableComponent hover = Component.empty();
        int i = 0;
        String lineKey;
        while (hasTranslation(lineKey = attributeDisplay.getDescriptionKey(i))) {
            if (i > 0) hover.append(Component.literal("\n"));
            hover.append(Component.translatable(lineKey));
            i++;
        }
        if (i == 0) {
            hover.append(Component.translatable("commands.chestcavitybeyond.attributes.no_description"));
        }
        return hover;
    }

    /**
     * 判断属性是否为百分比属性
     */
    public static boolean isPercentageAttribute(Holder<Attribute> attribute) {
        return attribute.value() instanceof PercentageAttribute;
    }

    /**
     * 向指定玩家发送目标实体的胸腔属性信息
     *
     * @param viewer 查看属性的玩家
     * @param target 被查看属性的目标实体
     * @return 显示的属性数量
     */
    public static int sendAttributeDisplay(ServerPlayer viewer, LivingEntity target) {
        // 标题行（含目标名称）
        viewer.sendSystemMessage(
            Component.translatable("commands.chestcavitybeyond.attributes.header", target.getDisplayName()).withStyle(ChatFormatting.YELLOW)
        );

        List<AttributeDisplay> displays = AttributeDisplayManager.getDisplays();
        int count = 0;

        for (AttributeDisplay display : displays) {
            AttributeInstance instance = target.getAttribute(display.attribute());
            if (instance == null) continue;
            double value = instance.getValue();

            // 跳过显示判断：hideValue ≠ NaN 时匹配即跳过
            boolean shouldSkip = !Double.isNaN(display.hideValue()) && value == display.hideValue();
            if (shouldSkip) continue;

            // 属性名（高亮 + 悬停）
            Component nameComp =
                Component.translatable(display.attribute().value().getDescriptionId())
                    .withStyle(style -> style
                        .withColor(ChatFormatting.AQUA)
                        .withHoverEvent(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            TooltipUtil.buildAttributeDescription(display)
                        ))
                    );

            // 属性值
            String valueStr;
            if (display.percentageDisplay()) {
                double displayValue = value * 100;
                valueStr = TooltipUtil.formatAttributeValue(displayValue) + "%";
            } else {
                valueStr = (value > 0 ? "+" : "") + TooltipUtil.formatAttributeValue(value);
            }
            Component valueComp = Component.literal(valueStr);

            viewer.sendSystemMessage(
                Component.literal(" ")
                    .append(nameComp)
                    .append(Component.literal(": "))
                    .append(valueComp)
            );
            count++;
        }

        return count;
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
        List<Component> result = new ArrayList<>();
        addDetailedLines(result, stack, "description", "");
        return result;
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
        String base = getBaseKey(stack);
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
        return addSimpleOrDetailedLines(stack, "passive_effect", isDetailedMode(keyContext), DEFAULT_PREFIX);
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
        List<Component> lines = addSimpleOrDetailedLines(stack, "active_skill", detailed, DEFAULT_PREFIX);
        // 详细模式 或 非详细模式下没有简略行（回退到了详细描述行）时，显示冷却时间
        if (detailed || !hasTranslation(getBaseKey(stack) + ".active_skill.simple.0")) {
            lines.addAll(cooldownLine(data, index, stack));
        }
        return lines;
    }

    /**
     * 根据器官的冷却生成冷却时间提示行
     */
    public static List<Component> cooldownLine(ChestCavityData data, int index, ItemStack stack) {
        int cooldownTicks = ChestCavityUtil.getOrganCap(stack).getCooldownTicks(ChestCavityUtil.createContext(data, index, stack));
        if (cooldownTicks > 0) {
            String formatted = formatAttributeValue(cooldownTicks / 20.0);
            return List.of(Component.literal(DEFAULT_PREFIX).append(Component.translatable(PREFIX + "tooltip.cooldown", formatted)));
        }
        return List.of();
    }
}
