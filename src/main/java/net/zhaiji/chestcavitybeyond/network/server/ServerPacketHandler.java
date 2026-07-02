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
        ChestCavityUtil.organSkill(data, slot, stack);
    }

    public static void handlerSyncSelectedSlotPacket(Player player, SyncSelectedSlotPacket packet) {
        ChestCavityData data = ChestCavityUtil.getData(player);
        int slot = packet.slot();
        if (slot < -1 || slot >= data.getSlots()) return;
        // selectedSlot 仅玩家自身使用（渲染/按键），其他玩家无需感知
        // 因为本身玩家可以绑定按键释放任何一个器官的技能，所以就算得知也没什么用，任何人不应该监听此字段来做任何事
        data.selectedSlot = slot;
    }
}
