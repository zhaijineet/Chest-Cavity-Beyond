package net.zhaiji.chestcavitybeyond.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.register.InitDamageType;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagsProvider extends TagsProvider<DamageType> {
    public DamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, ChestCavityBeyond.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DamageTypeTags.BYPASSES_ARMOR)
                .add(InitDamageType.ORGAN_LOSS);
        tag(DamageTypeTags.BYPASSES_SHIELD)
                .add(InitDamageType.ORGAN_LOSS);
        tag(DamageTypeTags.BYPASSES_WOLF_ARMOR)
                .add(InitDamageType.ORGAN_LOSS);
        tag(DamageTypeTags.BYPASSES_RESISTANCE)
                .add(InitDamageType.ORGAN_LOSS);
        tag(DamageTypeTags.NO_KNOCKBACK)
                .add(InitDamageType.ORGAN_LOSS);
    }
}
