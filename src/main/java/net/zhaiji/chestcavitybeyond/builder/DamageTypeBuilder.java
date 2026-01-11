package net.zhaiji.chestcavitybeyond.builder;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;

public class DamageTypeBuilder {
    private String msgId;
    private DamageScaling scaling = DamageScaling.NEVER;
    private float exhaustion = 0;
    private DamageEffects effects = DamageEffects.HURT;
    private DeathMessageType deathMessageType = DeathMessageType.DEFAULT;

    public DamageTypeBuilder(ResourceKey<DamageType> resourceKey) {
        msgId = resourceKey.location().getNamespace() + "." + resourceKey.location().getPath();
    }

    public static DamageTypeBuilder builder(ResourceKey<DamageType> resourceKey) {
        return new DamageTypeBuilder(resourceKey);
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public DamageTypeBuilder setScaling(DamageScaling scaling) {
        this.scaling = scaling;
        return this;
    }

    public DamageTypeBuilder setExhaustion(float exhaustion) {
        this.exhaustion = exhaustion;
        return this;
    }

    public DamageTypeBuilder setEffects(DamageEffects effects) {
        this.effects = effects;
        return this;
    }

    public DamageTypeBuilder setDeathMessageType(DeathMessageType deathMessageType) {
        this.deathMessageType = deathMessageType;
        return this;
    }

    public DamageType build() {
        return new DamageType(msgId, scaling, exhaustion, effects, deathMessageType);
    }
}
