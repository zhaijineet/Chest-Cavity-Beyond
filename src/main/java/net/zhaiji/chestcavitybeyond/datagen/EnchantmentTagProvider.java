package net.zhaiji.chestcavitybeyond.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.register.InitEnchantment;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTagProvider extends EnchantmentTagsProvider {
    public EnchantmentTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ChestCavityBeyond.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(EnchantmentTags.NON_TREASURE)
                .add(
                        InitEnchantment.TELEOPERATION,
                        InitEnchantment.ADVANCED_SURGERY,
                        InitEnchantment.SAFE_SURGERY,
                        InitEnchantment.PRUDENT_SURGERY
                );
//        tag(EnchantmentTags.TRADEABLE)
//                .add(
//                        InitEnchantment.TELEOPERATION,
//                        InitEnchantment.ADVANCED_SURGERY,
//                        InitEnchantment.SAFE_SURGERY,
//                        InitEnchantment.PRUDENT_SURGERY
//                );
    }
}
