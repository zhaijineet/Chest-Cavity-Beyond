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
            .setFirstRow(0, InitItem.MUSCLE.get())
            .setFirstRow(1, InitItem.RIB.get())
            .setFirstRow(2, InitItem.APPENDIX.get())
            .setFirstRow(3, InitItem.LUNG.get())
            .setFirstRow(4, InitItem.HEART.get())
            .setFirstRow(5, InitItem.LUNG.get())
            .setFirstRow(7, InitItem.RIB.get())
            .setFirstRow(8, InitItem.MUSCLE.get())

            .setSecondRow(0, InitItem.MUSCLE.get())
            .setSecondRow(1, InitItem.RIB.get())
            .setSecondRow(2, InitItem.SPLEEN.get())
            .setSecondRow(3, InitItem.KIDNEY.get())
            .setSecondRow(4, InitItem.SPINE.get())
            .setSecondRow(5, InitItem.KIDNEY.get())
            .setSecondRow(6, InitItem.LIVER.get())
            .setSecondRow(7, InitItem.RIB.get())
            .setSecondRow(8, InitItem.MUSCLE.get())

            .setThirdRow(0, InitItem.MUSCLE.get())
            .setThirdRow(1, InitItem.MUSCLE.get())
            .setThirdRow(2, InitItem.INTESTINE.get())
            .setThirdRow(3, InitItem.INTESTINE.get())
            .setThirdRow(4, InitItem.STOMACH.get())
            .setThirdRow(5, InitItem.INTESTINE.get())
            .setThirdRow(6, InitItem.INTESTINE.get())
            .setThirdRow(7, InitItem.MUSCLE.get())
            .setThirdRow(8, InitItem.MUSCLE.get());

    public static final ChestCavityType ANIMAL = register("animal")
            .setFirstRow(0, InitItem.ANIMAL_MUSCLE.get())
            .setFirstRow(1, InitItem.ANIMAL_RIB.get())
            .setFirstRow(2, InitItem.ANIMAL_APPENDIX.get())
            .setFirstRow(3, InitItem.ANIMAL_LUNG.get())
            .setFirstRow(4, InitItem.ANIMAL_HEART.get())
            .setFirstRow(5, InitItem.ANIMAL_LUNG.get())
            .setFirstRow(7, InitItem.ANIMAL_RIB.get())
            .setFirstRow(8, InitItem.ANIMAL_MUSCLE.get())

            .setSecondRow(0, InitItem.ANIMAL_MUSCLE.get())
            .setSecondRow(1, InitItem.ANIMAL_RIB.get())
            .setSecondRow(2, InitItem.ANIMAL_SPLEEN.get())
            .setSecondRow(3, InitItem.ANIMAL_KIDNEY.get())
            .setSecondRow(4, InitItem.ANIMAL_SPINE.get())
            .setSecondRow(5, InitItem.ANIMAL_KIDNEY.get())
            .setSecondRow(6, InitItem.ANIMAL_LIVER.get())
            .setSecondRow(7, InitItem.ANIMAL_RIB.get())
            .setSecondRow(8, InitItem.ANIMAL_MUSCLE.get())

            .setThirdRow(0, InitItem.ANIMAL_MUSCLE.get())
            .setThirdRow(1, InitItem.ANIMAL_MUSCLE.get())
            .setThirdRow(2, InitItem.ANIMAL_INTESTINE.get())
            .setThirdRow(3, InitItem.ANIMAL_INTESTINE.get())
            .setThirdRow(4, InitItem.ANIMAL_STOMACH.get())
            .setThirdRow(5, InitItem.ANIMAL_INTESTINE.get())
            .setThirdRow(6, InitItem.ANIMAL_INTESTINE.get())
            .setThirdRow(7, InitItem.ANIMAL_MUSCLE.get())
            .setThirdRow(8, InitItem.ANIMAL_MUSCLE.get());

    public static final ChestCavityType SMALL_ANIMAL = register("small_animal")
            .setFirstRow(0, InitItem.SMALL_ANIMAL_MUSCLE.get())
            .setFirstRow(1, InitItem.SMALL_ANIMAL_RIB.get())
            .setFirstRow(2, InitItem.SMALL_ANIMAL_APPENDIX.get())
            .setFirstRow(3, InitItem.SMALL_ANIMAL_LUNG.get())
            .setFirstRow(4, InitItem.SMALL_ANIMAL_HEART.get())
            .setFirstRow(5, InitItem.SMALL_ANIMAL_LUNG.get())
            .setFirstRow(7, InitItem.SMALL_ANIMAL_RIB.get())
            .setFirstRow(8, InitItem.SMALL_ANIMAL_MUSCLE.get())

            .setSecondRow(0, InitItem.SMALL_ANIMAL_MUSCLE.get())
            .setSecondRow(1, InitItem.SMALL_ANIMAL_RIB.get())
            .setSecondRow(2, InitItem.SMALL_ANIMAL_SPLEEN.get())
            .setSecondRow(3, InitItem.SMALL_ANIMAL_KIDNEY.get())
            .setSecondRow(4, InitItem.SMALL_ANIMAL_SPINE.get())
            .setSecondRow(5, InitItem.SMALL_ANIMAL_KIDNEY.get())
            .setSecondRow(6, InitItem.SMALL_ANIMAL_LIVER.get())
            .setSecondRow(7, InitItem.SMALL_ANIMAL_RIB.get())
            .setSecondRow(8, InitItem.SMALL_ANIMAL_MUSCLE.get())

            .setThirdRow(0, InitItem.SMALL_ANIMAL_MUSCLE.get())
            .setThirdRow(1, InitItem.SMALL_ANIMAL_MUSCLE.get())
            .setThirdRow(2, InitItem.SMALL_ANIMAL_INTESTINE.get())
            .setThirdRow(3, InitItem.SMALL_ANIMAL_INTESTINE.get())
            .setThirdRow(4, InitItem.SMALL_ANIMAL_STOMACH.get())
            .setThirdRow(5, InitItem.SMALL_ANIMAL_INTESTINE.get())
            .setThirdRow(6, InitItem.SMALL_ANIMAL_INTESTINE.get())
            .setThirdRow(7, InitItem.SMALL_ANIMAL_MUSCLE.get())
            .setThirdRow(8, InitItem.SMALL_ANIMAL_MUSCLE.get());

    /**
     * 获取实体的胸腔类型
     *
     * @param entity 实体
     * @return 胸腔类型
     */
    public static ChestCavityType getType(LivingEntity entity) {
        ChestCavityType type = ENTITY_CHEST_CAVITY_TYPE_MAP.get(entity.getType());
        if (type == null) {
            EntityType<?> entityType = entity.getType();
            // 找不到实体类型所属的胸腔类型，就注册一套新的人类器官给它
            registerEntity(entityType, HUMAN);
            return HUMAN;
        }
        return type;
    }

    /**
     * 为实体类型注册胸腔类型
     *
     * @param entityType      实体类型
     * @param chestCavityType 胸腔类型
     */
    public static void registerEntity(EntityType<?> entityType, ChestCavityType chestCavityType) {
        ENTITY_CHEST_CAVITY_TYPE_MAP.computeIfAbsent(entityType, chestCavityType::builder);
    }

    /**
     * 注册胸腔类型
     *
     * @param name 类型名称
     * @return 胸腔类型
     */
    public static ChestCavityType register(String name) {
        ChestCavityType chestCavityType = new ChestCavityType(name);
        CHEST_CAVITY_TYPES.add(chestCavityType);
        return chestCavityType;
    }
}
