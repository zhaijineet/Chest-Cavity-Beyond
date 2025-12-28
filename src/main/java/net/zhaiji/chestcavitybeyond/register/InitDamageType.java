package net.zhaiji.chestcavitybeyond.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;

public class InitDamageType {
    public static final ResourceKey<DamageType> ORGAN_LOSS = ResourceKey.create(Registries.DAMAGE_TYPE, ChestCavityBeyond.of("organ_loss"));

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(ORGAN_LOSS, builder(ORGAN_LOSS).build());
    }

    public static DamageTypeFactory builder(ResourceKey<DamageType> damageType) {
        return new DamageTypeFactory(damageType);
    }

    public static class DamageTypeFactory {
        private String msgId;
        private DamageScaling scaling = DamageScaling.NEVER;
        private float exhaustion = 0;
        private DamageEffects effects = DamageEffects.HURT;
        private DeathMessageType deathMessageType = DeathMessageType.DEFAULT;

        public DamageTypeFactory(ResourceKey<DamageType> damageTypeKey) {
            msgId = damageTypeKey.location().getNamespace() + "." + damageTypeKey.location().getPath();
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public DamageTypeFactory setScaling(DamageScaling scaling) {
            this.scaling = scaling;
            return this;
        }

        public DamageTypeFactory setExhaustion(float exhaustion) {
            this.exhaustion = exhaustion;
            return this;
        }

        public DamageTypeFactory setEffects(DamageEffects effects) {
            this.effects = effects;
            return this;
        }

        public DamageTypeFactory setDeathMessageType(DeathMessageType deathMessageType) {
            this.deathMessageType = deathMessageType;
            return this;
        }

        public DamageType build() {
            return new DamageType(msgId, scaling, exhaustion, effects, deathMessageType);
        }
    }
}
