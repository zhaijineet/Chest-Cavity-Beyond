package net.zhaiji.chestcavitybeyond.api.goal;

import net.minecraft.world.item.ItemStack;

/**
 * 技能缓存条目：槽位索引 + 物品栈 + 技能元数据
 */
public record SkillCacheEntry(int slot, ItemStack stack, GoalSkillMetadata metadata) {
}
