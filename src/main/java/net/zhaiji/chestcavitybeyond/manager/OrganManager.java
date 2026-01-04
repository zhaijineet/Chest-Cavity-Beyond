package net.zhaiji.chestcavitybeyond.manager;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.zhaiji.chestcavitybeyond.builder.OrganBuilder;
import net.zhaiji.chestcavitybeyond.util.OrganSkillUtil;

public class OrganManager {
    public static void register() {
        OrganBuilder.builder(Items.NETHER_STAR)
                .skill(context -> {
                    if (context.entity() instanceof Player player) {
                        OrganSkillUtil.witherSkull(player);
                    }
                })
                .build();
    }
}
