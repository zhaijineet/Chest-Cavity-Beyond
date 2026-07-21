package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.living.LivingDrownEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.mixinapi.IFoodData;
import net.zhaiji.chestcavitybeyond.mixinapi.IMobEffectInstance;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;

import java.util.List;

/**
 * 从各 Mixin 中提取的业务逻辑，便于测试和维护
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
        ChestCavityData chestCavityData = ChestCavityUtil.getData(foodDataAccessor.getPlayer());
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
        ChestCavityData data = ChestCavityUtil.getData(iFoodData.getPlayer());
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

        float modifiedBaseExhaustion = modifyExhaustion(baseExhaustion, data);
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
        return (float) (
            amount * getAffordableMetabolismRegenerationScale(
                foodLevel,
                saturationLevel,
                exhaustionLevel,
                baseExhaustion,
                data,
                originalThreshold
            )
        );
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
        return (float) (
            exhaustion * getAffordableMetabolismRegenerationScale(
                foodLevel,
                saturationLevel,
                exhaustionLevel,
                exhaustion,
                data,
                originalThreshold
            )
        );
    }

    /**
     * 修改饥饿消耗值
     *
     * @param value 原始消耗值
     * @param data  胸腔数据
     * @return 修改后的消耗值
     */
    public static float modifyExhaustion(float value, ChestCavityData data) {
        double enduranceDifference = data.getDifferenceValue(InitAttribute.ENDURANCE);
        return (float) (value * MathUtil.getSquareRootInverseScale(enduranceDifference));
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
        // TODO 待定：是否修复恶魂等生物在熔岩中溺死的问题，尚未决定，但我个人认为不应该修复，先写个TODO记录一下，别忘记了
        //   背景：熔岩等不可溺水流体被当作水下环境，导致这些生物在熔岩中溺死
        //   当前 isAir 仅判断空气流体，熔岩(canDrown=false)落入下方器官逻辑，无 WATER_BREATH 即溺水
        //   修复方法：在下方判断后补一道防线——非水流体若 canDrownInFluidType 返回 false 则视为可呼吸
        //   注意不能直接对水用 canDrownInFluidType：它对水返回 !canBreatheUnderwater()，
        //   会让守卫者等水生生物被取走鳃后仍不溺水，破坏器官呼吸设计
        //   正确条件：getEyeInFluidType() != NeoForgeMod.WATER_TYPE.value() && !canDrownInFluidType(getEyeInFluidType())
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
                if (isAir && defWaterBreath > 0 && defRecovery <= 0) {
                    // 纯水生（默认无空气呼吸）在空气中呼吸，跨界效率取决于空气呼吸能力与水下呼吸需求的对比
                    factor = currRecovery - defWaterBreath;
                } else if (!isAir && defRecovery > 0 && defWaterBreath <= 0) {
                    // 纯陆生（默认无水下呼吸）在水中呼吸（药水/器官修改），跨界效率取决于水下呼吸能力与空气呼吸需求的对比
                    factor = currWaterBreath - defRecovery;
                } else {
                    // 两栖型或同环境原生呼吸：与自身默认值对比
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
                consumer *= MathUtil.getSquareRootInverseScale(capacity);
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
        if (!isAir && !entity.level().isClientSide() && entity.isPassenger() && entity.getVehicle() != null && !entity.getVehicle()
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
                    player.getFoodData().eat((int) Math.max(crystallization / 5, 1), 0.5F);
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
            double launch = attacker.getAttributeValue(InitAttribute.LAUNCH);
            if (launch > 0) {
                double knockbackResistance = target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) / 8;
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
        if (entity.getAttributeValue(InitAttribute.SCAVENGER_DIGESTION) > 0) {
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
            return entity.getAttributeValue(InitAttribute.WATER_ALLERGY) > 0 ? 3 : 0;
        }
        return 0;
    }

    /**
     * 根据熔岩游泳速度属性，缩放原版 moveRelative 的输入参数
     */
    public static float applyLavaSwimSpeed(LivingEntity entity, float original) {
        return (float) (original * entity.getAttributeValue(InitAttribute.LAVA_SWIM_SPEED));
    }

    /**
     * 判断实体的火焰抗性是否达到免疫熔岩的阈值
     */
    public static boolean isLavaImmune(LivingEntity entity) {
        return entity.getAttributeValue(InitAttribute.FIRE_RESISTANCE) >= ChestCavityBeyondConfig.fireImmunityLava;
    }

    /**
     * 返回熔岩中水平移动的衰减系数，疾跑时 0.6（对齐水），非疾跑保持原版 0.5
     */
    public static float getLavaHorizontalDecay(LivingEntity entity) {
        return entity.isSprinting() ? 0.6F : 0.5F;
    }

    /**
     * 按 LAVA_SWIM_SPEED 属性值减缓熔岩额外的下沉重力
     */
    public static double modifyLavaExtraGravity(LivingEntity entity, double original) {
        if (!isLavaImmune(entity)) return original;
        double lavaSwimSpeed = entity.getAttributeValue(InitAttribute.LAVA_SWIM_SPEED);
        return lavaSwimSpeed > 1 ? original / lavaSwimSpeed : original;
    }

    public static boolean hasLavaWalkActive(LivingEntity entity) {
        return !entity.isShiftKeyDown() && entity.getAttributeValue(InitAttribute.LAVA_WALK) > 0;
    }

    /**
     * 判断实体是否应该站在指定流体表面（仅熔岩生效，需有激活的熔岩行者能力）
     */
    public static boolean canStandOnLava(FluidState fluidState, LivingEntity entity) {
        return fluidState.is(FluidTags.LAVA) && hasLavaWalkActive(entity);
    }

    /**
     * 对拥有熔岩行者属性的实体施加熔岩浮力，对齐原版炽足兽的 floatStrider 逻辑
     */
    public static void floatOnLava(LivingEntity entity) {
        if (entity.isInLava() && hasLavaWalkActive(entity)) {
            if (CollisionContext.of(entity).isAbove(LiquidBlock.STABLE_SHAPE, entity.blockPosition(), true)
                && !entity.level().getFluidState(entity.blockPosition().above()).is(FluidTags.LAVA)) {
                entity.setOnGround(true);
            } else {
                entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5).add(0.0, 0.05, 0.0));
            }
        }
    }

    public static boolean handleLavaFall(LivingEntity entity, BlockState state) {
        if (hasLavaWalkActive(entity)) {
            entity.checkInsideBlocks();
            if (state.getFluidState().is(FluidTags.LAVA)) {
                entity.resetFallDistance();
                return true;
            }
        }
        return false;
    }

    /**
     * 熔岩中跳跃时按 LAVA_SWIM_SPEED 的平方根增强向上脉冲，替代原版固定 0.04
     *
     * @return 是否已处理（调用方应 cancel 原方法）
     */
    public static boolean applyLavaJumpImpulse(LivingEntity entity, FluidType type) {
        if (type == NeoForgeMod.LAVA_TYPE.value()) {
            entity.setDeltaMovement(entity.getDeltaMovement()
                .add(0.0, 0.04 * Math.sqrt(entity.getAttributeValue(InitAttribute.LAVA_SWIM_SPEED)), 0.0));
            return true;
        }
        return false;
    }

    /**
     * 熔岩中下沉时按 LAVA_SWIM_SPEED 的平方根增强向下脉冲，替代原版固定 -0.04
     *
     * @return 是否已处理（调用方应 cancel 原方法）
     */
    public static boolean applyLavaSinkImpulse(LivingEntity entity, FluidType type) {
        if (type == NeoForgeMod.LAVA_TYPE.value()) {
            entity.setDeltaMovement(entity.getDeltaMovement()
                .add(0.0, -0.04 * Math.sqrt(entity.getAttributeValue(InitAttribute.LAVA_SWIM_SPEED)), 0.0));
            return true;
        }
        return false;
    }

    /**
     * 免疫熔岩伤害时覆盖原版 canSwim(false)，允许在熔岩中游泳（含游泳姿态与下沉）；
     * 熔岩行走激活时返回 false，使其不进入游泳判定以保留疾跑能力
     *
     * @param originalReturnValue 原版 canSwimInFluidType 的返回值
     * @return 是否应强制返回 true
     */
    public static boolean canSwimInLava(LivingEntity entity, FluidType type, boolean originalReturnValue) {
        if (originalReturnValue || type != NeoForgeMod.LAVA_TYPE.value() || hasLavaWalkActive(entity)) return false;
        return isLavaImmune(entity);
    }

    /**
     * 免疫熔岩伤害时清除熔岩迷雾，统一由 FIRE_RESISTANCE 属性阈值判定
     */
    public static boolean shouldClearLavaFog(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) return isLavaImmune(livingEntity);
        return false;
    }

    /**
     * 免疫火焰伤害时隐藏屏幕火焰覆盖层，统一由 FIRE_RESISTANCE 属性阈值判定
     */
    public static boolean shouldHideFireOverlay(Player player) {
        return player.getAttributeValue(InitAttribute.FIRE_RESISTANCE) >= ChestCavityBeyondConfig.fireImmunityFire;
    }
}
