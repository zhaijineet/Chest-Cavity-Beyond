package net.zhaiji.chestcavitybeyond.event;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
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
import net.zhaiji.chestcavitybeyond.manager.ChestCavityTypeManager;
import net.zhaiji.chestcavitybeyond.mixinapi.IMobEffectInstance;
import net.zhaiji.chestcavitybeyond.network.client.packet.SyncChestCavityDataPacket;
import net.zhaiji.chestcavitybeyond.register.InitAttachmentType;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.register.InitDamageType;
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
        ChestCavityTypeManager.registerEntity(EntityType.PLAYER, ChestCavityTypeManager.HUMAN);
        ChestCavityTypeManager.registerEntity(EntityType.VILLAGER, ChestCavityTypeManager.HUMAN);
        ChestCavityTypeManager.registerEntity(EntityType.WANDERING_TRADER, ChestCavityTypeManager.HUMAN);
        ChestCavityTypeManager.registerEntity(EntityType.ILLUSIONER, ChestCavityTypeManager.HUMAN);
        ChestCavityTypeManager.registerEntity(EntityType.EVOKER, ChestCavityTypeManager.HUMAN);
        ChestCavityTypeManager.registerEntity(EntityType.VINDICATOR, ChestCavityTypeManager.HUMAN);
        ChestCavityTypeManager.registerEntity(EntityType.PILLAGER, ChestCavityTypeManager.HUMAN);
        ChestCavityTypeManager.registerEntity(EntityType.WITCH, ChestCavityTypeManager.HUMAN);
        // 食草生物1型
        ChestCavityTypeManager.registerEntity(EntityType.HORSE, ChestCavityTypeManager.HERBIVORE1);
        ChestCavityTypeManager.registerEntity(EntityType.DONKEY, ChestCavityTypeManager.HERBIVORE1);
        ChestCavityTypeManager.registerEntity(EntityType.MULE, ChestCavityTypeManager.HERBIVORE1);
        ChestCavityTypeManager.registerEntity(EntityType.GOAT, ChestCavityTypeManager.HERBIVORE1);
        // 食草生物2型
        ChestCavityTypeManager.registerEntity(EntityType.SNIFFER, ChestCavityTypeManager.HERBIVORE2);
        // 食草生物3型
        ChestCavityTypeManager.registerEntity(EntityType.COW, ChestCavityTypeManager.HERBIVORE3);
        ChestCavityTypeManager.registerEntity(EntityType.MOOSHROOM, ChestCavityTypeManager.HERBIVORE3);
        ChestCavityTypeManager.registerEntity(EntityType.SHEEP, ChestCavityTypeManager.HERBIVORE3);
        // 食肉动物
        ChestCavityTypeManager.registerEntity(EntityType.WOLF, ChestCavityTypeManager.CARNIVORE);
        ChestCavityTypeManager.registerEntity(EntityType.RAVAGER, ChestCavityTypeManager.CARNIVORE);
        // 动物
        ChestCavityTypeManager.registerEntity(EntityType.PIG, ChestCavityTypeManager.ANIMAL);
        ChestCavityTypeManager.registerEntity(EntityType.CAMEL, ChestCavityTypeManager.ANIMAL);
        ChestCavityTypeManager.registerEntity(EntityType.PANDA, ChestCavityTypeManager.ANIMAL);
        ChestCavityTypeManager.registerEntity(EntityType.POLAR_BEAR, ChestCavityTypeManager.ANIMAL);
        ChestCavityTypeManager.registerEntity(EntityType.HOGLIN, ChestCavityTypeManager.ANIMAL);
        // 海龟
        ChestCavityTypeManager.registerEntity(EntityType.TURTLE, ChestCavityTypeManager.TURTLE);
        // 兔子
        ChestCavityTypeManager.registerEntity(EntityType.RABBIT, ChestCavityTypeManager.RABBIT);
        // 小型食肉动物
        ChestCavityTypeManager.registerEntity(EntityType.CAT, ChestCavityTypeManager.SMALL_CARNIVORE);
        ChestCavityTypeManager.registerEntity(EntityType.OCELOT, ChestCavityTypeManager.SMALL_CARNIVORE);
        ChestCavityTypeManager.registerEntity(EntityType.FOX, ChestCavityTypeManager.SMALL_CARNIVORE);
        // 羊驼
        ChestCavityTypeManager.registerEntity(EntityType.LLAMA, ChestCavityTypeManager.LLAMA);
        ChestCavityTypeManager.registerEntity(EntityType.TRADER_LLAMA, ChestCavityTypeManager.LLAMA);
        // 小型动物
        ChestCavityTypeManager.registerEntity(EntityType.CHICKEN, ChestCavityTypeManager.SMALL_ANIMAL);
        ChestCavityTypeManager.registerEntity(EntityType.PARROT, ChestCavityTypeManager.SMALL_ANIMAL);
        ChestCavityTypeManager.registerEntity(EntityType.BAT, ChestCavityTypeManager.SMALL_ANIMAL);
        ChestCavityTypeManager.registerEntity(EntityType.ALLAY, ChestCavityTypeManager.SMALL_ANIMAL);
        ChestCavityTypeManager.registerEntity(EntityType.VEX, ChestCavityTypeManager.SMALL_ANIMAL);
        ChestCavityTypeManager.registerEntity(EntityType.ARMADILLO, ChestCavityTypeManager.SMALL_ANIMAL);
        // 美西螈
        ChestCavityTypeManager.registerEntity(EntityType.AXOLOTL, ChestCavityTypeManager.AXOLOTL);
        // 抗火生物
        ChestCavityTypeManager.registerEntity(EntityType.PIGLIN, ChestCavityTypeManager.FIREPROOF);
        ChestCavityTypeManager.registerEntity(EntityType.PIGLIN_BRUTE, ChestCavityTypeManager.FIREPROOF);
        ChestCavityTypeManager.registerEntity(EntityType.STRIDER, ChestCavityTypeManager.FIREPROOF);
        // 水生生物
        ChestCavityTypeManager.registerEntity(EntityType.SQUID, ChestCavityTypeManager.AQUATIC);
        ChestCavityTypeManager.registerEntity(EntityType.GLOW_SQUID, ChestCavityTypeManager.AQUATIC);
        // 守卫者
        ChestCavityTypeManager.registerEntity(EntityType.GUARDIAN, ChestCavityTypeManager.GUARDIAN);
        // 远古守卫者
        ChestCavityTypeManager.registerEntity(EntityType.ELDER_GUARDIAN, ChestCavityTypeManager.ELDER_GUARDIAN);
        // 海豚
        ChestCavityTypeManager.registerEntity(EntityType.DOLPHIN, ChestCavityTypeManager.DOLPHIN);
        // 鱼类
        ChestCavityTypeManager.registerEntity(EntityType.COD, ChestCavityTypeManager.SMALL_FISH);
        ChestCavityTypeManager.registerEntity(EntityType.SALMON, ChestCavityTypeManager.SMALL_FISH);
        ChestCavityTypeManager.registerEntity(EntityType.PUFFERFISH, ChestCavityTypeManager.SMALL_FISH);
        ChestCavityTypeManager.registerEntity(EntityType.TROPICAL_FISH, ChestCavityTypeManager.SMALL_FISH);
        // 青蛙
        ChestCavityTypeManager.registerEntity(EntityType.FROG, ChestCavityTypeManager.FROG);
        // 蝌蚪
        ChestCavityTypeManager.registerEntity(EntityType.TADPOLE, ChestCavityTypeManager.SMALL_AQUATIC);
        // 亡灵
        ChestCavityTypeManager.registerEntity(EntityType.ZOMBIE, ChestCavityTypeManager.UNDEAD);
        ChestCavityTypeManager.registerEntity(EntityType.HUSK, ChestCavityTypeManager.UNDEAD);
        ChestCavityTypeManager.registerEntity(EntityType.DROWNED, ChestCavityTypeManager.UNDEAD);
        ChestCavityTypeManager.registerEntity(EntityType.ZOMBIE_HORSE, ChestCavityTypeManager.UNDEAD);
        ChestCavityTypeManager.registerEntity(EntityType.PHANTOM, ChestCavityTypeManager.UNDEAD);
        ChestCavityTypeManager.registerEntity(EntityType.ZOMBIE_VILLAGER, ChestCavityTypeManager.UNDEAD);
        ChestCavityTypeManager.registerEntity(EntityType.ZOMBIFIED_PIGLIN, ChestCavityTypeManager.UNDEAD);
        ChestCavityTypeManager.registerEntity(EntityType.ZOGLIN, ChestCavityTypeManager.UNDEAD);
        // 骷髅
        ChestCavityTypeManager.registerEntity(EntityType.SKELETON, ChestCavityTypeManager.SKELETON);
        ChestCavityTypeManager.registerEntity(EntityType.SKELETON_HORSE, ChestCavityTypeManager.SKELETON);
        ChestCavityTypeManager.registerEntity(EntityType.STRAY, ChestCavityTypeManager.SKELETON);
        ChestCavityTypeManager.registerEntity(EntityType.BOGGED, ChestCavityTypeManager.SKELETON);
        // 史莱姆
        ChestCavityTypeManager.registerEntity(EntityType.SLIME, ChestCavityTypeManager.SLIME);
        // 岩浆怪
        ChestCavityTypeManager.registerEntity(EntityType.MAGMA_CUBE, ChestCavityTypeManager.MAGMA_CUBE);
        // 节肢生物
        ChestCavityTypeManager.registerEntity(EntityType.SILVERFISH, ChestCavityTypeManager.ARTHROPOD);
        ChestCavityTypeManager.registerEntity(EntityType.ENDERMITE, ChestCavityTypeManager.ARTHROPOD);
        ChestCavityTypeManager.registerEntity(EntityType.BEE, ChestCavityTypeManager.ARTHROPOD);
        // 蜘蛛
        ChestCavityTypeManager.registerEntity(EntityType.SPIDER, ChestCavityTypeManager.SPIDER);
        // 洞穴蜘蛛
        ChestCavityTypeManager.registerEntity(EntityType.CAVE_SPIDER, ChestCavityTypeManager.CAVE_SPIDER);
        // 末影生物
        ChestCavityTypeManager.registerEntity(EntityType.ENDERMAN, ChestCavityTypeManager.ENDER);
        // 末影龙
        ChestCavityTypeManager.registerEntity(EntityType.ENDER_DRAGON, ChestCavityTypeManager.ENDER_DRAGON);
        // 烈焰人
        ChestCavityTypeManager.registerEntity(EntityType.BLAZE, ChestCavityTypeManager.BLAZE);
        // 恶魂
        ChestCavityTypeManager.registerEntity(EntityType.GHAST, ChestCavityTypeManager.GHAST);
        // 凋零骷髅
        ChestCavityTypeManager.registerEntity(EntityType.WITHER_SKELETON, ChestCavityTypeManager.WITHER_SKELETON);
        // 凋灵
        ChestCavityTypeManager.registerEntity(EntityType.WITHER, ChestCavityTypeManager.WITHER);
        // 苦力怕
        ChestCavityTypeManager.registerEntity(EntityType.CREEPER, ChestCavityTypeManager.CREEPER);
        // 潜影贝
        ChestCavityTypeManager.registerEntity(EntityType.SHULKER, ChestCavityTypeManager.SHULKER);
        // 旋风人
        ChestCavityTypeManager.registerEntity(EntityType.BREEZE, ChestCavityTypeManager.BREEZE);
        // 监守者
        ChestCavityTypeManager.registerEntity(EntityType.WARDEN, ChestCavityTypeManager.WARDEN);
        // 铁傀儡
        ChestCavityTypeManager.registerEntity(EntityType.IRON_GOLEM, ChestCavityTypeManager.IRON_GOLEM);
        // 雪傀儡
        ChestCavityTypeManager.registerEntity(EntityType.SNOW_GOLEM, ChestCavityTypeManager.SNOW_GOLEM);
        // 盔甲架
        ChestCavityTypeManager.registerEntity(EntityType.ARMOR_STAND, ChestCavityTypeManager.ARMOR_STAND);

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
        ChestCavityUtil.getData(event.getEntity()).initAttributeModifier();
    }

    /**
     * 开胸器打开生物胸腔
     *
     * @param event 玩家交互实体事件
     */
    public static void handlerPlayerInteractEvent$EntityInteract(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getEntity().getItemInHand(event.getHand());
        // 当玩家手持开胸器时，可能更希望打开胸腔
        if (stack.is(InitItem.CHEST_OPENER.get()) && event.getTarget() instanceof LivingEntity) {
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
     * 应用弹射物闪避效果、无重力取消伤害
     *
     * @param event 实体即将受伤事件
     */
    public static void handlerLivingIncomingDamageEvent(LivingIncomingDamageEvent event) {
        DamageSource source = event.getSource();
        boolean isProjectile = source.is(DamageTypeTags.IS_PROJECTILE);
        boolean isWaterPotion = source.getDirectEntity() instanceof ThrownPotion potion && potion.getItem().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER);
        LivingEntity entity = event.getEntity();
        if (isProjectile || isWaterPotion) {
            ChestCavityData data = ChestCavityUtil.getData(entity);
            double ender = data.getCurrentValue(InitAttribute.ENDER);
            if (data.getCurrentValue(InitAttribute.PROJECTILE_DODGE) > 0 && ender > 0) {
                OrganSkillUtil.randomTeleport(entity, ender);
                event.setCanceled(true);
            }
        }
        if (source.is(DamageTypeTags.IS_FALL) && entity.getAttribute(Attributes.GRAVITY).getValue() <= 0) {
            event.setCanceled(true);
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
        // 这样对吗？应该对的，出问题再改
        if (!flag && !source.is(DamageTypeTags.BYPASSES_ARMOR)) {
            double defense = data.getDifferenceValue(InitAttribute.DEFENSE);
            damage *= MathUtil.getAttenuationScale(damage, defense);
        }

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
        for (int i = 0; i < 27; i++) {
            ItemStack organ = data.getStackInSlot(i);
            ChestCavityUtil.getOrganCap(organ).hurt(
                    ChestCavityUtil.createContext(
                            data,
                            entity,
                            i,
                            organ
                    ),
                    source,
                    event.getContainer()
            );
        }
    }

    /**
     * 显示因开胸而死亡的生物的死亡信息
     *
     * @param event 实体死亡事件
     */
    public static void handlerLivingDeathEvent(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        if (!source.is(InitDamageType.OPEN_CHEST)) return;
        LivingEntity entity = event.getEntity();
        if (source.getDirectEntity() instanceof ServerPlayer player && !(entity instanceof TamableAnimal tamable && tamable.getOwner() != null)) {
            player.sendSystemMessage(source.getLocalizedDeathMessage(entity));
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
        if (event.getEntity() instanceof LivingEntity entity) {
            ChestCavityUtil.getData(entity).tick();
        }
    }
}
