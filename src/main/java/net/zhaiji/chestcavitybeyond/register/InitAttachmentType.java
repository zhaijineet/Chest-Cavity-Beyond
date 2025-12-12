package net.zhaiji.chestcavitybeyond.register;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;

import java.util.function.Supplier;

public class InitAttachmentType {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPE = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ChestCavityBeyond.MOD_ID);
    // 胸腔数据
    public static final Supplier<AttachmentType<ChestCavityData>> CHEST_CAVITY = ATTACHMENT_TYPE.register(
            "chest_cavity",
            () -> AttachmentType.serializable((attachmentHolder) -> new ChestCavityData(attachmentHolder, 27))
                    .copyOnDeath()
                    .build()
    );
}
