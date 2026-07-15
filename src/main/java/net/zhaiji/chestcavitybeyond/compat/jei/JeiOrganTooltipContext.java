package net.zhaiji.chestcavitybeyond.compat.jei;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class JeiOrganTooltipContext {
    private static @Nullable JeiOrganTooltipTarget pendingTarget;

    public static void prepare(ChestCavityTypeDisplay display, int organIndex, ItemStack itemStack) {
        pendingTarget = new JeiOrganTooltipTarget(display, organIndex, itemStack.copy());
    }

    public static boolean consume(ItemStack itemStack, Consumer<JeiOrganTooltipTarget> consumer) {
        JeiOrganTooltipTarget target = pendingTarget;
        if (target == null || !ItemStack.isSameItemSameComponents(target.itemStack(), itemStack)) return false;
        pendingTarget = null;
        consumer.accept(target);
        return true;
    }

    public static void clear() {
        pendingTarget = null;
    }
}
