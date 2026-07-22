package net.zhaiji.chestcavitybeyond;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 客户端配置
 */
public class ChestCavityBeyondClientConfig {
    public static boolean detailedTooltips;

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue DETAILED_TOOLTIPS = BUILDER
        .comment("Default mode shows detailed descriptions. When disabled, hold SHIFT to view details.")
        .define(
            "detailedTooltips",
            false
        );

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static void handlerModConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            detailedTooltips = DETAILED_TOOLTIPS.get();
        }
    }
}
