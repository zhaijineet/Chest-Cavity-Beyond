package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
import net.zhaiji.chestcavitybeyond.api.task.BlazeFireballTask;
import net.zhaiji.chestcavitybeyond.api.task.GuardianLaserTask;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.entity.ThrownCobweb;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 器官技能底层逻辑工具方法
 */
public class OrganSkillUtil {
    /**
     * 获取用于计算发射点偏移的有效眼高（含保底下限）。
     *
     * @param entity 实体
     * @return 不低于 0.5F 的眼高
     */
    public static float effectiveEyeHeight(LivingEntity entity) {
        return Math.max(entity.getEyeHeight(), 0.5F);
    }

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
            data.getItemCooldowns().addCooldown(stack.getItem(), cooldown);
        }
    }

    /**
     * 尝试为实体添加物品冷却
     *
     * @param entity   实体
     * @param item     冷却物品
     * @param cooldown 冷却时间
     */
    public static void addCooldown(LivingEntity entity, Item item, int cooldown) {
        if (entity instanceof Player player) {
            if (!player.isCreative()) {
                player.getCooldowns().addCooldown(item, cooldown);
            }
        } else {
            ChestCavityData data = ChestCavityUtil.getData(entity);
            data.getItemCooldowns().addCooldown(item, cooldown);
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
        return data.getItemCooldowns().isOnCooldown(stack.getItem());
    }

    /**
     * 检测实体的此物品是否正在冷却
     *
     * @param entity 实体
     * @param item   物品
     * @return 是否在冷却
     */
    public static boolean hasCooldown(LivingEntity entity, Item item) {
        if (entity instanceof Player player) {
            return !player.isCreative() && player.getCooldowns().isOnCooldown(item);
        }
        ChestCavityData data = ChestCavityUtil.getData(entity);
        return data.getItemCooldowns().isOnCooldown(item);
    }

    /**
     * 计算从实体眼睛位置指向目标的方向向量（归一化）
     */
    public static Vec3 directionTo(LivingEntity entity, LivingEntity target) {
        Vec3 direction = target.getEyePosition().subtract(entity.getEyePosition());
        if (direction.lengthSqr() < 1e-8) {
            // 两实体眼睛位置重合时，回退到实体视线方向，避免零向量导致投射物原地掉落
            return entity.getLookAngle().normalize();
        }
        return direction.normalize();
    }

    /**
     * 沿实体视线方向进行射线检测，返回命中的实体（排除自身），未命中返回 null
     *
     * @param entity   实体
     * @param distance 检测距离
     * @return 命中的实体，未命中返回 null
     */
    @Nullable
    public static Entity findLineOfSightTarget(LivingEntity entity, int distance) {
        HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(entity, checkEntity -> checkEntity != entity, distance);
        if (hitResult instanceof EntityHitResult entityHitResult) {
            return entityHitResult.getEntity();
        }
        return null;
    }

    /**
     * 沿着视线方向传送
     *
     * @param entity 实体
     * @param ender  末影属性值
     */
    public static boolean teleport(LivingEntity entity, double ender) {
        return TeleportUtil.teleport(entity, ender);
    }

    /**
     * 将目标传送到周围随机位置
     * <p>
     * {@link EnderMan//#teleport()}
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
     * 战术传送——传送到目标背后
     */
    public static boolean teleportBehind(LivingEntity entity, LivingEntity target, double ender) {
        Vec3 behind = target.getLookAngle().normalize().scale(-ender).add(target.position());
        return TeleportUtil.teleportTo(entity, behind);
    }

    /**
     * 逃跑传送——远离威胁方向传送
     */
    public static boolean fleeTeleport(LivingEntity entity, @Nullable LivingEntity threat, double ender) {
        Vec3 fleeDirection;
        if (threat != null) {
            Vec3 diff = entity.position().subtract(threat.position());
            if (diff.lengthSqr() < 1e-8) {
                // 实体与威胁重合时，随机方向逃跑
                double angle = entity.level().getRandom().nextDouble() * Math.PI * 2;
                diff = new Vec3(Math.cos(angle), 0, Math.sin(angle));
            }
            fleeDirection = diff.normalize();
        } else {
            double angle = entity.level().getRandom().nextDouble() * Math.PI * 2;
            fleeDirection = new Vec3(Math.cos(angle), 0, Math.sin(angle));
        }
        Vec3 targetPosition = entity.position().add(fleeDirection.scale(ender));
        return TeleportUtil.teleportTo(entity, targetPosition);
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
     * 吐丝
     *
     * @param entity    实体
     * @param direction 发射方向（归一化）
     */
    public static boolean silk(LivingEntity entity, Vec3 direction) {
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
        thrownCobweb.shoot(direction.x, direction.y, direction.z, 1, 1);
        level.addFreshEntity(thrownCobweb);
        return true;
    }

    /**
     * 吐口水
     *
     * @param entity    实体
     * @param direction 发射方向（归一化）
     */
    public static boolean spit(LivingEntity entity, Vec3 direction) {
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
        float eyeHeight = effectiveEyeHeight(entity);
        // Y偏移 0.31 ≈ 原0.5格 / 玩家眼高1.62
        llamaspit.setPos(entity.getX(), entity.getEyeY() - eyeHeight * 0.31F, entity.getZ());
        llamaspit.shoot(direction.x, direction.y, direction.z, 1, 1);
        level.addFreshEntity(llamaspit);
        return true;
    }

    /**
     * 发射雪球
     *
     * @param entity    实体
     * @param direction 发射方向（归一化）
     */
    public static boolean snowball(LivingEntity entity, Vec3 direction) {
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
        float eyeHeight = effectiveEyeHeight(entity);
        // 水平0.31≈原0.5格、Y0.12≈原0.2格（均 / 玩家眼高1.62）
        snowball.setPos(
            entity.getX() + direction.x * 0.31F * eyeHeight,
            entity.getEyeY() - eyeHeight * 0.12F,
            entity.getZ() + direction.z * 0.31F * eyeHeight
        );
        snowball.shoot(direction.x, direction.y, direction.z, 1.6F, 12);
        level.addFreshEntity(snowball);
        return true;
    }

    /**
     * 发射大火球
     *
     * @param entity    实体
     * @param direction 发射方向（归一化）
     * @param ghastly   恶魂属性值（爆炸威力）
     */
    public static boolean largeFireball(LivingEntity entity, Vec3 direction, double ghastly) {
        Level level = entity.level();
        level.levelEvent(null, 1016, entity.blockPosition(), 0);
        LargeFireball largefireball = new LargeFireball(level, entity, direction, (int) ghastly);
        float eyeHeight = effectiveEyeHeight(entity);
        // Y偏移 0.56 ≈ 原0.9格 / 玩家眼高1.62
        largefireball.setPos(entity.getX(), entity.getEyeY() - eyeHeight * 0.56F, entity.getZ());
        level.addFreshEntity(largefireball);
        return true;
    }

    /**
     * 连续发射小火球（通过 Task）
     *
     * @param data   胸腔数据
     * @param count  发射数量
     * @param target 目标实体，null 时沿视线发射（由 Task 内部处理）
     */
    public static boolean smallFireball(ChestCavityData data, int count, @Nullable LivingEntity target) {
        if (count <= 0) return false;
        data.addTask(new BlazeFireballTask(count, target));
        return true;
    }

    /**
     * 发射凋零骷髅头
     *
     * @param entity    实体
     * @param direction 发射方向（归一化）
     */
    public static boolean witherSkull(LivingEntity entity, Vec3 direction) {
        Level level = entity.level();
        level.levelEvent(null, 1024, entity.blockPosition(), 0);
        WitherSkull witherSkull = new WitherSkull(level, entity, direction);
        float eyeHeight = effectiveEyeHeight(entity);
        // 水平0.62≈原1.0格、Y0.31≈原0.5格（均 / 玩家眼高1.62）
        witherSkull.setPos(
            entity.getX() + direction.x * 0.62F * eyeHeight,
            entity.getEyeY() - eyeHeight * 0.31F,
            entity.getZ() + direction.z * 0.62F * eyeHeight
        );
        level.addFreshEntity(witherSkull);
        return true;
    }

    /**
     * 发射龙息弹
     *
     * @param entity    实体
     * @param direction 发射方向（归一化）
     */
    public static boolean dragonFireball(LivingEntity entity, Vec3 direction) {
        Level level = entity.level();
        level.levelEvent(null, 1017, entity.blockPosition(), 0);
        DragonFireball dragonfireball = new DragonFireball(level, entity, direction);
        float eyeHeight = effectiveEyeHeight(entity);
        // Y偏移 0.37 ≈ 原0.6格 / 玩家眼高1.62
        dragonfireball.setPos(entity.getX(), entity.getEyeY() - eyeHeight * 0.37F, entity.getZ());
        dragonfireball.shoot(direction.x, direction.y, direction.z, 1, 1);
        level.addFreshEntity(dragonfireball);
        return true;
    }

    /**
     * 发射风弹
     *
     * @param entity    实体
     * @param direction 发射方向（归一化）
     */
    public static boolean windCharge(LivingEntity entity, Vec3 direction) {
        Level level = entity.level();
        level.playSound(
            null,
            entity.blockPosition(),
            SoundEvents.BREEZE_SHOOT,
            entity.getSoundSource(),
            1.5F,
            1.0F
        );
        WindCharge windcharge = new WindCharge(
            level,
            entity.position().x(),
            entity.getEyePosition().y(),
            entity.position().z(),
            entity.getDeltaMovement()
        );
        windcharge.setOwner(entity);
        windcharge.shoot(direction.x, direction.y, direction.z, 1.5F, 1);
        level.addFreshEntity(windcharge);
        return true;
    }

    /**
     * 朝指定目标发射潜影子弹
     *
     * @param entity 发射者
     * @param target 目标实体
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

    /**
     * 释放音爆
     *
     * @param entity    实体
     * @param direction 音爆方向（归一化）
     */
    public static boolean sonicBoom(LivingEntity entity, Vec3 direction) {
        Level level = entity.level();
        Vec3 from = entity.getEyePosition();
        int sonicBoomDist = ChestCavityBeyondConfig.sonicBoomDistance;
        Vec3 to = from.add(direction.scale(sonicBoomDist));
        AABB searchBox = new AABB(from, to).inflate(1.0);
        List<LivingEntity> targets = level.getEntitiesOfClass(
            LivingEntity.class,
            searchBox,
            target -> {
                if (target == entity) return false;
                Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2, 0);
                Vec3 relative = targetPos.subtract(from);
                double distance = relative.length();
                if (distance > sonicBoomDist) return false;
                Vec3 projected = from.add(direction.scale(distance));
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
                Vec3 pos = from.add(direction.scale(i));
                serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, pos.x(), pos.y(), pos.z(), 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        for (LivingEntity target : targets) {
            target.hurt(level.damageSources().sonicBoom(entity), 10);
        }
        return true;
    }

    /**
     * 守卫者激光（通过 Task）
     *
     * @param entity 实体
     * @param target 目标实体
     * @param elder  是否为远古守卫者激光
     */
    public static boolean guardianLaser(LivingEntity entity, LivingEntity target, boolean elder) {
        ChestCavityUtil.getData(entity).addTask(new GuardianLaserTask(target, elder));
        return true;
    }
}
