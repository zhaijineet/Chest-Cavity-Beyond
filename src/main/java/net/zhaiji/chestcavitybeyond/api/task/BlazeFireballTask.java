package net.zhaiji.chestcavitybeyond.api.task;

import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.OrganSkillUtil;

public class BlazeFireballTask implements IChestCavityTask {
    private final ChestCavityData data;
    private int cooldown = 0;
    private int count = 0;

    public BlazeFireballTask(ChestCavityData data) {
        this.data = data;
        count = (int) Math.ceil(data.getCurrentValue(InitAttribute.VOMIT_FIREBALL));
    }

    @Override
    public void tick() {
        if (cooldown <= 0) {
            OrganSkillUtil.smallFireball(data.getOwner());
            count--;
            cooldown = 6;
        }
        cooldown--;
    }

    @Override
    public boolean canRemove() {
        return count <= 0;
    }
}
