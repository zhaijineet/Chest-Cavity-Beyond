package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.zhaiji.chestcavitybeyond.client.util.MixinClientUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private PlayerTabOverlay tabList;

    @Inject(
        method = "renderTabList",
        at = @At("HEAD"),
        cancellable = true
    )
    private void chestcavitybeyond$getPlayerInfos(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!MixinClientUtil.isFoolOrHalloween()) return;
        if (!this.minecraft.options.keyPlayerList.isDown()) {
            this.tabList.setVisible(false);
        } else {
            Scoreboard scoreboard = this.minecraft.level.getScoreboard();
            Objective objective = scoreboard.getDisplayObjective(DisplaySlot.LIST);
            this.tabList.setVisible(true);
            this.tabList.render(guiGraphics, guiGraphics.guiWidth(), scoreboard, objective);
            ci.cancel();
        }
    }
}
