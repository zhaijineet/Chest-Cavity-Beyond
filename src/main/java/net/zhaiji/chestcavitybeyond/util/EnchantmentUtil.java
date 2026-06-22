package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
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
        return maxHealth <= ChestCavityBeyondConfig.minChestOpenMaxHealth || maxHealth * (ChestCavityBeyondConfig.chestOpenBaseHealthRatio + enchantmentLevel * 0.1) >= health;
    }

    /**
     * 计算打开伤害
     */
    public static float calculateOpenDamage(Level level, ItemStack stack, float damage) {
        int enchantmentLevel = getEnchantmentLevel(level, stack, InitEnchantment.PRUDENT_SURGERY);
        return Math.max(0, damage - enchantmentLevel);
    }

    /**
     * 液压钳：消耗目标胸甲耐久，返回胸甲是否已被破坏
     *
     * @param level  世界
     * @param stack  开胸器物品栈
     * @param target 目标生物
     * @return true 如果胸甲已被破坏（耐久耗尽），false 如果胸甲仍然存在
     */
    public static boolean applyHydraulicClamp(Level level, ItemStack stack, LivingEntity target) {
        int enchantmentLevel = getEnchantmentLevel(level, stack, InitEnchantment.HYDRAULIC_CLAMP);
        if (enchantmentLevel <= 0) return false;
        int durability = enchantmentLevel * 25;
        ItemStack chestplate = target.getItemBySlot(EquipmentSlot.CHEST);
        if (chestplate.isEmpty()) return true;
        chestplate.hurtAndBreak(durability, target, EquipmentSlot.CHEST);
        return target.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
    }

    /**
     * 麻醉手术：成功开胸时对目标施加3秒缓慢效果
     * 缓慢等级等于附魔等级
     *
     * @param level  世界
     * @param stack  开胸器物品栈
     * @param target 被开胸的目标
     */
    public static void applyAnesthesiaSurgery(Level level, ItemStack stack, LivingEntity target) {
        int enchantmentLevel = getEnchantmentLevel(level, stack, InitEnchantment.ANESTHESIA_SURGERY);
        if (enchantmentLevel > 0) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, enchantmentLevel - 1));
        }
    }

    public static int getEnchantmentLevel(Level level, ItemStack stack, ResourceKey<Enchantment> resourceKey) {
        return stack.getEnchantmentLevel(level.registryAccess().holderOrThrow(resourceKey));
    }
}
