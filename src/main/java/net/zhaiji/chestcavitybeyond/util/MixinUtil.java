package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.living.LivingDrownEvent;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.mixinapi.IFoodData;
import net.zhaiji.chestcavitybeyond.mixinapi.IMobEffectInstance;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;

import java.util.List;

/**
 * Mixin 业务逻辑工具类
 * <p>
 * 从各 Mixin 中提取的业务逻辑，便于测试和维护
 * </p>
 */
public class MixinUtil {
    /**
     * 计算食物消化后的饥饿值和饱食度并添加
     *
     * @param foodData       食物数据（通过 {@link IFoodData} 访问内部字段）
     * @param foodProperties 食物属性
     */
    public static void eat(FoodData foodData, FoodProperties foodProperties) {
        IFoodData foodDataAccessor = (IFoodData) foodData;
        ChestCavityData chestCavityData = foodDataAccessor.getChestCavityData();
        ItemStack food = foodDataAccessor.getFood();

        FoodProperties actualFoodProperties = calculateActualFood(foodProperties, food, chestCavityData);
        foodData.add(actualFoodProperties.nutrition(), actualFoodProperties.saturation());
        foodDataAccessor.setFood(ItemStack.EMPTY);
    }

    /**
     * 根据胸腔属性计算食物实际恢复的饥饿值和饱和度。
     */
    public static FoodProperties calculateActualFood(
        FoodProperties originalFoodProperties, ItemStack foodStack, ChestCavityData chestCavityData
    ) {
        int foodLevel = originalFoodProperties.nutrition();
        float saturation = originalFoodProperties.saturation();
        float additionalSaturation = 0;
        boolean isPoisoning = foodStack.is(Tags.Items.FOODS_FOOD_POISONING);
        boolean isMeat = foodStack.is(ItemTags.MEAT);

        if (chestCavityData.getCurrentValue(InitAttribute.DIGESTION) <= 0
            && chestCavityData.getCurrentValue(InitAttribute.CARNIVOROUS_DIGESTION) <= 0
            && chestCavityData.getCurrentValue(InitAttribute.HERBIVOROUS_DIGESTION) <= 0
            && (!isPoisoning || chestCavityData.getCurrentValue(InitAttribute.SCAVENGER_DIGESTION) <= 0)
        ) {
            foodLevel = 0;
        }
        if (chestCavityData.getCurrentValue(InitAttribute.NUTRITION) <= 0
            && chestCavityData.getCurrentValue(InitAttribute.CARNIVOROUS_NUTRITION) <= 0
            && chestCavityData.getCurrentValue(InitAttribute.HERBIVOROUS_NUTRITION) <= 0
            && (!isPoisoning || chestCavityData.getCurrentValue(InitAttribute.SCAVENGER_NUTRITION) <= 0)
        ) {
            saturation = 0;
        }

        double digestion = chestCavityData.getDifferenceValue(InitAttribute.DIGESTION);
        double carnivorousDigestion = chestCavityData.getDifferenceValue(InitAttribute.CARNIVOROUS_DIGESTION);
        double herbivorousDigestion = chestCavityData.getDifferenceValue(InitAttribute.HERBIVOROUS_DIGESTION);
        double scavengerDigestion = chestCavityData.getDifferenceValue(InitAttribute.SCAVENGER_DIGESTION);

        double nutrition = chestCavityData.getDifferenceValue(InitAttribute.NUTRITION);
        double carnivorousNutrition = chestCavityData.getDifferenceValue(InitAttribute.CARNIVOROUS_NUTRITION);
        double herbivorousNutrition = chestCavityData.getDifferenceValue(InitAttribute.HERBIVOROUS_NUTRITION);
        double scavengerNutrition = chestCavityData.getDifferenceValue(InitAttribute.SCAVENGER_NUTRITION);

        double digestionDifference = isMeat
                                      ? digestion + carnivorousDigestion
                                      : digestion + herbivorousDigestion;
        double nutritionDifference = isMeat
                                      ? nutrition + carnivorousNutrition
                                      : nutrition + herbivorousNutrition;

        if (isPoisoning) {
            digestionDifference = digestionDifference + scavengerDigestion;
            nutritionDifference = nutritionDifference + scavengerNutrition;
            // 有毒食物额外加饱和
            for (FoodProperties.PossibleEffect possibleEffect : originalFoodProperties.effects()) {
                if (((IMobEffectInstance) possibleEffect.effect()).isHarmful()) {
                    additionalSaturation += (float) (possibleEffect.probability() * scavengerNutrition);
                }
            }
        }
        foodLevel = (int) (foodLevel * MathUtil.getDirectScale(digestionDifference));
        // 正数时，应该小心增加，负数时，应该大胆减少
        nutritionDifference = nutritionDifference > 0 ? nutritionDifference / 4 : nutritionDifference;
        saturation = (float) (saturation * MathUtil.getDirectScale(nutritionDifference) + additionalSaturation);

        foodLevel = Math.max(0, foodLevel);
        saturation = Math.max(0, saturation);

        // FoodProperties 是 record，直接构造，避免 Builder.saturationByModifier 转换 saturation
        return new FoodProperties(
            foodLevel,
            saturation,
            originalFoodProperties.canAlwaysEat(),
            originalFoodProperties.eatSeconds(),
            originalFoodProperties.usingConvertsTo(),
            originalFoodProperties.effects()
        );
    }

