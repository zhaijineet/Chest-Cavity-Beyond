package net.zhaiji.chestcavitybeyond.network.client.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.network.client.ClientPacketHandler;

/**
 * 服务端向所有追踪者同步单个胸腔器官槽位的增量变化
 *
 * @param entityId 目标实体 ID
 * @param slot     变化的器官槽位索引
 * @param stack    新的器官物品栈
 */
public record SyncOrganSlotPacket(int entityId, int slot, ItemStack stack) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncOrganSlotPacket> TYPE = new CustomPacketPayload.Type<>(ChestCavityBeyond.of("sync_organ_slot"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncOrganSlotPacket> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT,
        SyncOrganSlotPacket::entityId,
        ByteBufCodecs.INT,
        SyncOrganSlotPacket::slot,
        ItemStack.OPTIONAL_STREAM_CODEC,
        SyncOrganSlotPacket::stack,
        SyncOrganSlotPacket::new
    );

    public static void handler(SyncOrganSlotPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> ClientPacketHandler.handlerSyncOrganSlotPacket(context.player(), packet));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
