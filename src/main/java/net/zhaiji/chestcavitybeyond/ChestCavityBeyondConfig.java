package net.zhaiji.chestcavitybeyond;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ChestCavityBeyondConfig {
    public static int filtrationPeriod;
    public static int minChestOpenMaxHealth;
    public static int guardianLaserDistance;
    public static int randomTeleportAttempts;
    public static int furnacePowerMaxDuration;
    public static int shulkerBulletDistance;
    public static int sonicBoomDistance;
    public static int crystalEffectSearchRange;
    public static boolean enableChestCavityScaleSideEffect;

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

    private static final ModConfigSpec.IntValue MIN_CHEST_OPEN_MAX_HEALTH = BUILDER
            .comment(
                    "无需高级手术即可打开胸腔的最大生命值阈值",
                    "Minimum max health threshold to open chest cavity without advanced surgery"
            )
            .defineInRange(
                    "minChestOpenMaxHealth",
                    15,
                    1,
                    40
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

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static void handlerModConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            filtrationPeriod = FILTRATION_PERIOD.get();
            minChestOpenMaxHealth = MIN_CHEST_OPEN_MAX_HEALTH.get();
            guardianLaserDistance = GUARDIAN_LASER_DISTANCE.get();
            randomTeleportAttempts = RANDOM_TELEPORT_ATTEMPTS.get();
            furnacePowerMaxDuration = FURNACE_POWER_MAX_DURATION.get();
            shulkerBulletDistance = SHULKER_BULLET_DISTANCE.get();
            sonicBoomDistance = SONIC_BOOM_DISTANCE.get();
            crystalEffectSearchRange = CRYSTAL_EFFECT_SEARCH_RANGE.get();
            enableChestCavityScaleSideEffect = ENABLE_CHEST_CAVITY_SCALE_SIDE_EFFECT.get();
        }
    }
}
