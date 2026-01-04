package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.zhaiji.chestcavitybeyond.entity.ThrownCobweb;
import net.zhaiji.chestcavitybeyond.register.InitEffect;

public class OrganSkillUtil {
    /**
     * 食草的基本食物属性
     */
    private static final FoodProperties GRASS_FOOD = new FoodProperties.Builder()
            .nutrition(1)
            .saturationModifier(0.2F)
            .build();

    /**
     * 将目标传送到周围随机位置
     * <p>
     * {@link EnderMan//#teleport}
     * </p>
     *
     * @param entity 传送目标
     * @param ender  末影属性值
     * @return 传送是否成功
     */
    public static boolean randomTeleport(LivingEntity entity, double ender) {
        // TODO 尝试循环次数写入配置
        for (int i = 0; i < 16; i++) {
            if (TeleportUtil.randomTeleport(entity, ender)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 让玩家沿着视线方向传送
     *
     * @param player 玩家
     * @param ender  末影属性值
     */
    public static void teleport(Player player, double ender) {
        TeleportUtil.teleport(player, ender);
    }

    /**
     * 吃草
     */
    public static void graze(Player player) {
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
            } else if (state.is(Blocks.TALL_GRASS)) {
                player.getFoodData().eat(GRASS_FOOD);
                player.getFoodData().eat(GRASS_FOOD);
                level.destroyBlock(pos, false);
            } else if (state.is(Blocks.GRASS_BLOCK)) {
                player.getFoodData().eat(GRASS_FOOD);
                player.gameEvent(GameEvent.EAT);
                level.levelEvent(2001, pos, Block.getId(state));
                level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
            }
        }
    }

    /**
     * 自爆
     */
    public static void explosion(Player player, double creepy) {
        if (creepy <= 0) return;
        player.level().explode(null, player.getX(), player.getY(), player.getZ(), (float) (3 * creepy), Level.ExplosionInteraction.NONE);
    }

    /**
     * 用铁锭修复自身
     */
    public static void ironRepair(Player player, double ironRepair) {
        if (ironRepair <= 0) return;
        ItemStack stack = null;
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.is(Items.IRON_INGOT)) {
                stack = itemStack;
            }
        }
        for (ItemStack itemStack : player.getInventory().offhand) {
            if (itemStack.is(Items.IRON_INGOT)) {
                stack = itemStack;
            }
        }
        if (stack == null) return;
        float oldHealth = player.getHealth();
        player.heal((float) (2.5 * ironRepair));
        if (player.getHealth() > oldHealth) {
            Level level = player.level();
            stack.consume(1, player);
            float pitch = 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F;
            level.playSound(null, player.getOnPos().above(), SoundEvents.IRON_GOLEM_REPAIR, player.getSoundSource(), 1.0F, pitch);
        }
    }

    /**
     * 让玩家可以吃燃料回复饱食度饱和度
     */
    public static void furnacePower(Player player, double furnacePower) {
        if (furnacePower <= 0) return;
        InteractionHand hand = InteractionHand.MAIN_HAND;
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getBurnTime(null) <= 0) {
            hand = InteractionHand.OFF_HAND;
            stack = player.getItemInHand(hand);
        }
        if (stack.getBurnTime(null) <= 0) return;
        boolean isCrouching = player.isCrouching();
        int totalDuration = 0;
        int amplifier = Math.max(0, (int) (furnacePower - 1));
        // TODO 将最大上限时间写入配置
        int maxDuration = 24000;
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
            player.level().playSound(null, player.getOnPos().above(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS);
            player.addEffect(new MobEffectInstance(InitEffect.FURNACE_POWER, totalDuration, amplifier));
        }
    }

    /**
     * 吐丝
     */
    public static void silk(Player player) {
        boolean instabuild = player.getAbilities().instabuild;
        if (player.getFoodData().getFoodLevel() <= 0 && !instabuild) return;
        if (!instabuild) {
            player.getFoodData().addExhaustion(4);
        }
        Level level = player.level();
        level.playSound(
                null,
                player.getOnPos().above(),
                SoundEvents.EGG_THROW,
                SoundSource.PLAYERS,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        ThrownCobweb thrownCobweb = new ThrownCobweb(player, level);
        thrownCobweb.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1, 1);
        level.addFreshEntity(thrownCobweb);
    }

    /**
     * 吐口水
     */
    public static void spit(Player player) {
        Level level = player.level();
        LlamaSpit llamaspit = new LlamaSpit(EntityType.LLAMA_SPIT, level);
        llamaspit.setOwner(player);
        llamaspit.setPos(player.getX(), player.getEyeY() - 0.5, player.getZ());
        llamaspit.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1, 1);
        level.playSound(
                null,
                player.getOnPos().above(),
                SoundEvents.LLAMA_SPIT,
                player.getSoundSource(),
                1.0F,
                1.0F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F
        );
        level.addFreshEntity(llamaspit);
    }
}
