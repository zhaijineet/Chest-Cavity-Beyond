package net.zhaiji.chestcavitybeyond.compat.jei;

import net.minecraft.world.item.ItemStack;

public record JeiOrganTooltipTarget(
    ChestCavityTypeDisplay display,
    int organIndex,
    ItemStack itemStack
) {
}
