package net.zhaiji.chestcavitybeyond.manager;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.register.InitItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChestCavityManager {
    public static final List<ChestCavityType> CHEST_CAVITY_TYPES = new ArrayList<>();

    public static final Map<EntityType<?>, ChestCavityType> ENTITY_CHEST_CAVITY_TYPE_MAP = new HashMap<>();

    public static final ChestCavityType HUMAN = register("human")
            .setOrgan(4, InitItem.HEART);

    // 为实体类型添加胸腔类型
    static {
        registerEntity(EntityType.PLAYER, HUMAN);
    }

    /**
     * 获取实体的胸腔类型
     * @param entity 实体
     * @return 胸腔类型
     */
    public static ChestCavityType getType(LivingEntity entity) {
        return ENTITY_CHEST_CAVITY_TYPE_MAP.get(entity.getType());
    }

    /**
     * 为实体类型注册胸腔类型
     * @param entityType 实体类型
     * @param chestCavityType 胸腔类型
     */
    public static void registerEntity(EntityType<? extends LivingEntity> entityType, ChestCavityType chestCavityType) {
        ENTITY_CHEST_CAVITY_TYPE_MAP.put(entityType, chestCavityType.builder(entityType));
    }

    /**
     * 注册胸腔类型
     * @param name 类型名称
     * @return 胸腔类型
     */
    public static ChestCavityType register(String name) {
        ChestCavityType chestCavityType = new ChestCavityType(name);
        CHEST_CAVITY_TYPES.add(chestCavityType);
        return chestCavityType;
    }
}
