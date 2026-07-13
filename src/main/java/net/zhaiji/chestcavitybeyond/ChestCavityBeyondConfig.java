package net.zhaiji.chestcavitybeyond;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ChestCavityBeyondConfig {
    public static int filtrationPeriod;
    public static int detoxificationImmunityDurationThreshold;
    public static int minChestOpenMaxHealth;
    public static double chestOpenBaseHealthRatio;
    public static int guardianLaserDistance;
    public static int randomTeleportAttempts;
    public static int furnacePowerMaxDuration;
    public static int shulkerBulletDistance;
    public static int sonicBoomDistance;
    public static int crystalEffectSearchRange;
    public static boolean enableChestCavityScaleSideEffect;
    public static boolean chestplateBlocksChestOpener;
    public static double fireImmunityHotFloor;
    public static double fireImmunityFire;
    public static double fireImmunityLava;
    public static double frostImmunity;
    public static int goalSkillEvalInterval;
    public static double goalSkillEnemyDetectRange;
    public static int goalSkillTargetMemoryTicks;
    public static boolean enableMobGoalSkill;
    public static boolean mobSkillRetaliatePlayer;
    public static boolean mobSkillRetaliateOtherPet;

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder()
            .comment(
                    "配置",
                    "Config"
            )
            .push("Config");

    private static final ModConfigSpec.IntValue FILTRATION_PERIOD = BUILDER
            .comment(
                    "器官过滤系统的周期（tick）",
                    "Filtration period of organ system (ticks)"
            )
            .defineInRange(
                    "filtrationPeriod",
                    60,
                    20,
                    600
            );

    private static final ModConfigSpec.IntValue DETOXIFICATION_IMMUNITY_DURATION_THRESHOLD = BUILDER
            .comment(
                    "解毒或凋零化缩短后的有害效果持续时间不超过此值时直接免疫（tick，0 = 仅免疫缩短到0 tick的效果）",
                    "Immunize harmful effects reduced by detoxification or withered when their duration is at or below this value (ticks, 0 = only effects reduced to 0 ticks)"
            )
            .defineInRange(
                    "detoxificationImmunityDurationThreshold",
                    10,
                    0,
                    Integer.MAX_VALUE
            );

    private static final ModConfigSpec.IntValue MIN_CHEST_OPEN_MAX_HEALTH = BUILDER
            .comment(
                    "目标最大生命值不超过此值时可直接开胸，无需削减当前生命值",
                    "Target max health at or below this value can be opened directly without reducing current health"
            )
            .defineInRange(
                    "minChestOpenMaxHealth",
                    15,
                    1,
                    Integer.MAX_VALUE
            );

    private static final ModConfigSpec.DoubleValue CHEST_OPEN_BASE_HEALTH_RATIO = BUILDER
            .comment(
                    "超出直接开胸阈值时，目标当前生命值需低于最大生命值的该比例才能开胸（0.3 = 目标生命需≤30%，每级高级手术附魔额外+10%）",
                    "When target max health exceeds direct-open threshold, target current health must be below this ratio of max health to open (0.3 = ≤30%, each Advanced Surgery level adds +10%)"
            )
            .defineInRange(
                    "chestOpenBaseHealthRatio",
                    0.3,
                    0.0,
                    1.0
            );

    private static final ModConfigSpec.IntValue GUARDIAN_LASER_DISTANCE = BUILDER
            .comment(
                    "守卫者激光技能的有效距离",
                    "Effective distance of guardian laser skill"
            )
            .defineInRange(
                    "guardianLaserDistance",
                    16,
                    5,
                    64
            );

    private static final ModConfigSpec.IntValue RANDOM_TELEPORT_ATTEMPTS = BUILDER
            .comment(
                    "末影传送技能的尝试循环次数",
                    "Attempt loops for enderman teleport skill"
            )
            .defineInRange(
                    "randomTeleportAttempts",
                    16,
                    1,
                    64
            );

    private static final ModConfigSpec.IntValue FURNACE_POWER_MAX_DURATION = BUILDER
            .comment(
                    "熔炉之力效果的最大持续时间（tick）",
                    "Maximum duration of furnace power effect (ticks)"
            )
            .defineInRange(
                    "furnacePowerMaxDuration",
                    24000,
                    1200,
                    72000
            );

    private static final ModConfigSpec.IntValue SHULKER_BULLET_DISTANCE = BUILDER
            .comment(
                    "潜影贝子弹技能的检测距离",
                    "Detection distance of shulker bullet skill"
            )
            .defineInRange(
                    "shulkerBulletDistance",
                    32,
                    10,
                    128
            );

    private static final ModConfigSpec.IntValue SONIC_BOOM_DISTANCE = BUILDER
            .comment(
                    "监听者音爆技能的距离",
                    "Distance of warden sonic boom skill"
            )
            .defineInRange(
                    "sonicBoomDistance",
                    7,
                    3,
                    32
            );

    private static final ModConfigSpec.IntValue CRYSTAL_EFFECT_SEARCH_RANGE = BUILDER
            .comment(
                    "末影水晶对拥有结晶实体的搜索范围",
                    "Search range of end crystal for has crystallization entities"
            )
            .defineInRange(
                    "crystalEffectSearchRange",
                    16,
                    5,
                    64
            );

    private static final ModConfigSpec.BooleanValue ENABLE_CHEST_CAVITY_SCALE_SIDE_EFFECT = BUILDER
            .comment(
                    "是否启用胸腔容量增大时的实体尺寸副作用（每增加一排scale增加0.25）",
                    "Enable entity scale side effect when chest cavity size increases (each extra row adds 0.25 scale)"
            )
            .define(
                    "enableChestCavityScaleSideEffect",
                    true
            );

    private static final ModConfigSpec.BooleanValue CHESTPLATE_BLOCKS_CHEST_OPENER = BUILDER
            .comment(
                    "胸甲是否阻挡开胸器打开胸腔（创造模式不受影响）",
                    "Whether chestplate blocks the chest opener from opening the chest cavity (creative mode is not affected)"
            )
            .define(
                    "chestplateBlocksChestOpener",
                    true
            );

    private static final ModConfigSpec.DoubleValue FIRE_IMMUNITY_HOT_FLOOR = BUILDER
            .comment(
                    "火焰抗性阈值：免疫热方块（岩浆块/营火）伤害",
                    "Fire resistance threshold: immune to hot block (magma block/campfire) damage"
            )
            .defineInRange(
                    "fireImmunityHotFloor",
                    2.0,
                    0.0,
                    100.0
            );

    private static final ModConfigSpec.DoubleValue FIRE_IMMUNITY_FIRE = BUILDER
            .comment(
                    "火焰抗性阈值：免疫火焰/燃烧伤害，并清除着火状态",
                    "Fire resistance threshold: immune to fire/burn damage, and clear fire status"
            )
            .defineInRange(
                    "fireImmunityFire",
                    6.0,
                    0.0,
                    100.0
            );

    private static final ModConfigSpec.DoubleValue FIRE_IMMUNITY_LAVA = BUILDER
            .comment(
                    "火焰抗性阈值：免疫岩浆伤害",
                    "Fire resistance threshold: immune to lava damage"
            )
            .defineInRange(
                    "fireImmunityLava",
                    10.0,
                    0.0,
                    100.0
            );

    private static final ModConfigSpec.DoubleValue FROST_IMMUNITY = BUILDER
            .comment(
                    "冰霜抗性阈值：免疫冰冻伤害，并清除冰冻进度",
                    "Frost resistance threshold: immune to freezing damage, and clear frozen ticks"
            )
            .defineInRange(
                    "frostImmunity",
                    4.0,
                    0.0,
                    100.0
            );

    private static final ModConfigSpec.BooleanValue ENABLE_MOB_ORGAN_SKILL = BUILDER
            .comment(
                    "是否启用非玩家实体自动使用器官技能",
                    "Enable non-player entities to automatically use organ skills"
            )
            .define(
                    "enableMobGoalSkill",
                    true
            );

    private static final ModConfigSpec.IntValue GOAL_SKILL_EVAL_INTERVAL = BUILDER
            .comment(
                    "Goal 器官技能评估间隔（tick），值越大性能越好但反应越慢",
                    "注意：因 Mob AI 的双 tick 机制，奇数会自动向上取偶（如 3→4），最小有效值为 2",
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
            .comment(
                    "Goal 器官技能的敌人检测范围（格）",
                    "Enemy detection range for Goal organ skill (blocks)"
            )
            .defineInRange(
                    "goalSkillEnemyDetectRange",
                    16.0,
                    4.0,
                    64.0
            );

    private static final ModConfigSpec.IntValue GOAL_SKILL_TARGET_MEMORY_TICKS = BUILDER
            .comment(
                    "Goal 器官技能的目标记忆时长（tick），目标丢失后仍继续攻击的保持时间（0 = 禁用记忆，300 = 15秒）",
                    "Goal organ skill target memory duration (ticks), how long the mob keeps attacking after losing target (0 = disabled, 300 = 15s)"
            )
            .defineInRange(
                    "goalSkillTargetMemoryTicks",
                    300,
                    0,
                    1200
            );

    private static final ModConfigSpec.BooleanValue MOB_SKILL_RETALIATE_PLAYER = BUILDER
            .comment(
                    "宠物型 Mob（狼/猫/女仆等有主生物）的器官技能是否反击造成伤害的玩家",
                    "false = 不反击，默认保护玩家误伤场景（仅影响 OwnableEntity，不影响怪物）",
                    "Whether pet-type mobs' (wolves/cats/maids, i.e. ownable entities) organ skills retaliate against players who dealt damage",
                    "false = no, default protects against accidental player damage (only affects OwnableEntity, not monsters)"
            )
            .define(
                    "mobSkillRetaliatePlayer",
                    false
            );

    private static final ModConfigSpec.BooleanValue MOB_SKILL_RETALIATE_OTHER_PET = BUILDER
            .comment(
                    "宠物型 Mob（狼/猫/女仆等有主生物）的器官技能是否反击造成伤害的其他宠物（不同主人）",
                    "false = 不反击，默认防止宠物互伤（仅影响 OwnableEntity，不影响怪物）",
                    "Whether pet-type mobs' (wolves/cats/maids, i.e. ownable entities) organ skills retaliate against other pets with different owners",
                    "false = no, default prevents pet mutual damage (only affects OwnableEntity, not monsters)"
            )
            .define(
                    "mobSkillRetaliateOtherPet",
                    false
            );

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static void handlerModConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            filtrationPeriod = FILTRATION_PERIOD.get();
            detoxificationImmunityDurationThreshold = DETOXIFICATION_IMMUNITY_DURATION_THRESHOLD.get();
            minChestOpenMaxHealth = MIN_CHEST_OPEN_MAX_HEALTH.get();
            chestOpenBaseHealthRatio = CHEST_OPEN_BASE_HEALTH_RATIO.get();
            guardianLaserDistance = GUARDIAN_LASER_DISTANCE.get();
            randomTeleportAttempts = RANDOM_TELEPORT_ATTEMPTS.get();
            furnacePowerMaxDuration = FURNACE_POWER_MAX_DURATION.get();
            shulkerBulletDistance = SHULKER_BULLET_DISTANCE.get();
            sonicBoomDistance = SONIC_BOOM_DISTANCE.get();
            crystalEffectSearchRange = CRYSTAL_EFFECT_SEARCH_RANGE.get();
            enableChestCavityScaleSideEffect = ENABLE_CHEST_CAVITY_SCALE_SIDE_EFFECT.get();
            chestplateBlocksChestOpener = CHESTPLATE_BLOCKS_CHEST_OPENER.get();
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
