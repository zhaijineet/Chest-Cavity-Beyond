package net.zhaiji.chestcavitybeyond.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.PlayerSkin;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.client.util.MixinClientUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    @Unique
    private static final UUID HEROBRINE_UUID = UUID.fromString("f84c6a9d-33d4-4b53-95f5-4b74c1f9e83d");

    @Unique
    private static final String HEROBRINE_NAME = "Herobrine";

    @Unique
    private static PlayerInfo herobrineInfo;

    @Inject(
        method = "getPlayerInfos",
        at = @At("RETURN"),
        cancellable = true
    )
    private void chestcavitybeyond$getPlayerInfos(CallbackInfoReturnable<List<PlayerInfo>> cir) {
        if (!MixinClientUtil.isFoolOrHalloween()) return;
        List<PlayerInfo> original = cir.getReturnValue();
        if (herobrineInfo == null) {
            GameProfile profile = new GameProfile(HEROBRINE_UUID, HEROBRINE_NAME);
            herobrineInfo = new PlayerInfo(profile, false) {
                @Override
                public PlayerSkin getSkin() {
                    return new PlayerSkin(
                        ChestCavityBeyond.of("textures/entity/herobrine.png"),
                        null,
                        null,
                        null,
                        PlayerSkin.Model.WIDE,
                        true
                    );
                }
            };
        }
        List<PlayerInfo> modified = new ArrayList<>(original);
        modified.add(herobrineInfo);
        cir.setReturnValue(modified);
    }
}
