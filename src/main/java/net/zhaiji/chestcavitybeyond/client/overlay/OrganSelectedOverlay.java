package net.zhaiji.chestcavitybeyond.client.overlay;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.client.screen.OrganSkillScreen;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;

public class OrganSelectedOverlay {
    public static final ResourceLocation ORGAN_SELECTED = ChestCavityBeyond.of("organ_selected");

    /**
     * 渲染选择器官
     */
    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.hideGui) return;
        Player player = minecraft.player;
        ItemStack organ = ChestCavityUtil.getData(player).getOrgans().get(OrganSkillScreen.selectedSlot);
        if (!ChestCavityUtil.getOrganCap(organ).hasSkill()) return;
        boolean isRightHand = player.getMainArm().getOpposite() == HumanoidArm.RIGHT;
        int x = guiGraphics.guiWidth() / 2 + (isRightHand ? -120 : 98);
        int y = guiGraphics.guiHeight() - 22;
        guiGraphics.blit(ChestCavityBeyond.of("textures/gui/organ_selected.png"), x, y, 0, 0, 22, 22, 22, 22);
        guiGraphics.renderItem(organ, x + 3, y + 3);
    }
}
