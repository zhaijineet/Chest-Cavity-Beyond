package net.zhaiji.chestcavitybeyond.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zhaiji.chestcavitybeyond.network.client.packet.AddGuardianLaserRenderTaskPacket;
import net.zhaiji.chestcavitybeyond.network.client.packet.ChestOpenerMessagePacket;
import net.zhaiji.chestcavitybeyond.network.client.packet.SyncChestCavityFlagsPacket;
import net.zhaiji.chestcavitybeyond.network.client.packet.SyncOrganSlotPacket;
import net.zhaiji.chestcavitybeyond.network.client.packet.UnopenableChestCavityMessagePacket;
import net.zhaiji.chestcavitybeyond.network.common.packet.SyncSelectedSlotPacket;
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
                UseSkillPacket::handler
        );

        registrar.playBidirectional(
                SyncSelectedSlotPacket.TYPE,
                SyncSelectedSlotPacket.STREAM_CODEC,
                SyncSelectedSlotPacket.handler()
        );

        registrar.playToClient(
                AddGuardianLaserRenderTaskPacket.TYPE,
                AddGuardianLaserRenderTaskPacket.STREAM_CODEC,
                AddGuardianLaserRenderTaskPacket::handler
        );

        registrar.playToClient(
                SyncOrganSlotPacket.TYPE,
                SyncOrganSlotPacket.STREAM_CODEC,
                SyncOrganSlotPacket::handler
        );

        registrar.playToClient(
                ChestOpenerMessagePacket.TYPE,
                ChestOpenerMessagePacket.STREAM_CODEC,
                ChestOpenerMessagePacket::handler
        );

        registrar.playToClient(
                UnopenableChestCavityMessagePacket.TYPE,
                UnopenableChestCavityMessagePacket.STREAM_CODEC,
                UnopenableChestCavityMessagePacket::handler
        );

        registrar.playToClient(
                SyncChestCavityFlagsPacket.TYPE,
                SyncChestCavityFlagsPacket.STREAM_CODEC,
                SyncChestCavityFlagsPacket::handler
        );
    }
}
