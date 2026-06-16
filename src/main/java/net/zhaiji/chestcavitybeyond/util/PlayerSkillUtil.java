package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.register.InitEffect;

public class PlayerSkillUtil {

    /**
     * 末影阑尾——沿着视线方向传送
     */
    public static boolean teleport(ChestCavitySlotContext context) {
        return OrganSkillUtil.teleport(
            context.entity(),
            context.data().getCurrentValue(InitAttribute.ENDER)
        );
    }

    /**
     * 食草动物瘤胃——吃草
     */
    public static boolean graze(ChestCavitySlotContext context) {
        if (context.entity() instanceof Player player) {
            return graze(player);
        }
        return false;
    }

    /**
     * 苦力怕阑尾——自爆
     */
    public static boolean explosion(ChestCavitySlotContext context) {
        return OrganSkillUtil.explosion(
            context.entity(),
            context.data().getCurrentValue(InitAttribute.EXPLOSIVE)
        );
    }

    /**
     * 熔炉内核——吃燃料回复饱食度饱和度
     */
    public static boolean furnacePower(ChestCavitySlotContext context) {
        if (context.entity() instanceof Player player) {
            return furnacePower(player, context.data().getCurrentValue(InitAttribute.FURNACE_POWER));
        }
        return false;
    }

    /**
     * 傀儡装甲板——用铁锭修复自身
     */
    public static boolean ironRepair(ChestCavitySlotContext context) {
        if (context.entity() instanceof Player player) {
            return ironRepair(player, context.data().getCurrentValue(InitAttribute.IRON_REPAIR));
        }
        return false;
    }

    /**
     * 丝腺——吐丝（沿视线方向，消耗饱食度）
     */
    public static boolean silk(ChestCavitySlotContext context) {
        LivingEntity entity = context.entity();
        if (entity instanceof Player player) {
            boolean instabuild = player.getAbilities().instabuild;
            if (player.getFoodData().getFoodLevel() <= 0 && !instabuild) return false;
            if (!instabuild) {
                player.getFoodData().addExhaustion(4);
            }
        }
        return OrganSkillUtil.silk(entity, entity.getLookAngle().normalize());
    }

    /**
     * 炼金腺——给自己添加药水效果
     */
    public static boolean alchemistGland(ChestCavitySlotContext context) {
        context.stack()
            .getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
            .forEachEffect(context.entity()::addEffect);
        return true;
    }

    /**
     * 羊驼肺——吐口水（沿视线方向）
     */
    public static boolean spit(ChestCavitySlotContext context) {
        LivingEntity entity = context.entity();
        return OrganSkillUtil.spit(entity, entity.getLookAngle().normalize());
    }

    /**
     * 活性烈焰棒——连续发射小火球（沿视线方向，通过 Task）
     */
    public static boolean smallFireball(ChestCavitySlotContext context) {
        return OrganSkillUtil.smallFireball(
            context.data(),
            (int) context.data().getCurrentValue(InitAttribute.VOMIT_FIREBALL),
            null
        );
    }

    /**
     * 雪之核心——发射雪球（沿视线方向）
     */
    public static boolean snowball(ChestCavitySlotContext context) {
        LivingEntity entity = context.entity();
        return OrganSkillUtil.snowball(entity, entity.getLookAngle().normalize());
    }

    /**
     * 恶魂胃——发射大火球（沿视线方向）
     */
    public static boolean largeFireball(ChestCavitySlotContext context) {
        LivingEntity entity = context.entity();
        return OrganSkillUtil.largeFireball(
            entity,
            entity.getLookAngle().normalize(),
            context.data().getCurrentValue(InitAttribute.GHASTLY)
        );
    }

    /**
     * 潜影贝脾脏——沿视线方向射线检测，发射潜影子弹
     */
    public static boolean shulkerBullet(ChestCavitySlotContext context) {
        LivingEntity entity = context.entity();
        Entity target = OrganSkillUtil.findLineOfSightTarget(
            entity, ChestCavityBeyondConfig.shulkerBulletDistance);
        if (target == null) return false;
        OrganSkillUtil.shulkerBullet(entity, target);
        return true;
    }

    /**
     * 旋风核心——沿视线方向发射风弹
     */
    public static boolean windCharge(ChestCavitySlotContext context) {
        LivingEntity entity = context.entity();
        return OrganSkillUtil.windCharge(entity, entity.getLookAngle().normalize());
    }

    /**
     * 龙之肺脏——发射龙息弹（沿视线方向）
     */
    public static boolean dragonFireball(ChestCavitySlotContext context) {
        LivingEntity entity = context.entity();
        return OrganSkillUtil.dragonFireball(entity, entity.getLookAngle().normalize());
    }

    /**
     * 幽匿核心——释放音爆（沿视线方向）
     */
    public static boolean sonicBoom(ChestCavitySlotContext context) {
        LivingEntity entity = context.entity();
        return OrganSkillUtil.sonicBoom(entity, entity.getLookAngle().normalize());
    }

    /**
     * 守卫者之眼——沿视线射线检测目标后发射激光
     */
    public static boolean guardianLaser(ChestCavitySlotContext context) {
        LivingEntity entity = context.entity();
        Entity target = OrganSkillUtil.findLineOfSightTarget(
            entity, ChestCavityBeyondConfig.guardianLaserDistance);
        if (target instanceof LivingEntity livingTarget) {
            return OrganSkillUtil.guardianLaser(entity, livingTarget, false);
        }
        return false;
    }

