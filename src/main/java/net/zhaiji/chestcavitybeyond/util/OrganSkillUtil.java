package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
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
        ItemStack mainHandStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHandStack = player.getItemInHand(InteractionHand.OFF_HAND);
        if (mainHandStack.is(Items.IRON_INGOT)) {
            stack = mainHandStack;
        } else if (offHandStack.is(Items.IRON_INGOT)) {
            stack = offHandStack;
        }
        if (stack != null) {
            float oldHealth = player.getHealth();
            player.heal((float) (2.5 * ironRepair));
            if (player.getHealth() > oldHealth) {
                Level level = player.level();
                stack.consume(1, player);
                float pitch = 1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F;
                level.playSound(null, player.getOnPos(), SoundEvents.IRON_GOLEM_REPAIR, player.getSoundSource(), 1.0F, pitch);
            }
        }
    }

    /**
     * 让玩家可以吃燃料回复饱食度饱和度
     */
    public static void furnacePower(Player player, double furnacePower) {
        if (furnacePower <= 0) return;
        ItemStack mainHandStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHandStack = player.getItemInHand(InteractionHand.OFF_HAND);
        ItemStack stack = null;
        int burnTime = 0;
        if (!mainHandStack.isEmpty()) {
            burnTime = mainHandStack.getBurnTime(null);
            if (burnTime > 0) {
                stack = mainHandStack;
            }
        } else if (!offHandStack.isEmpty()) {
            burnTime = offHandStack.getBurnTime(null);
            if (burnTime > 0) {
                stack = offHandStack;
            }
        }
        if (stack == null) return;
        int amplifier = Math.max(0, (int) (furnacePower - 1));
        int duration = burnTime;
        MobEffectInstance effect = player.getEffect(InitEffect.FURNACE_POWER);
        boolean hasEffect = effect != null;
        if (hasEffect) {
            if (amplifier < effect.getAmplifier()) {
                duration += effect.getDuration() / (effect.getAmplifier() - amplifier + 1);
                amplifier = effect.getAmplifier();
            } else {
                duration += effect.getDuration();
            }
        }
        // TODO 将最大上限时间写入配置
        int maxDuration = 24000;
        // 当已有effect的情况下，计算时间大于最大值，不进行任何更改
        if (hasEffect && duration > maxDuration) {
            return;
        }
        player.addEffect(new MobEffectInstance(InitEffect.FURNACE_POWER, Math.min(duration, maxDuration), amplifier));
        if (stack.hasCraftingRemainingItem()) {
            ItemStack remaining = stack.getCraftingRemainingItem();
            if (!player.addItem(remaining)) {
                player.drop(remaining, false);
            }
        }
        stack.consume(1, player);
    }
}
