package net.zhaiji.chestcavitybeyond.compat.jei;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * 在 JEI 器官槽位渲染器与物品 tooltip 事件之间传递槽位上下文
 */
public class JeiOrganTooltipContext {
    private static @Nullable Target pendingTarget;

    public static void prepare(ChestCavityTypeDisplay display, int organIndex, ItemStack itemStack) {
        pendingTarget = new Target(display, organIndex, itemStack.copy());
    }

    public static boolean consume(ItemStack itemStack, Consumer<Target> consumer) {
        Target target = pendingTarget;
        if (target == null || !ItemStack.isSameItemSameComponents(target.itemStack(), itemStack)) return false;
        pendingTarget = null;
        consumer.accept(target);
        return true;
    }

    public static void clear() {
        pendingTarget = null;
    }

    public record Target(ChestCavityTypeDisplay display, int organIndex, ItemStack itemStack) {
    }
}
