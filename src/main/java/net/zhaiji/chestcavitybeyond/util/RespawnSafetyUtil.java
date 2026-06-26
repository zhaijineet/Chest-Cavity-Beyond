package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;
import net.zhaiji.chestcavitybeyond.register.InitItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 玩家复活时的器官安全兜底，确保不会因关键器官缺失而陷入反复死亡的循环
 */
public class RespawnSafetyUtil {
    private static final List<Item> REPLACEABLE_ORGANS = List.of(
        InitItem.MUSCLE.get(),
        InitItem.RIB.get(),
        InitItem.APPENDIX.get(),
        InitItem.INTESTINE.get(),
        InitItem.SPLEEN.get(),
        InitItem.LIVER.get(),
        InitItem.STOMACH.get()
    );

    /**
     * 检查玩家复活后是否缺失关键器官，若缺失则补充最低限度的器官避免立即死亡。
     * 优先使用空位，空位不足时替换非关键器官（被替换的器官归还背包或掉落）。
     */
    public static void ensureVitalOrgans(ServerPlayer player, ChestCavityData data) {
        List<Item> needed = collectNeededOrgans(data);
        if (needed.isEmpty()) return;

        List<Integer> emptySlots = collectEmptySlots(data);
        int emptyIndex = 0;

        for (Item organ : needed) {
            if (emptyIndex < emptySlots.size()) {
                data.setStackInSlot(emptySlots.get(emptyIndex), organ.getDefaultInstance());
                emptyIndex++;
            } else {
                int replaceSlot = findReplaceableSlot(data);
                if (replaceSlot == -1) break;
                ItemStack replaced = data.getStackInSlot(replaceSlot).copy();
                data.setStackInSlot(replaceSlot, organ.getDefaultInstance());
                returnToPlayer(player, replaced);
            }
        }

        data.initAttributeModifier();
    }

    private static List<Item> collectNeededOrgans(ChestCavityData data) {
        List<Item> needed = new ArrayList<>();

        if (data.isNeedHealth() && data.getCurrentValue(InitAttribute.HEALTH) <= 0) {
            needed.add(InitItem.HEART.get());
        }

        if (data.isNeedBreath()
            && (
                data.getCurrentValue(InitAttribute.BREATH_CAPACITY) <= 0
                || data.getCurrentValue(InitAttribute.BREATH_RECOVERY) <= 0
            )) {
            needed.add(InitItem.LUNG.get());
        }

        if (data.getCurrentValue(InitAttribute.NERVES) <= 0) {
            needed.add(InitItem.SPINE.get());
        }

        if (data.getDifferenceValue(InitAttribute.FILTRATION) < 0) {
            needed.add(InitItem.KIDNEY.get());
        }

        return needed;
    }

    private static List<Integer> collectEmptySlots(ChestCavityData data) {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < data.getSlots(); i++) {
            if (data.getStackInSlot(i).isEmpty()) {
                slots.add(i);
            }
        }
        return slots;
    }

    private static int findReplaceableSlot(ChestCavityData data) {
        for (Item replaceable : REPLACEABLE_ORGANS) {
            for (int i = 0; i < data.getSlots(); i++) {
                if (data.getStackInSlot(i).is(replaceable)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static void returnToPlayer(ServerPlayer player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }
}
