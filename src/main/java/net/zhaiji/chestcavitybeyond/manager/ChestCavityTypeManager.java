package net.zhaiji.chestcavitybeyond.manager;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.Items;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.register.InitItem;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChestCavityTypeManager {
    public static final Map<ResourceLocation, ChestCavityType> CHEST_CAVITY_TYPE_REGISTRY = new LinkedHashMap<>();

    public static final Map<EntityType<? extends LivingEntity>, ChestCavityType> ENTITY_CHEST_CAVITY_TYPE_MAP = new LinkedHashMap<>();

    public static final ChestCavityType HUMAN = register(ChestCavityBeyond.of("human"))
        .setFirstRow(0, InitItem.MUSCLE.get())
        .setFirstRow(1, InitItem.RIB.get())
        .setFirstRow(2, InitItem.APPENDIX.get())
        .setFirstRow(3, InitItem.LUNG.get())
        .setFirstRow(4, InitItem.HEART.get())
        .setFirstRow(5, InitItem.LUNG.get())
        .setFirstRow(7, InitItem.RIB.get())
        .setFirstRow(8, InitItem.MUSCLE.get())

        .setSecondRow(0, InitItem.MUSCLE.get())
        .setSecondRow(1, InitItem.RIB.get())
        .setSecondRow(2, InitItem.SPLEEN.get())
        .setSecondRow(3, InitItem.KIDNEY.get())
        .setSecondRow(4, InitItem.SPINE.get())
        .setSecondRow(5, InitItem.KIDNEY.get())
        .setSecondRow(6, InitItem.LIVER.get())
        .setSecondRow(7, InitItem.RIB.get())
        .setSecondRow(8, InitItem.MUSCLE.get())

        .setThirdRow(0, InitItem.MUSCLE.get())
        .setThirdRow(1, InitItem.MUSCLE.get())
        .setThirdRow(2, InitItem.INTESTINE.get())
        .setThirdRow(3, InitItem.INTESTINE.get())
        .setThirdRow(4, InitItem.STOMACH.get())
        .setThirdRow(5, InitItem.INTESTINE.get())
        .setThirdRow(6, InitItem.INTESTINE.get())
        .setThirdRow(7, InitItem.MUSCLE.get())
        .setThirdRow(8, InitItem.MUSCLE.get())
        // HUMAN → UNDEAD 器官转换映射
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.MUSCLE.get(), InitItem.ROTTEN_MUSCLE.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.RIB.get(), InitItem.ROTTEN_RIB.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.APPENDIX.get(), InitItem.ROTTEN_APPENDIX.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.HEART.get(), InitItem.ROTTEN_HEART.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.LUNG.get(), InitItem.ROTTEN_LUNG.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.SPINE.get(), InitItem.ROTTEN_SPINE.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.STOMACH.get(), InitItem.ROTTEN_STOMACH.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.INTESTINE.get(), InitItem.ROTTEN_INTESTINE.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.KIDNEY.get(), InitItem.ROTTEN_KIDNEY.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.SPLEEN.get(), InitItem.ROTTEN_SPLEEN.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.LIVER.get(), InitItem.ROTTEN_LIVER.get());

    public static final ChestCavityType ANIMAL = register(ChestCavityBeyond.of("animal"))
        .setFirstRow(0, InitItem.ANIMAL_MUSCLE.get())
        .setFirstRow(1, InitItem.ANIMAL_RIB.get())
        .setFirstRow(2, InitItem.ANIMAL_APPENDIX.get())
        .setFirstRow(3, InitItem.ANIMAL_LUNG.get())
        .setFirstRow(4, InitItem.ANIMAL_HEART.get())
        .setFirstRow(5, InitItem.ANIMAL_LUNG.get())
        .setFirstRow(7, InitItem.ANIMAL_RIB.get())
        .setFirstRow(8, InitItem.ANIMAL_MUSCLE.get())

        .setSecondRow(0, InitItem.ANIMAL_MUSCLE.get())
        .setSecondRow(1, InitItem.ANIMAL_RIB.get())
        .setSecondRow(2, InitItem.ANIMAL_SPLEEN.get())
        .setSecondRow(3, InitItem.ANIMAL_KIDNEY.get())
        .setSecondRow(4, InitItem.ANIMAL_SPINE.get())
        .setSecondRow(5, InitItem.ANIMAL_KIDNEY.get())
        .setSecondRow(6, InitItem.ANIMAL_LIVER.get())
        .setSecondRow(7, InitItem.ANIMAL_RIB.get())
        .setSecondRow(8, InitItem.ANIMAL_MUSCLE.get())

        .setThirdRow(0, InitItem.ANIMAL_MUSCLE.get())
        .setThirdRow(1, InitItem.ANIMAL_MUSCLE.get())
        .setThirdRow(2, InitItem.ANIMAL_INTESTINE.get())
        .setThirdRow(3, InitItem.ANIMAL_INTESTINE.get())
        .setThirdRow(4, InitItem.ANIMAL_STOMACH.get())
        .setThirdRow(5, InitItem.ANIMAL_INTESTINE.get())
        .setThirdRow(6, InitItem.ANIMAL_INTESTINE.get())
        .setThirdRow(7, InitItem.ANIMAL_MUSCLE.get())
        .setThirdRow(8, InitItem.ANIMAL_MUSCLE.get())
        // ANIMAL → UNDEAD 器官转换映射（用于Hoglin→Zoglin）
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.ANIMAL_MUSCLE.get(), InitItem.ROTTEN_MUSCLE.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.ANIMAL_RIB.get(), InitItem.ROTTEN_RIB.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.ANIMAL_APPENDIX.get(), InitItem.ROTTEN_APPENDIX.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.ANIMAL_HEART.get(), InitItem.ROTTEN_HEART.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.ANIMAL_LUNG.get(), InitItem.ROTTEN_LUNG.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.ANIMAL_SPINE.get(), InitItem.ROTTEN_SPINE.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.ANIMAL_STOMACH.get(), InitItem.ROTTEN_STOMACH.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.ANIMAL_INTESTINE.get(), InitItem.ROTTEN_INTESTINE.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.ANIMAL_KIDNEY.get(), InitItem.ROTTEN_KIDNEY.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.ANIMAL_SPLEEN.get(), InitItem.ROTTEN_SPLEEN.get())
        .addConversion(ChestCavityBeyond.of("undead"), InitItem.ANIMAL_LIVER.get(), InitItem.ROTTEN_LIVER.get());

    public static final ChestCavityType HERBIVORE1 = register(ChestCavityBeyond.of("herbivore1"))
        .copyWith(ANIMAL)
        .setThirdRow(2, InitItem.HERBIVORE_INTESTINE.get())
        .setThirdRow(3, InitItem.HERBIVORE_INTESTINE.get())
        .setThirdRow(4, InitItem.HERBIVORE_STOMACH.get())
        .setThirdRow(5, InitItem.HERBIVORE_INTESTINE.get())
        .setThirdRow(6, InitItem.HERBIVORE_INTESTINE.get());

    public static final ChestCavityType HERBIVORE2 = register(ChestCavityBeyond.of("herbivore2"))
        .copyWith(HERBIVORE1)
        .setThirdRow(5, InitItem.HERBIVORE_RUMEN.get());

    public static final ChestCavityType HERBIVORE3 = register(ChestCavityBeyond.of("herbivore3"))
        .copyWith(HERBIVORE2)
        .setThirdRow(3, InitItem.HERBIVORE_RUMEN.get());

    public static final ChestCavityType LLAMA = register(ChestCavityBeyond.of("llama"))
        .copyWith(HERBIVORE1)
        .setFirstRow(3, InitItem.LLAMA_LUNG.get())
        .setFirstRow(5, InitItem.LLAMA_LUNG.get());

    public static final ChestCavityType CARNIVORE = register(ChestCavityBeyond.of("carnivore"))
        .copyWith(ANIMAL)
        .setThirdRow(2, InitItem.CARNIVORE_INTESTINE.get())
        .setThirdRow(3, InitItem.CARNIVORE_INTESTINE.get())
        .setThirdRow(4, InitItem.CARNIVORE_STOMACH.get())
        .setThirdRow(5, InitItem.CARNIVORE_INTESTINE.get())
        .setThirdRow(6, InitItem.CARNIVORE_INTESTINE.get());

    public static final ChestCavityType SHULKER = register(ChestCavityBeyond.of("shulker"))
        .copyWith(ANIMAL)
        .setFirstRow(2, InitItem.ENDER_APPENDIX.get())
        .setSecondRow(2, InitItem.SHULKER_SPLEEN.get())
        .setSecondRow(6, InitItem.SHULKER_SPLEEN.get());

    public static final ChestCavityType TURTLE = register(ChestCavityBeyond.of("turtle"))
        .copyWith(ANIMAL)

        .addValueBonuses(
            InitItem.ANIMAL_LUNG.get(), Map.of(
                InitAttribute.WATER_BREATH, 0.5
            )
        );

    public static final ChestCavityType FROG = register(ChestCavityBeyond.of("frog"))
        .copyWith(ANIMAL)
        .addValueBonuses(
            InitItem.ANIMAL_LUNG.get(), Map.of(
                InitAttribute.WATER_BREATH, 0.5
            )
        );

    public static final ChestCavityType SMALL_ANIMAL = register(ChestCavityBeyond.of("small_animal"))
        .setFirstRow(0, InitItem.SMALL_ANIMAL_MUSCLE.get())
        .setFirstRow(1, InitItem.SMALL_ANIMAL_RIB.get())
        .setFirstRow(2, InitItem.SMALL_ANIMAL_APPENDIX.get())
        .setFirstRow(3, InitItem.SMALL_ANIMAL_LUNG.get())
        .setFirstRow(4, InitItem.SMALL_ANIMAL_HEART.get())
        .setFirstRow(5, InitItem.SMALL_ANIMAL_LUNG.get())
        .setFirstRow(7, InitItem.SMALL_ANIMAL_RIB.get())
        .setFirstRow(8, InitItem.SMALL_ANIMAL_MUSCLE.get())

        .setSecondRow(0, InitItem.SMALL_ANIMAL_MUSCLE.get())
        .setSecondRow(1, InitItem.SMALL_ANIMAL_RIB.get())
        .setSecondRow(2, InitItem.SMALL_ANIMAL_SPLEEN.get())
        .setSecondRow(3, InitItem.SMALL_ANIMAL_KIDNEY.get())
        .setSecondRow(4, InitItem.SMALL_ANIMAL_SPINE.get())
        .setSecondRow(5, InitItem.SMALL_ANIMAL_KIDNEY.get())
        .setSecondRow(6, InitItem.SMALL_ANIMAL_LIVER.get())
        .setSecondRow(7, InitItem.SMALL_ANIMAL_RIB.get())
        .setSecondRow(8, InitItem.SMALL_ANIMAL_MUSCLE.get())

        .setThirdRow(0, InitItem.SMALL_ANIMAL_MUSCLE.get())
        .setThirdRow(1, InitItem.SMALL_ANIMAL_MUSCLE.get())
        .setThirdRow(2, InitItem.SMALL_ANIMAL_INTESTINE.get())
        .setThirdRow(3, InitItem.SMALL_ANIMAL_INTESTINE.get())
        .setThirdRow(4, InitItem.SMALL_ANIMAL_STOMACH.get())
        .setThirdRow(5, InitItem.SMALL_ANIMAL_INTESTINE.get())
        .setThirdRow(6, InitItem.SMALL_ANIMAL_INTESTINE.get())
        .setThirdRow(7, InitItem.SMALL_ANIMAL_MUSCLE.get())
        .setThirdRow(8, InitItem.SMALL_ANIMAL_MUSCLE.get());

    public static final ChestCavityType SMALL_HERBIVORE = register(ChestCavityBeyond.of("small_herbivore"))
        .copyWith(SMALL_ANIMAL)
        .setThirdRow(2, InitItem.SMALL_HERBIVORE_INTESTINE.get())
        .setThirdRow(3, InitItem.SMALL_HERBIVORE_INTESTINE.get())
        .setThirdRow(4, InitItem.SMALL_HERBIVORE_STOMACH.get())
        .setThirdRow(5, InitItem.SMALL_HERBIVORE_INTESTINE.get())
        .setThirdRow(6, InitItem.SMALL_HERBIVORE_INTESTINE.get());

    public static final ChestCavityType SMALL_CARNIVORE = register(ChestCavityBeyond.of("small_carnivore"))
        .copyWith(SMALL_ANIMAL)
        .setThirdRow(2, InitItem.SMALL_CARNIVORE_INTESTINE.get())
        .setThirdRow(3, InitItem.SMALL_CARNIVORE_INTESTINE.get())
        .setThirdRow(4, InitItem.SMALL_CARNIVORE_STOMACH.get())
        .setThirdRow(5, InitItem.SMALL_CARNIVORE_INTESTINE.get())
        .setThirdRow(6, InitItem.SMALL_CARNIVORE_INTESTINE.get());

    public static final ChestCavityType RABBIT = register(ChestCavityBeyond.of("rabbit"))
        .setFirstRow(0, InitItem.SMALL_LEAPING_MUSCLE.get())
        .setFirstRow(1, InitItem.SMALL_ANIMAL_RIB.get())
        .setFirstRow(2, InitItem.SMALL_ANIMAL_APPENDIX.get())
        .setFirstRow(3, InitItem.SMALL_ANIMAL_LUNG.get())
        .setFirstRow(4, InitItem.SMALL_ANIMAL_HEART.get())
        .setFirstRow(5, InitItem.SMALL_ANIMAL_LUNG.get())
        .setFirstRow(7, InitItem.SMALL_ANIMAL_RIB.get())
        .setFirstRow(8, InitItem.SMALL_LEAPING_MUSCLE.get())

        .setSecondRow(0, InitItem.SMALL_LEAPING_MUSCLE.get())
        .setSecondRow(1, InitItem.SMALL_ANIMAL_RIB.get())
        .setSecondRow(2, InitItem.SMALL_ANIMAL_SPLEEN.get())
        .setSecondRow(3, InitItem.SMALL_ANIMAL_KIDNEY.get())
        .setSecondRow(4, InitItem.SMALL_ANIMAL_SPINE.get())
        .setSecondRow(5, InitItem.SMALL_ANIMAL_KIDNEY.get())
        .setSecondRow(6, InitItem.SMALL_ANIMAL_LIVER.get())
        .setSecondRow(7, InitItem.SMALL_ANIMAL_RIB.get())
        .setSecondRow(8, InitItem.SMALL_LEAPING_MUSCLE.get())

        .setThirdRow(0, InitItem.SMALL_LEAPING_MUSCLE.get())
        .setThirdRow(1, InitItem.SMALL_LEAPING_MUSCLE.get())
        .setThirdRow(2, InitItem.SMALL_HERBIVORE_INTESTINE.get())
        .setThirdRow(3, InitItem.SMALL_HERBIVORE_INTESTINE.get())
        .setThirdRow(4, InitItem.SMALL_HERBIVORE_STOMACH.get())
        .setThirdRow(5, InitItem.SMALL_HERBIVORE_INTESTINE.get())
        .setThirdRow(6, InitItem.SMALL_HERBIVORE_INTESTINE.get())
        .setThirdRow(7, InitItem.SMALL_LEAPING_MUSCLE.get())
        .setThirdRow(8, InitItem.SMALL_LEAPING_MUSCLE.get());

    public static final ChestCavityType SLIME = register(ChestCavityBeyond.of("slime"))
        .setFirstRow(4, Items.SLIME_BALL)

        .setSecondRow(3, Items.SLIME_BALL)
        .setSecondRow(4, InitItem.SLIME_CORE.get())
        .setSecondRow(5, Items.SLIME_BALL)

        .setThirdRow(4, Items.SLIME_BALL)

        .addValueBonuses(
            InitItem.SLIME_CORE.get(), Map.of(
                InitAttribute.NERVES, 1.0,
                InitAttribute.BREATH_CAPACITY, 1.0,
                InitAttribute.BREATH_RECOVERY, 1.0
            )
        );

    public static final ChestCavityType MAGMA_CUBE = register(ChestCavityBeyond.of("magma_cube"))
        .setFirstRow(4, Items.MAGMA_CREAM)

        .setSecondRow(3, Items.MAGMA_CREAM)
        .setSecondRow(4, InitItem.MAGMA_CUBE_CORE.get())
        .setSecondRow(5, Items.MAGMA_CREAM)

        .setThirdRow(4, Items.MAGMA_CREAM)

        .addValueBonuses(
            InitItem.MAGMA_CUBE_CORE.get(), Map.of(
                InitAttribute.NERVES, 1.0,
                InitAttribute.BREATH_CAPACITY, 1.0,
                InitAttribute.BREATH_RECOVERY, 1.0
            )
        );

    public static final ChestCavityType FIREPROOF = register(ChestCavityBeyond.of("fireproof"))
        .setFirstRow(0, InitItem.FIREPROOF_MUSCLE.get())
        .setFirstRow(1, InitItem.FIREPROOF_RIB.get())
        .setFirstRow(2, InitItem.FIREPROOF_APPENDIX.get())
        .setFirstRow(3, InitItem.FIREPROOF_LUNG.get())
        .setFirstRow(4, InitItem.FIREPROOF_HEART.get())
        .setFirstRow(5, InitItem.FIREPROOF_LUNG.get())
        .setFirstRow(7, InitItem.FIREPROOF_RIB.get())
        .setFirstRow(8, InitItem.FIREPROOF_MUSCLE.get())

        .setSecondRow(0, InitItem.FIREPROOF_MUSCLE.get())
        .setSecondRow(1, InitItem.FIREPROOF_RIB.get())
        .setSecondRow(2, InitItem.FIREPROOF_SPLEEN.get())
        .setSecondRow(3, InitItem.FIREPROOF_KIDNEY.get())
        .setSecondRow(4, InitItem.FIREPROOF_SPINE.get())
        .setSecondRow(5, InitItem.FIREPROOF_KIDNEY.get())
        .setSecondRow(6, InitItem.FIREPROOF_LIVER.get())
        .setSecondRow(7, InitItem.FIREPROOF_RIB.get())
        .setSecondRow(8, InitItem.FIREPROOF_MUSCLE.get())

        .setThirdRow(0, InitItem.FIREPROOF_MUSCLE.get())
        .setThirdRow(1, InitItem.FIREPROOF_MUSCLE.get())
        .setThirdRow(2, InitItem.FIREPROOF_INTESTINE.get())
        .setThirdRow(3, InitItem.FIREPROOF_INTESTINE.get())
        .setThirdRow(4, InitItem.FIREPROOF_STOMACH.get())
        .setThirdRow(5, InitItem.FIREPROOF_INTESTINE.get())
        .setThirdRow(6, InitItem.FIREPROOF_INTESTINE.get())
        .setThirdRow(7, InitItem.FIREPROOF_MUSCLE.get())
        .setThirdRow(8, InitItem.FIREPROOF_MUSCLE.get());

    public static final ChestCavityType GHAST = register(ChestCavityBeyond.of("ghast"))
        .copyWith(FIREPROOF)
        .setFirstRow(1, InitItem.GAS_SAC.get())
        .setFirstRow(3, InitItem.GAS_SAC.get())
        .setFirstRow(5, InitItem.GAS_SAC.get())
        .setFirstRow(7, InitItem.GAS_SAC.get())
        .setThirdRow(4, InitItem.GHAST_STOMACH.get());

    public static final ChestCavityType ENDER = register(ChestCavityBeyond.of("ender"))
        .setFirstRow(0, InitItem.ENDER_MUSCLE.get())
        .setFirstRow(1, InitItem.ENDER_RIB.get())
        .setFirstRow(2, InitItem.ENDER_APPENDIX.get())
        .setFirstRow(3, InitItem.ENDER_LUNG.get())
        .setFirstRow(4, InitItem.ENDER_HEART.get())
        .setFirstRow(5, InitItem.ENDER_LUNG.get())
        .setFirstRow(7, InitItem.ENDER_RIB.get())
        .setFirstRow(8, InitItem.ENDER_MUSCLE.get())

        .setSecondRow(0, InitItem.ENDER_MUSCLE.get())
        .setSecondRow(1, InitItem.ENDER_RIB.get())
        .setSecondRow(2, InitItem.ENDER_SPLEEN.get())
        .setSecondRow(3, InitItem.ENDER_KIDNEY.get())
        .setSecondRow(4, InitItem.ENDER_SPINE.get())
        .setSecondRow(5, InitItem.ENDER_KIDNEY.get())
        .setSecondRow(6, InitItem.ENDER_LIVER.get())
        .setSecondRow(7, InitItem.ENDER_RIB.get())
        .setSecondRow(8, InitItem.ENDER_MUSCLE.get())

        .setThirdRow(0, InitItem.ENDER_MUSCLE.get())
        .setThirdRow(1, InitItem.ENDER_MUSCLE.get())
        .setThirdRow(2, InitItem.ENDER_INTESTINE.get())
        .setThirdRow(3, InitItem.ENDER_INTESTINE.get())
        .setThirdRow(4, InitItem.ENDER_STOMACH.get())
        .setThirdRow(5, InitItem.ENDER_INTESTINE.get())
        .setThirdRow(6, InitItem.ENDER_INTESTINE.get())
        .setThirdRow(7, InitItem.ENDER_MUSCLE.get())
        .setThirdRow(8, InitItem.ENDER_MUSCLE.get());

    public static final ChestCavityType ENDER_DRAGON = register(ChestCavityBeyond.of("ender_dragon"))
        .setFirstRow(0, InitItem.DRAGON_MUSCLE.get())
        .setFirstRow(1, InitItem.DRAGON_RIB.get())
        .setFirstRow(2, InitItem.DRAGON_APPENDIX.get())
        .setFirstRow(3, InitItem.DRAGON_LUNG.get())
        .setFirstRow(4, InitItem.DRAGON_HEART.get())
        .setFirstRow(5, InitItem.DRAGON_LUNG.get())
        .setFirstRow(7, InitItem.DRAGON_RIB.get())
        .setFirstRow(8, InitItem.DRAGON_MUSCLE.get())

        .setSecondRow(0, InitItem.DRAGON_MUSCLE.get())
        .setSecondRow(1, InitItem.DRAGON_RIB.get())
        .setSecondRow(2, InitItem.DRAGON_SPLEEN.get())
        .setSecondRow(3, InitItem.DRAGON_KIDNEY.get())
        .setSecondRow(4, InitItem.DRAGON_SPINE.get())
        .setSecondRow(5, InitItem.DRAGON_KIDNEY.get())
        .setSecondRow(6, InitItem.DRAGON_LIVER.get())
        .setSecondRow(7, InitItem.DRAGON_RIB.get())
        .setSecondRow(8, InitItem.DRAGON_MUSCLE.get())

        .setThirdRow(0, InitItem.DRAGON_MUSCLE.get())
        .setThirdRow(1, InitItem.DRAGON_MUSCLE.get())
        .setThirdRow(2, InitItem.MANA_REACTOR.get())
        .setThirdRow(3, InitItem.MANA_REACTOR.get())
        .setThirdRow(4, InitItem.MANA_REACTOR.get())
        .setThirdRow(5, InitItem.MANA_REACTOR.get())
        .setThirdRow(6, InitItem.MANA_REACTOR.get())
        .setThirdRow(7, InitItem.DRAGON_MUSCLE.get())
        .setThirdRow(8, InitItem.DRAGON_MUSCLE.get());

    public static final ChestCavityType UNDEAD = register(ChestCavityBeyond.of("undead"))
        .setNeedHealth(false)

        .setFirstRow(0, InitItem.ROTTEN_MUSCLE.get())
        .setFirstRow(1, InitItem.ROTTEN_RIB.get())
        .setFirstRow(2, InitItem.ROTTEN_APPENDIX.get())
        .setFirstRow(4, InitItem.ROTTEN_HEART.get())
        .setFirstRow(5, InitItem.ROTTEN_LUNG.get())
        .setFirstRow(7, InitItem.ROTTEN_RIB.get())
        .setFirstRow(8, InitItem.ROTTEN_MUSCLE.get())

        .setSecondRow(0, InitItem.ROTTEN_MUSCLE.get())
        .setSecondRow(1, InitItem.ROTTEN_RIB.get())
        .setSecondRow(2, InitItem.ROTTEN_SPLEEN.get())
        .setSecondRow(3, InitItem.ROTTEN_KIDNEY.get())
        .setSecondRow(4, InitItem.ROTTEN_SPINE.get())
        .setSecondRow(6, InitItem.ROTTEN_LIVER.get())
        .setSecondRow(7, InitItem.ROTTEN_RIB.get())

        .setThirdRow(0, InitItem.ROTTEN_MUSCLE.get())
        .setThirdRow(2, InitItem.ROTTEN_INTESTINE.get())
        .setThirdRow(4, InitItem.ROTTEN_STOMACH.get())
        .setThirdRow(6, InitItem.ROTTEN_INTESTINE.get())
        .setThirdRow(8, InitItem.ROTTEN_MUSCLE.get())

        .addValueBonuses(
            InitItem.ROTTEN_LUNG.get(), Map.of(
                InitAttribute.WATER_BREATH, 0.5
            )
        )
        // UNDEAD → HUMAN 器官转换映射（用于僵尸村民治愈）
        .addConversion(ChestCavityBeyond.of("human"), InitItem.ROTTEN_MUSCLE.get(), InitItem.MUSCLE.get())
        .addConversion(ChestCavityBeyond.of("human"), InitItem.ROTTEN_RIB.get(), InitItem.RIB.get())
        .addConversion(ChestCavityBeyond.of("human"), InitItem.ROTTEN_APPENDIX.get(), InitItem.APPENDIX.get())
        .addConversion(ChestCavityBeyond.of("human"), InitItem.ROTTEN_HEART.get(), InitItem.HEART.get())
        .addConversion(ChestCavityBeyond.of("human"), InitItem.ROTTEN_LUNG.get(), InitItem.LUNG.get())
        .addConversion(ChestCavityBeyond.of("human"), InitItem.ROTTEN_SPINE.get(), InitItem.SPINE.get())
        .addConversion(ChestCavityBeyond.of("human"), InitItem.ROTTEN_STOMACH.get(), InitItem.STOMACH.get())
        .addConversion(ChestCavityBeyond.of("human"), InitItem.ROTTEN_INTESTINE.get(), InitItem.INTESTINE.get())
        .addConversion(ChestCavityBeyond.of("human"), InitItem.ROTTEN_KIDNEY.get(), InitItem.KIDNEY.get())
        .addConversion(ChestCavityBeyond.of("human"), InitItem.ROTTEN_SPLEEN.get(), InitItem.SPLEEN.get())
        .addConversion(ChestCavityBeyond.of("human"), InitItem.ROTTEN_LIVER.get(), InitItem.LIVER.get());

    public static final ChestCavityType SKELETON = register(ChestCavityBeyond.of("skeleton"))
        .setNeedHealth(false)

        .setFirstRow(1, InitItem.ROTTEN_RIB.get())
        .setFirstRow(7, InitItem.ROTTEN_RIB.get())
        .setSecondRow(1, InitItem.ROTTEN_RIB.get())
        .setSecondRow(4, InitItem.ROTTEN_SPINE.get())
        .setSecondRow(7, InitItem.ROTTEN_RIB.get())

        .addValueBonuses(
            InitItem.ROTTEN_SPINE.get(), Map.of(
                InitAttribute.HEALTH, 0.5,
                InitAttribute.BREATH_CAPACITY, 0.5,
                InitAttribute.BREATH_RECOVERY, 0.5,
                InitAttribute.WATER_BREATH, 0.5
            )
        );

    public static final ChestCavityType WITHER_SKELETON = register(ChestCavityBeyond.of("wither_skeleton"))
        .setNeedHealth(false)

        .setFirstRow(1, InitItem.WITHERED_RIB.get())
        .setFirstRow(7, InitItem.WITHERED_RIB.get())

        .setSecondRow(1, InitItem.WITHERED_RIB.get())
        .setSecondRow(4, InitItem.WITHERED_SPINE.get())
        .setSecondRow(7, InitItem.WITHERED_RIB.get())

        .addValueBonuses(
            InitItem.WITHERED_SPINE.get(), Map.of(
                InitAttribute.HEALTH, 0.5,
                InitAttribute.BREATH_CAPACITY, 0.5,
                InitAttribute.BREATH_RECOVERY, 0.5,
                InitAttribute.WATER_BREATH, 0.5
            )
        );

    public static final ChestCavityType WITHER = register(ChestCavityBeyond.of("wither"))
        .setNeedHealth(false)

        .setFirstRow(0, InitItem.WRITHING_SOUL_SAND.get())
        .setFirstRow(1, InitItem.WITHERED_RIB.get())
        .setFirstRow(2, InitItem.WRITHING_SOUL_SAND.get())
        .setFirstRow(3, InitItem.WRITHING_SOUL_SAND.get())
        .setFirstRow(4, Items.NETHER_STAR)
        .setFirstRow(5, InitItem.WRITHING_SOUL_SAND.get())
        .setFirstRow(6, InitItem.WRITHING_SOUL_SAND.get())
        .setFirstRow(7, InitItem.WITHERED_RIB.get())
        .setFirstRow(8, InitItem.WRITHING_SOUL_SAND.get())

        .setSecondRow(0, InitItem.WRITHING_SOUL_SAND.get())
        .setSecondRow(1, InitItem.WITHERED_RIB.get())
        .setSecondRow(2, InitItem.WRITHING_SOUL_SAND.get())
        .setSecondRow(3, InitItem.WRITHING_SOUL_SAND.get())
        .setSecondRow(4, InitItem.WITHERED_SPINE.get())
        .setSecondRow(5, InitItem.WRITHING_SOUL_SAND.get())
        .setSecondRow(6, InitItem.WRITHING_SOUL_SAND.get())
        .setSecondRow(7, InitItem.WITHERED_RIB.get())
        .setSecondRow(8, InitItem.WRITHING_SOUL_SAND.get())

        .setThirdRow(0, InitItem.WRITHING_SOUL_SAND.get())
        .setThirdRow(1, InitItem.WRITHING_SOUL_SAND.get())
        .setThirdRow(2, InitItem.WRITHING_SOUL_SAND.get())
        .setThirdRow(3, InitItem.WRITHING_SOUL_SAND.get())
        .setThirdRow(4, InitItem.WRITHING_SOUL_SAND.get())
        .setThirdRow(5, InitItem.WRITHING_SOUL_SAND.get())
        .setThirdRow(6, InitItem.WRITHING_SOUL_SAND.get())
        .setThirdRow(7, InitItem.WRITHING_SOUL_SAND.get())
        .setThirdRow(8, InitItem.WRITHING_SOUL_SAND.get())

        .addValueBonuses(
            Items.NETHER_STAR, Map.of(
                InitAttribute.BREATH_CAPACITY, 1.0,
                InitAttribute.BREATH_RECOVERY, 1.0,
                InitAttribute.WATER_BREATH, 1.0
            )
        );

    public static final ChestCavityType ARTHROPOD = register(ChestCavityBeyond.of("arthropod"))
        .setFirstRow(0, InitItem.ARTHROPOD_MUSCLE.get())
        .setFirstRow(1, InitItem.ARTHROPOD_CAECUM.get())
        .setFirstRow(3, InitItem.ARTHROPOD_LUNG.get())
        .setFirstRow(4, InitItem.ARTHROPOD_HEART.get())
        .setFirstRow(5, InitItem.ARTHROPOD_LUNG.get())
        .setFirstRow(7, InitItem.ARTHROPOD_CAECUM.get())
        .setFirstRow(8, InitItem.ARTHROPOD_MUSCLE.get())

        .setSecondRow(0, InitItem.ARTHROPOD_MUSCLE.get())
        .setSecondRow(2, InitItem.ARTHROPOD_INTESTINE.get())
        .setSecondRow(3, InitItem.ARTHROPOD_INTESTINE.get())
        .setSecondRow(4, InitItem.ARTHROPOD_STOMACH.get())
        .setSecondRow(5, InitItem.ARTHROPOD_INTESTINE.get())
        .setSecondRow(6, InitItem.ARTHROPOD_INTESTINE.get())
        .setSecondRow(8, InitItem.ARTHROPOD_MUSCLE.get())

        .setThirdRow(0, InitItem.ARTHROPOD_MUSCLE.get())
        .setThirdRow(1, InitItem.ARTHROPOD_MUSCLE.get())
        .setThirdRow(2, InitItem.ARTHROPOD_MUSCLE.get())
        .setThirdRow(3, InitItem.ARTHROPOD_INTESTINE.get())
        .setThirdRow(4, InitItem.ARTHROPOD_STOMACH.get())
        .setThirdRow(5, InitItem.ARTHROPOD_INTESTINE.get())
        .setThirdRow(6, InitItem.ARTHROPOD_MUSCLE.get())
        .setThirdRow(7, InitItem.ARTHROPOD_MUSCLE.get())
        .setThirdRow(8, InitItem.ARTHROPOD_MUSCLE.get());

    public static final ChestCavityType SPIDER = register(ChestCavityBeyond.of("spider"))
        .copyWith(ARTHROPOD)
        .setSecondRow(2, InitItem.SILK_GLAND.get())
        .setSecondRow(6, InitItem.SILK_GLAND.get());

    public static final ChestCavityType CAVE_SPIDER = register(ChestCavityBeyond.of("cave_spider"))
        .copyWith(SPIDER)
        .setSecondRow(6, InitItem.VENOM_GLAND.get());

    public static final ChestCavityType AQUATIC = register(ChestCavityBeyond.of("aquatic"))
        .setFirstRow(0, InitItem.AQUATIC_MUSCLE.get())
        .setFirstRow(1, InitItem.ANIMAL_RIB.get())
        .setFirstRow(3, InitItem.GILL.get())
        .setFirstRow(4, InitItem.ANIMAL_HEART.get())
        .setFirstRow(5, InitItem.GILL.get())
        .setFirstRow(7, InitItem.ANIMAL_RIB.get())
        .setFirstRow(8, InitItem.AQUATIC_MUSCLE.get())

        .setSecondRow(0, InitItem.AQUATIC_MUSCLE.get())
        .setSecondRow(1, InitItem.ANIMAL_RIB.get())
        .setSecondRow(2, InitItem.ANIMAL_SPLEEN.get())
        .setSecondRow(3, InitItem.ANIMAL_KIDNEY.get())
        .setSecondRow(4, InitItem.ANIMAL_SPINE.get())
        .setSecondRow(5, InitItem.ANIMAL_KIDNEY.get())
        .setSecondRow(6, InitItem.ANIMAL_LIVER.get())
        .setSecondRow(7, InitItem.ANIMAL_RIB.get())
        .setSecondRow(8, InitItem.AQUATIC_MUSCLE.get())

        .setThirdRow(0, InitItem.AQUATIC_MUSCLE.get())
        .setThirdRow(1, InitItem.AQUATIC_MUSCLE.get())
        .setThirdRow(2, InitItem.ANIMAL_INTESTINE.get())
        .setThirdRow(3, InitItem.ANIMAL_INTESTINE.get())
        .setThirdRow(4, InitItem.ANIMAL_STOMACH.get())
        .setThirdRow(5, InitItem.ANIMAL_INTESTINE.get())
        .setThirdRow(6, InitItem.ANIMAL_INTESTINE.get())
        .setThirdRow(7, InitItem.AQUATIC_MUSCLE.get())
        .setThirdRow(8, InitItem.AQUATIC_MUSCLE.get());

    public static final ChestCavityType DOLPHIN = register(ChestCavityBeyond.of("dolphin"))
        .copyWith(AQUATIC)
        .setFirstRow(3, InitItem.ANIMAL_LUNG.get())
        .setFirstRow(5, InitItem.ANIMAL_LUNG.get());

    public static final ChestCavityType FISH = register(ChestCavityBeyond.of("fish"))
        .copyWith(AQUATIC)
        .setFirstRow(0, InitItem.FISH_MUSCLE.get())
        .setFirstRow(1, InitItem.FISH_BONE.get())
        .setFirstRow(7, InitItem.FISH_BONE.get())
        .setFirstRow(8, InitItem.FISH_MUSCLE.get())

        .setSecondRow(0, InitItem.FISH_MUSCLE.get())
        .setSecondRow(1, InitItem.FISH_BONE.get())
        .setSecondRow(4, InitItem.FISH_SPINE.get())
        .setSecondRow(7, InitItem.FISH_BONE.get())
        .setSecondRow(8, InitItem.FISH_MUSCLE.get())

        .setThirdRow(0, InitItem.FISH_MUSCLE.get())
        .setThirdRow(1, InitItem.FISH_MUSCLE.get())
        .setThirdRow(2, InitItem.FISH_INTESTINE.get())
        .setThirdRow(3, InitItem.FISH_INTESTINE.get())
        .setThirdRow(5, InitItem.FISH_INTESTINE.get())
        .setThirdRow(6, InitItem.FISH_INTESTINE.get())
        .setThirdRow(7, InitItem.FISH_MUSCLE.get())
        .setThirdRow(8, InitItem.FISH_MUSCLE.get());


    public static final ChestCavityType SMALL_AQUATIC = register(ChestCavityBeyond.of("small_aquatic"))
        .setFirstRow(0, InitItem.SMALL_AQUATIC_MUSCLE.get())
        .setFirstRow(1, InitItem.SMALL_ANIMAL_RIB.get())
        .setFirstRow(3, InitItem.SMALL_GILL.get())
        .setFirstRow(4, InitItem.SMALL_ANIMAL_HEART.get())
        .setFirstRow(5, InitItem.SMALL_GILL.get())
        .setFirstRow(7, InitItem.SMALL_ANIMAL_RIB.get())
        .setFirstRow(8, InitItem.SMALL_AQUATIC_MUSCLE.get())

        .setSecondRow(0, InitItem.SMALL_AQUATIC_MUSCLE.get())
        .setSecondRow(1, InitItem.SMALL_ANIMAL_RIB.get())
        .setSecondRow(2, InitItem.SMALL_ANIMAL_SPLEEN.get())
        .setSecondRow(3, InitItem.SMALL_ANIMAL_KIDNEY.get())
        .setSecondRow(4, InitItem.SMALL_ANIMAL_SPINE.get())
        .setSecondRow(5, InitItem.SMALL_ANIMAL_KIDNEY.get())
        .setSecondRow(6, InitItem.SMALL_ANIMAL_LIVER.get())
        .setSecondRow(7, InitItem.SMALL_ANIMAL_RIB.get())
        .setSecondRow(8, InitItem.SMALL_AQUATIC_MUSCLE.get())

        .setThirdRow(0, InitItem.SMALL_AQUATIC_MUSCLE.get())
        .setThirdRow(1, InitItem.SMALL_AQUATIC_MUSCLE.get())
        .setThirdRow(2, InitItem.SMALL_ANIMAL_INTESTINE.get())
        .setThirdRow(3, InitItem.SMALL_ANIMAL_INTESTINE.get())
        .setThirdRow(4, InitItem.SMALL_ANIMAL_STOMACH.get())
        .setThirdRow(5, InitItem.SMALL_ANIMAL_INTESTINE.get())
        .setThirdRow(6, InitItem.SMALL_ANIMAL_INTESTINE.get())
        .setThirdRow(7, InitItem.SMALL_AQUATIC_MUSCLE.get())
        .setThirdRow(8, InitItem.SMALL_AQUATIC_MUSCLE.get());

    public static final ChestCavityType SMALL_FISH = register(ChestCavityBeyond.of("small_fish"))
        .copyWith(SMALL_AQUATIC)
        .setFirstRow(0, InitItem.SMALL_FISH_MUSCLE.get())
        .setFirstRow(1, InitItem.SMALL_FISH_BONE.get())
        .setFirstRow(7, InitItem.SMALL_FISH_BONE.get())
        .setFirstRow(8, InitItem.SMALL_FISH_MUSCLE.get())

        .setSecondRow(0, InitItem.SMALL_FISH_MUSCLE.get())
        .setSecondRow(1, InitItem.SMALL_FISH_BONE.get())
        .setSecondRow(4, InitItem.SMALL_FISH_SPINE.get())
        .setSecondRow(7, InitItem.SMALL_FISH_BONE.get())
        .setSecondRow(8, InitItem.SMALL_FISH_MUSCLE.get())

        .setThirdRow(0, InitItem.SMALL_FISH_MUSCLE.get())
        .setThirdRow(1, InitItem.SMALL_FISH_MUSCLE.get())
        .setThirdRow(2, InitItem.SMALL_FISH_INTESTINE.get())
        .setThirdRow(3, InitItem.SMALL_FISH_INTESTINE.get())
        .setThirdRow(5, InitItem.SMALL_FISH_INTESTINE.get())
        .setThirdRow(6, InitItem.SMALL_FISH_INTESTINE.get())
        .setThirdRow(7, InitItem.SMALL_FISH_MUSCLE.get())
        .setThirdRow(8, InitItem.SMALL_FISH_MUSCLE.get());

    public static final ChestCavityType SALTWATER = register(ChestCavityBeyond.of("saltwater"))
        .setFirstRow(0, InitItem.SALTWATER_MUSCLE.get())
        .setFirstRow(1, InitItem.RIB.get())
        .setFirstRow(2, InitItem.APPENDIX.get())
        .setFirstRow(3, InitItem.SALTWATER_LUNG.get())
        .setFirstRow(4, InitItem.SALTWATER_HEART.get())
        .setFirstRow(5, InitItem.SALTWATER_LUNG.get())
        .setFirstRow(7, InitItem.RIB.get())
        .setFirstRow(8, InitItem.SALTWATER_MUSCLE.get())

        .setSecondRow(0, InitItem.SALTWATER_MUSCLE.get())
        .setSecondRow(1, InitItem.RIB.get())
        .setSecondRow(2, InitItem.SPLEEN.get())
        .setSecondRow(3, InitItem.KIDNEY.get())
        .setSecondRow(4, InitItem.SPINE.get())
        .setSecondRow(5, InitItem.KIDNEY.get())
        .setSecondRow(6, InitItem.LIVER.get())
        .setSecondRow(7, InitItem.RIB.get())
        .setSecondRow(8, InitItem.SALTWATER_MUSCLE.get())

        .setThirdRow(0, InitItem.SALTWATER_MUSCLE.get())
        .setThirdRow(1, InitItem.SALTWATER_MUSCLE.get())
        .setThirdRow(2, InitItem.INTESTINE.get())
        .setThirdRow(3, InitItem.INTESTINE.get())
        .setThirdRow(4, InitItem.STOMACH.get())
        .setThirdRow(5, InitItem.INTESTINE.get())
        .setThirdRow(6, InitItem.INTESTINE.get())
        .setThirdRow(7, InitItem.SALTWATER_MUSCLE.get())
        .setThirdRow(8, InitItem.SALTWATER_MUSCLE.get());

    public static final ChestCavityType CREEPER = register(ChestCavityBeyond.of("creeper"))
        .setFirstRow(0, InitItem.CREEPER_LEAF.get())
        .setFirstRow(1, InitItem.ANIMAL_RIB.get())
        .setFirstRow(4, InitItem.CREEPER_APPENDIX.get())
        .setFirstRow(7, InitItem.ANIMAL_RIB.get())
        .setFirstRow(8, InitItem.CREEPER_LEAF.get())

        .setSecondRow(0, InitItem.CREEPER_LEAF.get())
        .setSecondRow(1, InitItem.ANIMAL_RIB.get())
        .setSecondRow(3, Items.GUNPOWDER)
        .setSecondRow(4, Items.OAK_LOG)
        .setSecondRow(5, Items.GUNPOWDER)
        .setSecondRow(7, InitItem.ANIMAL_RIB.get())
        .setSecondRow(8, InitItem.CREEPER_LEAF.get())

        .setThirdRow(0, InitItem.CREEPER_LEAF.get())
        .setThirdRow(1, InitItem.CREEPER_LEAF.get())
        .setThirdRow(2, InitItem.CREEPER_LEAF.get())
        .setThirdRow(4, Items.GUNPOWDER)
        .setThirdRow(6, InitItem.CREEPER_LEAF.get())
        .setThirdRow(7, InitItem.CREEPER_LEAF.get())
        .setThirdRow(8, InitItem.CREEPER_LEAF.get())

        .addValueBonuses(
            Items.OAK_LOG, Map.of(
                InitAttribute.HEALTH, 1.0,
                InitAttribute.NERVES, 1.0
            )
        )
        .addValueBonuses(
            InitItem.CREEPER_LEAF.get(), Map.of(
                InitAttribute.BREATH_CAPACITY, 1.0,
                InitAttribute.BREATH_RECOVERY, 1.0
            )
        );

    public static final ChestCavityType BLAZE = register(ChestCavityBeyond.of("blaze"))
        .setFirstRow(1, InitItem.ACTIVE_BLAZE_ROD.get())
        .setFirstRow(2, InitItem.BLAZE_SHELL.get())
        .setFirstRow(3, Items.MAGMA_BLOCK)
        .setFirstRow(4, Items.MAGMA_BLOCK)
        .setFirstRow(5, Items.MAGMA_BLOCK)
        .setFirstRow(6, InitItem.BLAZE_SHELL.get())

        .setSecondRow(2, InitItem.BLAZE_SHELL.get())
        .setSecondRow(3, Items.MAGMA_BLOCK)
        .setSecondRow(4, InitItem.BLAZE_CORE.get())
        .setSecondRow(5, Items.MAGMA_BLOCK)
        .setSecondRow(6, InitItem.BLAZE_SHELL.get())
        .setSecondRow(7, InitItem.ACTIVE_BLAZE_ROD.get())

        .setThirdRow(0, InitItem.ACTIVE_BLAZE_ROD.get())
        .setThirdRow(2, InitItem.BLAZE_SHELL.get())
        .setThirdRow(3, Items.MAGMA_BLOCK)
        .setThirdRow(4, Items.MAGMA_BLOCK)
        .setThirdRow(5, Items.MAGMA_BLOCK)
        .setThirdRow(6, InitItem.BLAZE_SHELL.get())

        .addValueBonuses(
            Items.MAGMA_BLOCK, Map.of(
                InitAttribute.BREATH_CAPACITY, 1.0,
                InitAttribute.BREATH_RECOVERY, 1.0
            )
        );

    public static final ChestCavityType BREEZE = register(ChestCavityBeyond.of("breeze"))
        .setFirstRow(3, InitItem.ACTIVE_BREEZE_ROD.get())
        .setFirstRow(4, InitItem.BREEZE_CORE.get())
        .setFirstRow(5, InitItem.ACTIVE_BREEZE_ROD.get())

        .setSecondRow(3, Items.WIND_CHARGE)
        .setSecondRow(4, InitItem.ACTIVE_BREEZE_ROD.get())
        .setSecondRow(5, Items.WIND_CHARGE)

        .setThirdRow(4, Items.WIND_CHARGE)

        .addValueBonuses(
            Items.WIND_CHARGE, Map.of(
                InitAttribute.BREATH_CAPACITY, 2.0,
                InitAttribute.BREATH_RECOVERY, 2.0
            )
        )
        .addValueBonuses(
            InitItem.BREEZE_CORE.get(), Map.of(
                InitAttribute.NERVES, 1.0
            )
        );

    public static final ChestCavityType IRON_GOLEM = register(ChestCavityBeyond.of("iron_golem"))
        .setNeedBreath(false)

        .setFirstRow(0, InitItem.GOLEM_ARMOR_PLATE.get())
        .setFirstRow(1, InitItem.PISTON_MUSCLE.get())
        .setFirstRow(2, InitItem.PISTON_MUSCLE.get())
        .setFirstRow(3, InitItem.GOLEM_ARMOR_PLATE.get())
        .setFirstRow(4, InitItem.GOLEM_CORE.get())
        .setFirstRow(5, InitItem.GOLEM_ARMOR_PLATE.get())
        .setFirstRow(6, InitItem.PISTON_MUSCLE.get())
        .setFirstRow(7, InitItem.PISTON_MUSCLE.get())
        .setFirstRow(8, InitItem.GOLEM_ARMOR_PLATE.get())

        .setSecondRow(0, InitItem.GOLEM_ARMOR_PLATE.get())
        .setSecondRow(1, InitItem.PISTON_MUSCLE.get())
        .setSecondRow(2, InitItem.PISTON_MUSCLE.get())
        .setSecondRow(3, InitItem.GOLEM_ARMOR_PLATE.get())
        .setSecondRow(4, InitItem.GOLEM_CABLE.get())
        .setSecondRow(5, InitItem.GOLEM_ARMOR_PLATE.get())
        .setSecondRow(6, InitItem.PISTON_MUSCLE.get())
        .setSecondRow(7, InitItem.PISTON_MUSCLE.get())
        .setSecondRow(8, InitItem.GOLEM_ARMOR_PLATE.get())

        .setThirdRow(0, InitItem.GOLEM_ARMOR_PLATE.get())
        .setThirdRow(1, InitItem.PISTON_MUSCLE.get())
        .setThirdRow(2, InitItem.INNER_FURNACE.get())
        .setThirdRow(3, InitItem.INNER_FURNACE.get())
        .setThirdRow(4, InitItem.INNER_FURNACE.get())
        .setThirdRow(5, InitItem.INNER_FURNACE.get())
        .setThirdRow(6, InitItem.INNER_FURNACE.get())
        .setThirdRow(7, InitItem.PISTON_MUSCLE.get())
        .setThirdRow(8, InitItem.GOLEM_ARMOR_PLATE.get());

    public static final ChestCavityType SNOW_GOLEM = register(ChestCavityBeyond.of("snow_golem"))
        .setFirstRow(3, Items.SNOWBALL)
        .setFirstRow(4, Items.SNOWBALL)
        .setFirstRow(5, Items.SNOWBALL)

        .setSecondRow(3, Items.SNOWBALL)
        .setSecondRow(4, InitItem.SNOW_CORE.get())
        .setSecondRow(5, Items.SNOWBALL)

        .setThirdRow(3, Items.SNOWBALL)
        .setThirdRow(4, Items.SNOWBALL)
        .setThirdRow(5, Items.SNOWBALL)

        .addValueBonuses(
            InitItem.SNOW_CORE.get(), Map.of(
                InitAttribute.NERVES, 1.0,
                InitAttribute.BREATH_CAPACITY, 1.0,
                InitAttribute.BREATH_RECOVERY, 1.0
            )
        );

    public static final ChestCavityType WARDEN = register(ChestCavityBeyond.of("warden"))
        .setFirstRow(0, InitItem.SCULK_MUSCLE.get())
        .setFirstRow(1, InitItem.SCULK_RIB.get())
        .setFirstRow(4, InitItem.SCULK_HEART.get())
        .setFirstRow(7, InitItem.SCULK_RIB.get())
        .setFirstRow(8, InitItem.SCULK_MUSCLE.get())

        .setSecondRow(0, InitItem.SCULK_MUSCLE.get())
        .setSecondRow(1, InitItem.SCULK_RIB.get())
        .setSecondRow(4, InitItem.SCULK_CORE.get())
        .setSecondRow(7, InitItem.SCULK_RIB.get())
        .setSecondRow(8, InitItem.SCULK_MUSCLE.get())

        .setThirdRow(0, InitItem.SCULK_MUSCLE.get())
        .setThirdRow(1, InitItem.SCULK_MUSCLE.get())
        .setThirdRow(7, InitItem.SCULK_MUSCLE.get())
        .setThirdRow(8, InitItem.SCULK_MUSCLE.get())

        .addValueBonuses(
            InitItem.SCULK_CORE.get(), Map.of(
                InitAttribute.BREATH_CAPACITY, 1.0,
                InitAttribute.BREATH_RECOVERY, 1.0
            )
        );

    public static final ChestCavityType ELDER = register(ChestCavityBeyond.of("elder"))
        .setFirstRow(0, InitItem.ELDER_MUSCLE.get())
        .setFirstRow(1, InitItem.ELDER_RIB.get())
        .setFirstRow(2, InitItem.ELDER_APPENDIX.get())
        .setFirstRow(3, InitItem.ELDER_GILL.get())
        .setFirstRow(4, InitItem.ELDER_HEART.get())
        .setFirstRow(5, InitItem.ELDER_GILL.get())
        .setFirstRow(7, InitItem.ELDER_RIB.get())
        .setFirstRow(8, InitItem.ELDER_MUSCLE.get())

        .setSecondRow(0, InitItem.ELDER_MUSCLE.get())
        .setSecondRow(1, InitItem.ELDER_RIB.get())
        .setSecondRow(2, InitItem.ELDER_SPLEEN.get())
        .setSecondRow(3, InitItem.ELDER_KIDNEY.get())
        .setSecondRow(4, InitItem.ELDER_SPINE.get())
        .setSecondRow(5, InitItem.ELDER_KIDNEY.get())
        .setSecondRow(6, InitItem.ELDER_LIVER.get())
        .setSecondRow(7, InitItem.ELDER_RIB.get())
        .setSecondRow(8, InitItem.ELDER_MUSCLE.get())

        .setThirdRow(0, InitItem.ELDER_MUSCLE.get())
        .setThirdRow(1, InitItem.ELDER_MUSCLE.get())
        .setThirdRow(2, InitItem.ELDER_INTESTINE.get())
        .setThirdRow(3, InitItem.ELDER_INTESTINE.get())
        .setThirdRow(4, InitItem.ELDER_STOMACH.get())
        .setThirdRow(5, InitItem.ELDER_INTESTINE.get())
        .setThirdRow(6, InitItem.ELDER_INTESTINE.get())
        .setThirdRow(7, InitItem.ELDER_MUSCLE.get())
        .setThirdRow(8, InitItem.ELDER_MUSCLE.get());

    public static final ChestCavityType ELDER_FISH = register(ChestCavityBeyond.of("elder_fish"))
        .copyWith(ELDER)
        .setFirstRow(0, InitItem.ELDER_FISH_MUSCLE.get())
        .setFirstRow(1, InitItem.ELDER_FISH_BONE.get())
        .setFirstRow(3, InitItem.ELDER_GILL.get())
        .setFirstRow(4, InitItem.ELDER_HEART.get())
        .setFirstRow(5, InitItem.ELDER_GILL.get())
        .setFirstRow(7, InitItem.ELDER_FISH_BONE.get())
        .setFirstRow(8, InitItem.ELDER_FISH_MUSCLE.get())

        .setSecondRow(0, InitItem.ELDER_FISH_MUSCLE.get())
        .setSecondRow(1, InitItem.ELDER_FISH_BONE.get())
        .setSecondRow(4, InitItem.ELDER_FISH_SPINE.get())
        .setSecondRow(7, InitItem.ELDER_FISH_BONE.get())
        .setSecondRow(8, InitItem.ELDER_FISH_MUSCLE.get())

        .setThirdRow(0, InitItem.ELDER_FISH_MUSCLE.get())
        .setThirdRow(1, InitItem.ELDER_FISH_MUSCLE.get())
        .setThirdRow(2, InitItem.ELDER_FISH_INTESTINE.get())
        .setThirdRow(3, InitItem.ELDER_FISH_INTESTINE.get())
        .setThirdRow(5, InitItem.ELDER_FISH_INTESTINE.get())
        .setThirdRow(6, InitItem.ELDER_FISH_INTESTINE.get())
        .setThirdRow(7, InitItem.ELDER_FISH_MUSCLE.get())
        .setThirdRow(8, InitItem.ELDER_FISH_MUSCLE.get());

    public static final ChestCavityType GUARDIAN = register(ChestCavityBeyond.of("guardian"))
        .copyWith(FISH)
        .setFirstRow(2, InitItem.GILL.get())
        .setFirstRow(3, Items.AIR)
        .setFirstRow(4, InitItem.GUARDIAN_EYE.get())
        .setFirstRow(5, InitItem.ANIMAL_HEART.get())
        .setFirstRow(6, InitItem.GILL.get());

    public static final ChestCavityType ELDER_GUARDIAN = register(ChestCavityBeyond.of("elder_guardian"))
        .copyWith(ELDER_FISH)
        .setFirstRow(2, InitItem.ELDER_GILL.get())
        .setFirstRow(3, Items.AIR)
        .setFirstRow(4, InitItem.ELDER_GUARDIAN_EYE.get())
        .setFirstRow(5, InitItem.ELDER_HEART.get())
        .setFirstRow(6, InitItem.ELDER_GILL.get());

    public static final ChestCavityType ARMOR_STAND = register(ChestCavityBeyond.of("armor_stand"))
        .setNeedBreath(false)
        .setNeedHealth(false)
        .setFirstRow(3, Items.STICK)
        .setFirstRow(4, Items.STICK)
        .setFirstRow(5, Items.STICK)

        .setSecondRow(4, Items.STICK)

        .setThirdRow(4, Items.STICK);

    /**
     * 获取实体的胸腔类型
     *
     * @param entity 实体
     * @return 胸腔类型
     */
    public static ChestCavityType getType(LivingEntity entity) {
        ChestCavityType type = ENTITY_CHEST_CAVITY_TYPE_MAP.get(entity.getType());
        if (type == null) {
            EntityType<? extends LivingEntity> entityType = (EntityType<? extends LivingEntity>) entity.getType();
            // 以下为 instanceof 回退检测，子类必须在父类之前

            // 远古守卫者（Guardian 的子类，先于 Guardian 检测）
            if (entity instanceof ElderGuardian) {
                return registerEntity(entityType, ELDER_GUARDIAN);
            }
            // 守卫者（水生类别 WATER_CREATURE，需在水生检测之前）
            if (entity instanceof Guardian) {
                return registerEntity(entityType, GUARDIAN);
            }
            // 凋灵骷髅（AbstractSkeleton 的子类，先于普通骷髅检测）
            if (entity instanceof WitherSkeleton) {
                return registerEntity(entityType, WITHER_SKELETON);
            }
            // 骷髅
            if (entity instanceof AbstractSkeleton) {
                return registerEntity(entityType, SKELETON);
            }
            // 洞穴蜘蛛（Spider 的子类，先于蜘蛛检测）
            if (entity instanceof CaveSpider) {
                return registerEntity(entityType, CAVE_SPIDER);
            }
            // 蜘蛛
            if (entity instanceof Spider) {
                return registerEntity(entityType, SPIDER);
            }
            // 亡灵
            if (entity instanceof Zombie || entityType.is(EntityTypeTags.UNDEAD)) {
                return registerEntity(entityType, UNDEAD);
            }
            // 苦力怕
            if (entity instanceof Creeper) {
                return registerEntity(entityType, CREEPER);
            }
            // 烈焰人
            if (entity instanceof Blaze) {
                return registerEntity(entityType, BLAZE);
            }
            // 末影人
            if (entity instanceof EnderMan) {
                return registerEntity(entityType, ENDER);
            }
            // 末影龙
            if (entity instanceof EnderDragon) {
                return registerEntity(entityType, ENDER_DRAGON);
            }
            // 恶魂
            if (entity instanceof Ghast) {
                return registerEntity(entityType, GHAST);
            }
            // 凋灵
            if (entity instanceof WitherBoss) {
                return registerEntity(entityType, WITHER);
            }
            // 潜影贝
            if (entity instanceof Shulker) {
                return registerEntity(entityType, SHULKER);
            }
            // 铁傀儡
            if (entity instanceof IronGolem) {
                return registerEntity(entityType, IRON_GOLEM);
            }
            // 雪傀儡
            if (entity instanceof SnowGolem) {
                return registerEntity(entityType, SNOW_GOLEM);
            }
            // 旋风人
            if (entity instanceof Breeze) {
                return registerEntity(entityType, BREEZE);
            }
            // 监守者
            if (entity instanceof Warden) {
                return registerEntity(entityType, WARDEN);
            }
            // 羊驼（Animal 子类，需在动物检测之前）
            if (entity instanceof Llama) {
                return registerEntity(entityType, LLAMA);
            }
            // 鱼类（AbstractFish 子类，含蝌蚪等）
            if (entity instanceof AbstractFish) {
                return registerEntity(entityType, FISH);
            }
            // 水生生物（鳃呼吸）
            // 注意：此处会拦住部分 Animal 子类的水生生物（如美西螈，在水中呼吸、上岸窒息）
            // 也会提前拦住一些可以在水下呼吸的生物，但难以通过简单的 instanceof/category 判断来区分真正的两栖生物，因此需要注意一下
            if (entity instanceof WaterAnimal
                || entityType.getCategory() == MobCategory.WATER_CREATURE
                || entityType.getCategory() == MobCategory.WATER_AMBIENT
                || entityType.getCategory() == MobCategory.UNDERGROUND_WATER_CREATURE) {
                return registerEntity(entityType, AQUATIC);
            }
            // 动物（陆地动物）
            // 上方水生检测可能已提前拦住部分可在水下呼吸的 Animal 子类
            if (entity instanceof Animal || entityType.getCategory() == MobCategory.CREATURE) {
                return registerEntity(entityType, ANIMAL);
            }
            // 以上检测全未通过，就注册一套新的人类器官给它
            return registerEntity(entityType, HUMAN);
        }
        return type;
    }

    /**
     * 根据注册名获取
     *
     * @param name 名字
     * @return 胸腔类型
     */
    public static ChestCavityType getType(ResourceLocation name) {
        // 找不到就返回人类器官
        return CHEST_CAVITY_TYPE_REGISTRY.getOrDefault(name, HUMAN);
    }

    /**
     * 为实体类型注册胸腔类型
     *
     * @param entityType      实体类型
     * @param chestCavityType 胸腔类型
     * @return 胸腔类型
     */
    public static ChestCavityType registerEntity(EntityType<? extends LivingEntity> entityType, ChestCavityType chestCavityType) {
        ENTITY_CHEST_CAVITY_TYPE_MAP.put(entityType, chestCavityType.builder(entityType));
        return chestCavityType;
    }

    /**
     * 通过注册名为实体类型注册胸腔类型
     * <p>
     * 用于第三方模组实体兼容注册
     * </p>
     *
     * @param entityTypeId    实体类型的注册名
     * @param chestCavityType 要分配的胸腔类型
     */
    public static void registerEntity(ResourceLocation entityTypeId, ChestCavityType chestCavityType) {
        BuiltInRegistries.ENTITY_TYPE.getOptional(entityTypeId)
            .ifPresent(entityType -> registerEntity((EntityType<? extends LivingEntity>) entityType, chestCavityType));
    }

    /**
     * 注册胸腔类型
     *
     * @param name 类型名称
     * @return 胸腔类型
     */
    public static ChestCavityType register(ResourceLocation name) {
        ChestCavityType chestCavityType = new ChestCavityType();
        chestCavityType.setId(name);
        CHEST_CAVITY_TYPE_REGISTRY.put(name, chestCavityType);
        return chestCavityType;
    }
}