    /**
     * 处理光合作用逻辑
     * <p>
     * 通过 {@link IFoodData} 接口访问 FoodData 内部字段和缓存
     * </p>
     *
     * @param foodData 食物数据
     * @param player   玩家
     */
    public static void tickPhotosynthesis(FoodData foodData, Player player) {
        IFoodData iFoodData = (IFoodData) foodData;
        ChestCavityData data = iFoodData.getChestCavityData();
        int foodLevel = foodData.getFoodLevel();

        double photosynthesis = data.getCurrentValue(InitAttribute.PHOTOSYNTHESIS);
        Level level = player.level();
        // 白天且能看见天空时
        if (photosynthesis > 0 && level.isDay() && level.canSeeSky(player.blockPosition())) {
            double photosynthesisTimer = iFoodData.getPhotosynthesisTimer();
            photosynthesisTimer += photosynthesis;
            if (photosynthesisTimer >= 800) {
                photosynthesisTimer = 0;
                if (foodLevel < 20) {
                    foodData.add(1, 0);
                } else {
                    foodData.add(0, 1);
                }
            }
            iFoodData.setPhotosynthesisTimer(photosynthesisTimer);
        }
    }

    /**
     * 获取新陈代谢倍率。
     */
    public static double getMetabolismScale(ChestCavityData data) {
        if (data == null) return 1;
        return MathUtil.getDirectScale(data.getDifferenceValue(InitAttribute.METABOLISM));
    }

    /**
     * 根据新陈代谢倍率修改原版自然回血/饥饿伤害阈值。
     */
    public static int modifyMetabolismThreshold(int original, ChestCavityData data) {
        double scale = getMetabolismScale(data);
        return Math.max(1, (int) Math.ceil(original / scale));
    }

    /**
     * 获取阈值降低至1tick后，超出的新陈代谢倍率。
     */
    public static double getMetabolismOverflowScale(ChestCavityData data, double originalThreshold) {
        double scale = getMetabolismScale(data);
        return scale > originalThreshold ? scale / originalThreshold : 1;
    }

    /**
     * 将阈值降低至1tick后超出的新陈代谢倍率应用到单次效果量。
     */
    public static float modifyMetabolismOverflowAmount(float amount, ChestCavityData data, double originalThreshold) {
        return (float) (amount * getMetabolismOverflowScale(data, originalThreshold));
    }

    /**
     * 获取自然回血在当前饥饿/饱和/疲劳队列下可安全支付的新陈代谢溢出倍率。
     */
    public static double getAffordableMetabolismRegenerationScale(
        int foodLevel,
        float saturationLevel,
        float exhaustionLevel,
        float baseExhaustion,
        ChestCavityData data,
        double originalThreshold
    ) {
        double overflowScale = getMetabolismOverflowScale(data, originalThreshold);
        if (baseExhaustion <= 0) return overflowScale;

        float modifiedBaseExhaustion = data == null ? baseExhaustion : modifyExhaustion(baseExhaustion, data);
        if (modifiedBaseExhaustion <= 0) return overflowScale;

        int saturationDrainCount = (int) Math.ceil(Math.max(saturationLevel, 0.0F));
        int foodLevelDrainCount = Math.max(foodLevel - 18, 0);
        int availableDrainCount = saturationDrainCount + foodLevelDrainCount;
        double maxSafeExhaustion = 4.0 * (availableDrainCount + 1);
        double safeExhaustionCapacity = maxSafeExhaustion - exhaustionLevel;
        double queueExhaustionCapacity = 40.0 - exhaustionLevel;
        double affordableExhaustion = Math.min(safeExhaustionCapacity, queueExhaustionCapacity);
        if (affordableExhaustion < modifiedBaseExhaustion) return 0;

        double affordableScale = affordableExhaustion / modifiedBaseExhaustion;
        return Math.min(overflowScale, affordableScale);
    }

    /**
     * 判断自然回血是否至少能安全支付一次原版消耗。
     */
    public static boolean canApplyMetabolismRegeneration(
        int foodLevel,
        float saturationLevel,
        float exhaustionLevel,
        float baseExhaustion,
        ChestCavityData data,
        double originalThreshold
    ) {
        return getAffordableMetabolismRegenerationScale(
            foodLevel,
            saturationLevel,
            exhaustionLevel,
            baseExhaustion,
            data,
            originalThreshold
        ) >= 1;
    }

