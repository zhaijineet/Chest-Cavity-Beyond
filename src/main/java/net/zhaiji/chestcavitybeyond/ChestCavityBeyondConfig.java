package net.zhaiji.chestcavitybeyond;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ChestCavityBeyondConfig {
    public static int filtrationPeriod;
    public static int detoxificationImmunityDurationThreshold;
    public static boolean enableChestCavityScaleSideEffect;
    public static int minChestOpenMaxHealth;
    public static double chestOpenBaseHealthRatio;
    public static boolean chestplateBlocksChestOpener;
    public static int guardianLaserDistance;
    public static int randomTeleportAttempts;
    public static int furnacePowerMaxDuration;
    public static int shulkerBulletDistance;
    public static int sonicBoomDistance;
    public static int crystalEffectSearchRange;
    public static double fireImmunityHotFloor;
    public static double fireImmunityFire;
    public static double fireImmunityLava;
    public static double frostImmunity;
    public static boolean enableMobGoalSkill;
    public static int goalSkillEvalInterval;
    public static double goalSkillEnemyDetectRange;
    public static int goalSkillTargetMemoryTicks;
    public static boolean mobSkillRetaliatePlayer;
    public static boolean mobSkillRetaliateOtherPet;

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // ==================== Organ ====================
    private static final ModConfigSpec.IntValue FILTRATION_PERIOD = BUILDER
            .push("Organ")
            .comment("Filtration period of organ system (ticks)")
            .defineInRange(
                    "filtrationPeriod",
                    60,
                    20,
                    600
            );

    private static final ModConfigSpec.IntValue DETOXIFICATION_IMMUNITY_DURATION_THRESHOLD = BUILDER
            .comment("Immunize harmful effects reduced by detoxification or withered when their duration is at or below this value (ticks, 0 = only effects reduced to 0 ticks)")
            .defineInRange(
                    "detoxificationImmunityDurationThreshold",
                    20,
                    0,
                    Integer.MAX_VALUE
            );

    private static final ModConfigSpec.BooleanValue ENABLE_CHEST_CAVITY_SCALE_SIDE_EFFECT = BUILDER
            .comment("Enable entity scale side effect when chest cavity size increases (each extra row adds 0.25 scale)")
            .define(
                    "enableChestCavityScaleSideEffect",
                    true
            );

    // ==================== ChestOpener ====================
    private static final ModConfigSpec.IntValue MIN_CHEST_OPEN_MAX_HEALTH = BUILDER
            .pop()
            .push("ChestOpener")
            .comment("Target max health at or below this value can be opened directly without reducing current health")
            .defineInRange(
                    "minChestOpenMaxHealth",
                    15,
                    1,
                    Integer.MAX_VALUE
            );

    private static final ModConfigSpec.DoubleValue CHEST_OPEN_BASE_HEALTH_RATIO = BUILDER
            .comment("When target max health exceeds direct-open threshold, target current health must be below this ratio of max health to open (0.3 = ≤30%, each Advanced Surgery level adds +10%)")
            .defineInRange(
                    "chestOpenBaseHealthRatio",
                    0.3,
                    0.0,
                    1.0
            );

    private static final ModConfigSpec.BooleanValue CHESTPLATE_BLOCKS_CHEST_OPENER = BUILDER
            .comment("Whether chestplate blocks the chest opener from opening the chest cavity (creative mode is not affected)")
            .define(
                    "chestplateBlocksChestOpener",
                    true
            );

    // ==================== SkillParameters ====================
    private static final ModConfigSpec.IntValue GUARDIAN_LASER_DISTANCE = BUILDER
            .pop()
            .push("SkillParameters")
            .comment("Effective distance of guardian laser skill")
            .defineInRange(
                    "guardianLaserDistance",
                    16,
                    5,
                    64
            );

    private static final ModConfigSpec.IntValue RANDOM_TELEPORT_ATTEMPTS = BUILDER
            .comment("Attempt loops for enderman teleport skill")
            .defineInRange(
                    "randomTeleportAttempts",
                    16,
                    1,
                    64
            );

    private static final ModConfigSpec.IntValue FURNACE_POWER_MAX_DURATION = BUILDER
            .comment("Maximum duration of furnace power effect (ticks)")
            .defineInRange(
                    "furnacePowerMaxDuration",
                    24000,
                    1200,
                    72000
            );

    private static final ModConfigSpec.IntValue SHULKER_BULLET_DISTANCE = BUILDER
            .comment("Detection distance of shulker bullet skill")
            .defineInRange(
                    "shulkerBulletDistance",
                    32,
                    10,
                    128
            );

    private static final ModConfigSpec.IntValue SONIC_BOOM_DISTANCE = BUILDER
            .comment("Distance of warden sonic boom skill")
            .defineInRange(
                    "sonicBoomDistance",
                    7,
                    3,
                    32
            );

    private static final ModConfigSpec.IntValue CRYSTAL_EFFECT_SEARCH_RANGE = BUILDER
            .comment("Search range of end crystal for has crystallization entities")
            .defineInRange(
                    "crystalEffectSearchRange",
                    16,
                    5,
                    64
            );

    // ==================== Immunity ====================
    private static final ModConfigSpec.DoubleValue FIRE_IMMUNITY_HOT_FLOOR = BUILDER
            .pop()
            .push("Immunity")
            .comment("Fire resistance threshold: immune to hot block (magma block/campfire) damage")
            .defineInRange(
                    "fireImmunityHotFloor",
                    2.0,
                    0.0,
                    100.0
            );

    private static final ModConfigSpec.DoubleValue FIRE_IMMUNITY_FIRE = BUILDER
            .comment("Fire resistance threshold: immune to fire damage, and clear fire status")
            .defineInRange(
                    "fireImmunityFire",
                    6.0,
                    0.0,
                    100.0
            );

    private static final ModConfigSpec.DoubleValue FIRE_IMMUNITY_LAVA = BUILDER
            .comment("Fire resistance threshold: immune to lava damage")
            .defineInRange(
                    "fireImmunityLava",
                    10.0,
                    0.0,
                    100.0
            );

    private static final ModConfigSpec.DoubleValue FROST_IMMUNITY = BUILDER
            .comment("Frost resistance threshold: immune to freezing damage, and clear frozen ticks")
            .defineInRange(
                    "frostImmunity",
                    4.0,
                    0.0,
                    100.0
            );

    // ==================== MobSkill ====================
    private static final ModConfigSpec.BooleanValue ENABLE_MOB_ORGAN_SKILL = BUILDER
            .pop()
            .push("MobSkill")
            .comment("Enable non-player entities to automatically use organ skills")
            .define(
                    "enableMobGoalSkill",
                    true
            );

    private static final ModConfigSpec.IntValue GOAL_SKILL_EVAL_INTERVAL = BUILDER
            .comment(
                    "Goal organ skill evaluation interval (ticks), higher = better performance but slower reaction",
                    "Note: odd values are rounded up to even (e.g. 3→4) due to Mob AI dual-tick, minimum effective value is 2"
            )
            .defineInRange(
                    "goalSkillEvalInterval",
                    100,
                    2,
                    1000
            );

    private static final ModConfigSpec.DoubleValue GOAL_SKILL_ENEMY_DETECT_RANGE = BUILDER
            .comment("Enemy detection range for Goal organ skill (blocks)")
            .defineInRange(
                    "goalSkillEnemyDetectRange",
                    16.0,
                    4.0,
                    64.0
            );

    private static final ModConfigSpec.IntValue GOAL_SKILL_TARGET_MEMORY_TICKS = BUILDER
            .comment("Goal organ skill target memory duration (ticks), how long the mob keeps attacking after losing target (0 = disabled, 300 = 15s)")
            .defineInRange(
                    "goalSkillTargetMemoryTicks",
                    300,
                    0,
                    1200
            );

    private static final ModConfigSpec.BooleanValue MOB_SKILL_RETALIATE_PLAYER = BUILDER
            .comment(
                    "Whether pet-type mobs' (wolves/cats, i.e. ownable entities) organ skills retaliate against players who dealt damage",
                    "false = no, default protects against accidental player damage (only affects OwnableEntity, not monsters)"
            )
            .define(
                    "mobSkillRetaliatePlayer",
                    false
            );

    private static final ModConfigSpec.BooleanValue MOB_SKILL_RETALIATE_OTHER_PET = BUILDER
            .comment(
                    "Whether pet-type mobs' (wolves/cats, i.e. ownable entities) organ skills retaliate against other pets with different owners",
                    "false = no, default prevents pet mutual damage (only affects OwnableEntity, not monsters)"
            )
            .define(
                    "mobSkillRetaliateOtherPet",
                    false
            );

    public static final ModConfigSpec SPEC = BUILDER.pop().build();

    public static void handlerModConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            filtrationPeriod = FILTRATION_PERIOD.get();
            detoxificationImmunityDurationThreshold = DETOXIFICATION_IMMUNITY_DURATION_THRESHOLD.get();
            enableChestCavityScaleSideEffect = ENABLE_CHEST_CAVITY_SCALE_SIDE_EFFECT.get();
            minChestOpenMaxHealth = MIN_CHEST_OPEN_MAX_HEALTH.get();
            chestOpenBaseHealthRatio = CHEST_OPEN_BASE_HEALTH_RATIO.get();
            chestplateBlocksChestOpener = CHESTPLATE_BLOCKS_CHEST_OPENER.get();
            guardianLaserDistance = GUARDIAN_LASER_DISTANCE.get();
            randomTeleportAttempts = RANDOM_TELEPORT_ATTEMPTS.get();
            furnacePowerMaxDuration = FURNACE_POWER_MAX_DURATION.get();
            shulkerBulletDistance = SHULKER_BULLET_DISTANCE.get();
            sonicBoomDistance = SONIC_BOOM_DISTANCE.get();
            crystalEffectSearchRange = CRYSTAL_EFFECT_SEARCH_RANGE.get();
            fireImmunityHotFloor = FIRE_IMMUNITY_HOT_FLOOR.get();
            fireImmunityFire = FIRE_IMMUNITY_FIRE.get();
            fireImmunityLava = FIRE_IMMUNITY_LAVA.get();
            frostImmunity = FROST_IMMUNITY.get();
            enableMobGoalSkill = ENABLE_MOB_ORGAN_SKILL.get();
            goalSkillEvalInterval = GOAL_SKILL_EVAL_INTERVAL.get();
            goalSkillEnemyDetectRange = GOAL_SKILL_ENEMY_DETECT_RANGE.get();
            goalSkillTargetMemoryTicks = GOAL_SKILL_TARGET_MEMORY_TICKS.get();
            mobSkillRetaliatePlayer = MOB_SKILL_RETALIATE_PLAYER.get();
            mobSkillRetaliateOtherPet = MOB_SKILL_RETALIATE_OTHER_PET.get();
        }
    }
}
