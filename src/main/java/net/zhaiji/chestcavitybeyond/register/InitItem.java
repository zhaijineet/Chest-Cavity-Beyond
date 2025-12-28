package net.zhaiji.chestcavitybeyond.register;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.capability.OrganFactory;
import net.zhaiji.chestcavitybeyond.item.ChestOpenerItem;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;
import net.zhaiji.chestcavitybeyond.util.OrganSkillUtil;

import java.util.function.Supplier;

public class InitItem {
    public static final DeferredRegister<Item> ITEM = DeferredRegister.create(BuiltInRegistries.ITEM, ChestCavityBeyond.MOD_ID);
    // 开胸器
    public static final Supplier<Item> CHEST_OPENER = ITEM.register(
            "chest_opener",
            ChestOpenerItem::new
    );

    // 心脏
    public static final Supplier<Item> HEART = ITEM.register(
            "heart",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肺脏
    public static final Supplier<Item> LUNG = ITEM.register(
            "lung",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.BREATH_RECOVERY, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.BREATH_CAPACITY, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.ENDURANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 脊柱
    public static final Supplier<Item> SPINE = ITEM.register(
            "spine",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 胃
    public static final Supplier<Item> STOMACH = ITEM.register(
            "stomach",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肠子
    public static final Supplier<Item> INTESTINE = ITEM.register(
            "intestine",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肾脏
    public static final Supplier<Item> KIDNEY = ITEM.register(
            "kidney",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.FILTRATION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 脾脏
    public static final Supplier<Item> SPLEEN = ITEM.register(
            "spleen",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肝脏
    public static final Supplier<Item> LIVER = ITEM.register(
            "liver",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DETOXIFICATION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 阑尾
    public static final Supplier<Item> APPENDIX = ITEM.register(
            "appendix",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(Attributes.LUCK, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肋骨
    public static final Supplier<Item> RIB = ITEM.register(
            "rib",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肌肉
    public static final Supplier<Item> MUSCLE = ITEM.register(
            "muscle",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 动物心脏
    public static final Supplier<Item> ANIMAL_HEART = ITEM.register(
            "animal_heart",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物肺脏
    public static final Supplier<Item> ANIMAL_LUNG = ITEM.register(
            "animal_lung",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.BREATH_RECOVERY, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.BREATH_CAPACITY, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.ENDURANCE, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物脊柱 - 防御特殊处理为0.375
    public static final Supplier<Item> ANIMAL_SPINE = ITEM.register(
            "animal_spine",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.375));
                    })
                    .build()
    );

    // 动物胃
    public static final Supplier<Item> ANIMAL_STOMACH = ITEM.register(
            "animal_stomach",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物肠子
    public static final Supplier<Item> ANIMAL_INTESTINE = ITEM.register(
            "animal_intestine",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物肾脏
    public static final Supplier<Item> ANIMAL_KIDNEY = ITEM.register(
            "animal_kidney",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.FILTRATION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物脾脏
    public static final Supplier<Item> ANIMAL_SPLEEN = ITEM.register(
            "animal_spleen",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物肝脏
    public static final Supplier<Item> ANIMAL_LIVER = ITEM.register(
            "animal_liver",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DETOXIFICATION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物阑尾
    public static final Supplier<Item> ANIMAL_APPENDIX = ITEM.register(
            "animal_appendix",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(Attributes.LUCK, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物肋骨
    public static final Supplier<Item> ANIMAL_RIB = ITEM.register(
            "animal_rib",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物肌肉
    public static final Supplier<Item> ANIMAL_MUSCLE = ITEM.register(
            "animal_muscle",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 小型动物心脏
    public static final Supplier<Item> SMALL_ANIMAL_HEART = ITEM.register(
            "small_animal_heart",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物肺脏
    public static final Supplier<Item> SMALL_ANIMAL_LUNG = ITEM.register(
            "small_animal_lung",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.BREATH_RECOVERY, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.BREATH_CAPACITY, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.ENDURANCE, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物脊柱 - 防御特殊处理为0.375，其他属性0.5
    public static final Supplier<Item> SMALL_ANIMAL_SPINE = ITEM.register(
            "small_animal_spine",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.375));
                    })
                    .build()
    );

    // 小型动物胃
    public static final Supplier<Item> SMALL_ANIMAL_STOMACH = ITEM.register(
            "small_animal_stomach",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物肠子
    public static final Supplier<Item> SMALL_ANIMAL_INTESTINE = ITEM.register(
            "small_animal_intestine",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物肾脏
    public static final Supplier<Item> SMALL_ANIMAL_KIDNEY = ITEM.register(
            "small_animal_kidney",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.FILTRATION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物脾脏
    public static final Supplier<Item> SMALL_ANIMAL_SPLEEN = ITEM.register(
            "small_animal_spleen",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物肝脏
    public static final Supplier<Item> SMALL_ANIMAL_LIVER = ITEM.register(
            "small_animal_liver",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DETOXIFICATION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物阑尾
    public static final Supplier<Item> SMALL_ANIMAL_APPENDIX = ITEM.register(
            "small_animal_appendix",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(Attributes.LUCK, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物肋骨
    public static final Supplier<Item> SMALL_ANIMAL_RIB = ITEM.register(
            "small_animal_rib",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物肌肉
    public static final Supplier<Item> SMALL_ANIMAL_MUSCLE = ITEM.register(
            "small_animal_muscle",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 鳃
    public static final Supplier<Item> GILLS = ITEM.register(
            "gills",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.WATER_BREATH, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.BREATH_CAPACITY, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.ENDURANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 小型鳃
    public static final Supplier<Item> SMALL_GILLS = ITEM.register(
            "small_gills",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.WATER_BREATH, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.BREATH_CAPACITY, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.ENDURANCE, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 水生生物肌肉
    public static final Supplier<Item> AQUATIC_MUSCLE = ITEM.register(
            "aquatic_muscle",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(NeoForgeMod.SWIM_SPEED, OrganAttributeUtil.createMultipliedBaseModifier(id, 0.1));
                    })
                    .build()
    );

    // 小型水生生物肌肉
    public static final Supplier<Item> SMALL_AQUATIC_MUSCLE = ITEM.register(
            "small_aquatic_muscle",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(NeoForgeMod.SWIM_SPEED, OrganAttributeUtil.createMultipliedBaseModifier(id, 0.05));
                    })
                    .build()
    );

    // 鱼类肌肉
    public static final Supplier<Item> FISH_MUSCLE = ITEM.register(
            "fish_muscle",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(NeoForgeMod.SWIM_SPEED, OrganAttributeUtil.createMultipliedBaseModifier(id, 0.075));
                    })
                    .build()
    );

    // 小型鱼类肌肉
    public static final Supplier<Item> SMALL_FISH_MUSCLE = ITEM.register(
            "small_fish_muscle",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(NeoForgeMod.SWIM_SPEED, OrganAttributeUtil.createMultipliedBaseModifier(id, 0.025));
                    })
                    .build()
    );

    // 盐水型心脏
    public static final Supplier<Item> SALTWATER_HEART = ITEM.register(
            "saltwater_heart",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.WATER_BREATH, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, -1));
                    })
                    .build()
    );

    // 盐水型肺脏
    public static final Supplier<Item> SALTWATER_LUNG = ITEM.register(
            "saltwater_lung",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.BREATH_RECOVERY, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.BREATH_CAPACITY, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.ENDURANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.WATER_BREATH, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, -1));
                    })
                    .build()
    );

    // 盐水型肌肉
    public static final Supplier<Item> SALTWATER_MUSCLE = ITEM.register(
            "saltwater_muscle",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(NeoForgeMod.SWIM_SPEED, OrganAttributeUtil.createMultipliedBaseModifier(id, 0.05));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, -0.5));
                    })
                    .build()
    );

    // 抗火生物心脏
    public static final Supplier<Item> FIREPROOF_HEART = ITEM.register(
            "fireproof_heart",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物肺脏
    public static final Supplier<Item> FIREPROOF_LUNG = ITEM.register(
            "fireproof_lung",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.BREATH_RECOVERY, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.BREATH_CAPACITY, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.ENDURANCE, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物脊柱
    public static final Supplier<Item> FIREPROOF_SPINE = ITEM.register(
            "fireproof_spine",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.375));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物胃
    public static final Supplier<Item> FIREPROOF_STOMACH = ITEM.register(
            "fireproof_stomach",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物肠子
    public static final Supplier<Item> FIREPROOF_INTESTINE = ITEM.register(
            "fireproof_intestine",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物肾脏
    public static final Supplier<Item> FIREPROOF_KIDNEY = ITEM.register(
            "fireproof_kidney",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.FILTRATION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物脾脏
    public static final Supplier<Item> FIREPROOF_SPLEEN = ITEM.register(
            "fireproof_spleen",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物肝脏
    public static final Supplier<Item> FIREPROOF_LIVER = ITEM.register(
            "fireproof_liver",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DETOXIFICATION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物阑尾
    public static final Supplier<Item> FIREPROOF_APPENDIX = ITEM.register(
            "fireproof_appendix",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(Attributes.LUCK, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物肋骨
    public static final Supplier<Item> FIREPROOF_RIB = ITEM.register(
            "fireproof_rib",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物肌肉
    public static final Supplier<Item> FIREPROOF_MUSCLE = ITEM.register(
            "fireproof_muscle",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影心脏
    public static final Supplier<Item> ENDER_HEART = ITEM.register(
            "ender_heart",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.ENDER, OrganAttributeUtil.createAddValueModifier(id, 2));
                        modifiers.put(InitAttribute.WATER_ALLERGY, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影肺脏
    public static final Supplier<Item> ENDER_LUNG = ITEM.register(
            "ender_lung",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.BREATH_RECOVERY, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.BREATH_CAPACITY, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.ENDURANCE, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.ENDER, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影脊柱
    public static final Supplier<Item> ENDER_SPINE = ITEM.register(
            "ender_spine",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.625));
                        modifiers.put(InitAttribute.PROJECTILE_DODGE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影胃
    public static final Supplier<Item> ENDER_STOMACH = ITEM.register(
            "ender_stomach",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.ENDER, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影肠子
    public static final Supplier<Item> ENDER_INTESTINE = ITEM.register(
            "ender_intestine",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.ENDER, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影肾脏
    public static final Supplier<Item> ENDER_KIDNEY = ITEM.register(
            "ender_kidney",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.FILTRATION, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.ENDER, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影脾脏
    public static final Supplier<Item> ENDER_SPLEEN = ITEM.register(
            "ender_spleen",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.ENDER, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影肝脏
    public static final Supplier<Item> ENDER_LIVER = ITEM.register(
            "ender_liver",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DETOXIFICATION, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.ENDER, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影阑尾
    public static final Supplier<Item> ENDER_APPENDIX = ITEM.register(
            "ender_appendix",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(Attributes.LUCK, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.ENDER, OrganAttributeUtil.createAddValueModifier(id, 8));
                    })
                    .skill(context -> {
                        if (context.entity() instanceof Player player) {
                            OrganSkillUtil.teleport(player);
                        }
                    })
                    .build()
    );

    // 末影肋骨
    public static final Supplier<Item> ENDER_RIB = ITEM.register(
            "ender_rib",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                    })
                    .build()
    );

    // 末影肌肉
    public static final Supplier<Item> ENDER_MUSCLE = ITEM.register(
            "ender_muscle",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.WATER_ALLERGY, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 力量型肌肉
    public static final Supplier<Item> BRUTE_MUSCLE = ITEM.register(
            "brute_muscle",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 速度型肌肉
    public static final Supplier<Item> SWIFT_MUSCLE = ITEM.register(
            "swift_muscle",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                    })
                    .build()
    );


    // 弹跳型肌肉
    public static final Supplier<Item> LEAPING_MUSCLE = ITEM.register(
            "leaping_muscle",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.LEAPING, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 食肉动物胃
    public static final Supplier<Item> CARNIVORE_STOMACH = ITEM.register(
            "carnivore_stomach",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.HERBIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                    })
                    .build()
    );

    // 小型食肉动物胃
    public static final Supplier<Item> SMALL_CARNIVORE_STOMACH = ITEM.register(
            "small_carnivore_stomach",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.HERBIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                    })
                    .build()
    );

    // 食肉动物肠子
    public static final Supplier<Item> CARNIVORE_INTESTINE = ITEM.register(
            "carnivore_intestine",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.HERBIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                    })
                    .build()
    );

    // 小型食肉动物肠子
    public static final Supplier<Item> SMALL_CARNIVORE_INTESTINE = ITEM.register(
            "small_carnivore_intestine",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.HERBIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                    })
                    .build()
    );

    // 食草动物胃
    public static final Supplier<Item> HERBIVORE_STOMACH = ITEM.register(
            "herbivore_stomach",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.HERBIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                    })
                    .build()
    );

    // 小型食草动物胃
    public static final Supplier<Item> SMALL_HERBIVORE_STOMACH = ITEM.register(
            "small_herbivore_stomach",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.HERBIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 食草动物肠子
    public static final Supplier<Item> HERBIVORE_INTESTINE = ITEM.register(
            "herbivore_intestine",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.HERBIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                    })
                    .build()
    );

    // 小型食草动物肠子
    public static final Supplier<Item> SMALL_HERBIVORE_INTESTINE = ITEM.register(
            "small_herbivore_intestine",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.HERBIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 食草动物瘤胃
    public static final Supplier<Item> HERBIVORE_RUMEN = ITEM.register(
            "herbivore_rumen",
            () -> OrganFactory.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, -0.5));
                        modifiers.put(InitAttribute.HERBIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .skill(context -> {
                        if (context.entity() instanceof Player player) {
                            OrganSkillUtil.graze(player);
                        }
                    })
                    .build()
    );
}
