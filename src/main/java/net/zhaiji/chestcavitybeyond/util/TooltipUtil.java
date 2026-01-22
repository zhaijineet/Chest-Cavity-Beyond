package net.zhaiji.chestcavitybeyond.util;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;

import java.util.ArrayList;
import java.util.List;

public class TooltipUtil {
    /**
     * 为器官属性工具提示
     */
    public static void addOrganAttributeTooltip(ChestCavityData data, ItemStack stack, TooltipsKeyContext keyContext, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers = ChestCavityUtil.getAttributeModifiers(
                new ChestCavitySlotContext(data, data.getOwner(), ChestCavityUtil.getSlotId(0), 0, stack)
        );
        if (attributeModifiers == null || attributeModifiers.isEmpty()) return;
        List<Component> tooltips = new ArrayList<>();
        attributeModifiers.forEach((attribute, modifier) -> {
            double value = modifier.amount();
            if (modifier.operation() != AttributeModifier.Operation.ADD_VALUE) {
                value *= 100;
            }
            if (value == (int) value) {
                int i = (int) value;
                tooltips.add(
                        Component.translatable(
                                "organ." + ChestCavityBeyond.MOD_ID + ".attribute.tooltips_" + modifier.operation().ordinal(),
                                i > 0 ? "+" + i : i,
                                Component.translatable(attribute.value().getDescriptionId())
                        )
                );
            } else {
                tooltips.add(
                        Component.translatable(
                                "organ." + ChestCavityBeyond.MOD_ID + ".attribute.tooltips_" + modifier.operation().ordinal(),
                                value > 0 ? "+" + value : value,
                                Component.translatable(attribute.value().getDescriptionId())
                        )
                );
            }
        });
        simpleTooltipAdd(tooltipComponents, tooltips);
    }

    /**
     * 添加简单技能描述工具提示
     */
    public static void simpleSkillTooltip(ChestCavityData data, ItemStack stack, TooltipsKeyContext keyContext, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        List<Component> skillTooltips = new ArrayList<>();
        skillTooltips.add(Component.empty());
        skillTooltips.add(Component.translatable("organ." + ChestCavityBeyond.MOD_ID + "." + stack.getItem().getDescriptionId() + ".skill"));
        simpleTooltipAdd(tooltipComponents, skillTooltips);
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
}
