package net.zhaiji.chestcavitybeyond.network.server.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.network.server.ServerPacketHandler;

/**
 * 使用技能
 */
public record UseSkillPacket(int slot) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UseSkillPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ChestCavityBeyond.MOD_ID, "use_skill_packet"));

    public static final StreamCodec<ByteBuf, UseSkillPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            UseSkillPacket::slot,
            UseSkillPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(UseSkillPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ServerPacketHandler.handleUseSkillPacket(context.player(), packet));
    }
}
