package net.zhaiji.chestcavitybeyond.api.task;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zhaiji.chestcavitybeyond.network.client.packet.AddGuardianLaserRenderTaskPacket;

public class GuardianLaserTask implements IChestCavityTask {
    private final int maxTime;
    private final LivingEntity target;
    private final boolean elder;
    private int timer = 0;

    public GuardianLaserTask(LivingEntity target, boolean elder) {
        this.target = target;
        this.elder = elder;
        maxTime = elder ? 60 : 80;
    }

    @Override
    public void onAdded(LivingEntity entity) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new AddGuardianLaserRenderTaskPacket(entity.getId(), target.getId(), elder));
    }

    @Override
    public void tick(LivingEntity entity) {
        if (timer >= maxTime) {
            target.hurt(entity.damageSources().indirectMagic(entity, entity), elder ? 4 : 2);
            target.hurt(entity.damageSources().mobAttack(entity), elder ? 6 : 3);
        }
        timer++;
    }

    @Override
    public boolean canRemove(LivingEntity entity) {
        // TODO 距离写入配置
        return timer > maxTime || entity.isRemoved() || target.isRemoved() || entity.distanceTo(target) > 16;
    }
}