    /**
     * 按可安全支付的新陈代谢倍率修改自然回血量。
     */
    public static float modifyMetabolismRegenerationAmount(
        float amount,
        int foodLevel,
        float saturationLevel,
        float exhaustionLevel,
        float baseExhaustion,
        ChestCavityData data,
        double originalThreshold
    ) {
        return (float) (amount * getAffordableMetabolismRegenerationScale(
            foodLevel,
            saturationLevel,
            exhaustionLevel,
            baseExhaustion,
            data,
            originalThreshold
        ));
    }

    /**
     * 按可安全支付的新陈代谢倍率同步修改自然回血产生的饥饿消耗。
     */
    public static float modifyMetabolismRegenerationExhaustion(
        float exhaustion,
        int foodLevel,
        float saturationLevel,
        float exhaustionLevel,
        ChestCavityData data,
        double originalThreshold
    ) {
        return (float) (exhaustion * getAffordableMetabolismRegenerationScale(
            foodLevel,
            saturationLevel,
            exhaustionLevel,
            exhaustion,
            data,
            originalThreshold
        ));
    }

    /**
     * 修改饥饿消耗值
     *
     * @param value 原始消耗值
     * @param data  胸腔数据
     * @return 修改后的消耗值
     */
    public static float modifyExhaustion(float value, ChestCavityData data) {
        return (float) (value * MathUtil.getInverseScale(data.getDifferenceValue(InitAttribute.ENDURANCE)));
    }

