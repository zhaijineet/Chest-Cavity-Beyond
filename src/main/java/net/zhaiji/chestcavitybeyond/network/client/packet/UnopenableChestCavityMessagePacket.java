package net.zhaiji.chestcavitybeyond.network.client.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.network.client.ClientPacketHandler;

public record UnopenableChestCavityMessagePacket(int entityId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UnopenableChestCavityMessagePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ChestCavityBeyond.MOD_ID, "unopenable_cavity_message"));

    public static final StreamCodec<ByteBuf, UnopenableChestCavityMessagePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            UnopenableChestCavityMessagePacket::entityId,
            UnopenableChestCavityMessagePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(UnopenableChestCavityMessagePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientPacketHandler.handlerUnopenableCavityMessagePacket(packet));
    }
}
