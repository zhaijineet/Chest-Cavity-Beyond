package net.zhaiji.chestcavitybeyond.compat.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.zhaiji.chestcavitybeyond.client.easter.EasterEggManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 缓存用于 JEI 页面渲染和预览数据构建的生物实体实例
 */
public class JeiEntityCache {
    private static final Map<EntityType<?>, Optional<LivingEntity>> ENTITY_CACHE = new HashMap<>();

    /**
     * 获取要渲染的实体实例，PLAYER 使用 RemotePlayer 避免影响真实玩家状态
     */
    public static Optional<LivingEntity> getOrCreate(EntityType<?> entityType) {
        if (entityType == EntityType.PLAYER && EasterEggManager.shouldUseHerobrine()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level == null) return Optional.empty();
            return Optional.of(EasterEggManager.getOrCreateHerobrinePlayer(minecraft.level));
        }
        return ENTITY_CACHE.computeIfAbsent(entityType, JeiEntityCache::createEntity);
    }

    private static Optional<LivingEntity> createEntity(EntityType<?> entityType) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) return Optional.empty();

        if (entityType == EntityType.PLAYER) {
            if (minecraft.player == null) return Optional.empty();
            return Optional.of(new RemotePlayer(level, minecraft.player.getGameProfile()));
        }

        Entity entity = entityType.create(level);
        if (entity instanceof LivingEntity livingEntity) {
            return Optional.of(livingEntity);
        }
        return Optional.empty();
    }

    /**
     * JEI runtime 不可用时清理实体缓存
     */
    public static void clear() {
        ENTITY_CACHE.clear();
    }
}
