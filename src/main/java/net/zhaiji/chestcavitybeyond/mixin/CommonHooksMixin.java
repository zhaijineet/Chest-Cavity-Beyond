package net.zhaiji.chestcavitybeyond.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.living.LivingDrownEvent;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.MathUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CommonHooks.class)
public abstract class CommonHooksMixin {
    /**
     * @author zhaijineet
     * @reason 水平太菜，只能用Overwrite了
     */
    @Overwrite
    public static void onLivingBreathe(LivingEntity entity, int consumeAirAmount, int refillAirAmount) {
        boolean isAir = entity.getEyeInFluidType().isAir() || entity.level().getBlockState(BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ())).is(Blocks.BUBBLE_COLUMN);
        ChestCavityData data = ChestCavityUtil.getData(entity);
        // 检测呼吸功能
        boolean canBreathe =
                (entity instanceof Player player && player.getAbilities().invulnerable)
                        || (
                        data.getCurrentValue(InitAttribute.BREATH_CAPACITY) > 0
                                && (
                                isAir
                                        ? data.getCurrentValue(InitAttribute.BREATH_RECOVERY) > 0
                                        : data.getCurrentValue(InitAttribute.WATER_BREATH) > 0
                                        || (MobEffectUtil.hasWaterBreathing(entity) && data.getCurrentValue(InitAttribute.BREATH_RECOVERY) > 0)
                        )
                );
        double consumer = consumeAirAmount;
        double refill = 4;
        if (canBreathe) {
            double waterBreath = data.getDifferenceValue(InitAttribute.WATER_BREATH);
            double recovery = data.getDifferenceValue(InitAttribute.BREATH_RECOVERY);
            // 水中用水下呼吸，否则使用呼吸效率
            double factor = !isAir ? waterBreath : recovery;
            if (factor != 0) {
                refill *= 1 + factor / 2;
                // 如果因子为负数，则代表效率低下，增加额外惩罚
                if (factor < 0 && (entity.isSprinting() || entity.isSwimming())) {
                    refill -= 4;
                }
            }
            // residueOxygen为小数缓存
            refill += data.oxygenRemainder;
            data.oxygenRemainder = refill % 1;
        } else {
            double capacity = data.getDifferenceValue(InitAttribute.BREATH_CAPACITY);
            consumer *= MathUtil.getInverseExpScale(capacity);
            // 此处缓存负数氧气为了和上面缓存的氧气匹配
            consumer -= data.oxygenRemainder;
            data.oxygenRemainder = -consumer % 1;
        }
        refillAirAmount = (int) refill;
        consumeAirAmount = (int) consumer;
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
                Vec3 vec3 = entity.getDeltaMovement();
                for (int i = 0; i < drownEvent.getBubbleCount(); ++i) {
                    double d2 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                    double d3 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                    double d4 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                    entity.level().addParticle(ParticleTypes.BUBBLE, entity.getX() + d2, entity.getY() + d3, entity.getZ() + d4, vec3.x, vec3.y, vec3.z);
                }
                if (drownEvent.getDamageAmount() > 0) {
                    entity.hurt(entity.damageSources().drown(), drownEvent.getDamageAmount());
                }
            }
        }
        if (!isAir && !entity.level().isClientSide && entity.isPassenger() && entity.getVehicle() != null && !entity.getVehicle().canBeRiddenUnderFluidType(entity.getEyeInFluidType(), entity)) {
            entity.stopRiding();
        }
    }
}
