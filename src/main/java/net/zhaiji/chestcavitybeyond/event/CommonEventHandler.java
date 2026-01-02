package net.zhaiji.chestcavitybeyond.event;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zhaiji.chestcavitybeyond.api.capability.OrganFactory;
import net.zhaiji.chestcavitybeyond.api.event.RegisterChestCavityEvent;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.manager.CapabilityManager;
import net.zhaiji.chestcavitybeyond.manager.ChestCavityManager;
import net.zhaiji.chestcavitybeyond.mixinapi.IMobEffectInstance;
import net.zhaiji.chestcavitybeyond.network.client.packet.SyncChestCavityDataPacket;
import net.zhaiji.chestcavitybeyond.register.InitAttachmentType;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.MathUtil;
import net.zhaiji.chestcavitybeyond.util.OrganSkillUtil;

public class CommonEventHandler {
    /**
     * 为物品注册器官capability
     *
     * @param event 注册capability事件
     */
    public static void handlerRegisterCapabilitiesEvent(RegisterCapabilitiesEvent event) {
        OrganFactory.ORGAN_REGISTRY.forEach(((item, organ) -> {
            event.registerItem(CapabilityManager.ORGAN, (itemStack, context) -> organ, item);
        }));
    }

    /**
     * 注册实体类型关联胸腔类型
     *
     * @param event FML完成事件
     */
    public static void handlerFMLLoadCompleteEvent(FMLLoadCompleteEvent event) {
        ChestCavityManager.registerEntity(EntityType.PLAYER, ChestCavityManager.HUMAN);
        NeoForge.EVENT_BUS.post(new RegisterChestCavityEvent());
    }

    /**
     * 当实体第一次生成时，为其附加胸腔数据
     *
     * @param event 实体加入维度事件
     */
    public static void handlerEntityJoinLevelEvent(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && !event.loadedFromDisk() && event.getEntity() instanceof LivingEntity entity) {
            ChestCavityData data = entity.getData(InitAttachmentType.CHEST_CAVITY);
            data.init();
            if (entity instanceof ServerPlayer player) {
                PacketDistributor.sendToPlayer(player, new SyncChestCavityDataPacket(data.getOrgans(), data.selectedSlot));
            }
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
     * 重置玩家器官属性
     *
     * @param event 玩家重生事件
     */
    public static void handlerPlayerEvent$PlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        ChestCavityUtil.getData(event.getEntity()).resetAttributeModifier();
    }

    /**
     * 应用实体解毒效率和凋零化的更改
     *
     * @param event 效果能否适用事件
     */
    public static void handlerMobEffectEvent$Applicable(MobEffectEvent.Applicable event) {
        ChestCavityData data = ChestCavityUtil.getData(event.getEntity());
        double detoxification = data.getDifferenceValue(InitAttribute.DETOXIFICATION);
        double withered = data.getDifferenceValue(InitAttribute.WITHERED);
        if (detoxification == 0 && withered == 0) return;
        MobEffectInstance instance = event.getEffectInstance();
        if (instance instanceof IMobEffectInstance mobEffectInstance && mobEffectInstance.isHarmful()) {
            double scale = 1;
            scale *= MathUtil.getInverseScale(detoxification);
            if (instance.is(MobEffects.WITHER)) {
                scale *= MathUtil.getInverseScale(withered);
            }
            double finalScale = scale;
            int duration = instance.mapDuration(oldDuration -> (int) (oldDuration * finalScale));
            // if (duration <= 20) event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            mobEffectInstance.setDuration(duration);
        }
    }

