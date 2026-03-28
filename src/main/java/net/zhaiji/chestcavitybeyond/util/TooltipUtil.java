package net.zhaiji.chestcavitybeyond.util;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.api.function.OrganTooltipConsumer;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;

import java.util.ArrayList;
import java.util.List;

public class TooltipUtil {
    /**
     * 默认的器官属性工具提示回调
     * 可以被替换以全局改变所有器官的属性 tooltip 显示方式
     */
    public static OrganTooltipConsumer DEFAULT_ATTRIBUTE_TOOLTIP = TooltipUtil::addOrganAttributeTooltip;

    /**
     * 为器官属性工具提示
     */
    public static void addOrganAttributeTooltip(
        ChestCavityData data,
        int index,
        ItemStack stack,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers = ChestCavityUtil.getAttributeModifiers(
            new ChestCavitySlotContext(data, data.getOwner(), ChestCavityUtil.getSlotId(index), index, stack)
        );
        if (attributeModifiers == null || attributeModifiers.isEmpty()) return;
        List<Component> tooltips = new ArrayList<>();
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
                tooltips.add(
                    Component.translatable(
                        "organ." + ChestCavityBeyond.MOD_ID + ".attribute.tooltips_" + modifier.operation().ordinal(),
                        string,
                        Component.translatable(attribute.value().getDescriptionId())
                    )
                );
            } else {
                String string = value > 0 ? "+" + value : String.valueOf(value);
                if (modifier.operation() == AttributeModifier.Operation.ADD_VALUE && attribute.value() instanceof PercentageAttribute) {
                    string += "%";
                }
                tooltips.add(
                    Component.translatable(
                        "organ." + ChestCavityBeyond.MOD_ID + ".attribute.tooltips_" + modifier.operation().ordinal(),
                        string,
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
    public static void simpleSkillTooltip(
        ChestCavityData data,
        int index,
        ItemStack stack,
        TooltipsKeyContext keyContext,
        Item.TooltipContext context,
        List<Component> tooltipComponents,
        TooltipFlag tooltipFlag
    ) {
        List<Component> skillTooltips = new ArrayList<>();
        skillTooltips.add(Component.empty());
        skillTooltips.add(Component.translatable("organ." + ChestCavityBeyond.MOD_ID + "." + BuiltInRegistries.ITEM.getKey(stack.getItem())
            .getPath() + ".skill"));
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
