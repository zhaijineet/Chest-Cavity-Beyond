package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
import net.zhaiji.chestcavitybeyond.api.task.BlazeFireballTask;
import net.zhaiji.chestcavitybeyond.api.task.GuardianLaserTask;
import net.zhaiji.chestcavitybeyond.api.task.OrganCooldownTask;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.entity.ThrownCobweb;
import net.zhaiji.chestcavitybeyond.register.InitEffect;

import java.util.List;

public class OrganSkillUtil {
    /**
     * 食草的基本食物属性
     */
    private static final FoodProperties GRASS_FOOD = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(1)
            .build();

    /**
     * 尝试为实体添加物品冷却
     *
     * @param entity   实体
     * @param stack    冷却物品
     * @param cooldown 冷却时间
     */
    public static void addCooldown(LivingEntity entity, ItemStack stack, int cooldown) {
        if (entity instanceof Player player) {
            if (!player.isCreative()) {
                player.getCooldowns().addCooldown(stack.getItem(), cooldown);
            }
        } else {
            ChestCavityData data = ChestCavityUtil.getData(entity);
            Item item = stack.getItem();
            data.getFirstTaskIf(task -> task instanceof OrganCooldownTask ct && ct.isOnCooldown(item))
                .ifPresentOrElse(
                    task -> ((OrganCooldownTask) task).setOrRefresh(cooldown),
                    () -> data.addTask(new OrganCooldownTask(item, cooldown))
                );
        }
    }

