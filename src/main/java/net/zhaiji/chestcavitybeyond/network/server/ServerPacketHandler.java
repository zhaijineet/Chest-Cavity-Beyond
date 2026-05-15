package net.zhaiji.chestcavitybeyond.network.server;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.network.server.packet.SyncSelectedSlotPacket;
import net.zhaiji.chestcavitybeyond.network.server.packet.UseSkillPacket;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;

public class ServerPacketHandler {
    public static void handlerUseSkillPacket(Player player, UseSkillPacket packet) {
        ChestCavityData data = ChestCavityUtil.getData(player);
        int slot = packet.slot();
        if (slot < 0 || slot >= data.getSlots()) return;
        ItemStack stack = data.getStackInSlot(slot);
        ChestCavityUtil.organSkill(data, player, slot, stack);
    }

    public static void handlerSyncSelectedSlotPacket(Player player, SyncSelectedSlotPacket packet) {
        ChestCavityData data = ChestCavityUtil.getData(player);
        int slot = packet.slot();
        if (slot < -1 || slot >= data.getSlots()) return;
        data.selectedSlot = slot;
    }
}
