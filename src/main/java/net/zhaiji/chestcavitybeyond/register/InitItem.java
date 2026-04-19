package net.zhaiji.chestcavitybeyond.register;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.capability.Organ;
import net.zhaiji.chestcavitybeyond.item.ChestOpenerItem;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.OrganSkillUtil;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;

import java.util.List;
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
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 1)
            .build()
    );

    // 肺脏
    public static final Supplier<Item> LUNG = ITEM.register(
        "lung",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.BREATH_RECOVERY, 1)
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 1)
            .addValueAttribute(InitAttribute.ENDURANCE, 1)
            .build()
    );

    // 脊柱
    public static final Supplier<Item> SPINE = ITEM.register(
        "spine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 1)
            .addValueAttribute(InitAttribute.DEFENSE, 0.5)
            .build()
    );

    // 胃
    public static final Supplier<Item> STOMACH = ITEM.register(
        "stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DIGESTION, 1)
            .build()
    );

    // 肠子
    public static final Supplier<Item> INTESTINE = ITEM.register(
        "intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NUTRITION, 1)
            .build()
    );

    // 肾脏
    public static final Supplier<Item> KIDNEY = ITEM.register(
        "kidney",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.FILTRATION, 1)
            .build()
    );

    // 脾脏
    public static final Supplier<Item> SPLEEN = ITEM.register(
        "spleen",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.METABOLISM, 1)
            .build()
    );

    // 肝脏
    public static final Supplier<Item> LIVER = ITEM.register(
        "liver",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DETOXIFICATION, 1)
            .build()
    );

    // 阑尾
    public static final Supplier<Item> APPENDIX = ITEM.register(
        "appendix",
        () -> Organ.builder()
            .addValueAttribute(Attributes.LUCK, 1)
            .build()
    );

    // 肋骨
    public static final Supplier<Item> RIB = ITEM.register(
        "rib",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DEFENSE, 1)
            .build()
    );

    // 肌肉
    public static final Supplier<Item> MUSCLE = ITEM.register(
        "muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 1)
            .addValueAttribute(InitAttribute.SPEED, 1)
            .build()
    );

    // 动物心脏
    public static final Supplier<Item> ANIMAL_HEART = ITEM.register(
        "animal_heart",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 0.75)
            .build()
    );

    // 动物肺脏
    public static final Supplier<Item> ANIMAL_LUNG = ITEM.register(
        "animal_lung",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.BREATH_RECOVERY, 0.75)
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 0.75)
            .addValueAttribute(InitAttribute.ENDURANCE, 0.75)
            .build()
    );

    // 动物脊柱
    public static final Supplier<Item> ANIMAL_SPINE = ITEM.register(
        "animal_spine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 0.75)
            .addValueAttribute(InitAttribute.DEFENSE, 0.375)
            .build()
    );

    // 动物胃
    public static final Supplier<Item> ANIMAL_STOMACH = ITEM.register(
        "animal_stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DIGESTION, 0.75)
            .build()
    );

    // 动物肠子
    public static final Supplier<Item> ANIMAL_INTESTINE = ITEM.register(
        "animal_intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NUTRITION, 0.75)
            .build()
    );

    // 动物肾脏
    public static final Supplier<Item> ANIMAL_KIDNEY = ITEM.register(
        "animal_kidney",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.FILTRATION, 0.75)
            .build()
    );

    // 动物脾脏
    public static final Supplier<Item> ANIMAL_SPLEEN = ITEM.register(
        "animal_spleen",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.METABOLISM, 0.75)
            .build()
    );

    // 动物肝脏
    public static final Supplier<Item> ANIMAL_LIVER = ITEM.register(
        "animal_liver",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DETOXIFICATION, 0.75)
            .build()
    );

    // 动物阑尾
    public static final Supplier<Item> ANIMAL_APPENDIX = ITEM.register(
        "animal_appendix",
        () -> Organ.builder()
            .addValueAttribute(Attributes.LUCK, 0.75)
            .build()
    );

    // 动物肋骨
    public static final Supplier<Item> ANIMAL_RIB = ITEM.register(
        "animal_rib",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DEFENSE, 0.75)
            .build()
    );

    // 动物肌肉
    public static final Supplier<Item> ANIMAL_MUSCLE = ITEM.register(
        "animal_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 0.75)
            .addValueAttribute(InitAttribute.SPEED, 0.75)
            .build()
    );

    // 小型动物心脏
    public static final Supplier<Item> SMALL_ANIMAL_HEART = ITEM.register(
        "small_animal_heart",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 0.5)
            .build()
    );

    // 小型动物肺脏
    public static final Supplier<Item> SMALL_ANIMAL_LUNG = ITEM.register(
        "small_animal_lung",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.BREATH_RECOVERY, 0.5)
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 0.5)
            .addValueAttribute(InitAttribute.ENDURANCE, 0.5)
            .build()
    );

    // 小型动物脊柱
    public static final Supplier<Item> SMALL_ANIMAL_SPINE = ITEM.register(
        "small_animal_spine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 0.5)
            .addValueAttribute(InitAttribute.DEFENSE, 0.375)
            .build()
    );

    // 小型动物胃
    public static final Supplier<Item> SMALL_ANIMAL_STOMACH = ITEM.register(
        "small_animal_stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DIGESTION, 0.5)
            .build()
    );

    // 小型动物肠子
    public static final Supplier<Item> SMALL_ANIMAL_INTESTINE = ITEM.register(
        "small_animal_intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NUTRITION, 0.5)
            .build()
    );

    // 小型动物肾脏
    public static final Supplier<Item> SMALL_ANIMAL_KIDNEY = ITEM.register(
        "small_animal_kidney",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.FILTRATION, 0.5)
            .build()
    );

    // 小型动物脾脏
    public static final Supplier<Item> SMALL_ANIMAL_SPLEEN = ITEM.register(
        "small_animal_spleen",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.METABOLISM, 0.5)
            .build()
    );

    // 小型动物肝脏
    public static final Supplier<Item> SMALL_ANIMAL_LIVER = ITEM.register(
        "small_animal_liver",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DETOXIFICATION, 0.5)
            .build()
    );

    // 小型动物阑尾
    public static final Supplier<Item> SMALL_ANIMAL_APPENDIX = ITEM.register(
        "small_animal_appendix",
        () -> Organ.builder()
            .addValueAttribute(Attributes.LUCK, 0.5)
            .build()
    );

    // 小型动物肋骨
    public static final Supplier<Item> SMALL_ANIMAL_RIB = ITEM.register(
        "small_animal_rib",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DEFENSE, 0.5)
            .build()
    );

    // 小型动物肌肉
    public static final Supplier<Item> SMALL_ANIMAL_MUSCLE = ITEM.register(
        "small_animal_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 0.5)
            .addValueAttribute(InitAttribute.SPEED, 0.5)
            .build()
    );

    // 鳃
    public static final Supplier<Item> GILL = ITEM.register(
        "gill",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.WATER_BREATH, 1)
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 1)
            .addValueAttribute(InitAttribute.ENDURANCE, 1)
            .build()
    );

    // 小型鳃
    public static final Supplier<Item> SMALL_GILL = ITEM.register(
        "small_gill",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.WATER_BREATH, 0.5)
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 0.5)
            .addValueAttribute(InitAttribute.ENDURANCE, 0.5)
            .build()
    );

    // 水生生物肌肉
    public static final Supplier<Item> AQUATIC_MUSCLE = ITEM.register(
        "aquatic_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 1)
            .addValueAttribute(InitAttribute.SPEED, 0.5)
            .baseMultipliedAttribute(NeoForgeMod.SWIM_SPEED, 0.1)
            .build()
    );

    // 小型水生生物肌肉
    public static final Supplier<Item> SMALL_AQUATIC_MUSCLE = ITEM.register(
        "small_aquatic_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 0.5)
            .addValueAttribute(InitAttribute.SPEED, 0.25)
            .baseMultipliedAttribute(NeoForgeMod.SWIM_SPEED, 0.05)
            .build()
    );

    // 鱼类脊柱
    public static final Supplier<Item> FISH_SPINE = ITEM.register(
        "fish_spine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 1)
            .build()
    );

    // 小型鱼类脊柱
    public static final Supplier<Item> SMALL_FISH_SPINE = ITEM.register(
        "small_fish_spine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 0.5)
            .build()
    );

    // 鱼骨
    public static final Supplier<Item> FISH_BONE = ITEM.register(
        "fish_bone",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DEFENSE, 0.5)
            .build()
    );

    // 细小鱼骨
    public static final Supplier<Item> SMALL_FISH_BONE = ITEM.register(
        "small_fish_bone",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 0.25)
            .build()
    );

    // 鱼肠
    public static final Supplier<Item> FISH_INTESTINE = ITEM.register(
        "fish_intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NUTRITION, 1.25)
            .build()
    );

    // 小型鱼骨
    public static final Supplier<Item> SMALL_FISH_INTESTINE = ITEM.register(
        "small_fish_intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NUTRITION, 1)
            .build()
    );

    // 鱼类肌肉
    public static final Supplier<Item> FISH_MUSCLE = ITEM.register(
        "fish_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 0.75)
            .addValueAttribute(InitAttribute.SPEED, 0.25)
            .baseMultipliedAttribute(NeoForgeMod.SWIM_SPEED, 0.075)
            .build()
    );

    // 小型鱼类肌肉
    public static final Supplier<Item> SMALL_FISH_MUSCLE = ITEM.register(
        "small_fish_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 0.25)
            .addValueAttribute(InitAttribute.SPEED, 0.25)
            .baseMultipliedAttribute(NeoForgeMod.SWIM_SPEED, 0.025)
            .build()
    );

    // 盐水型心脏
    public static final Supplier<Item> SALTWATER_HEART = ITEM.register(
        "saltwater_heart",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 1)
            .addValueAttribute(InitAttribute.WATER_BREATH, 0.5)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, -1)
            .build()
    );

    // 盐水型肺脏
    public static final Supplier<Item> SALTWATER_LUNG = ITEM.register(
        "saltwater_lung",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.BREATH_RECOVERY, 1)
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 1)
            .addValueAttribute(InitAttribute.ENDURANCE, 1)
            .addValueAttribute(InitAttribute.WATER_BREATH, 0.5)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, -1)
            .build()
    );

    // 盐水型肌肉
    public static final Supplier<Item> SALTWATER_MUSCLE = ITEM.register(
        "saltwater_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 1)
            .addValueAttribute(InitAttribute.SPEED, 1)
            .baseMultipliedAttribute(NeoForgeMod.SWIM_SPEED, 0.05)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, -0.5)
            .build()
    );

    // 抗火生物心脏
    public static final Supplier<Item> FIREPROOF_HEART = ITEM.register(
        "fireproof_heart",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 0.75)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 1)
            .build()
    );

    // 抗火生物肺脏
    public static final Supplier<Item> FIREPROOF_LUNG = ITEM.register(
        "fireproof_lung",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.BREATH_RECOVERY, 0.75)
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 0.75)
            .addValueAttribute(InitAttribute.ENDURANCE, 0.75)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 1)
            .build()
    );

    // 抗火生物脊柱
    public static final Supplier<Item> FIREPROOF_SPINE = ITEM.register(
        "fireproof_spine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 0.75)
            .addValueAttribute(InitAttribute.DEFENSE, 0.375)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 1)
            .build()
    );

    // 抗火生物胃
    public static final Supplier<Item> FIREPROOF_STOMACH = ITEM.register(
        "fireproof_stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DIGESTION, 0.75)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 1)
            .build()
    );

    // 抗火生物肠子
    public static final Supplier<Item> FIREPROOF_INTESTINE = ITEM.register(
        "fireproof_intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NUTRITION, 0.75)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 1)
            .build()
    );

    // 抗火生物肾脏
    public static final Supplier<Item> FIREPROOF_KIDNEY = ITEM.register(
        "fireproof_kidney",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.FILTRATION, 0.75)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 1)
            .build()
    );

    // 抗火生物脾脏
    public static final Supplier<Item> FIREPROOF_SPLEEN = ITEM.register(
        "fireproof_spleen",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.METABOLISM, 0.75)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 1)
            .build()
    );

    // 抗火生物肝脏
    public static final Supplier<Item> FIREPROOF_LIVER = ITEM.register(
        "fireproof_liver",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DETOXIFICATION, 0.75)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 1)
            .build()
    );

    // 抗火生物阑尾
    public static final Supplier<Item> FIREPROOF_APPENDIX = ITEM.register(
        "fireproof_appendix",
        () -> Organ.builder()
            .addValueAttribute(Attributes.LUCK, 0.75)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 1)
            .build()
    );

    // 抗火生物肋骨
    public static final Supplier<Item> FIREPROOF_RIB = ITEM.register(
        "fireproof_rib",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DEFENSE, 0.75)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 1)
            .build()
    );

    // 抗火生物肌肉
    public static final Supplier<Item> FIREPROOF_MUSCLE = ITEM.register(
        "fireproof_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 0.75)
            .addValueAttribute(InitAttribute.SPEED, 0.75)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 1)
            .build()
    );

    // 末影心脏
    public static final Supplier<Item> ENDER_HEART = ITEM.register(
        "ender_heart",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 1.25)
            .addValueAttribute(InitAttribute.ENDER, 2)
            .addValueAttribute(InitAttribute.WATER_ALLERGY, 1)
            .build()
    );

    // 末影肺脏
    public static final Supplier<Item> ENDER_LUNG = ITEM.register(
        "ender_lung",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.BREATH_RECOVERY, 1.25)
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 1.25)
            .addValueAttribute(InitAttribute.ENDURANCE, 1.25)
            .addValueAttribute(InitAttribute.ENDER, 1)
            .build()
    );

    // 末影脊柱
    public static final Supplier<Item> ENDER_SPINE = ITEM.register(
        "ender_spine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 1.25)
            .addValueAttribute(InitAttribute.DEFENSE, 0.625)
            .addValueAttribute(InitAttribute.PROJECTILE_DODGE, 1)
            .addValueAttribute(InitAttribute.WATER_ALLERGY, 1)
            .build()
    );

    // 末影胃
    public static final Supplier<Item> ENDER_STOMACH = ITEM.register(
        "ender_stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DIGESTION, 1.25)
            .addValueAttribute(InitAttribute.ENDER, 1)
            .build()
    );

    // 末影肠子
    public static final Supplier<Item> ENDER_INTESTINE = ITEM.register(
        "ender_intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NUTRITION, 1.25)
            .addValueAttribute(InitAttribute.ENDER, 1)
            .build()
    );

    // 末影肾脏
    public static final Supplier<Item> ENDER_KIDNEY = ITEM.register(
        "ender_kidney",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.FILTRATION, 1)
            .addValueAttribute(InitAttribute.ENDER, 1)
            .build()
    );

    // 末影脾脏
    public static final Supplier<Item> ENDER_SPLEEN = ITEM.register(
        "ender_spleen",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.METABOLISM, 1.25)
            .addValueAttribute(InitAttribute.ENDER, 1)
            .build()
    );

    // 末影肝脏
    public static final Supplier<Item> ENDER_LIVER = ITEM.register(
        "ender_liver",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DETOXIFICATION, 1)
            .addValueAttribute(InitAttribute.ENDER, 1)
            .build()
    );

    // 末影阑尾
    public static final Supplier<Item> ENDER_APPENDIX = ITEM.register(
        "ender_appendix",
        () -> Organ.builder()
            .addValueAttribute(Attributes.LUCK, 1.25)
            .addValueAttribute(InitAttribute.ENDER, 8)
            .cooldown(8 * 20)
            .skill(context -> {
                OrganSkillUtil.teleport(context.entity(), context.data().getCurrentValue(InitAttribute.ENDER));
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 末影肋骨
    public static final Supplier<Item> ENDER_RIB = ITEM.register(
        "ender_rib",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DEFENSE, 1.25)
            .build()
    );

    // 末影肌肉
    public static final Supplier<Item> ENDER_MUSCLE = ITEM.register(
        "ender_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 1.25)
            .addValueAttribute(InitAttribute.SPEED, 1.25)
            .addValueAttribute(InitAttribute.WATER_ALLERGY, 1)
            .build()
    );

    // 力量型肌肉
    public static final Supplier<Item> BRUTE_MUSCLE = ITEM.register(
        "brute_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 1.25)
            .addValueAttribute(InitAttribute.SPEED, 0.75)
            .build()
    );

    // 速度型肌肉
    public static final Supplier<Item> SWIFT_MUSCLE = ITEM.register(
        "swift_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 0.75)
            .addValueAttribute(InitAttribute.SPEED, 1.25)
            .build()
    );


    // 弹跳型肌肉
    public static final Supplier<Item> LEAPING_MUSCLE = ITEM.register(
        "leaping_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 0.75)
            .addValueAttribute(InitAttribute.SPEED, 0.75)
            .addValueAttribute(InitAttribute.LEAPING, 1)
            .build()
    );

    // 小型弹跳型肌肉
    public static final Supplier<Item> SMALL_LEAPING_MUSCLE = ITEM.register(
        "small_leaping_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 0.5)
            .addValueAttribute(InitAttribute.SPEED, 0.5)
            .addValueAttribute(InitAttribute.LEAPING, 0.75)
            .build()
    );

    // 食肉动物胃
    public static final Supplier<Item> CARNIVORE_STOMACH = ITEM.register(
        "carnivore_stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.CARNIVOROUS_DIGESTION, 1.25)
            .addValueAttribute(InitAttribute.HERBIVOROUS_DIGESTION, 0.25)
            .build()
    );

    // 小型食肉动物胃
    public static final Supplier<Item> SMALL_CARNIVORE_STOMACH = ITEM.register(
        "small_carnivore_stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.CARNIVOROUS_DIGESTION, 0.75)
            .addValueAttribute(InitAttribute.HERBIVOROUS_DIGESTION, 0.25)
            .build()
    );

    // 食肉动物肠子
    public static final Supplier<Item> CARNIVORE_INTESTINE = ITEM.register(
        "carnivore_intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.CARNIVOROUS_NUTRITION, 1.25)
            .addValueAttribute(InitAttribute.HERBIVOROUS_NUTRITION, 0.25)
            .build()
    );

    // 小型食肉动物肠子
    public static final Supplier<Item> SMALL_CARNIVORE_INTESTINE = ITEM.register(
        "small_carnivore_intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.CARNIVOROUS_NUTRITION, 0.75)
            .addValueAttribute(InitAttribute.HERBIVOROUS_NUTRITION, 0.25)
            .build()
    );

    // 食草动物胃
    public static final Supplier<Item> HERBIVORE_STOMACH = ITEM.register(
        "herbivore_stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.CARNIVOROUS_DIGESTION, 0.25)
            .addValueAttribute(InitAttribute.HERBIVOROUS_DIGESTION, 1.25)
            .build()
    );

    // 小型食草动物胃
    public static final Supplier<Item> SMALL_HERBIVORE_STOMACH = ITEM.register(
        "small_herbivore_stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.CARNIVOROUS_DIGESTION, 0.25)
            .addValueAttribute(InitAttribute.HERBIVOROUS_DIGESTION, 0.75)
            .build()
    );

    // 食草动物肠子
    public static final Supplier<Item> HERBIVORE_INTESTINE = ITEM.register(
        "herbivore_intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.CARNIVOROUS_NUTRITION, 0.25)
            .addValueAttribute(InitAttribute.HERBIVOROUS_NUTRITION, 1.25)
            .build()
    );

    // 小型食草动物肠子
    public static final Supplier<Item> SMALL_HERBIVORE_INTESTINE = ITEM.register(
        "small_herbivore_intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.CARNIVOROUS_NUTRITION, 0.25)
            .addValueAttribute(InitAttribute.HERBIVOROUS_NUTRITION, 0.75)
            .build()
    );

    // 食草动物瘤胃
    public static final Supplier<Item> HERBIVORE_RUMEN = ITEM.register(
        "herbivore_rumen",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.CARNIVOROUS_DIGESTION, -0.5)
            .addValueAttribute(InitAttribute.HERBIVOROUS_DIGESTION, 1)
            .cooldown(2 * 20)
            .skill(context -> {
                if (context.entity() instanceof Player player) {
                    OrganSkillUtil.graze(player);
                }
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 苦力怕阑尾
    public static final Supplier<Item> CREEPER_APPENDIX = ITEM.register(
        "creeper_appendix",
        () -> Organ.builder()
            .addValueAttribute(Attributes.LUCK, 0.75)
            .addValueAttribute(InitAttribute.EXPLOSIVE, 1)
            .cooldown(20 * 20)
            .skill(context -> {
                OrganSkillUtil.explosion(context.entity(), context.data().getCurrentValue(InitAttribute.EXPLOSIVE));
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 苦力怕叶
    public static final Supplier<Item> CREEPER_LEAF = ITEM.register(
        "creeper_leaf",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.SPEED, 1)
            .addValueAttribute(InitAttribute.PHOTOSYNTHESIS, 1)
            .build()
    );

    // 腐烂心脏
    public static final Supplier<Item> ROTTEN_HEART = ITEM.register(
        "rotten_heart",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 0.5)
            .build()
    );

    // 腐烂肺脏
    public static final Supplier<Item> ROTTEN_LUNG = ITEM.register(
        "rotten_lung",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.BREATH_RECOVERY, 0.5)
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 0.5)
            .addValueAttribute(InitAttribute.ENDURANCE, 0.5)
            .build()
    );

    // 腐烂脊柱
    public static final Supplier<Item> ROTTEN_SPINE = ITEM.register(
        "rotten_spine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 0.5)
            .addValueAttribute(InitAttribute.DEFENSE, 0.25)
            .build()
    );

    // 腐烂胃
    public static final Supplier<Item> ROTTEN_STOMACH = ITEM.register(
        "rotten_stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DIGESTION, 0.5)
            .addValueAttribute(InitAttribute.SCAVENGER_DIGESTION, 1)
            .build()
    );

    // 腐烂肠子
    public static final Supplier<Item> ROTTEN_INTESTINE = ITEM.register(
        "rotten_intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NUTRITION, 0.5)
            .addValueAttribute(InitAttribute.SCAVENGER_NUTRITION, 1)
            .build()
    );

    // 腐烂肾脏
    public static final Supplier<Item> ROTTEN_KIDNEY = ITEM.register(
        "rotten_kidney",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.FILTRATION, 0.5)
            .build()
    );

    // 腐烂脾脏
    public static final Supplier<Item> ROTTEN_SPLEEN = ITEM.register(
        "rotten_spleen",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.METABOLISM, 0.5)
            .build()
    );

    // 腐烂肝脏
    public static final Supplier<Item> ROTTEN_LIVER = ITEM.register(
        "rotten_liver",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DETOXIFICATION, 0.5)
            .build()
    );

    // 腐烂阑尾
    public static final Supplier<Item> ROTTEN_APPENDIX = ITEM.register(
        "rotten_appendix",
        () -> Organ.builder()
            .addValueAttribute(Attributes.LUCK, 0.5)
            .build()
    );

    // 腐烂肋骨
    public static final Supplier<Item> ROTTEN_RIB = ITEM.register(
        "rotten_rib",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DEFENSE, 0.5)
            .build()
    );

    // 腐烂肌肉
    public static final Supplier<Item> ROTTEN_MUSCLE = ITEM.register(
        "rotten_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 0.5)
            .addValueAttribute(InitAttribute.SPEED, 0.5)
            .build()
    );

    // 凋零脊柱
    public static final Supplier<Item> WITHERED_SPINE = ITEM.register(
        "withered_spine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 0.5)
            .addValueAttribute(InitAttribute.DEFENSE, 0.25)
            .addValueAttribute(InitAttribute.WITHERED, 1)
            .build()
    );

    // 凋零肋骨
    public static final Supplier<Item> WITHERED_RIB = ITEM.register(
        "withered_rib",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DEFENSE, 0.5)
            .addValueAttribute(InitAttribute.WITHERED, 1)
            .build()
    );

    // 扭曲灵魂沙
    public static final Supplier<Item> WRITHING_SOUL_SAND = ITEM.register(
        "writhing_soul_sand",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 3)
            .addValueAttribute(InitAttribute.SPEED, 1)
            .addValueAttribute(InitAttribute.WITHERED, 1)
            .build()
    );

    // 傀儡核心
    public static final Supplier<Item> GOLEM_CORE = ITEM.register(
        "golem_core",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 3)
            .addValueAttribute(Attributes.KNOCKBACK_RESISTANCE, 1)
            .addValueAttribute(InitAttribute.SPEED, -2)
            .build()
    );

    // 傀儡电缆
    public static final Supplier<Item> GOLEM_CABLE = ITEM.register(
        "golem_cable",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 0.5)
            .addValueAttribute(InitAttribute.DEFENSE, 1)
            .addValueAttribute(Attributes.KNOCKBACK_RESISTANCE, 1)
            .build()
    );

    // 熔炉内核
    public static final Supplier<Item> INNER_FURNACE = ITEM.register(
        "inner_furnace",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.METABOLISM, 0.25)
            .addValueAttribute(InitAttribute.DEFENSE, 0.25)
            .addValueAttribute(InitAttribute.FURNACE_POWER, 1)
            .cooldown(20)
            .skill(context -> {
                if (context.entity() instanceof Player player) {
                    OrganSkillUtil.furnacePower(player, context.data().getCurrentValue(InitAttribute.FURNACE_POWER));
                }
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 活塞型肌肉
    public static final Supplier<Item> PISTON_MUSCLE = ITEM.register(
        "piston_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 2)
            .addValueAttribute(InitAttribute.SPEED, 0.5)
            .addValueAttribute(InitAttribute.LAUNCH, 1)
            .build()
    );

    // 傀儡装甲板
    public static final Supplier<Item> GOLEM_ARMOR_PLATE = ITEM.register(
        "golem_armor_plate",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DEFENSE, 1.25)
            .addValueAttribute(InitAttribute.IRON_REPAIR, 1)
            .addValueAttribute(InitAttribute.METABOLISM, -0.5)
            .cooldown(20)
            .skill(context -> {
                if (context.entity() instanceof Player player) {
                    OrganSkillUtil.ironRepair(player, context.data().getCurrentValue(InitAttribute.IRON_REPAIR));
                }
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 丝腺
    public static final Supplier<Item> SILK_GLAND = ITEM.register(
        "silk_gland",
        () -> Organ.builder()
            .cooldown(4 * 20)
            .skill(context -> {
                OrganSkillUtil.silk(context.entity());
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 毒腺
    public static final Supplier<Item> VENOM_GLAND = ITEM.register(
        "venom_gland",
        () -> Organ.builder(
                new Item(
                    new Item.Properties()
                        .stacksTo(1)
                        .component(
                            DataComponents.POTION_CONTENTS,
                            ChestCavityUtil.calculatePotionContents(Potions.POISON.value().getEffects())
                        )
                ) {
                    @Override
                    public void appendHoverText(
                        ItemStack stack,
                        TooltipContext context,
                        List<Component> tooltipComponents,
                        TooltipFlag tooltipFlag
                    ) {
                        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                        PotionContents potioncontents = stack.get(DataComponents.POTION_CONTENTS);
                        if (potioncontents != null) {
                            potioncontents.addPotionTooltip(tooltipComponents::add, 1.0F, context.tickRate());
                        }
                    }

                    @Override
                    public ItemStack getDefaultInstance() {
                        return ChestCavityUtil.attachPotionContents(super.getDefaultInstance(), Potions.POISON.value().getEffects());
                    }
                }
            )
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .attack((context, target, source, damageContainer) -> {
                if (OrganSkillUtil.hasCooldown(context.entity(), context.stack())) return;
                OrganSkillUtil.addCooldown(context.entity(), context.stack(), 4 * 20);
                context.stack()
                    .getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                    .forEachEffect(target::addEffect);
            })
            .build()
    );

    // 节肢生物心脏
    public static final Supplier<Item> ARTHROPOD_HEART = ITEM.register(
        "arthropod_heart",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 0.5)
            .addValueAttribute(InitAttribute.FILTRATION, 0.5)
            .build()
    );

    // 节肢生物肠子
    public static final Supplier<Item> ARTHROPOD_INTESTINE = ITEM.register(
        "arthropod_intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NUTRITION, 0.5)
            .addValueAttribute(InitAttribute.DIGESTION, 0.25)
            .addValueAttribute(InitAttribute.DETOXIFICATION, 0.25)
            .build()
    );

    // 节肢生物肺脏
    public static final Supplier<Item> ARTHROPOD_LUNG = ITEM.register(
        "arthropod_lung",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.BREATH_RECOVERY, 0.75)
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 0.75)
            .addValueAttribute(InitAttribute.ENDURANCE, 0.75)
            .addValueAttribute(InitAttribute.METABOLISM, 0.25)
            .build()
    );

    // 节肢生物肌肉
    public static final Supplier<Item> ARTHROPOD_MUSCLE = ITEM.register(
        "arthropod_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 0.5)
            .addValueAttribute(InitAttribute.SPEED, 1.25)
            .build()
    );

    // 节肢生物胃
    public static final Supplier<Item> ARTHROPOD_STOMACH = ITEM.register(
        "arthropod_stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DIGESTION, 0.5)
            .addValueAttribute(InitAttribute.NUTRITION, 0.25)
            .addValueAttribute(InitAttribute.METABOLISM, 0.25)
            .build()
    );

    // 节肢生物盲囊
    public static final Supplier<Item> ARTHROPOD_CAECUM = ITEM.register(
        "arthropod_caecum",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NUTRITION, 0.5)
            .addValueAttribute(InitAttribute.DIGESTION, 0.25)
            .addValueAttribute(InitAttribute.NERVES, 0.5)
            .build()
    );

    // 羊驼肺脏
    public static final Supplier<Item> LLAMA_LUNG = ITEM.register(
        "llama_lung",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.BREATH_RECOVERY, 0.75)
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 0.75)
            .addValueAttribute(InitAttribute.ENDURANCE, 0.75)
            .skill(context -> {
                OrganSkillUtil.spit(context.entity());
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 烈焰核心
    public static final Supplier<Item> BLAZE_CORE = ITEM.register(
        "blaze_core",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 1)
            .addValueAttribute(InitAttribute.NERVES, 0.25)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 3)
            .addValueAttribute(InitAttribute.WATER_ALLERGY, 3)
            .build()
    );

    // 烈焰外壳
    public static final Supplier<Item> BLAZE_SHELL = ITEM.register(
        "blaze_shell",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DEFENSE, 1)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 3)
            .addValueAttribute(InitAttribute.WATER_ALLERGY, 3)
            .build()
    );

    // 活性烈焰棒
    public static final Supplier<Item> ACTIVE_BLAZE_ROD = ITEM.register(
        "active_blaze_rod",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.VOMIT_FIREBALL, 1)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 3)
            .addValueAttribute(InitAttribute.WATER_ALLERGY, 3)
            .cooldown(15 * 20)
            .skill(context -> {
                OrganSkillUtil.smallFireball(
                    context.data(),
                    context.entity(),
                    context.data().getCurrentValue(InitAttribute.VOMIT_FIREBALL)
                );
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 雪之核心
    public static final Supplier<Item> SNOW_CORE = ITEM.register(
        "snow_core",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 0.25)
            .addValueAttribute(InitAttribute.WATER_ALLERGY, 1)
            .skill(context -> {
                OrganSkillUtil.snowball(context.entity());
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 恶魂胃
    public static final Supplier<Item> GHAST_STOMACH = ITEM.register(
        "ghast_stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DIGESTION, 0.5)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 1)
            .addValueAttribute(InitAttribute.GHASTLY, 1)
            .cooldown(15 * 20)
            .skill(context -> {
                OrganSkillUtil.largeFireball(context.entity(), context.data().getCurrentValue(InitAttribute.GHASTLY));
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 气囊
    public static final Supplier<Item> GAS_SAC = ITEM.register(
        "gas_sac",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 1.5)
            .addValueAttribute(InitAttribute.BREATH_RECOVERY, 0.25)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 1)
            .baseMultipliedAttribute(Attributes.GRAVITY, -0.25)
            .build()
    );

    // 潜影贝脾脏
    public static final Supplier<Item> SHULKER_SPLEEN = ITEM.register(
        "shulker_spleen",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.METABOLISM, 0.75)
            .cooldown(5 * 20)
            .skill(context -> {
                OrganSkillUtil.shulkerBullet(context.entity());
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 旋风核心
    public static final Supplier<Item> BREEZE_CORE = ITEM.register(
        "breeze_core",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 0.75)
            .cooldown(8 * 20)
            .skill(context -> {
                if (context.entity() instanceof Player player) {
                    OrganSkillUtil.windCharge(player);
                }
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 活性旋风棒
    public static final Supplier<Item> ACTIVE_BREEZE_ROD = ITEM.register(
        "active_breeze_rod",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.SPEED, 1)
            .addValueAttribute(InitAttribute.LEAPING, 1)
            .build()
    );

    // 龙之心脏
    public static final Supplier<Item> DRAGON_HEART = ITEM.register(
        "dragon_heart",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 5)
            .build()
    );

    // 龙之肺脏
    public static final Supplier<Item> DRAGON_LUNG = ITEM.register(
        "dragon_lung",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.BREATH_RECOVERY, 5)
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 5)
            .addValueAttribute(InitAttribute.ENDURANCE, 5)
            .cooldown(60 * 20)
            .skill(context -> {
                OrganSkillUtil.dragonFireball(context.entity());
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 龙之脊柱
    public static final Supplier<Item> DRAGON_SPINE = ITEM.register(
        "dragon_spine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 5)
            .addValueAttribute(InitAttribute.DEFENSE, 5)
            .addValueAttribute(Attributes.KNOCKBACK_RESISTANCE, 1)
            .build()
    );

    // 龙之肾脏
    public static final Supplier<Item> DRAGON_KIDNEY = ITEM.register(
        "dragon_kidney",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.FILTRATION, 5)
            .build()
    );

    // 龙之脾脏
    public static final Supplier<Item> DRAGON_SPLEEN = ITEM.register(
        "dragon_spleen",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.METABOLISM, 5)
            .build()
    );

    // 龙之肝脏
    public static final Supplier<Item> DRAGON_LIVER = ITEM.register(
        "dragon_liver",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DETOXIFICATION, 5)
            .build()
    );

    // 龙之阑尾
    public static final Supplier<Item> DRAGON_APPENDIX = ITEM.register(
        "dragon_appendix",
        () -> Organ.builder()
            .addValueAttribute(Attributes.LUCK, 5)
            .build()
    );

    // 龙之肋骨
    public static final Supplier<Item> DRAGON_RIB = ITEM.register(
        "dragon_rib",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DEFENSE, 5)
            .addValueAttribute(Attributes.KNOCKBACK_RESISTANCE, 1)
            .build()
    );

    // 龙之肌肉
    public static final Supplier<Item> DRAGON_MUSCLE = ITEM.register(
        "dragon_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 5)
            .addValueAttribute(InitAttribute.SPEED, 5)
            .build()
    );

    // 魔力反应装置
    public static final Supplier<Item> MANA_REACTOR = ITEM.register(
        "mana_reactor",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.CRYSTALLIZATION, 1)
            .build()
    );

    // 史莱姆核心
    public static final Supplier<Item> SLIME_CORE = ITEM.register(
        "slime_core",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 0.5)
            .addValueAttribute(InitAttribute.DIGESTION, 1)
            .addValueAttribute(InitAttribute.SCAVENGER_DIGESTION, 1)
            .addValueAttribute(InitAttribute.NUTRITION, 1)
            .addValueAttribute(InitAttribute.SCAVENGER_NUTRITION, 1)
            .build()
    );

    // 粘液胃
    public static final Supplier<Item> SLIME_STOMACH = ITEM.register(
        "slime_stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DIGESTION, 2)
            .addValueAttribute(InitAttribute.SCAVENGER_DIGESTION, 2)
            .addValueAttribute(InitAttribute.NUTRITION, 4)
            .addValueAttribute(InitAttribute.SCAVENGER_NUTRITION, 4)
            .build()
    );

    // 岩浆怪核心
    public static final Supplier<Item> MAGMA_CUBE_CORE = ITEM.register(
        "magma_cube_core",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 0.5)
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 2)
            .addValueAttribute(InitAttribute.DIGESTION, 1)
            .addValueAttribute(InitAttribute.NUTRITION, 1)
            .build()
    );

    // 熔岩胃
    public static final Supplier<Item> MAGMA_STOMACH = ITEM.register(
        "magma_stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.FIRE_RESISTANCE, 4)
            .addValueAttribute(InitAttribute.DIGESTION, 3)
            .addValueAttribute(InitAttribute.NUTRITION, 3)
            .build()
    );

    // 幽匿心脏
    public static final Supplier<Item> SCULK_HEART = ITEM.register(
        "sculk_heart",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 3)
            .build()
    );

    // 幽匿脊柱
    public static final Supplier<Item> SCULK_SPINE = ITEM.register(
        "sculk_spine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 2)
            .build()
    );

    // 幽匿肋骨
    public static final Supplier<Item> SCULK_RIB = ITEM.register(
        "sculk_rib",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DEFENSE, 2)
            .build()
    );

    // 幽匿肌肉
    public static final Supplier<Item> SCULK_MUSCLE = ITEM.register(
        "sculk_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 4)
            .addValueAttribute(InitAttribute.SPEED, 1)
            .build()
    );

    // 幽匿核心
    public static final Supplier<Item> SCULK_CORE = ITEM.register(
        "sculk_core",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 5)
            .addValueAttribute(Attributes.KNOCKBACK_RESISTANCE, 1)
            .cooldown(60 * 20)
            .skill(context -> {
                OrganSkillUtil.sonicBoom(context.entity());
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 远古心脏
    public static final Supplier<Item> ELDER_HEART = ITEM.register(
        "elder_heart",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.HEALTH, 2)
            .build()
    );

    // 远古肺脏
    public static final Supplier<Item> ELDER_LUNG = ITEM.register(
        "elder_lung",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.BREATH_RECOVERY, 2)
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 2)
            .addValueAttribute(InitAttribute.ENDURANCE, 2)
            .build()
    );

    // 远古鳃
    public static final Supplier<Item> ELDER_GILL = ITEM.register(
        "elder_gill",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.WATER_BREATH, 2)
            .addValueAttribute(InitAttribute.BREATH_CAPACITY, 2)
            .addValueAttribute(InitAttribute.ENDURANCE, 2)
            .build()
    );

    // 远古阑尾
    public static final Supplier<Item> ELDER_APPENDIX = ITEM.register(
        "elder_appendix",
        () -> Organ.builder()
            .addValueAttribute(Attributes.LUCK, 2)
            .build()
    );

    // 远古肝脏
    public static final Supplier<Item> ELDER_LIVER = ITEM.register(
        "elder_liver",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DETOXIFICATION, 2)
            .build()
    );

    // 远古脾脏
    public static final Supplier<Item> ELDER_SPLEEN = ITEM.register(
        "elder_spleen",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.METABOLISM, 2)
            .build()
    );

    // 远古肾脏
    public static final Supplier<Item> ELDER_KIDNEY = ITEM.register(
        "elder_kidney",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.FILTRATION, 2)
            .build()
    );

    // 远古胃
    public static final Supplier<Item> ELDER_STOMACH = ITEM.register(
        "elder_stomach",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DIGESTION, 2)
            .build()
    );

    // 远古肌肉
    public static final Supplier<Item> ELDER_MUSCLE = ITEM.register(
        "elder_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 2)
            .addValueAttribute(InitAttribute.SPEED, 2)
            .build()
    );

    // 远古鱼类肌肉
    public static final Supplier<Item> ELDER_FISH_MUSCLE = ITEM.register(
        "elder_fish_muscle",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.STRENGTH, 2)
            .addValueAttribute(InitAttribute.SPEED, 1.25)
            .baseMultipliedAttribute(NeoForgeMod.SWIM_SPEED, 0.2)
            .build()
    );

    // 远古脊柱
    public static final Supplier<Item> ELDER_SPINE = ITEM.register(
        "elder_spine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 2)
            .addValueAttribute(InitAttribute.DEFENSE, 1)
            .build()
    );

    // 远古肋骨
    public static final Supplier<Item> ELDER_RIB = ITEM.register(
        "elder_rib",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DEFENSE, 2)
            .build()
    );

    // 远古鱼类脊柱
    public static final Supplier<Item> ELDER_FISH_SPINE = ITEM.register(
        "elder_fish_spine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NERVES, 2)
            .build()
    );

    // 远古鱼骨
    public static final Supplier<Item> ELDER_FISH_BONE = ITEM.register(
        "elder_fish_bone",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.DEFENSE, 1)
            .build()
    );

    // 远古肠子
    public static final Supplier<Item> ELDER_INTESTINE = ITEM.register(
        "elder_intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NUTRITION, 2)
            .build()
    );

    // 远古鱼肠
    public static final Supplier<Item> ELDER_FISH_INTESTINE = ITEM.register(
        "elder_fish_intestine",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.NUTRITION, 2.5)
            .build()
    );

    // 远古魔力反应装置
    public static final Supplier<Item> ELDER_MANA_REACTOR = ITEM.register(
        "elder_mana_reactor",
        () -> Organ.builder()
            .addValueAttribute(InitAttribute.CRYSTALLIZATION, 2)
            .build()
    );

    // 守卫者之眼
    public static final Supplier<Item> GUARDIAN_EYE = ITEM.register(
        "guardian_eye",
        () -> Organ.builder()
            .addValueAttribute(Attributes.ENTITY_INTERACTION_RANGE, 1)
            .cooldown(5 * 20)
            .skill(context -> {
                OrganSkillUtil.guardianLaser(context.entity(), false);
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );

    // 远古守卫者之眼
    public static final Supplier<Item> ELDER_GUARDIAN_EYE = ITEM.register(
        "elder_guardian_eye",
        () -> Organ.builder()
            .addValueAttribute(Attributes.ENTITY_INTERACTION_RANGE, 1)
            .cooldown(8 * 20)
            .skill(context -> {
                OrganSkillUtil.guardianLaser(context.entity(), true);
                return true;
            })
            .skillTooltip(TooltipUtil::simpleSkillTooltip)
            .build()
    );
}
