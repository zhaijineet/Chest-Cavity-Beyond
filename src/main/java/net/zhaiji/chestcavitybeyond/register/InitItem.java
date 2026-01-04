package net.zhaiji.chestcavitybeyond.register;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.task.BlazeFireballTask;
import net.zhaiji.chestcavitybeyond.builder.OrganBuilder;
import net.zhaiji.chestcavitybeyond.item.ChestOpenerItem;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;
import net.zhaiji.chestcavitybeyond.util.OrganSkillUtil;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;

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
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肺脏
    public static final Supplier<Item> LUNG = ITEM.register(
            "lung",
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 胃
    public static final Supplier<Item> STOMACH = ITEM.register(
            "stomach",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肠子
    public static final Supplier<Item> INTESTINE = ITEM.register(
            "intestine",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肾脏
    public static final Supplier<Item> KIDNEY = ITEM.register(
            "kidney",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.FILTRATION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 脾脏
    public static final Supplier<Item> SPLEEN = ITEM.register(
            "spleen",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肝脏
    public static final Supplier<Item> LIVER = ITEM.register(
            "liver",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DETOXIFICATION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 阑尾
    public static final Supplier<Item> APPENDIX = ITEM.register(
            "appendix",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(Attributes.LUCK, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肋骨
    public static final Supplier<Item> RIB = ITEM.register(
            "rib",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 肌肉
    public static final Supplier<Item> MUSCLE = ITEM.register(
            "muscle",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 动物心脏
    public static final Supplier<Item> ANIMAL_HEART = ITEM.register(
            "animal_heart",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物肺脏
    public static final Supplier<Item> ANIMAL_LUNG = ITEM.register(
            "animal_lung",
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.375));
                    })
                    .build()
    );

    // 动物胃
    public static final Supplier<Item> ANIMAL_STOMACH = ITEM.register(
            "animal_stomach",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物肠子
    public static final Supplier<Item> ANIMAL_INTESTINE = ITEM.register(
            "animal_intestine",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物肾脏
    public static final Supplier<Item> ANIMAL_KIDNEY = ITEM.register(
            "animal_kidney",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.FILTRATION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物脾脏
    public static final Supplier<Item> ANIMAL_SPLEEN = ITEM.register(
            "animal_spleen",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物肝脏
    public static final Supplier<Item> ANIMAL_LIVER = ITEM.register(
            "animal_liver",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DETOXIFICATION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物阑尾
    public static final Supplier<Item> ANIMAL_APPENDIX = ITEM.register(
            "animal_appendix",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(Attributes.LUCK, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物肋骨
    public static final Supplier<Item> ANIMAL_RIB = ITEM.register(
            "animal_rib",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 动物肌肉
    public static final Supplier<Item> ANIMAL_MUSCLE = ITEM.register(
            "animal_muscle",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 小型动物心脏
    public static final Supplier<Item> SMALL_ANIMAL_HEART = ITEM.register(
            "small_animal_heart",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物肺脏
    public static final Supplier<Item> SMALL_ANIMAL_LUNG = ITEM.register(
            "small_animal_lung",
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.375));
                    })
                    .build()
    );

    // 小型动物胃
    public static final Supplier<Item> SMALL_ANIMAL_STOMACH = ITEM.register(
            "small_animal_stomach",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物肠子
    public static final Supplier<Item> SMALL_ANIMAL_INTESTINE = ITEM.register(
            "small_animal_intestine",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物肾脏
    public static final Supplier<Item> SMALL_ANIMAL_KIDNEY = ITEM.register(
            "small_animal_kidney",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.FILTRATION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物脾脏
    public static final Supplier<Item> SMALL_ANIMAL_SPLEEN = ITEM.register(
            "small_animal_spleen",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物肝脏
    public static final Supplier<Item> SMALL_ANIMAL_LIVER = ITEM.register(
            "small_animal_liver",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DETOXIFICATION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物阑尾
    public static final Supplier<Item> SMALL_ANIMAL_APPENDIX = ITEM.register(
            "small_animal_appendix",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(Attributes.LUCK, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物肋骨
    public static final Supplier<Item> SMALL_ANIMAL_RIB = ITEM.register(
            "small_animal_rib",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 小型动物肌肉
    public static final Supplier<Item> SMALL_ANIMAL_MUSCLE = ITEM.register(
            "small_animal_muscle",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 鳃
    public static final Supplier<Item> GILLS = ITEM.register(
            "gills",
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物肺脏
    public static final Supplier<Item> FIREPROOF_LUNG = ITEM.register(
            "fireproof_lung",
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物肠子
    public static final Supplier<Item> FIREPROOF_INTESTINE = ITEM.register(
            "fireproof_intestine",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物肾脏
    public static final Supplier<Item> FIREPROOF_KIDNEY = ITEM.register(
            "fireproof_kidney",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.FILTRATION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物脾脏
    public static final Supplier<Item> FIREPROOF_SPLEEN = ITEM.register(
            "fireproof_spleen",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物肝脏
    public static final Supplier<Item> FIREPROOF_LIVER = ITEM.register(
            "fireproof_liver",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DETOXIFICATION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物阑尾
    public static final Supplier<Item> FIREPROOF_APPENDIX = ITEM.register(
            "fireproof_appendix",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(Attributes.LUCK, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物肋骨
    public static final Supplier<Item> FIREPROOF_RIB = ITEM.register(
            "fireproof_rib",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 抗火生物肌肉
    public static final Supplier<Item> FIREPROOF_MUSCLE = ITEM.register(
            "fireproof_muscle",
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.625));
                        modifiers.put(InitAttribute.PROJECTILE_DODGE, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.WATER_ALLERGY, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影胃
    public static final Supplier<Item> ENDER_STOMACH = ITEM.register(
            "ender_stomach",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.ENDER, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影肠子
    public static final Supplier<Item> ENDER_INTESTINE = ITEM.register(
            "ender_intestine",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.ENDER, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影肾脏
    public static final Supplier<Item> ENDER_KIDNEY = ITEM.register(
            "ender_kidney",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.FILTRATION, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.ENDER, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影脾脏
    public static final Supplier<Item> ENDER_SPLEEN = ITEM.register(
            "ender_spleen",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.ENDER, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影肝脏
    public static final Supplier<Item> ENDER_LIVER = ITEM.register(
            "ender_liver",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DETOXIFICATION, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.ENDER, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 末影阑尾
    public static final Supplier<Item> ENDER_APPENDIX = ITEM.register(
            "ender_appendix",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(Attributes.LUCK, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.ENDER, OrganAttributeUtil.createAddValueModifier(id, 8));
                    })
                    .skill(context -> {
                        OrganSkillUtil.teleport(context.entity(), context.data().getCurrentValue(InitAttribute.ENDER));
                    })
                    .build()
    );

    // 末影肋骨
    public static final Supplier<Item> ENDER_RIB = ITEM.register(
            "ender_rib",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                    })
                    .build()
    );

    // 末影肌肉
    public static final Supplier<Item> ENDER_MUSCLE = ITEM.register(
            "ender_muscle",
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 速度型肌肉
    public static final Supplier<Item> SWIFT_MUSCLE = ITEM.register(
            "swift_muscle",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                    })
                    .build()
    );


    // 弹跳型肌肉
    public static final Supplier<Item> LEAPING_MUSCLE = ITEM.register(
            "leaping_muscle",
            () -> OrganBuilder.builder()
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
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.HERBIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                    })
                    .build()
    );

    // 小型食肉动物胃
    public static final Supplier<Item> SMALL_CARNIVORE_STOMACH = ITEM.register(
            "small_carnivore_stomach",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.HERBIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                    })
                    .build()
    );

    // 食肉动物肠子
    public static final Supplier<Item> CARNIVORE_INTESTINE = ITEM.register(
            "carnivore_intestine",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.HERBIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                    })
                    .build()
    );

    // 小型食肉动物肠子
    public static final Supplier<Item> SMALL_CARNIVORE_INTESTINE = ITEM.register(
            "small_carnivore_intestine",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.HERBIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                    })
                    .build()
    );

    // 食草动物胃
    public static final Supplier<Item> HERBIVORE_STOMACH = ITEM.register(
            "herbivore_stomach",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.HERBIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                    })
                    .build()
    );

    // 小型食草动物胃
    public static final Supplier<Item> SMALL_HERBIVORE_STOMACH = ITEM.register(
            "small_herbivore_stomach",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.HERBIVOROUS_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 食草动物肠子
    public static final Supplier<Item> HERBIVORE_INTESTINE = ITEM.register(
            "herbivore_intestine",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.HERBIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                    })
                    .build()
    );

    // 小型食草动物肠子
    public static final Supplier<Item> SMALL_HERBIVORE_INTESTINE = ITEM.register(
            "small_herbivore_intestine",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.CARNIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.HERBIVOROUS_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .build()
    );

    // 食草动物瘤胃
    public static final Supplier<Item> HERBIVORE_RUMEN = ITEM.register(
            "herbivore_rumen",
            () -> OrganBuilder.builder()
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

    // 苦力怕阑尾
    public static final Supplier<Item> CREEPER_APPENDIX = ITEM.register(
            "creeper_appendix",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(Attributes.LUCK, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.EXPLOSIVE, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .skill(context -> {
                        OrganSkillUtil.explosion(context.entity(), context.data().getCurrentValue(InitAttribute.EXPLOSIVE));
                    })
                    .build()
    );

    // 苦力怕阑尾
    public static final Supplier<Item> CREEPER_LEAF = ITEM.register(
            "creeper_leaf",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.PHOTOSYNTHESIS, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 腐烂心脏
    public static final Supplier<Item> ROTTEN_HEART = ITEM.register(
            "rotten_heart",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 腐烂肺脏
    public static final Supplier<Item> ROTTEN_LUNG = ITEM.register(
            "rotten_lung",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.BREATH_RECOVERY, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.BREATH_CAPACITY, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.ENDURANCE, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 腐烂脊柱
    public static final Supplier<Item> ROTTEN_SPINE = ITEM.register(
            "rotten_spine",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                    })
                    .build()
    );

    // 腐烂胃
    public static final Supplier<Item> ROTTEN_STOMACH = ITEM.register(
            "rotten_stomach",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.SCAVENGER_DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 腐烂肠子
    public static final Supplier<Item> ROTTEN_INTESTINE = ITEM.register(
            "rotten_intestine",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.SCAVENGER_NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 腐烂肾脏
    public static final Supplier<Item> ROTTEN_KIDNEY = ITEM.register(
            "rotten_kidney",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.FILTRATION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 腐烂脾脏
    public static final Supplier<Item> ROTTEN_SPLEEN = ITEM.register(
            "rotten_spleen",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 腐烂肝脏
    public static final Supplier<Item> ROTTEN_LIVER = ITEM.register(
            "rotten_liver",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DETOXIFICATION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 腐烂阑尾
    public static final Supplier<Item> ROTTEN_APPENDIX = ITEM.register(
            "rotten_appendix",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(Attributes.LUCK, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 腐烂肋骨
    public static final Supplier<Item> ROTTEN_RIB = ITEM.register(
            "rotten_rib",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 腐烂肌肉
    public static final Supplier<Item> ROTTEN_MUSCLE = ITEM.register(
            "rotten_muscle",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 凋零脊柱
    public static final Supplier<Item> WITHERED_SPINE = ITEM.register(
            "withered_spine",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.WITHERED, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 凋零肋骨
    public static final Supplier<Item> WITHERED_RIB = ITEM.register(
            "withered_rib",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.WITHERED, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 扭曲灵魂沙
    public static final Supplier<Item> WRITHING_SOUL_SAND = ITEM.register(
            "writhing_soul_sand",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 3));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.WITHERED, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 傀儡核心
    public static final Supplier<Item> GOLEM_CORE = ITEM.register(
            "golem_core",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(Attributes.KNOCKBACK_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                    })
                    .build()
    );

    // 傀儡电缆
    public static final Supplier<Item> GOLEM_CABLE = ITEM.register(
            "golem_cable",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(Attributes.KNOCKBACK_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, -0.5));
                    })
                    .build()
    );

    // 熔炉内核
    public static final Supplier<Item> INNER_FURNACE = ITEM.register(
            "inner_furnace",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.FURNACE_POWER, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .skill(context -> {
                        if (context.entity() instanceof Player player) {
                            OrganSkillUtil.furnacePower(player, context.data().getCurrentValue(InitAttribute.FURNACE_POWER));
                        }
                    })
                    .build()
    );

    // 活塞型肌肉
    public static final Supplier<Item> PISTON_MUSCLE = ITEM.register(
            "piston_muscle",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 2));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.LAUNCH, OrganAttributeUtil.createAddValueModifier(id, 1));
                    })
                    .build()
    );

    // 傀儡装甲板
    public static final Supplier<Item> GOLEM_ARMOR_PLATE = ITEM.register(
            "golem_armor_plate",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                        modifiers.put(InitAttribute.IRON_REPAIR, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, -0.5));
                    })
                    .skill(context -> {
                        if (context.entity() instanceof Player player) {
                            OrganSkillUtil.ironRepair(player, context.data().getCurrentValue(InitAttribute.IRON_REPAIR));
                        }
                    })
                    .build()
    );

    // 丝腺
    public static final Supplier<Item> SILK_GLAND = ITEM.register(
            "silk_gland",
            () -> OrganBuilder.builder()
                    .skill(context -> {
                        OrganSkillUtil.silk(context.entity());
                    })
                    .build()
    );

    // 毒腺
    public static final Supplier<Item> VENOM_GLAND = ITEM.register(
            "venom_gland",
            () -> OrganBuilder.builder(
                            new Item(
                                    new Item.Properties()
                                            .stacksTo(1)
                                            .component(
                                                    DataComponents.POTION_CONTENTS,
                                                    ChestCavityUtil.calculatePotionContents(Potions.POISON.value().getEffects())
                                            )
                            ) {
                                @Override
                                public ItemStack getDefaultInstance() {
                                    return ChestCavityUtil.attachPotionContents(super.getDefaultInstance(), Potions.POISON.value().getEffects());
                                }
                            }
                    )
                    .tooltips((data, stack, keyContext, context, tooltipComponents, tooltipFlag) -> {
                        TooltipUtil.addOrganTooltip(data, stack, keyContext, context, tooltipComponents, tooltipFlag);
                        PotionContents potioncontents = stack.get(DataComponents.POTION_CONTENTS);
                        if (potioncontents != null) {
                            potioncontents.addPotionTooltip(tooltipComponents::add, 1.0F, context.tickRate());
                        }
                    })
                    .attack((context, target, source, damageContainer) -> {
                        context.stack()
                                .getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                                .forEachEffect(target::addEffect);
                    })
                    .build()
    );

    // 节肢生物心脏
    public static final Supplier<Item> ARTHROPOD_HEART = ITEM.register(
            "arthropod_heart",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.FILTRATION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 节肢生物肠子
    public static final Supplier<Item> ARTHROPOD_INTESTINE = ITEM.register(
            "arthropod_intestine",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.DETOXIFICATION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                    })
                    .build()
    );

    // 节肢生物肺脏
    public static final Supplier<Item> ARTHROPOD_LUNG = ITEM.register(
            "arthropod_lung",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.BREATH_RECOVERY, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.BREATH_CAPACITY, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.ENDURANCE, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                    })
                    .build()
    );

    // 节肢生物肌肉
    public static final Supplier<Item> ARTHROPOD_MUSCLE = ITEM.register(
            "arthropod_muscle",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.STRENGTH, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.SPEED, OrganAttributeUtil.createAddValueModifier(id, 1.25));
                    })
                    .build()
    );

    // 节肢生物胃
    public static final Supplier<Item> ARTHROPOD_STOMACH = ITEM.register(
            "arthropod_stomach",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.METABOLISM, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                    })
                    .build()
    );

    // 节肢生物盲囊
    public static final Supplier<Item> ARTHROPOD_CAECUM = ITEM.register(
            "arthropod_caecum",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.NUTRITION, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                        modifiers.put(InitAttribute.DIGESTION, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 0.5));
                    })
                    .build()
    );

    // 羊驼肺脏
    public static final Supplier<Item> LLAMA_LUNG = ITEM.register(
            "llama_lung",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.BREATH_RECOVERY, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.BREATH_CAPACITY, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                        modifiers.put(InitAttribute.ENDURANCE, OrganAttributeUtil.createAddValueModifier(id, 0.75));
                    })
                    .skill(context -> {
                        OrganSkillUtil.spit(context.entity());
                    })
                    .build()
    );

    // 烈焰核心
    public static final Supplier<Item> BLAZE_CORE = ITEM.register(
            "blaze_core",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.NERVES, OrganAttributeUtil.createAddValueModifier(id, 0.25));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 3));
                        modifiers.put(InitAttribute.WATER_ALLERGY, OrganAttributeUtil.createAddValueModifier(id, 3));
                    })
                    .build()
    );

    // 烈焰外壳
    public static final Supplier<Item> BLAZE_SHELL = ITEM.register(
            "blaze_shell",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.DEFENSE, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 3));
                        modifiers.put(InitAttribute.WATER_ALLERGY, OrganAttributeUtil.createAddValueModifier(id, 3));
                    })
                    .build()
    );

    // 活性烈焰棒
    public static final Supplier<Item> ACTIVE_BLAZE_ROD = ITEM.register(
            "active_blaze_rod",
            () -> OrganBuilder.builder()
                    .modifier((id, modifiers) -> {
                        modifiers.put(InitAttribute.VOMIT_FIREBALL, OrganAttributeUtil.createAddValueModifier(id, 1));
                        modifiers.put(InitAttribute.FIRE_RESISTANCE, OrganAttributeUtil.createAddValueModifier(id, 3));
                        modifiers.put(InitAttribute.WATER_ALLERGY, OrganAttributeUtil.createAddValueModifier(id, 3));
                    })
                    .skill(context -> {
                        context.data().addTask(new BlazeFireballTask(context.data()));
                    })
                    .build()
    );
}
