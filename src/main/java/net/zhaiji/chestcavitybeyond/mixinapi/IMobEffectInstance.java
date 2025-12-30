package net.zhaiji.chestcavitybeyond.mixinapi;

public interface IMobEffectInstance {
    /**
     * @return 是否为负面效果
     */
    boolean isHarmful();

    /**
     * 设置持续时间
     * @param duration 持续时间
     */
    void setDuration(int duration);

    /**
     * 设置实例等级
     * @param amplifier 实例等级
     */
    void setAmplifier(int amplifier);
}
