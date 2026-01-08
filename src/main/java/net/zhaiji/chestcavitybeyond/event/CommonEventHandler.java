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
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zhaiji.chestcavitybeyond.api.event.RegisterChestCavityEvent;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.builder.OrganBuilder;
import net.zhaiji.chestcavitybeyond.manager.CapabilityManager;
import net.zhaiji.chestcavitybeyond.manager.ChestCavityManager;
import net.zhaiji.chestcavitybeyond.mixinapi.IMobEffectInstance;
import net.zhaiji.chestcavitybeyond.network.client.packet.SyncChestCavityDataPacket;
import net.zhaiji.chestcavitybeyond.register.InitAttachmentType;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.register.InitItem;
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
        OrganBuilder.ORGAN_REGISTRY.forEach(((item, organ) -> {
            event.registerItem(CapabilityManager.ORGAN, (itemStack, context) -> organ, item);
        }));
    }

    /**
     * 注册实体类型关联胸腔类型
     *
     * @param event FML完成事件
     */
    public static void handlerFMLLoadCompleteEvent(FMLLoadCompleteEvent event) {
        // 人类
        ChestCavityManager.registerEntity(EntityType.PLAYER, ChestCavityManager.HUMAN);
        ChestCavityManager.registerEntity(EntityType.VILLAGER, ChestCavityManager.HUMAN);
        ChestCavityManager.registerEntity(EntityType.WANDERING_TRADER, ChestCavityManager.HUMAN);
        ChestCavityManager.registerEntity(EntityType.ILLUSIONER, ChestCavityManager.HUMAN);
        ChestCavityManager.registerEntity(EntityType.EVOKER, ChestCavityManager.HUMAN);
        ChestCavityManager.registerEntity(EntityType.VINDICATOR, ChestCavityManager.HUMAN);
        ChestCavityManager.registerEntity(EntityType.PILLAGER, ChestCavityManager.HUMAN);
        ChestCavityManager.registerEntity(EntityType.WITCH, ChestCavityManager.HUMAN);
        // 动物
        ChestCavityManager.registerEntity(EntityType.PIG, ChestCavityManager.ANIMAL);
        // 食草生物2型
        ChestCavityManager.registerEntity(EntityType.COW, ChestCavityManager.HERBIVORE2);
        ChestCavityManager.registerEntity(EntityType.MOOSHROOM, ChestCavityManager.HERBIVORE2);
        ChestCavityManager.registerEntity(EntityType.SHEEP, ChestCavityManager.HERBIVORE2);
        // 食草生物1型
        ChestCavityManager.registerEntity(EntityType.HORSE, ChestCavityManager.HERBIVORE1);
        ChestCavityManager.registerEntity(EntityType.DONKEY, ChestCavityManager.HERBIVORE1);
        ChestCavityManager.registerEntity(EntityType.MULE, ChestCavityManager.HERBIVORE1);
        ChestCavityManager.registerEntity(EntityType.GOAT, ChestCavityManager.HERBIVORE1);
        // 食肉动物
        ChestCavityManager.registerEntity(EntityType.WOLF, ChestCavityManager.CARNIVORE);
        ChestCavityManager.registerEntity(EntityType.RAVAGER, ChestCavityManager.CARNIVORE);
        // 动物
        ChestCavityManager.registerEntity(EntityType.CAMEL, ChestCavityManager.ANIMAL);
        ChestCavityManager.registerEntity(EntityType.PANDA, ChestCavityManager.ANIMAL);
        ChestCavityManager.registerEntity(EntityType.POLAR_BEAR, ChestCavityManager.ANIMAL);
        ChestCavityManager.registerEntity(EntityType.TURTLE, ChestCavityManager.ANIMAL);
        ChestCavityManager.registerEntity(EntityType.AXOLOTL, ChestCavityManager.ANIMAL);
        ChestCavityManager.registerEntity(EntityType.FROG, ChestCavityManager.ANIMAL);
        ChestCavityManager.registerEntity(EntityType.HOGLIN, ChestCavityManager.ANIMAL);
        // 兔子
        ChestCavityManager.registerEntity(EntityType.RABBIT, ChestCavityManager.RABBIT);
        // 小型食肉动物
        ChestCavityManager.registerEntity(EntityType.CAT, ChestCavityManager.SMALL_CARNIVORE);
        ChestCavityManager.registerEntity(EntityType.OCELOT, ChestCavityManager.SMALL_CARNIVORE);
        ChestCavityManager.registerEntity(EntityType.FOX, ChestCavityManager.SMALL_CARNIVORE);
        // 羊驼
        ChestCavityManager.registerEntity(EntityType.LLAMA, ChestCavityManager.LLAMA);
        // 小型动物
        ChestCavityManager.registerEntity(EntityType.CHICKEN, ChestCavityManager.SMALL_ANIMAL);
        ChestCavityManager.registerEntity(EntityType.PARROT, ChestCavityManager.SMALL_ANIMAL);
        ChestCavityManager.registerEntity(EntityType.BAT, ChestCavityManager.SMALL_ANIMAL);
        ChestCavityManager.registerEntity(EntityType.ALLAY, ChestCavityManager.SMALL_ANIMAL);
        ChestCavityManager.registerEntity(EntityType.VEX, ChestCavityManager.SMALL_ANIMAL);
        // 抗火生物
        ChestCavityManager.registerEntity(EntityType.PIGLIN, ChestCavityManager.FIREPROOF);
        ChestCavityManager.registerEntity(EntityType.PIGLIN_BRUTE, ChestCavityManager.FIREPROOF);
        ChestCavityManager.registerEntity(EntityType.STRIDER, ChestCavityManager.FIREPROOF);
        // 水生生物
        ChestCavityManager.registerEntity(EntityType.SQUID, ChestCavityManager.AQUATIC);
        ChestCavityManager.registerEntity(EntityType.GLOW_SQUID, ChestCavityManager.AQUATIC);
        ChestCavityManager.registerEntity(EntityType.GUARDIAN, ChestCavityManager.AQUATIC);
        ChestCavityManager.registerEntity(EntityType.ELDER_GUARDIAN, ChestCavityManager.AQUATIC);
        // 海豚
        ChestCavityManager.registerEntity(EntityType.DOLPHIN, ChestCavityManager.DOLPHIN);
        // 鱼类
        ChestCavityManager.registerEntity(EntityType.COD, ChestCavityManager.SMALL_FISH);
        ChestCavityManager.registerEntity(EntityType.SALMON, ChestCavityManager.SMALL_FISH);
        ChestCavityManager.registerEntity(EntityType.PUFFERFISH, ChestCavityManager.SMALL_FISH);
        ChestCavityManager.registerEntity(EntityType.TROPICAL_FISH, ChestCavityManager.SMALL_FISH);
        // 亡灵
        ChestCavityManager.registerEntity(EntityType.ZOMBIE, ChestCavityManager.UNDEAD);
        ChestCavityManager.registerEntity(EntityType.HUSK, ChestCavityManager.UNDEAD);
        ChestCavityManager.registerEntity(EntityType.DROWNED, ChestCavityManager.UNDEAD);
        ChestCavityManager.registerEntity(EntityType.ZOMBIE_HORSE, ChestCavityManager.UNDEAD);
        ChestCavityManager.registerEntity(EntityType.PHANTOM, ChestCavityManager.UNDEAD);
        ChestCavityManager.registerEntity(EntityType.ZOMBIE_VILLAGER, ChestCavityManager.UNDEAD);
        ChestCavityManager.registerEntity(EntityType.ZOMBIFIED_PIGLIN, ChestCavityManager.UNDEAD);
        ChestCavityManager.registerEntity(EntityType.ZOGLIN, ChestCavityManager.UNDEAD);
        // 骷髅
        ChestCavityManager.registerEntity(EntityType.SKELETON, ChestCavityManager.SKELETON);
        ChestCavityManager.registerEntity(EntityType.SKELETON_HORSE, ChestCavityManager.SKELETON);
        ChestCavityManager.registerEntity(EntityType.STRAY, ChestCavityManager.SKELETON);
        // 史莱姆
        ChestCavityManager.registerEntity(EntityType.SLIME, ChestCavityManager.SLIME);
        // 岩浆怪
        ChestCavityManager.registerEntity(EntityType.MAGMA_CUBE, ChestCavityManager.MAGMA_CUBE);
        // 节肢生物
        ChestCavityManager.registerEntity(EntityType.SILVERFISH, ChestCavityManager.ARTHROPOD);
        ChestCavityManager.registerEntity(EntityType.ENDERMITE, ChestCavityManager.ARTHROPOD);
        ChestCavityManager.registerEntity(EntityType.BEE, ChestCavityManager.ARTHROPOD);
        // 蜘蛛
        ChestCavityManager.registerEntity(EntityType.SPIDER, ChestCavityManager.SPIDER);
        // 洞穴蜘蛛
        ChestCavityManager.registerEntity(EntityType.CAVE_SPIDER, ChestCavityManager.CAVE_SPIDER);
        // 末影生物
        ChestCavityManager.registerEntity(EntityType.ENDERMAN, ChestCavityManager.ENDER);
        // 末影龙
        ChestCavityManager.registerEntity(EntityType.ENDER_DRAGON, ChestCavityManager.ENDER_DRAGON);
        // 烈焰人
        ChestCavityManager.registerEntity(EntityType.BLAZE, ChestCavityManager.BLAZE);
        // 恶魂
        ChestCavityManager.registerEntity(EntityType.GHAST, ChestCavityManager.GHAST);
        // 凋零骷髅
        ChestCavityManager.registerEntity(EntityType.WITHER_SKELETON, ChestCavityManager.WITHER_SKELETON);
        // 凋灵
        ChestCavityManager.registerEntity(EntityType.WITHER, ChestCavityManager.WITHER);
        // 苦力怕
        ChestCavityManager.registerEntity(EntityType.CREEPER, ChestCavityManager.CREEPER);
        // 潜影贝
        ChestCavityManager.registerEntity(EntityType.SHULKER, ChestCavityManager.SHULKER);
        // 旋风人
        ChestCavityManager.registerEntity(EntityType.BREEZE, ChestCavityManager.BREEZE);
        // 坚守者
        ChestCavityManager.registerEntity(EntityType.WARDEN, ChestCavityManager.HUMAN);
        // 铁傀儡
        ChestCavityManager.registerEntity(EntityType.IRON_GOLEM, ChestCavityManager.IRON_GOLEM);
        // 雪傀儡
        ChestCavityManager.registerEntity(EntityType.SNOW_GOLEM, ChestCavityManager.SNOW_GOLEM);

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
     * 开胸器打开生物胸腔
     *
     * @param event 玩家交互实体事件
     */
    public static void handlerPlayerInteractEvent$EntityInteract(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getEntity().getItemInHand(event.getHand());
        // 当玩家手持开胸器时，可能更希望打开胸腔
        if (stack.is(InitItem.CHEST_OPENER.get())) {
            // 取消实体交互，使开胸器能够正常使用
            event.setCanceled(true);
        }
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