    /**
     * 检测实体的此物品是否正在冷却
     *
     * @param entity 实体
     * @param stack  物品
     * @return 是否在冷却
     */
    public static boolean hasCooldown(LivingEntity entity, ItemStack stack) {
        if (entity instanceof Player player) {
            return !player.isCreative() && player.getCooldowns().isOnCooldown(stack.getItem());
        }
        ChestCavityData data = ChestCavityUtil.getData(entity);
        return data.hasTaskIf(task -> task instanceof OrganCooldownTask ct && ct.isOnCooldown(stack));
    }

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
        for (int i = 0; i < ChestCavityBeyondConfig.randomTeleportAttempts; i++) {
            if (TeleportUtil.randomTeleport(entity, ender)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 让玩家沿着视线方向传送
     *
     * @param entity 实体
     * @param ender  末影属性值
     */
    public static boolean teleport(LivingEntity entity, double ender) {
        return TeleportUtil.teleport(entity, ender);
    }

    /**
     * 吃草
     */
    public static boolean graze(Player player) {
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
     * 自爆
     */
    public static boolean explosion(LivingEntity entity, double creepy) {
        if (creepy <= 0) return false;
        entity.level().explode(null, entity.getX(), entity.getY(), entity.getZ(), (float) (3 * creepy), Level.ExplosionInteraction.NONE);
        return true;
    }

    /**
     * 用铁锭修复自身
     */
    public static boolean ironRepair(Player player, double ironRepair) {
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
    public static boolean furnacePower(Player player, double furnacePower) {
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

    /**
     * 吐丝
     */
    public static boolean silk(LivingEntity entity) {
        if (entity instanceof Player player) {
            boolean instabuild = player.getAbilities().instabuild;
            if (player.getFoodData().getFoodLevel() <= 0 && !instabuild) return false;
            if (!instabuild) {
                player.getFoodData().addExhaustion(4);
            }
        }
        Level level = entity.level();
        level.playSound(
                null,
                entity.blockPosition(),
                SoundEvents.EGG_THROW,
                SoundSource.PLAYERS,
                0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        ThrownCobweb thrownCobweb = new ThrownCobweb(entity, level);
        thrownCobweb.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0, 1, 1);
        level.addFreshEntity(thrownCobweb);
        return true;
    }

    /**
     * 吐口水
     */
    public static boolean spit(LivingEntity entity) {
        Level level = entity.level();
        level.playSound(
                null,
                entity.blockPosition(),
                SoundEvents.LLAMA_SPIT,
                entity.getSoundSource(),
                1.0F,
                1.0F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F
        );
        LlamaSpit llamaspit = new LlamaSpit(EntityType.LLAMA_SPIT, level);
        llamaspit.setOwner(entity);
        llamaspit.setPos(entity.getX(), entity.getEyeY() - 0.5, entity.getZ());
        llamaspit.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0, 1, 1);
        level.addFreshEntity(llamaspit);
        return true;
    }

    /**
     * 发射凋零骷髅头
     */
    public static boolean witherSkull(LivingEntity entity) {
        Level level = entity.level();
        level.levelEvent(null, 1024, entity.blockPosition(), 0);
        Vec3 lookAngle = entity.getLookAngle();
        WitherSkull witherSkull = new WitherSkull(level, entity, lookAngle.normalize());
        witherSkull.setPos(entity.getX() + lookAngle.x, entity.getEyeY() - 0.5, entity.getZ() + lookAngle.z);
        level.addFreshEntity(witherSkull);
        return true;
    }

    /**
     * 发射小火球
     */
    public static boolean smallFireball(ChestCavityData data, LivingEntity entity, int vomitFireball) {
        if (vomitFireball <= 0) return false;
        data.addTask(new BlazeFireballTask(vomitFireball));
        return true;
    }

    /**
     * 发射雪球
     */
    public static boolean snowball(LivingEntity entity) {
        Level level = entity.level();
        level.playSound(
                null,
                entity.blockPosition(),
                SoundEvents.SNOW_GOLEM_SHOOT,
                entity.getSoundSource(),
                1,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        Snowball snowball = new Snowball(level, entity);
        Vec3 lookAngle = entity.getLookAngle();
        snowball.setPos(entity.getX() + lookAngle.x / 2, entity.getEyeY() - 0.2, entity.getZ() + lookAngle.z / 2);
        snowball.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0, 1.6F, 12);
        level.addFreshEntity(snowball);
        return true;
    }

    /**
     * 发射大火球
     */
    public static boolean largeFireball(LivingEntity entity, double ghastly) {
        Level level = entity.level();
        level.levelEvent(null, 1016, entity.blockPosition(), 0);
        Vec3 lookAngle = entity.getLookAngle();
        LargeFireball largefireball = new LargeFireball(level, entity, lookAngle.normalize(), (int) ghastly);
        largefireball.setPos(entity.getX(), entity.getEyeY() - 0.9, entity.getZ());
        level.addFreshEntity(largefireball);
        return true;
    }

    /**
     * 发射潜影子弹
     */
    public static void shulkerBullet(LivingEntity entity, Entity target) {
        Level level = entity.level();
        level.playSound(
                null,
                entity.blockPosition(),
                SoundEvents.SHULKER_SHOOT,
                entity.getSoundSource(),
                2.0F,
                (level.random.nextFloat() - level.random.nextFloat()) * 0.2F + 1.0F
        );
        level.addFreshEntity(new ShulkerBullet(level, entity, target, Direction.UP.getAxis()));
    }

    public static boolean shulkerBullet(LivingEntity entity) {
        int distance = ChestCavityBeyondConfig.shulkerBulletDistance;
        HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(entity, checkEntity -> checkEntity != entity, distance);
        if (hitResult instanceof EntityHitResult entityHitResult) {
            shulkerBullet(entity, entityHitResult.getEntity());
            return true;
        }
        return false;
    }

    /**
     * 发射风弹
     */
    public static boolean windCharge(Player player) {
        Level level = player.level();
        level.playSound(
                null,
                player.blockPosition(),
                SoundEvents.BREEZE_SHOOT,
                player.getSoundSource(),
                1.5F,
                1.0F
        );
        WindCharge windcharge = new WindCharge(player, level, player.position().x(), player.getEyePosition().y(), player.position().z());
        windcharge.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 1);
        level.addFreshEntity(windcharge);
        return true;
    }

    /**
     * 发射龙息弹
     */
    public static boolean dragonFireball(LivingEntity entity) {
        Level level = entity.level();
        level.levelEvent(null, 1017, entity.blockPosition(), 0);
        DragonFireball dragonfireball = new DragonFireball(level, entity, entity.getLookAngle().normalize());
        dragonfireball.setPos(entity.getX(), entity.getEyeY() - 0.6, entity.getZ());
        dragonfireball.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0, 1, 1);
        level.addFreshEntity(dragonfireball);
        return true;
    }

    /**
     * 发射音爆
     */
    public static boolean sonicBoom(LivingEntity entity) {
        Level level = entity.level();
        Vec3 from = entity.getEyePosition();
        Vec3 lookAngle = entity.getLookAngle().normalize();
        int sonicBoomDist = ChestCavityBeyondConfig.sonicBoomDistance;
        Vec3 to = from.add(lookAngle.scale(sonicBoomDist));
        AABB searchBox = new AABB(from, to).inflate(1.0);
        List<LivingEntity> targets = level.getEntitiesOfClass(
                LivingEntity.class,
                searchBox,
                target -> {
                    if (target == entity) return false;
                    Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2, 0);
                    Vec3 direction = targetPos.subtract(from);
                    double distance = direction.length();
                    if (distance > sonicBoomDist) return false;
                    Vec3 projected = from.add(lookAngle.scale(distance));
                    return projected.distanceTo(targetPos) < 1.5;
                }
        );
        level.playSound(
                null,
                entity.blockPosition(),
                SoundEvents.WARDEN_SONIC_BOOM,
                entity.getSoundSource(),
                3.0F,
                1.0F
        );
        if (level instanceof ServerLevel serverLevel) {
            for (int i = 1; i < sonicBoomDist; i++) {
                Vec3 pos = from.add(lookAngle.scale(i));
                serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, pos.x(), pos.y(), pos.z(), 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        for (LivingEntity target : targets) {
            target.hurt(level.damageSources().sonicBoom(entity), 10);
        }
        return true;
    }

    /**
     * 守卫者激光
     */
    public static boolean guardianLaser(LivingEntity entity, boolean elder) {
        int distance = ChestCavityBeyondConfig.guardianLaserDistance;
        HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(entity, checkEntity -> checkEntity != entity, distance);
        if (hitResult instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof LivingEntity target) {
            ChestCavityUtil.getData(entity).addTask(new GuardianLaserTask(target, elder));
            return true;
        }
        return false;
    }
}
