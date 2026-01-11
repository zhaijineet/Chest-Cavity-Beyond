package net.zhaiji.chestcavitybeyond.api.task;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BlazeFireballTask implements IChestCavityTask {
    private int cooldown;
    private int count;

    public BlazeFireballTask(int count) {
        this.count = count;
    }

    @Override
    public void tick(LivingEntity entity) {
        if (cooldown <= 0) {
            Level level = entity.level();
            level.levelEvent(null, 1018, entity.blockPosition(), 0);
            Vec3 lookAngle = entity.getLookAngle();
            SmallFireball smallfireball = new SmallFireball(level, entity, lookAngle.normalize());
            smallfireball.setPos(entity.getX() + lookAngle.x / 2, entity.getEyeY() - 0.4, entity.getZ() + lookAngle.z / 2);
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
