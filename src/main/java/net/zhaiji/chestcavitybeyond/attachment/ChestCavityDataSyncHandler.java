package net.zhaiji.chestcavitybeyond.attachment;

import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySize;
import org.jetbrains.annotations.Nullable;

/**
 * 胸腔数据的网络同步处理器
 */
public class ChestCavityDataSyncHandler implements AttachmentSyncHandler<ChestCavityData> {
    public static final ChestCavityDataSyncHandler INSTANCE = new ChestCavityDataSyncHandler();

    @Override
    public void write(RegistryFriendlyByteBuf buf, ChestCavityData attachment, boolean initialSync) {
        ChestCavitySize size = attachment.getSize();
        buf.writeEnum(size);
        NonNullList<ItemStack> organs = attachment.getOrgans();
        for (int i = 0; i < size.getSlots(); i++) {
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, organs.get(i));
        }
        buf.writeInt(attachment.getSelectedSlot());
        buf.writeBoolean(attachment.isNeedBreath());
        buf.writeBoolean(attachment.isNeedHealth());
    }

    @Override
    public ChestCavityData read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable ChestCavityData previousValue) {
        ChestCavityData data = previousValue != null ? previousValue : new ChestCavityData(holder);
        ChestCavitySize size = buf.readEnum(ChestCavitySize.class);
        data.updateSize(size);
        NonNullList<ItemStack> organs = data.getOrgans();
        organs.clear();
        for (int i = 0; i < size.getSlots(); i++) {
            organs.set(i, ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
        }
        data.setSelectedSlot(buf.readInt(), false);
        data.setNeedBreath(buf.readBoolean());
        data.setNeedHealth(buf.readBoolean());
        return data;
    }
}
