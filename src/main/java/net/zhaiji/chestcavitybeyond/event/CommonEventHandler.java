package net.zhaiji.chestcavitybeyond.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.mixinapi.IMobEffectInstance;
import net.zhaiji.chestcavitybeyond.register.InitAttachmentType;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.MathUtil;

public class CommonEventHandler {
    /**
     * 当实体第一次生成时，为其附加胸腔数据
     *
     * @param event 实体加入维度事件
     */
    public static void handlerEntityJoinLevelEvent(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && !event.loadedFromDisk() && event.getEntity() instanceof LivingEntity entity) {
            entity.getData(InitAttachmentType.CHEST_CAVITY).init();
        }
    }

    /**
     * 为所有实体添加默认初始化器官属性
     *
     * @param event 实体属性初始化事件
     */
    public static void handlerEntityAttributeModificationEvent(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entityType -> {
            InitAttribute.ATTRIBUTE.getEntries().forEach(attribute -> {
                event.add(entityType, attribute);
            });
        });
    }

    /**
     * 应用实体解毒效率属性的更改
     *
     * @param event 效果添加事件
     */
    public static void handlerMobEffectEvent$Added(MobEffectEvent.Added event) {
        ChestCavityData data = ChestCavityUtil.getData(event.getEntity());
        double difference = data.getDifferenceValue(InitAttribute.DETOXIFICATION);
        MobEffectInstance instance = event.getEffectInstance();
        if (difference != 0 && instance instanceof IMobEffectInstance mobEffectInstance && mobEffectInstance.isHarmful()) {
            mobEffectInstance.setDuration(instance.mapDuration(duration -> (int) (duration * MathUtil.getInverseExpScale(difference))));
        }
    }

    /**
     * 应用实体防御属性的增减伤
     *
     * @param event 实体受伤前事件
     */
    public static void handlerLivingDamageEvent$Pre(LivingDamageEvent.Pre event) {
        ChestCavityData data = ChestCavityUtil.getData(event.getEntity());
        double defense = data.getDifferenceValue(InitAttribute.DEFENSE);
        double damage = event.getOriginalDamage();
        damage *= MathUtil.getAttenuationScale(damage, defense);
        // TODO 未检测伤害类型
        event.setNewDamage((float) damage);
    }

    /**
     * 应用胸腔Tick
     *
     * @param event 实体tick之后事件
     */
    public static void handlerEntityTickEvent$Post(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            ChestCavityUtil.getData(entity).tick();
        }
    }
}
