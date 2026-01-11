package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.register.InitEnchantment;

/**
 * 附魔工具类
 *
 * <p>
 * 实在是没招了，我看不懂mojang的数据驱动，希望有好心人写个PR
 * </p>
 */
public class EnchantmentUtil {
    /**
     * 计算打开距离
     */
    public static double calculateOpenDistance(Level level, ItemStack stack, double distance) {
        int enchantmentLevel = getEnchantmentLevel(level, stack, InitEnchantment.TELEOPERATION);
        return distance + enchantmentLevel;
    }

    /**
     * 是否可以打开胸腔
     */
    public static boolean canOpenChestCavity(Level level, ItemStack stack, float maxHealth, float health) {
        int enchantmentLevel = getEnchantmentLevel(level, stack, InitEnchantment.ADVANCED_SURGERY);
        // TODO 最小可开胸的最大生命值应该加入配置
        return maxHealth <= 15 || maxHealth * (0.2 + enchantmentLevel * 0.1) >= health;
    }

    /**
     * 计算打开伤害
     */
    public static float calculateOpenDamage(Level level, ItemStack stack, float damage) {
        int enchantmentLevel = getEnchantmentLevel(level, stack, InitEnchantment.PRUDENT_SURGERY);
        return damage - enchantmentLevel;
    }

    public static int getEnchantmentLevel(Level level, ItemStack stack, ResourceKey<Enchantment> resourceKey) {
        return stack.getEnchantmentLevel(level.registryAccess().holderOrThrow(resourceKey));
    }
}
