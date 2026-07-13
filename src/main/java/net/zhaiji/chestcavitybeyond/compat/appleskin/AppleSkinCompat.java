package net.zhaiji.chestcavitybeyond.compat.appleskin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.MathUtil;
import net.zhaiji.chestcavitybeyond.util.MixinUtil;
import squeek.appleskin.api.event.FoodValuesEvent;

/**
 * 让 AppleSkin 显示的食物与回血估算使用本模组实际生效值。
 */
public class AppleSkinCompat {
    public static void init(IEventBus modBus, IEventBus gameBus) {
        modBusListener(modBus);
        gameBusListener(gameBus);
    }

    public static void modBusListener(IEventBus modBus) {
    }

    public static void gameBusListener(IEventBus gameBus) {
        gameBus.addListener(AppleSkinCompat::handlerFoodValuesEvent);
    }

    /**
     * 替换 AppleSkin 食物显示值为实际消化后的值。
     */
    public static void handlerFoodValuesEvent(FoodValuesEvent event) {
        Player player = event.player;
        ItemStack itemStack = event.itemStack;
        ChestCavityData chestCavityData = ChestCavityUtil.getData(player);
        event.modifiedFoodProperties = MixinUtil.calculateActualFood(
            event.defaultFoodProperties, itemStack, chestCavityData
        );
    }

    /**
     * 估算吃下食物后按本模组新陈代谢和耐力实际可恢复的血量。
     */
    public static float estimateHealthIncrement(Player player, FoodProperties foodProperties) {
        if (!player.isHurt()) return 0;

        ChestCavityData chestCavityData = ChestCavityUtil.getData(player);
        FoodData foodData = player.getFoodData();
        Level level = player.getCommandSenderWorld();
        float missingHealth = player.getMaxHealth() - player.getHealth();

        int foodLevel = Math.min(foodData.getFoodLevel() + foodProperties.nutrition(), 20);
        float healthIncrement = 0;

        if (foodLevel >= 18.0F && level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)) {
            float saturationLevel = Math.min(
                foodData.getSaturationLevel() + foodProperties.saturation(), (float) foodLevel
            );
            float exhaustionLevel = foodData.getExhaustionLevel();
            healthIncrement = simulateRegeneration(foodLevel, saturationLevel, exhaustionLevel, chestCavityData, missingHealth);
        }

        for (FoodProperties.PossibleEffect possibleEffect : foodProperties.effects()) {
            MobEffectInstance effectInstance = possibleEffect.effect();
            if (effectInstance.is(MobEffects.REGENERATION)) {
                int amplifier = effectInstance.getAmplifier();
                int duration = effectInstance.getDuration();
                healthIncrement += (float) Math.floor((double) duration / Math.max(50 >> amplifier, 1));
                break;
            }
        }
        return healthIncrement;
    }

    /**
     * 模拟自然回血直到饥饿值低于自然回血阈值。
     */
    private static float simulateRegeneration(
        int foodLevel,
        float saturationLevel,
        float exhaustionLevel,
        ChestCavityData chestCavityData,
        float missingHealth
    ) {
        if (!Float.isFinite(exhaustionLevel) || !Float.isFinite(saturationLevel)) return 0;

        double enduranceScale = MathUtil.getSquareRootInverseScale(chestCavityData.getDifferenceValue(InitAttribute.ENDURANCE));

        float health = 0;
        float maximumExhaustion = 4.0F;
        float regenerationExhaustionIncrement = 6.0F;

        while (foodLevel >= 18 && health < missingHealth) {
            while (exhaustionLevel > maximumExhaustion) {
                exhaustionLevel -= maximumExhaustion;
                if (saturationLevel > 0) {
                    saturationLevel = Math.max(saturationLevel - 1, 0);
                } else {
                    foodLevel -= 1;
                }
                if (foodLevel < 18) break;
            }
            if (foodLevel < 18) break;

            if (foodLevel >= 20 && Float.compare(saturationLevel, Float.MIN_NORMAL) > 0) {
                float limitedSaturation = Math.min(saturationLevel, regenerationExhaustionIncrement);
                double affordableScale = MixinUtil.getAffordableMetabolismRegenerationScale(
                    foodLevel, saturationLevel, exhaustionLevel, limitedSaturation, chestCavityData, 10
                );
                if (affordableScale <= 0) break;

                float exhaustionIncrement = limitedSaturation * (float) (affordableScale * enduranceScale);
                float exhaustionUntilAboveMaximum = Math.nextUp(maximumExhaustion) - exhaustionLevel;
                int regenerationCount = Math.max(1, (int) Math.ceil(exhaustionUntilAboveMaximum / exhaustionIncrement));
                health += (limitedSaturation / regenerationExhaustionIncrement) * (float) affordableScale * regenerationCount;
                exhaustionLevel += exhaustionIncrement * regenerationCount;
            } else {
                double affordableScale = MixinUtil.getAffordableMetabolismRegenerationScale(
                    foodLevel, saturationLevel, exhaustionLevel, regenerationExhaustionIncrement, chestCavityData, 80
                );
                if (affordableScale <= 0) break;

                health += (float) affordableScale;
                exhaustionLevel += regenerationExhaustionIncrement * (float) (affordableScale * enduranceScale);
            }
        }
        return health;
    }
}
