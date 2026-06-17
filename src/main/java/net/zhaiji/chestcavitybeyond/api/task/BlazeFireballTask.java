package net.zhaiji.chestcavitybeyond.api.task;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.zhaiji.chestcavitybeyond.util.OrganSkillUtil;
import org.jetbrains.annotations.Nullable;

public class BlazeFireballTask implements IChestCavityTask {
    @Nullable
    private final LivingEntity target;

    private int cooldown;

    private int count;

    public BlazeFireballTask(int count) {
        this(count, null);
    }

    /**
     * @param target Goal 追踪目标，非 null 时朝目标方向发射火球
     */
    public BlazeFireballTask(int count, @Nullable LivingEntity target) {
        this.count = count;
        this.target = target;
    }

    @Override
    public void tick(LivingEntity entity) {
        if (cooldown <= 0) {
            Level level = entity.level();
            level.levelEvent(null, 1018, entity.blockPosition(), 0);
            Vec3 direction;
            if (target != null && target.isAlive() && !target.isRemoved()) {
                Vec3 diff = target.getEyePosition().subtract(entity.getEyePosition());
                // 两实体眼睛位置重合时回退到视线方向，避免零向量 normalize 产生 NaN
                direction = diff.lengthSqr() < 1e-8 ? entity.getLookAngle() : diff.normalize();
            } else {
                direction = entity.getLookAngle().normalize();
            }
            SmallFireball smallfireball = new SmallFireball(level, entity, direction);
            float eyeHeight = OrganSkillUtil.effectiveEyeHeight(entity);
            // 水平0.31≈原0.5格、Y0.25≈原0.4格（均 / 玩家眼高1.62）
            smallfireball.setPos(
                entity.getX() + direction.x * 0.31F * eyeHeight,
                entity.getEyeY() - eyeHeight * 0.25F,
                entity.getZ() + direction.z * 0.31F * eyeHeight
            );
            level.addFreshEntity(smallfireball);
            count--;
            cooldown = 6;
        }
        cooldown--;
    }

    @Override
    public boolean canRemove(LivingEntity entity) {
        return count <= 0 || entity.isRemoved();
    }
}
