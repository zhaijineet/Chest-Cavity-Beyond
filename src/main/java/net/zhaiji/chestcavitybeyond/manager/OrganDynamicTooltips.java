package net.zhaiji.chestcavitybeyond.manager;

import net.minecraft.network.chat.Component;
import net.zhaiji.chestcavitybeyond.api.DynamicValues;
import net.zhaiji.chestcavitybeyond.api.FormulaValue;
import net.zhaiji.chestcavitybeyond.api.OrganTooltip;
import net.zhaiji.chestcavitybeyond.api.function.OrganTooltipConsumer;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;

import java.util.List;
import java.util.Map;

public class OrganDynamicTooltips {
    private static final FormulaValue INNER_FURNACE_FORMULA_VALUE = new FormulaValue(
        context -> {
            double furnacePower = context.data().getCurrentValue(InitAttribute.FURNACE_POWER);
            if (context.index() == -1) {
                furnacePower += 1;
            }
            int effectLevel = Math.max(1, (int) furnacePower);
            int intervalTicks = 200 / effectLevel;
            return Component.literal(TooltipUtil.formatAttributeValue(intervalTicks / 20.0));
        },
        context -> {
            double furnacePower = context.data().getCurrentValue(InitAttribute.FURNACE_POWER);
            if (context.index() == -1) {
                furnacePower += 1;
            }
            int effectLevel = Math.max(1, (int) furnacePower);
            return Component.empty()
                .append(Component.literal("200"))
                .append(TooltipUtil.formulaOperator("÷"))
                .append(TooltipUtil.attributeName(InitAttribute.FURNACE_POWER))
                .append(Component.literal(String.valueOf(effectLevel)))
                .append(TooltipUtil.formulaOperator("÷"))
                .append(Component.literal("20"));
        }
    );

    public static final OrganTooltipConsumer INNER_FURNACE_TOOLTIP = OrganTooltip.builder()
        .dynamicActiveSkill(slotContext -> DynamicValues.split(
            Map.of(0, List.of(INNER_FURNACE_FORMULA_VALUE)),
            Map.of(1, List.of(INNER_FURNACE_FORMULA_VALUE))
        ))
        .build();
}
