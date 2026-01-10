package net.zhaiji.chestcavitybeyond.manager;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.zhaiji.chestcavitybeyond.builder.OrganBuilder;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.util.OrganAttributeUtil;
import net.zhaiji.chestcavitybeyond.util.OrganSkillUtil;

public class OrganManager {
    public static void register() {
        // 下界之星
        OrganBuilder.builder(Items.NETHER_STAR)
                .modifier((id, modifiers) -> {
                    modifiers.put(InitAttribute.HEALTH, OrganAttributeUtil.createAddValueModifier(id, 2.5));
                })
                .skill(context -> {
                    if (context.entity() instanceof Player player) {
                        OrganSkillUtil.witherSkull(player);
                    }
                })
                .build();
    }
}
