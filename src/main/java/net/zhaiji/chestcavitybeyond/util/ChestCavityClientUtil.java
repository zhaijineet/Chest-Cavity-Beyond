package net.zhaiji.chestcavitybeyond.util;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ChestCavityClientUtil {
    /**
     * 为器官添加类似create的tooltips
     */
    public static void addOrganTooltips(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        Player player = Minecraft.getInstance().player;
        Multimap<Holder<Attribute>, AttributeModifier> attributeModifiers = ChestCavityUtil.getAttributeModifiers(
                new ChestCavitySlotContext(ChestCavityUtil.getData(player), player, ChestCavityUtil.getSlotId(0), 0, stack)
        );
        if (attributeModifiers != null) return;
        if (isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
            // TODO
        } else {
            // TODO
        }
    }

    /**
     * 检测按键是否按下
     * <p>
     * 不走Minecraft默认的按键检测
     * </P>
     *
     * @param key GLFW
     * @return 是否按下
     */
    public static boolean isKeyDown(int key) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key);
    }
}