    /**
     * 远古守卫者之眼——沿视线射线检测目标后发射远古激光
     */
    public static boolean elderGuardianLaser(ChestCavitySlotContext context) {
        LivingEntity entity = context.entity();
        Entity target = OrganSkillUtil.findLineOfSightTarget(
            entity, ChestCavityBeyondConfig.guardianLaserDistance);
        if (target instanceof LivingEntity livingTarget) {
            return OrganSkillUtil.guardianLaser(entity, livingTarget, true);
        }
        return false;
    }

    /**
     * 下界之星——发射凋零骷髅头（沿视线方向）
     */
    public static boolean witherSkull(ChestCavitySlotContext context) {
        LivingEntity entity = context.entity();
        return OrganSkillUtil.witherSkull(entity, entity.getLookAngle().normalize());
    }

    // ==================== Player 专用底层逻辑 ====================

    /**
     * 食草的基本食物属性
     */
    private static final FoodProperties GRASS_FOOD = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(1)
            .build();

    /**
     * 吃草
     */
    private static boolean graze(Player player) {
        Vec3 from = player.getEyePosition();
        Vec3 to = from.add(player.getLookAngle().normalize().scale(player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue()));
        ClipContext clipContext = new ClipContext(
                from,
                to,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                CollisionContext.empty()
        );
        Level level = player.level();
        BlockHitResult blockHitResult = level.clip(clipContext);
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = blockHitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);
            if (state.is(Blocks.SHORT_GRASS)) {
                player.getFoodData().eat(GRASS_FOOD);
                level.destroyBlock(pos, false);
                return true;
            } else if (state.is(Blocks.TALL_GRASS)) {
                player.getFoodData().eat(GRASS_FOOD);
                player.getFoodData().eat(GRASS_FOOD);
                level.destroyBlock(pos, false);
                return true;
            } else if (state.is(Blocks.GRASS_BLOCK)) {
                player.getFoodData().eat(GRASS_FOOD);
                player.gameEvent(GameEvent.EAT);
                level.levelEvent(2001, pos, Block.getId(state));
                level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
                return true;
            }
        }
        return false;
    }

    /**
     * 用铁锭修复自身
     */
    private static boolean ironRepair(Player player, double ironRepair) {
        if (ironRepair <= 0) return false;
        ItemStack stack = null;
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.is(Items.IRON_INGOT)) {
                stack = itemStack;
                break;
            }
        }
        if (stack == null) {
            for (ItemStack itemStack : player.getInventory().offhand) {
                if (itemStack.is(Items.IRON_INGOT)) {
                    stack = itemStack;
                    break;
                }
            }
        }
        if (stack == null) return false;
        float oldHealth = player.getHealth();
        player.heal((float) (2.5 * ironRepair));
        if (player.getHealth() > oldHealth) {
            Level level = player.level();
            stack.consume(1, player);
            float pitch = 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F;
            level.playSound(null, player.blockPosition(), SoundEvents.IRON_GOLEM_REPAIR, player.getSoundSource(), 1.0F, pitch);
            return true;
        }
        return false;
    }

    /**
     * 让玩家可以吃燃料回复饱食度饱和度
     */
    private static boolean furnacePower(Player player, double furnacePower) {
        if (furnacePower <= 0) return false;
        InteractionHand hand = InteractionHand.MAIN_HAND;
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getBurnTime(null) <= 0) {
            hand = InteractionHand.OFF_HAND;
            stack = player.getItemInHand(hand);
        }
        if (stack.getBurnTime(null) <= 0) return false;
        boolean isCrouching = player.isCrouching();
        int totalDuration = 0;
        int amplifier = Math.max(0, (int) (furnacePower - 1));
        int maxDuration = ChestCavityBeyondConfig.furnacePowerMaxDuration;
        boolean isConsume = false;
        MobEffectInstance effect = player.getEffect(InitEffect.FURNACE_POWER);
        if (effect != null) {
            if (amplifier < effect.getAmplifier()) {
                totalDuration += effect.getDuration() / (1 + effect.getAmplifier() - amplifier);
                amplifier = effect.getAmplifier();
            } else {
                totalDuration += effect.getDuration();
            }
        }
        do {
            int duration = stack.getBurnTime(null);
            // 当已有effect的情况下，计算时间大于最大值，不进行任何更改
            if (totalDuration + duration > maxDuration) {
                break;
            }
            totalDuration += duration;
            ItemStack remaining = stack.getCraftingRemainingItem();
            stack.consume(1, player);
            isConsume = true;
            if (!remaining.isEmpty()) {
                if (player.getItemInHand(hand).isEmpty()) {
                    player.setItemInHand(hand, remaining);
                } else if (!player.addItem(remaining)) {
                    player.drop(remaining, false);
                }
            }

            // 更新stack，因为有可能为消耗后的剩余物品
            stack = player.getItemInHand(hand);
        } while (isCrouching && stack.getBurnTime(null) > 0);
        if (isConsume) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS);
            player.removeEffect(InitEffect.FURNACE_POWER);
            player.addEffect(new MobEffectInstance(InitEffect.FURNACE_POWER, totalDuration, amplifier));
            return true;
        }
        return false;
    }
}