    /**
     * 处理实体呼吸逻辑
     * <p>
     * 从 CommonHooks.onLivingBreathe 中提取
     * </p>
     *
     * @param entity           实体
     * @param consumeAirAmount 默认消耗的氧气量
     * @param refillAirAmount  默认回复的氧气量
     */
    public static void onLivingBreathe(LivingEntity entity, int consumeAirAmount, int refillAirAmount) {
        boolean isAir = entity.getEyeInFluidType().isAir() || entity.level()
            .getBlockState(BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ()))
            .is(Blocks.BUBBLE_COLUMN);
        ChestCavityData data = ChestCavityUtil.getData(entity);
        boolean canBreathe = !data.isNeedBreath();
        if (!canBreathe) {
            double consumer = consumeAirAmount;
            double refill = 4;
            double currRecovery = data.getCurrentValue(InitAttribute.BREATH_RECOVERY);
            double currWaterBreath = data.getCurrentValue(InitAttribute.WATER_BREATH);
            boolean isInvulnerable = entity instanceof Player player && player.getAbilities().invulnerable;
            // 检测呼吸功能
            canBreathe = isInvulnerable
                         || (
                             data.getCurrentValue(InitAttribute.BREATH_CAPACITY) > 0
                             && (
                                 isAir
                                 ? currRecovery > 0
                                 : (currWaterBreath > 0 || (MobEffectUtil.hasWaterBreathing(entity) && data.getCurrentValue(InitAttribute.BREATH_RECOVERY) > 0))
                             )
                         );
            if (canBreathe) {
                double defRecovery = data.getDefaultValue(InitAttribute.BREATH_RECOVERY);
                double defWaterBreath = data.getDefaultValue(InitAttribute.WATER_BREATH);
                double diffWaterBreath = currWaterBreath - defWaterBreath;
                double diffRecovery = currRecovery - defRecovery;
                double factor;
                if (isAir && defWaterBreath > 0) {
                    // 是水生生物且在空气中，检测默认水下呼吸和当前呼吸回复的差值
                    factor = (currRecovery - defWaterBreath);
                } else if (!isAir && defRecovery > 0) {
                    // 是陆生生物且在水中，检测默认默认呼吸回复和当前水下呼吸的差值
                    factor = (currWaterBreath - defRecovery);
                } else {
                    // 陆上用呼吸效率，水中用水下呼吸
                    factor = isAir ? diffRecovery : diffWaterBreath;
                }
                if (factor != 0) {
                    refill *= 1 + factor / 2;
                    // 如果因子为负数，则代表回复低下，在疾跑状态增加额外惩罚
                    if (!isInvulnerable && factor < 0 && entity.isSprinting()) {
                        refill -= 4;
                    }
                    // residueOxygen为小数缓存
                    refill += data.oxygenRemainder;
                    data.oxygenRemainder = refill % 1;
                }
            } else {
                double capacity = data.getDifferenceValue(InitAttribute.BREATH_CAPACITY);
                consumer *= MathUtil.getInverseScale(capacity);
                // 此处缓存负数氧气为了和上面缓存的氧气匹配
                consumer -= data.oxygenRemainder;
                data.oxygenRemainder = -consumer % 1;
            }
            refillAirAmount = (int) refill;
            consumeAirAmount = (int) consumer;
        }
        LivingBreatheEvent breatheEvent = new LivingBreatheEvent(entity, canBreathe, consumeAirAmount, refillAirAmount);
        NeoForge.EVENT_BUS.post(breatheEvent);
        if (breatheEvent.canBreathe()) {
            entity.setAirSupply(Math.min(entity.getAirSupply() + breatheEvent.getRefillAirAmount(), entity.getMaxAirSupply()));
        } else {
            entity.setAirSupply(entity.getAirSupply() - breatheEvent.getConsumeAirAmount());
        }
        if (entity.getAirSupply() <= 0) {
            LivingDrownEvent drownEvent = new LivingDrownEvent(entity);
            if (!NeoForge.EVENT_BUS.post(drownEvent).isCanceled() && drownEvent.isDrowning()) {
                entity.setAirSupply(0);
                // 只在水中才会出现粒子效果
                if (!isAir) {
                    Vec3 vec3 = entity.getDeltaMovement();
                    for (int i = 0; i < drownEvent.getBubbleCount(); ++i) {
                        double d2 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                        double d3 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                        double d4 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                        entity.level().addParticle(
                            ParticleTypes.BUBBLE,
                            entity.getX() + d2,
                            entity.getY() + d3,
                            entity.getZ() + d4,
                            vec3.x,
                            vec3.y,
                            vec3.z
                        );
                    }
                }
                if (drownEvent.getDamageAmount() > 0) {
                    entity.hurt(entity.damageSources().drown(), drownEvent.getDamageAmount());
                }
            }
        }
        if (!isAir && !entity.level().isClientSide && entity.isPassenger() && entity.getVehicle() != null && !entity.getVehicle()
            .canBeRiddenUnderFluidType(entity.getEyeInFluidType(), entity)) {
            entity.stopRiding();
        }
    }

    /**
     * 应用结晶治疗效果
     *
     * @param crystal   末影水晶
     * @param tickCount tick 计数
     */
    public static void applyCrystalHealing(EndCrystal crystal, int tickCount) {
        List<LivingEntity> list = crystal.level().getEntitiesOfClass(
            LivingEntity.class,
            crystal.getBoundingBox().inflate(ChestCavityBeyondConfig.crystalEffectSearchRange),
            entity -> !(entity instanceof EnderDragon)
                      && entity.getAttribute(InitAttribute.CRYSTALLIZATION).getValue() > 0
        );
        for (LivingEntity entity : list) {
            ChestCavityData data = ChestCavityUtil.getData(entity);
            double crystallization = data.getCurrentValue(InitAttribute.CRYSTALLIZATION);
            if (crystallization <= 0) continue;
            if (tickCount % 10 == 0) {
                entity.heal((float) crystallization / 5);
                if (tickCount % 20 == 0 && entity instanceof Player player) {
                    player.getFoodData().eat((int) Math.min(crystallization / 5, 1), 0.5F);
                }
            }
        }
    }

    /**
     * 应用发射效果（攻击时将目标向上弹起）
     *
     * @param target 被攻击的实体
     * @param source 伤害源
     */
    public static void applyLaunchEffect(LivingEntity target, DamageSource source) {
        if (source.getDirectEntity() instanceof LivingEntity attacker) {
            double launch = ChestCavityUtil.getData(attacker).getCurrentValue(InitAttribute.LAUNCH);
            if (launch > 0) {
                double knockbackResistance = ChestCavityUtil.getData(target).getCurrentValue(Attributes.KNOCKBACK_RESISTANCE) / 8;
                double yAdd = Math.max(0.0, 1 - knockbackResistance);
                target.setDeltaMovement(target.getDeltaMovement().add(0.0, 0.04 * launch * yAdd, 0.0));
            }
        }
    }

    /**
     * 判断是否应该添加食物效果
     * <p>
     * 如果有腐食消化，则不会附加中毒和饥饿效果
     * </p>
     *
     * @param entity         实体
     * @param effectInstance 药效实例
     * @return 是否应该添加效果
     */
    public static boolean shouldAddEatEffect(LivingEntity entity, MobEffectInstance effectInstance) {
        if (ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.SCAVENGER_DIGESTION) > 0) {
            return effectInstance.getEffect() != MobEffects.POISON && effectInstance.getEffect() != MobEffects.HUNGER;
        }
        return true;
    }

    /**
     * 根据水过敏属性计算雪球伤害
     *
     * @param result 命中结果
     * @return 伤害值
     */
    public static int getSnowballDamage(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity entity) {
            return ChestCavityUtil.getData(entity).getCurrentValue(InitAttribute.WATER_ALLERGY) > 0 ? 3 : 0;
        }
        return 0;
    }
}
