package net.zhaiji.chestcavitybeyond.compat.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存按胸腔类型构建的独立 JEI 预览数据
 */
public class JeiChestCavityPreviewCache {
    private static final Map<ChestCavityType, ChestCavityData> PREVIEW_DATA = new HashMap<>();

    public static ChestCavityData get(ChestCavityTypeDisplay display) {
        return PREVIEW_DATA.computeIfAbsent(display.type(), type -> create(display));
    }

    private static ChestCavityData create(ChestCavityTypeDisplay display) {
        return ChestCavityData.createPreview(createOwner(display), display.type());
    }

    private static LivingEntity createOwner(ChestCavityTypeDisplay display) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        for (EntityType<?> entityType : display.entities()) {
            if (entityType == EntityType.PLAYER) return createRemotePlayer(minecraft, level);
            Entity entity = entityType.create(level);
            if (entity instanceof LivingEntity livingEntity) return livingEntity;
        }
        return createRemotePlayer(minecraft, level);
    }

    private static RemotePlayer createRemotePlayer(Minecraft minecraft, ClientLevel level) {
        return new RemotePlayer(level, minecraft.player.getGameProfile());
    }

    public static void clear() {
        PREVIEW_DATA.clear();
    }
}
