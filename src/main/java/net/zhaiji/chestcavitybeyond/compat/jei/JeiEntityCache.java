package net.zhaiji.chestcavitybeyond.compat.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 缓存用于 JEI 页面渲染的生物实体实例
 */
public class JeiEntityCache {
    private static final Map<EntityType<?>, Optional<LivingEntity>> ENTITY_CACHE = new HashMap<>();

    /**
     * 获取要渲染的实体实例，玩家直接返回本地玩家不入缓存
     */
    public static Optional<LivingEntity> getOrCreate(EntityType<?> entityType) {
        if (entityType == EntityType.PLAYER) {
            return Optional.ofNullable(Minecraft.getInstance().player);
        }
        return ENTITY_CACHE.computeIfAbsent(entityType, JeiEntityCache::createEntity);
    }

    private static Optional<LivingEntity> createEntity(EntityType<?> entityType) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return Optional.empty();
        Entity entity = entityType.create(level);
        if (entity instanceof LivingEntity livingEntity) {
            return Optional.of(livingEntity);
        }
        return Optional.empty();
    }

    /**
     * 清理缓存的实体实例，在客户端世界卸载时调用，避免引用已卸载世界的幽灵实体
     */
    public static void clear() {
        ENTITY_CACHE.clear();
    }
}
