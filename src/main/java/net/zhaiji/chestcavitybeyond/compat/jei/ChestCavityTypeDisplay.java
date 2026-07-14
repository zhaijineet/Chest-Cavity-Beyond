package net.zhaiji.chestcavitybeyond.compat.jei;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;

import java.util.List;

/**
 * JEI 显示数据类，表示一个胸腔类型的完整信息
 * <p>
 * 包含胸腔类型的注册名、器官布局、是否需要呼吸/健康、以及附加的实体列表
 * </p>
 */
public record ChestCavityTypeDisplay(ResourceLocation typeId, ChestCavityType type, List<EntityType<?>> entities) {
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
