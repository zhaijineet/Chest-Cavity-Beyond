package net.zhaiji.chestcavitybeyond.network.client.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.network.client.ClientPacketHandler;

public record ChestOpenerMessagePacket(boolean isEquipChestPlate) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ChestOpenerMessagePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ChestCavityBeyond.MOD_ID, "chest_opener_message"));

    public static final StreamCodec<ByteBuf, ChestOpenerMessagePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ChestOpenerMessagePacket::isEquipChestPlate,
            ChestOpenerMessagePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(ChestOpenerMessagePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientPacketHandler.handlerChestOpenerMessagePacket(packet));
    }
}
