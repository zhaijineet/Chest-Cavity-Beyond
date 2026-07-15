package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.zhaiji.chestcavitybeyond.client.easter.EasterEggManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    @Inject(
        method = "getPlayerInfos",
        at = @At("RETURN"),
        cancellable = true
    )
    private void chestCavityBeyond$getPlayerInfos(CallbackInfoReturnable<List<PlayerInfo>> cir) {
        if (!EasterEggManager.isFoolOrHalloween()) return;
        List<PlayerInfo> original = cir.getReturnValue();
        List<PlayerInfo> modified = new ArrayList<>(original);
        modified.add(EasterEggManager.getOrCreateHerobrineInfo());
        cir.setReturnValue(modified);
    }
}
