package net.zhaiji.chestcavitybeyond.api;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Function;

/**
 * 动态公式显示
 */
public record FormulaValue(
    Function<ChestCavitySlotContext, MutableComponent> valueProvider,
    Function<ChestCavitySlotContext, MutableComponent> formulaProvider
) {

    /**
     * 构建嵌入描述文本的 Component
     *
     * @param slotContext 槽位上下文（entity 为 null 时显示 "?"）
     * @param showFormula 是否展开公式
     */
    public Component buildComponent(ChestCavitySlotContext slotContext, boolean showFormula) {
        if (slotContext.entity() == null) {
            return Component.literal("?").withStyle(ChatFormatting.YELLOW);
        }
        MutableComponent value = valueProvider.apply(slotContext);
        // 仅当未显式设置颜色时使用默认黄色，保留 valueProvider 自定义颜色
        if (value.getStyle().getColor() == null) {
            value = value.withStyle(ChatFormatting.YELLOW);
        }
        if (!showFormula) {
            return value;
        }
        return Component.empty()
            .append(value)
            .append(Component.literal("\u00A0=\u00A0").withStyle(ChatFormatting.GRAY))
            .append(formulaProvider.apply(slotContext).withStyle(ChatFormatting.GRAY));
    }
}
