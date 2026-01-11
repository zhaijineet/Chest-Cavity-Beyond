package net.zhaiji.chestcavitybeyond.network.client.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;

public record AddGuardianLaserRenderTaskPacket(int attackerId, int targetId,boolean elder) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AddGuardianLaserRenderTaskPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ChestCavityBeyond.MOD_ID, "add_guardian_laser_render_task"));

    public static final StreamCodec<ByteBuf, AddGuardianLaserRenderTaskPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            AddGuardianLaserRenderTaskPacket::attackerId,
            ByteBufCodecs.INT,
            AddGuardianLaserRenderTaskPacket::targetId,
            ByteBufCodecs.BOOL,
            AddGuardianLaserRenderTaskPacket::elder,
            AddGuardianLaserRenderTaskPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
