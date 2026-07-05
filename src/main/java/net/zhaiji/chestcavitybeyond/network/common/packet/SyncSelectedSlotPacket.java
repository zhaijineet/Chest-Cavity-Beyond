package net.zhaiji.chestcavitybeyond.network.common.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.network.client.ClientPacketHandler;
import net.zhaiji.chestcavitybeyond.network.server.ServerPacketHandler;

/**
 * 同步当前选择的技能槽位，双向包
 *
 * @param slot 槽位索引，-1 表示未选择
 */
public record SyncSelectedSlotPacket(int slot) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncSelectedSlotPacket> TYPE = new CustomPacketPayload.Type<>(ChestCavityBeyond.of("sync_selected_slot"));

    public static final StreamCodec<ByteBuf, SyncSelectedSlotPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        SyncSelectedSlotPacket::slot,
        SyncSelectedSlotPacket::new
    );

    /**
     * 双向处理器，客户端/服务端分别分流到对应 handler
     */
    public static DirectionalPayloadHandler<SyncSelectedSlotPacket> handler() {
        return new DirectionalPayloadHandler<>(
            (packet, context) -> context.enqueueWork(() -> ClientPacketHandler.handlerSyncSelectedSlotPacket(context.player(), packet)),
            (packet, context) -> context.enqueueWork(() -> ServerPacketHandler.handlerSyncSelectedSlotPacket(context.player(), packet))
        );
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
