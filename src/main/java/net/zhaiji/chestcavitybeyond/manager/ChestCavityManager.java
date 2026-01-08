package net.zhaiji.chestcavitybeyond.manager;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.register.InitItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChestCavityManager {
    public static final List<ChestCavityType> CHEST_CAVITY_TYPES = new ArrayList<>();

    public static final Map<EntityType<? extends LivingEntity>, ChestCavityType> ENTITY_CHEST_CAVITY_TYPE_MAP = new HashMap<>();

    public static final ChestCavityType HUMAN = register("human")
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
            .setThirdRow(8, InitItem.MUSCLE.get());

    public static final ChestCavityType ANIMAL = register("animal")
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
            .setThirdRow(8, InitItem.ANIMAL_MUSCLE.get());

    public static final ChestCavityType LLAMA = register("llama")
            .copyWith(ANIMAL)
            .setFirstRow(3, InitItem.LLAMA_LUNG.get())
            .setFirstRow(5, InitItem.LLAMA_LUNG.get());

    public static final ChestCavityType HERBIVORE2 = register("herbivore")
            .copyWith(ANIMAL)
            .setFirstRow(3, InitItem.HERBIVORE_RUMEN.get())
            .setFirstRow(5, InitItem.HERBIVORE_RUMEN.get())
            .setThirdRow(2, InitItem.HERBIVORE_INTESTINE.get())
            .setThirdRow(4, InitItem.HERBIVORE_STOMACH.get())
            .setThirdRow(6, InitItem.HERBIVORE_INTESTINE.get());

    public static final ChestCavityType HERBIVORE1 = register("equid")
            .copyWith(HERBIVORE2)
            .setThirdRow(3, InitItem.HERBIVORE_INTESTINE.get())
            .setThirdRow(5, InitItem.HERBIVORE_INTESTINE.get());

    public static final ChestCavityType CARNIVORE = register("carnivore")
            .copyWith(ANIMAL)
            .setThirdRow(2, InitItem.CARNIVORE_INTESTINE.get())
            .setThirdRow(3, InitItem.CARNIVORE_INTESTINE.get())
            .setThirdRow(4, InitItem.CARNIVORE_STOMACH.get())
            .setThirdRow(5, InitItem.CARNIVORE_INTESTINE.get())
            .setThirdRow(6, InitItem.CARNIVORE_INTESTINE.get());

    public static final ChestCavityType SMALL_ANIMAL = register("small_animal")
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

    public static final ChestCavityType SMALL_HERBIVORE = register("small_herbivore")
            .copyWith(SMALL_ANIMAL)
            .setThirdRow(2, InitItem.SMALL_HERBIVORE_INTESTINE.get())
            .setThirdRow(3, InitItem.SMALL_HERBIVORE_INTESTINE.get())
            .setThirdRow(4, InitItem.SMALL_HERBIVORE_STOMACH.get())
            .setThirdRow(5, InitItem.SMALL_HERBIVORE_INTESTINE.get())
            .setThirdRow(6, InitItem.SMALL_HERBIVORE_INTESTINE.get());

    public static final ChestCavityType SMALL_CARNIVORE = register("small_carnivore")
            .copyWith(SMALL_ANIMAL)
            .setThirdRow(2, InitItem.SMALL_CARNIVORE_INTESTINE.get())
            .setThirdRow(3, InitItem.SMALL_CARNIVORE_INTESTINE.get())
            .setThirdRow(4, InitItem.SMALL_CARNIVORE_STOMACH.get())
            .setThirdRow(5, InitItem.SMALL_CARNIVORE_INTESTINE.get())
            .setThirdRow(6, InitItem.SMALL_CARNIVORE_INTESTINE.get());

    public static final ChestCavityType RABBIT = register("rabbit")
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

    public static final ChestCavityType SLIME = register("slime")
            .setNeedBreath(false)
            .setFirstRow(4, Items.SLIME_BALL)

            .setSecondRow(3, Items.SLIME_BALL)
            .setSecondRow(4, InitItem.SLIME_CORE.get())
            .setSecondRow(5, Items.SLIME_BALL)

            .setThirdRow(4, Items.SLIME_BALL);

    public static final ChestCavityType MAGMA_CUBE = register("magma_cube")
            .setNeedBreath(false)
            .setFirstRow(4, Items.MAGMA_CREAM)

            .setSecondRow(3, Items.MAGMA_CREAM)
            .setSecondRow(4, InitItem.MAGMA_CUBE_CORE.get())
            .setSecondRow(5, Items.MAGMA_CREAM)

            .setThirdRow(4, Items.MAGMA_CREAM);

    public static final ChestCavityType FIREPROOF = register("fireproof")
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

    public static final ChestCavityType ENDER = register("ender")
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

    public static final ChestCavityType ENDER_DRAGON = register("ender_dragon")
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

    public static final ChestCavityType UNDEAD = register("undead")
            .setNeedBreath(false)
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
            .setThirdRow(8, InitItem.ROTTEN_MUSCLE.get());

    public static final ChestCavityType SKELETON = register("skeleton")
            .setNeedBreath(false)
            .setFirstRow(1, InitItem.ROTTEN_RIB.get())
            .setFirstRow(7, InitItem.ROTTEN_RIB.get())
            .setSecondRow(1, InitItem.ROTTEN_RIB.get())
            .setSecondRow(4, InitItem.ROTTEN_SPINE.get())
            .setSecondRow(7, InitItem.ROTTEN_RIB.get());

    public static final ChestCavityType WITHER_SKELETON = register("wither_skeleton")
            .setNeedBreath(false)
            .setFirstRow(1, InitItem.WITHERED_RIB.get())
            .setFirstRow(7, InitItem.WITHERED_RIB.get())
            .setSecondRow(1, InitItem.WITHERED_RIB.get())
            .setSecondRow(4, InitItem.WITHERED_SPINE.get())
            .setSecondRow(7, InitItem.WITHERED_RIB.get());

    public static final ChestCavityType WITHER = register("wither")
            .setNeedBreath(false)
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
            .setThirdRow(8, InitItem.WRITHING_SOUL_SAND.get());

    public static final ChestCavityType ARTHROPOD = register("arthropod")
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

    public static final ChestCavityType SPIDER = register("spider")
            .copyWith(ARTHROPOD)
            .setSecondRow(2, InitItem.SILK_GLAND.get())
            .setSecondRow(6, InitItem.SILK_GLAND.get());

    public static final ChestCavityType CAVE_SPIDER = register("cave_spider")
            .copyWith(SPIDER)
            .setSecondRow(6, InitItem.VENOM_GLAND.get());

    public static final ChestCavityType AQUATIC = register("aquatic")
            .setFirstRow(0, InitItem.AQUATIC_MUSCLE.get())
            .setFirstRow(1, InitItem.ANIMAL_RIB.get())
            .setFirstRow(3, InitItem.GILLS.get())
            .setFirstRow(4, InitItem.ANIMAL_HEART.get())
            .setFirstRow(5, InitItem.GILLS.get())
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

    public static final ChestCavityType DOLPHIN = register("dolphin")
            .copyWith(AQUATIC)
            .setFirstRow(3, InitItem.ANIMAL_LUNG.get())
            .setFirstRow(5, InitItem.ANIMAL_LUNG.get());

    public static final ChestCavityType SMALL_AQUATIC = register("small_aquatic")
            .setFirstRow(0, InitItem.SMALL_AQUATIC_MUSCLE.get())
            .setFirstRow(1, InitItem.SMALL_ANIMAL_RIB.get())
            .setFirstRow(3, InitItem.SMALL_GILLS.get())
            .setFirstRow(4, InitItem.SMALL_ANIMAL_HEART.get())
            .setFirstRow(5, InitItem.SMALL_GILLS.get())
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

    public static final ChestCavityType FISH = register("fish")
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
            .setThirdRow(7, InitItem.FISH_MUSCLE.get())
            .setThirdRow(8, InitItem.FISH_MUSCLE.get());

    public static final ChestCavityType SMALL_FISH = register("small_fish")
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
            .setThirdRow(7, InitItem.SMALL_FISH_MUSCLE.get())
            .setThirdRow(8, InitItem.SMALL_FISH_MUSCLE.get());

    public static final ChestCavityType SALTWATER = register("saltwater")
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

    public static final ChestCavityType CREEPER = register("creeper")
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
            .setThirdRow(8, InitItem.CREEPER_LEAF.get());

    public static final ChestCavityType BLAZE = register("blaze")
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
            .setThirdRow(6, InitItem.BLAZE_SHELL.get());

    public static final ChestCavityType GHAST = register("ghast")
            .copyWith(FIREPROOF)
            .setFirstRow(1, InitItem.GAS_SAC.get())
            .setFirstRow(3, InitItem.GAS_SAC.get())
            .setFirstRow(5, InitItem.GAS_SAC.get())
            .setFirstRow(7, InitItem.GAS_SAC.get())
            .setThirdRow(4, InitItem.GHAST_STOMACH.get());

    public static final ChestCavityType SHULKER = register("shulker")
            .copyWith(ANIMAL)
            .setFirstRow(2, InitItem.ENDER_APPENDIX.get())
            .setSecondRow(2, InitItem.SHULKER_SPLEEN.get())
            .setSecondRow(6, InitItem.SHULKER_SPLEEN.get());

    public static final ChestCavityType BREEZE = register("breeze")
            .setNeedBreath(false)
            .setFirstRow(3, InitItem.ACTIVE_BREEZE_ROD.get())
            .setFirstRow(4, InitItem.BREEZE_HEART.get())
            .setFirstRow(5, InitItem.ACTIVE_BREEZE_ROD.get())

            .setSecondRow(3, Items.WIND_CHARGE)
            .setSecondRow(4, InitItem.ACTIVE_BREEZE_ROD.get())
            .setSecondRow(5, Items.WIND_CHARGE)

            .setThirdRow(4, Items.WIND_CHARGE);

    public static final ChestCavityType IRON_GOLEM = register("iron_golem")
            .setNeedBreath(false)
            .setFirstRow(0, InitItem.PISTON_MUSCLE.get())
            .setFirstRow(1, InitItem.GOLEM_ARMOR_PLATE.get())
            .setFirstRow(2, InitItem.GOLEM_ARMOR_PLATE.get())
            .setFirstRow(4, InitItem.GOLEM_CORE.get())
            .setFirstRow(5, InitItem.GOLEM_ARMOR_PLATE.get())
            .setFirstRow(6, InitItem.GOLEM_ARMOR_PLATE.get())
            .setFirstRow(7, InitItem.GOLEM_ARMOR_PLATE.get())
            .setFirstRow(8, InitItem.PISTON_MUSCLE.get())

            .setSecondRow(0, InitItem.PISTON_MUSCLE.get())
            .setSecondRow(1, InitItem.GOLEM_CABLE.get())
            .setSecondRow(2, InitItem.GOLEM_CABLE.get())
            .setSecondRow(3, InitItem.GOLEM_CABLE.get())
            .setSecondRow(4, InitItem.GOLEM_CABLE.get())
            .setSecondRow(5, InitItem.GOLEM_CABLE.get())
            .setSecondRow(6, InitItem.GOLEM_CABLE.get())
            .setSecondRow(7, InitItem.GOLEM_CABLE.get())
            .setSecondRow(8, InitItem.PISTON_MUSCLE.get())

            .setThirdRow(0, InitItem.PISTON_MUSCLE.get())
            .setSecondRow(1, InitItem.PISTON_MUSCLE.get())
            .setThirdRow(2, InitItem.GOLEM_ARMOR_PLATE.get())
            .setThirdRow(3, InitItem.GOLEM_ARMOR_PLATE.get())
            .setThirdRow(4, InitItem.INNER_FURNACE.get())
            .setThirdRow(5, InitItem.GOLEM_ARMOR_PLATE.get())
            .setThirdRow(6, InitItem.GOLEM_ARMOR_PLATE.get())
            .setThirdRow(7, InitItem.PISTON_MUSCLE.get())
            .setThirdRow(8, InitItem.PISTON_MUSCLE.get());

    public static final ChestCavityType SNOW_GOLEM = register("snow_golem")
            .setNeedBreath(false)
            .setFirstRow(3, Items.SNOWBALL)
            .setFirstRow(4, Items.SNOWBALL)
            .setFirstRow(5, Items.SNOWBALL)

            .setSecondRow(3, Items.SNOWBALL)
            .setSecondRow(4, InitItem.SNOW_HEART.get())
            .setSecondRow(5, Items.SNOWBALL)

            .setThirdRow(3, Items.SNOWBALL)
            .setThirdRow(4, Items.SNOWBALL)
            .setThirdRow(5, Items.SNOWBALL);

    public static final ChestCavityType SCULK = register("sculk")
            .setNeedBreath(false)
            .setFirstRow(0, InitItem.SCULK_MUSCLE.get())
            .setFirstRow(1, InitItem.SCULK_RIB.get())
            .setFirstRow(4, InitItem.SCULK_HEART.get())
            .setFirstRow(7, InitItem.SCULK_RIB.get())
            .setFirstRow(8, InitItem.SCULK_MUSCLE.get())

            .setSecondRow(0, InitItem.SCULK_MUSCLE.get())
            .setSecondRow(1, InitItem.SCULK_RIB.get())
            .setSecondRow(4, InitItem.SCULK_SPINE.get())
            .setSecondRow(7, InitItem.SCULK_RIB.get())
            .setSecondRow(8, InitItem.SCULK_MUSCLE.get())

            .setThirdRow(0, InitItem.SCULK_MUSCLE.get())
            .setThirdRow(1, InitItem.SCULK_MUSCLE.get())
            .setThirdRow(4, InitItem.SCULK_CORE.get())
            .setThirdRow(7, InitItem.SCULK_MUSCLE.get())
            .setThirdRow(8, InitItem.SCULK_MUSCLE.get());

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
            // 找不到实体类型所属的胸腔类型，就注册一套新的人类器官给它
            registerEntity(entityType, HUMAN);
            return HUMAN;
        }
        return type;
    }

    /**
     * 为实体类型注册胸腔类型
     *
     * @param entityType      实体类型
     * @param chestCavityType 胸腔类型
     */
    public static void registerEntity(EntityType<? extends LivingEntity> entityType, ChestCavityType chestCavityType) {
        ENTITY_CHEST_CAVITY_TYPE_MAP.computeIfAbsent(entityType, chestCavityType::builder);
    }

    /**
     * 注册胸腔类型
     *
     * @param name 类型名称
     * @return 胸腔类型
     */
    public static ChestCavityType register(String name) {
        ChestCavityType chestCavityType = new ChestCavityType(name);
        CHEST_CAVITY_TYPES.add(chestCavityType);
        return chestCavityType;
    }
}
