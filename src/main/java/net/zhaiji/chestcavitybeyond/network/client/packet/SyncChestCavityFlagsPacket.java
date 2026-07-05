package net.zhaiji.chestcavitybeyond.network.client.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.network.client.ClientPacketHandler;

/**
 * 服务端向所有追踪者同步胸腔是否需要呼吸/健康的增量变化
 *
 * @param entityId   目标实体 ID
 * @param needBreath 是否需要呼吸
 * @param needHealth 是否需要健康
 */
public record SyncChestCavityFlagsPacket(int entityId, boolean needBreath, boolean needHealth) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncChestCavityFlagsPacket> TYPE = new CustomPacketPayload.Type<>(ChestCavityBeyond.of("sync_chest_cavity_flags"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncChestCavityFlagsPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        SyncChestCavityFlagsPacket::entityId,
        ByteBufCodecs.BOOL,
        SyncChestCavityFlagsPacket::needBreath,
        ByteBufCodecs.BOOL,
        SyncChestCavityFlagsPacket::needHealth,
        SyncChestCavityFlagsPacket::new
    );

    public static void handler(SyncChestCavityFlagsPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientPacketHandler.handlerSyncChestCavityFlagsPacket(context.player(), packet));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
