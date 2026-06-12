package net.zhaiji.chestcavitybeyond.compat.jei;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;

import java.util.List;

/**
 * JEI 显示数据类，表示一个胸腔类型的完整信息。
 * <p>
 * 包含胸腔类型的注册名、器官布局、是否需要呼吸/健康、以及附加的实体列表。
 * </p>
 */
public class ChestCavityTypeDisplay {
    private final ResourceLocation typeId;
    private final ChestCavityType type;
    private final List<EntityType<?>> entities;

    public ChestCavityTypeDisplay(ResourceLocation typeId, ChestCavityType type, List<EntityType<?>> entities) {
        this.typeId = typeId;
        this.type = type;
        this.entities = entities;
    }

    /**
     * 获取胸腔类型的翻译键
     *
     * @param typeId 胸腔类型注册名
     * @return 翻译键
     */
    public static String getTranslationKey(ResourceLocation typeId) {
        return "chest_cavity_type." + typeId.getNamespace() + "." + typeId.getPath();
    }

    /**
     * 获取胸腔类型的翻译组件
     *
     * @param typeId 胸腔类型注册名
     * @return 国际化的胸腔类型名称
     */
    public static Component getTranslatedName(ResourceLocation typeId) {
        return Component.translatable(getTranslationKey(typeId));
    }

    public ResourceLocation getTypeId() {
        return typeId;
    }

    public ChestCavityType getType() {
        return type;
    }

    public List<EntityType<?>> getEntities() {
        return entities;
    }

    public NonNullList<Item> getOrgans() {
        return type.getOrgans();
    }

    public boolean getNeedBreath() {
        return type.getNeedBreath();
    }

    public boolean getNeedHealth() {
        return type.getNeedHealth();
    }

    public int getSlots() {
        return type.getSize().getSlots();
    }
}
