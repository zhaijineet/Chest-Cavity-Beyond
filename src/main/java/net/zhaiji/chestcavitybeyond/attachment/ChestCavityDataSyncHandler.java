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
 * <p>
 * 客户端已有 ChestCavityData 实例时复用（保留 owner 引用和 mixin 注入字段，避免破坏对象身份），
 * 首次同步时（previousValue 为 null）按需新建。默认同步给所有 tracking 玩家，供附属 mod 在客户端读取其他实体胸腔数据。
 * </p>
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
        buf.writeInt(attachment.selectedSlot);
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
        data.selectedSlot = buf.readInt();
        return data;
    }
}
