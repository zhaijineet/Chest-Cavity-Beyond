package net.zhaiji.chestcavitybeyond.mixinapi;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

/**
 * LivingEntity的扩展接口，暴露效果同步方法
 */
public interface ILivingEntity {

    /**
     * 触发效果更新同步到客户端
     *
     * @param effectInstance 效果实例
     * @param forced         是否强制（true会重新应用属性修饰符）
     * @param entity         来源实体，可为null
     */
    void chestCavityBeyond$onEffectUpdated(MobEffectInstance effectInstance, boolean forced, @Nullable Entity entity);
}
