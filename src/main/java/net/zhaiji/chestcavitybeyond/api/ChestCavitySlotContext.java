package net.zhaiji.chestcavitybeyond.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import org.jetbrains.annotations.Nullable;

/**
 * 胸腔槽位上下文
 *
 * @param data   胸腔数据 (可能为null)
 * @param entity 胸腔主人 (可能为null)
 * @param id     槽位id
 * @param index  位置索引
 * @param stack  对应物品
 */
public record ChestCavitySlotContext(@Nullable ChestCavityData data, @Nullable LivingEntity entity, ResourceLocation id,
                                     int index, ItemStack stack) {
}