    /**
     * 应用弹射物闪避效果
     *
     * @param event 实体即将受伤事件
     */
    public static void handlerLivingIncomingDamageEvent(LivingIncomingDamageEvent event) {
        DamageSource source = event.getSource();
        boolean isProjectile = source.is(DamageTypeTags.IS_PROJECTILE);
        boolean isWaterPotion = source.getDirectEntity() instanceof ThrownPotion potion && potion.getItem().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER);
        if (isProjectile || isWaterPotion) {
            LivingEntity entity = event.getEntity();
            ChestCavityData data = ChestCavityUtil.getData(entity);
            double ender = data.getCurrentValue(InitAttribute.ENDER);
            if (data.getCurrentValue(InitAttribute.PROJECTILE_DODGE) > 0 && ender > 0) {
                OrganSkillUtil.randomTeleport(entity, ender);
                event.setCanceled(true);
            }
        }
    }

    /**
     * 应用实体防御属性的增减伤
     *
     * @param event 实体受伤前事件
     */
    public static void handlerLivingDamageEvent$Pre(LivingDamageEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        ChestCavityData data = ChestCavityUtil.getData(entity);
        double damage = event.getNewDamage();
        DamageSource source = event.getSource();
        boolean flag = false;
        // 应用火焰伤害修改
        if (source.is(DamageTypeTags.IS_FIRE)) {
            double fireResistance = data.getDifferenceValue(InitAttribute.FIRE_RESISTANCE);
            damage *= MathUtil.getAttenuationScale(damage, fireResistance);
            flag = true;
        }

        // 应用溺水伤害修改
        if (source.is(DamageTypeTags.IS_DROWNING)) {
            double ender = data.getCurrentValue(InitAttribute.ENDER);
            if (ender > 0) OrganSkillUtil.randomTeleport(event.getEntity(), ender);
            flag = true;
        }

        // 应用凋零化效果
        if (source.getDirectEntity() instanceof LivingEntity sourceEntity && source.getEntity() == sourceEntity) {
            ChestCavityData sourceData = ChestCavityUtil.getData(sourceEntity);
            double withered = sourceData.getCurrentValue(InitAttribute.WITHERED);
            if (withered > 0) {
                int duration = (int) (40 * withered);
                int amplifier = 0;
                if (sourceData.hasOrgan(Items.NETHER_STAR)) {
                    duration += 200;
                    amplifier++;
                }
                entity.addEffect(new MobEffectInstance(MobEffects.WITHER, duration, amplifier), sourceEntity);
            }
        }

        // 当以上伤害类型都未检测通过时，应用防御减伤
        // TODO 这样对吗？应该不太对，之后在重新理清楚逻辑
        if (!flag) {
            double defense = data.getDifferenceValue(InitAttribute.DEFENSE);
            damage *= MathUtil.getAttenuationScale(damage, defense);
        }
        // TODO 未检测伤害类型
        event.setNewDamage((float) damage);

        // 触发所有器官的attack效果
        if (source.getDirectEntity() instanceof LivingEntity sourceEntity) {
            ChestCavityData sourceData = ChestCavityUtil.getData(sourceEntity);
            for (int i = 0; i < 27; i++) {
                ItemStack organ = sourceData.getStackInSlot(i);
                ChestCavityUtil.getOrganCap(organ).attack(
                        ChestCavityUtil.createContext(
                                sourceData,
                                sourceEntity,
                                i,
                                organ
                        ),
                        entity,
                        source,
                        event.getContainer()
                );
            }
        }
    }

    /**
     * 应用神经效率的挖掘速度改变
     *
     * @param event 玩家破坏速度事件
     */
    public static void handlerPlayerEvent$BreakSpeed(PlayerEvent.BreakSpeed event) {
        ChestCavityData data = ChestCavityUtil.getData(event.getEntity());
        double nerves = data.getDifferenceValue(InitAttribute.NERVES);
        if (nerves != 0) {
            double factor = 1 + MathUtil.getLog10Scale(nerves);
            double value = nerves > 0 ? factor : 1 / factor;
            event.setNewSpeed((float) (event.getOriginalSpeed() * value));
        }
    }

    /**
     * 应用胸腔Tick
     *
     * @param event 实体tick之后事件
     */
    public static void handlerEntityTickEvent$Post(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity entity && !entity.level().isClientSide()) {
            ChestCavityUtil.getData(entity).tick();
        }
    }
}
