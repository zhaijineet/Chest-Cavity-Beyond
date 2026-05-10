package net.zhaiji.chestcavitybeyond.mixinapi;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.world.entity.LivingEntity;

public interface IMobEffectInstance {
    /**
     * @return 是否为负面效果
     */
    boolean isHarmful();

    /**
     * 设置持续时间
     *
     * @param mapper 修改持续事件回调
     * @param entity 拥有此效果的实体，非null时自动触发同步
     */
    void setDuration(Int2IntFunction mapper, LivingEntity entity);

    /**
     * 设置效果等级
     *
     * @param amplifier 效果等级
     * @param entity    拥有此效果的实体，非null时自动触发同步
     */
    void setAmplifier(int amplifier, LivingEntity entity);
}
