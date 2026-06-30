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
import net.minecraft.tags.TagKey;
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
import net.zhaiji.chestcavitybeyond.api.AttributeBonus;
import net.zhaiji.chestcavitybeyond.api.AttributeDisplay;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.api.DynamicValues;
import net.zhaiji.chestcavitybeyond.api.FormulaValue;
import net.zhaiji.chestcavitybeyond.api.OrganTooltip;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.api.function.OrganTooltipConsumer;
import net.zhaiji.chestcavitybeyond.api.function.TooltipSectionFunction;
import net.zhaiji.chestcavitybeyond.manager.AttributeDisplayManager;
import net.zhaiji.chestcavitybeyond.manager.ChestCavityTypeManager;
import net.zhaiji.chestcavitybeyond.manager.ItemTagManager;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
        DecimalFormat decimalFormat = new DecimalFormat("0.###", DecimalFormatSymbols.getInstance(Locale.US));
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat;
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
     * 按数字索引遍历添加翻译键对应的提示行（key.type.0、key.type.1、…）
     *
     * @param lines  行列表
     * @param stack  物品栈
     * @param type   段落类型前缀（如 description、passive_effect、passive_effect.simple）
     * @param prefix 行首前缀
     */
    public static void addTranslationLines(List<Component> lines, ItemStack stack, String type, String prefix) {
        int index = 0;
        String key;
        String baseKey = getBaseKey(stack) + "." + type + ".";
        while (hasTranslation(key = baseKey + index)) {
            lines.add(Component.literal(prefix).append(Component.translatable(key)));
            index++;
        }
    }

    /**
     * 判断指定段落类型是否存在 simple.0 翻译键
     */
    private static boolean hasSimpleTranslation(ItemStack stack, String baseType) {
        return hasTranslation(getBaseKey(stack) + "." + baseType + ".simple.0");
    }

    /**
     * 简略模式优先使用 simple 翻译键，不存在则回退到详细文本
     *
     * @param stack    物品栈
     * @param type     段落类型（如 passive_effect、active_skill）
     * @param detailed 是否详细模式
     * @param prefix   行首前缀
     */
    public static List<Component> addSimpleOrDetailedLines(ItemStack stack, String type, boolean detailed, String prefix) {
        List<Component> result = new ArrayList<>();
        boolean useSimple = !detailed && hasSimpleTranslation(stack, type);
        addTranslationLines(result, stack, useSimple ? type + ".simple" : type, prefix);
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
        return (slotContext, keyContext, context, tooltipComponents, tooltipFlag) -> {
            BiConsumer<TooltipSectionFunction, List<Component>> apply = (section, lines) -> lines.addAll(section.apply(
                slotContext,
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

            // 4. Hint
            apply.accept(organTooltip.hintSection, lines);
            apply.accept(organTooltip.afterHint, lines);

            String base = getBaseKey(slotContext.stack());

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
        ChestCavitySlotContext slotContext,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        List<Component> result = new ArrayList<>();
        Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers = ChestCavityUtil.getAttributeModifiers(slotContext);
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
     * 为胸腔类型的器官属性加值生成 tooltip 行
     *
     * @param bonuses 属性加成列表
     * @return 格式化后的 tooltip 行
     */
    public static List<Component> attributeBonusTooltip(List<AttributeBonus> bonuses) {
        List<Component> result = new ArrayList<>();
        for (AttributeBonus bonus : bonuses) {
            boolean isPercentage = isPercentageAttribute(bonus.attribute());
            boolean isAddValue = bonus.operation() == AttributeModifier.Operation.ADD_VALUE;
            double value = bonus.bonusValue();
            if (!isAddValue || isPercentage) {
                value *= 100;
            }
            String string = (value > 0 ? " +" : " ") + formatAttributeValue(value);
            if (isAddValue && isPercentage) {
                string += "%";
            }
            result.add(
                Component.translatable(
                    PREFIX + "attribute.tooltips_" + bonus.operation().ordinal(),
                    string,
                    Component.translatable(bonus.attribute().value().getDescriptionId())
                )
            );
        }
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
        // 动态描述优先
        Supplier<Component> override = attributeDisplay.descriptionOverride();
        if (override != null) {
            return override.get();
        }
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
        ChestCavityType type = ChestCavityTypeManager.getType(target);
        Map<Holder<Attribute>, Double> typeAttrs = type.getDefaultAttributes(target.getType());
        int count = 0;

        for (AttributeDisplay display : displays) {
            AttributeInstance instance = target.getAttribute(display.attribute());
            if (instance == null) continue;
            double value = instance.getValue();

            if (typeAttrs.containsKey(display.attribute())) {
                // 属性在类型默认属性集中：按 hidePredicate 逻辑过滤
                boolean shouldSkip = display.hidePredicate() != null && display.hidePredicate().test(value);
                if (shouldSkip) continue;
            } else {
                // 属性不在类型默认属性集中：当前值等于默认基础值则跳过，有变化则显示
                if (value == instance.getBaseValue()) continue;
            }

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

            // 实时效果（独立新行）
            Function<LivingEntity, Component> effectFn = display.valueEffect();
            if (effectFn != null) {
                Component effectComp = effectFn.apply(target);
                if (effectComp != null) {
                    viewer.sendSystemMessage(
                        Component.literal("   ↳ ").append(effectComp).withStyle(ChatFormatting.GRAY)
                    );
                }
            }

            count++;
        }

        return count;
    }

    /**
     * Tags 段落回调
     */
    public static List<Component> tagsSection(
        ChestCavitySlotContext slotContext,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        Component tagsLine = ItemTagManager.getTagsLine(slotContext.stack());
        return !tagsLine.getString().isEmpty() ? List.of(tagsLine) : List.of();
    }

    /**
     * Description 段落回调
     */
    public static List<Component> descriptionSection(
        ChestCavitySlotContext slotContext,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        List<Component> result = new ArrayList<>();
        addTranslationLines(result, slotContext.stack(), "description", "");
        return result;
    }

    /**
     * Shift 提示段落
     */
    public static TooltipSectionFunction shiftHintSection() {
        return (slotContext, keyContext, context, tooltipComponents, tooltipFlag) -> {
            if (ChestCavityBeyondClientConfig.detailedTooltips) {
                return List.of();
            }
            String base = getBaseKey(slotContext.stack());
            boolean hasEffect = hasTranslation(base + ".passive_effect.0") || hasTranslation(base + ".active_skill.0");
            boolean hasSimple = hasTranslation(base + ".passive_effect.simple.0") || hasTranslation(base + ".active_skill.simple.0");
            if (!hasEffect || !hasSimple) return List.of();
            return List.of(Component.translatable(
                PREFIX + "tooltip.shift_hint",
                Component.translatable(PREFIX + "tooltip.hint.shift")
                    .withStyle(keyContext.isKeyShiftDown() ? ChatFormatting.YELLOW : ChatFormatting.DARK_GRAY)
            ).withStyle(ChatFormatting.GRAY));
        };
    }

    /**
     * Ctrl 公式提示段落
     */
    public static TooltipSectionFunction ctrlHintSection() {
        return (slotContext, keyContext, context, tooltipComponents, tooltipFlag) ->
            List.of(Component.translatable(
                    PREFIX + "tooltip.ctrl_hint",
                    Component.translatable(PREFIX + "tooltip.hint.ctrl")
                        .withStyle(keyContext.isKeyCtrlDown() ? ChatFormatting.YELLOW : ChatFormatting.DARK_GRAY)
                )
                .withStyle(ChatFormatting.GRAY));
    }

    /**
     * 默认 Hint 段落，Shift 始终挂载，Ctrl 仅在 hasDynamicValues 为 true 时挂载
     */
    public static TooltipSectionFunction hintSection(boolean hasDynamicValues) {
        TooltipSectionFunction shift = shiftHintSection();
        if (!hasDynamicValues) {
            return shift;
        }
        TooltipSectionFunction ctrl = ctrlHintSection();
        return (slotContext, keyContext, context, tooltipComponents, tooltipFlag) -> {
            List<Component> result = new ArrayList<>();
            result.addAll(shift.apply(slotContext, keyContext, context, tooltipComponents, tooltipFlag));
            result.addAll(ctrl.apply(slotContext, keyContext, context, tooltipComponents, tooltipFlag));
            return result;
        };
    }

    /**
     * PassiveEffect 段落回调
     */
    public static List<Component> passiveEffectSection(
        ChestCavitySlotContext slotContext,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        return addSimpleOrDetailedLines(slotContext.stack(), "passive_effect", isDetailedMode(keyContext), DEFAULT_PREFIX);
    }

    /**
     * ActiveSkill 段落回调（含冷却时间显示）
     */
    public static List<Component> activeSkillSection(
        ChestCavitySlotContext slotContext,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        boolean detailed = isDetailedMode(keyContext);
        List<Component> lines = addSimpleOrDetailedLines(slotContext.stack(), "active_skill", detailed, DEFAULT_PREFIX);
        // 详细模式 或 非详细模式下没有简略行（回退到了详细描述行）时，显示冷却时间
        if (detailed || !hasTranslation(getBaseKey(slotContext.stack()) + ".active_skill.simple.0")) {
            lines.addAll(cooldownLine(slotContext));
        }
        return lines;
    }

    /**
     * 根据器官的冷却生成冷却时间提示行
     */
    public static List<Component> cooldownLine(ChestCavitySlotContext slotContext) {
        int cooldownTicks = ChestCavityUtil.getOrganCap(slotContext.stack()).getCooldownTicks(slotContext);
        if (cooldownTicks > 0) {
            String formatted = formatAttributeValue(cooldownTicks / 20.0);
            return List.of(Component.literal(DEFAULT_PREFIX).append(Component.translatable(PREFIX + "tooltip.cooldown", formatted)));
        }
        return List.of();
    }

    /**
     * 渲染含动态公式的效果段落，active_skill 自动追加冷却时间
     */
    public static List<Component> dynamicEffectLines(
        ChestCavitySlotContext slotContext,
        TooltipsKeyContext keyContext,
        String baseType,
        DynamicValues dynamicValues
    ) {
        boolean useSimple = !isDetailedMode(keyContext) && hasSimpleTranslation(slotContext.stack(), baseType);
        Map<Integer, List<FormulaValue>> values = dynamicValues != null
                                                  ? (useSimple ? dynamicValues.simple() : dynamicValues.detailed())
                                                  : null;

        ItemStack stack = slotContext.stack();
        String effectiveType = useSimple ? baseType + ".simple" : baseType;
        String baseKey = getBaseKey(stack) + "." + effectiveType + ".";

        List<Component> result = new ArrayList<>();
        int index = 0;
        String key;
        while (hasTranslation(key = baseKey + index)) {
            List<FormulaValue> lineValues = values != null ? values.get(index) : null;
            if (lineValues != null && !lineValues.isEmpty()) {
                Object[] componentArgs = lineValues.stream()
                    .map(formulaValue -> formulaValue.buildComponent(slotContext, keyContext.isKeyCtrlDown()))
                    .toArray();
                result.add(Component.literal(DEFAULT_PREFIX).append(Component.translatable(key, componentArgs)));
            } else {
                result.add(Component.literal(DEFAULT_PREFIX).append(Component.translatable(key)));
            }
            index++;
        }

        if ("active_skill".equals(baseType) && (isDetailedMode(keyContext) || !useSimple)) {
            result.addAll(cooldownLine(slotContext));
        }
        return result;
    }

    /**
     * 动态被动效果段落工厂
     */
    public static TooltipSectionFunction dynamicPassiveEffect(
        Function<ChestCavitySlotContext, DynamicValues> valuesFunction
    ) {
        return (slotContext, keyContext, context, tooltipComponents, tooltipFlag) ->
            dynamicEffectLines(slotContext, keyContext, "passive_effect", valuesFunction.apply(slotContext));
    }

    /**
     * 动态主动技能段落工厂
     */
    public static TooltipSectionFunction dynamicActiveSkill(
        Function<ChestCavitySlotContext, DynamicValues> valuesFunction
    ) {
        return (slotContext, keyContext, context, tooltipComponents, tooltipFlag) ->
            dynamicEffectLines(slotContext, keyContext, "active_skill", valuesFunction.apply(slotContext));
    }

    /**
     * 公式操作符（不换行空格包裹）
     */
    public static MutableComponent formulaOperator(String operator) {
        return Component.literal("\u00A0" + operator + "\u00A0");
    }

    /**
     * 属性名翻译组件
     */
    public static MutableComponent attributeName(Holder<Attribute> attribute) {
        return Component.translatable(attribute.value().getDescriptionId());
    }

    /**
     * 物品标签名翻译组件
     */
    public static MutableComponent tagName(TagKey<Item> tag) {
        return ItemTagManager.getTagDisplayName(tag);
    }

    /**
     * 标签器官数量名（形如「机械器官数量」）
     */
    public static MutableComponent tagOrganCountName(TagKey<Item> tag) {
        return Component.translatable(PREFIX + "formula.tag_organ_count", tagName(tag));
    }
}
