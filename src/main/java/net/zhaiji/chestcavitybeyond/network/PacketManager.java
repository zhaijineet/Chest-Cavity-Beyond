package net.zhaiji.chestcavitybeyond.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zhaiji.chestcavitybeyond.network.client.ClientPacketHandler;
import net.zhaiji.chestcavitybeyond.network.client.packet.SyncChestCavityDataPacket;
import net.zhaiji.chestcavitybeyond.network.server.ServerPacketHandler;
import net.zhaiji.chestcavitybeyond.network.server.packet.SyncSelectedSlotPacket;
import net.zhaiji.chestcavitybeyond.network.server.packet.UseSkillPacket;

public class PacketManager {
    public static final String VERSION = "1.0";

    /**
     * @param event 注册网络包事件
     */
    public static void handlerRegisterPayloadHandlersEvent(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(VERSION);
        registrar.playToServer(
                UseSkillPacket.TYPE,
                UseSkillPacket.STREAM_CODEC,
                ServerPacketHandler::handleUseSkillPacket
        );

        registrar.playToServer(
                SyncSelectedSlotPacket.TYPE,
                SyncSelectedSlotPacket.STREAM_CODEC,
                ServerPacketHandler::handlerSyncSelectedSlotPacket
        );

        registrar.playToClient(
                SyncChestCavityDataPacket.TYPE,
                SyncChestCavityDataPacket.STREAM_CODEC,
                ClientPacketHandler::handlerSyncChestCavityDataPacket
        );
    }
}
