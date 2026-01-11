package net.zhaiji.chestcavitybeyond.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.manager.ItemTagManager;
import net.zhaiji.chestcavitybeyond.register.InitItem;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends IntrinsicHolderTagsProvider<Item> {
    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.ITEM, lookupProvider, item -> item.builtInRegistryHolder().key(), ChestCavityBeyond.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // 心脏
        tag(ItemTagManager.HEART)
                .add(
                        InitItem.HEART.get(),
                        InitItem.ANIMAL_HEART.get(),
                        InitItem.SMALL_ANIMAL_HEART.get(),
                        InitItem.SALTWATER_HEART.get(),
                        InitItem.FIREPROOF_HEART.get(),
                        InitItem.ENDER_HEART.get(),
                        InitItem.ROTTEN_HEART.get(),
                        InitItem.ARTHROPOD_HEART.get(),
                        InitItem.DRAGON_HEART.get(),
                        InitItem.SCULK_HEART.get(),
                        InitItem.SLIME_CORE.get(),
                        InitItem.MAGMA_CUBE_CORE.get(),
                        InitItem.BLAZE_CORE.get(),
                        InitItem.BREEZE_CORE.get(),
                        InitItem.GOLEM_CORE.get(),
                        InitItem.SNOW_CORE.get(),
                        InitItem.SCULK_CORE.get(),
                        InitItem.ELDER_HEART.get(),
                        Items.NETHER_STAR
                );

        // 肺脏
        tag(ItemTagManager.LUNG)
                .add(
                        InitItem.LUNG.get(),
                        InitItem.ANIMAL_LUNG.get(),
                        InitItem.SMALL_ANIMAL_LUNG.get(),
                        InitItem.SALTWATER_LUNG.get(),
                        InitItem.FIREPROOF_LUNG.get(),
                        InitItem.ENDER_LUNG.get(),
                        InitItem.ROTTEN_LUNG.get(),
                        InitItem.ARTHROPOD_LUNG.get(),
                        InitItem.DRAGON_LUNG.get(),
                        InitItem.GILL.get(),
                        InitItem.SMALL_GILL.get(),
                        InitItem.ELDER_GILL.get(),
                        InitItem.ELDER_LUNG.get(),
                        InitItem.GAS_SAC.get()
                );

        // 肌肉
        tag(ItemTagManager.MUSCLE)
                .add(
                        InitItem.MUSCLE.get(),
                        InitItem.ANIMAL_MUSCLE.get(),
                        InitItem.SMALL_ANIMAL_MUSCLE.get(),
                        InitItem.FIREPROOF_MUSCLE.get(),
                        InitItem.ENDER_MUSCLE.get(),
                        InitItem.ROTTEN_MUSCLE.get(),
                        InitItem.ARTHROPOD_MUSCLE.get(),
                        InitItem.DRAGON_MUSCLE.get(),
                        InitItem.BRUTE_MUSCLE.get(),
                        InitItem.SWIFT_MUSCLE.get(),
                        InitItem.LEAPING_MUSCLE.get(),
                        InitItem.SMALL_LEAPING_MUSCLE.get(),
                        InitItem.AQUATIC_MUSCLE.get(),
                        InitItem.SMALL_AQUATIC_MUSCLE.get(),
                        InitItem.FISH_MUSCLE.get(),
                        InitItem.SMALL_FISH_MUSCLE.get(),
                        InitItem.SALTWATER_MUSCLE.get(),
                        InitItem.PISTON_MUSCLE.get(),
                        InitItem.SCULK_MUSCLE.get(),
                        InitItem.ELDER_FISH_MUSCLE.get(),
                        InitItem.ELDER_MUSCLE.get(),
                        InitItem.WRITHING_SOUL_SAND.get(),
                        InitItem.CREEPER_LEAF.get()
                );

        // 肋骨
        tag(ItemTagManager.RIB)
                .add(
                        InitItem.RIB.get(),
                        InitItem.ANIMAL_RIB.get(),
                        InitItem.SMALL_ANIMAL_RIB.get(),
                        InitItem.FIREPROOF_RIB.get(),
                        InitItem.ENDER_RIB.get(),
                        InitItem.ROTTEN_RIB.get(),
                        InitItem.WITHERED_RIB.get(),
                        InitItem.DRAGON_RIB.get(),
                        InitItem.SCULK_RIB.get(),
                        InitItem.FISH_BONE.get(),
                        InitItem.SMALL_FISH_BONE.get(),
                        InitItem.ELDER_FISH_BONE.get(),
                        InitItem.ELDER_RIB.get(),
                        InitItem.GOLEM_ARMOR_PLATE.get(),
                        InitItem.BLAZE_SHELL.get()
                );

        // 阑尾
        tag(ItemTagManager.APPENDIX)
                .add(
                        InitItem.APPENDIX.get(),
                        InitItem.ANIMAL_APPENDIX.get(),
                        InitItem.SMALL_ANIMAL_APPENDIX.get(),
                        InitItem.FIREPROOF_APPENDIX.get(),
                        InitItem.ENDER_APPENDIX.get(),
                        InitItem.ROTTEN_APPENDIX.get(),
                        InitItem.DRAGON_APPENDIX.get(),
                        InitItem.CREEPER_APPENDIX.get(),
                        InitItem.ELDER_APPENDIX.get()
                );

        // 脾脏
        tag(ItemTagManager.SPLEEN)
                .add(
                        InitItem.SPLEEN.get(),
                        InitItem.ANIMAL_SPLEEN.get(),
                        InitItem.SMALL_ANIMAL_SPLEEN.get(),
                        InitItem.FIREPROOF_SPLEEN.get(),
                        InitItem.ENDER_SPLEEN.get(),
                        InitItem.ROTTEN_SPLEEN.get(),
                        InitItem.DRAGON_SPLEEN.get(),
                        InitItem.SHULKER_SPLEEN.get(),
                        InitItem.ELDER_SPLEEN.get()
                );

        // 肾脏
        tag(ItemTagManager.KIDNEY)
                .add(
                        InitItem.KIDNEY.get(),
                        InitItem.ANIMAL_KIDNEY.get(),
                        InitItem.SMALL_ANIMAL_KIDNEY.get(),
                        InitItem.FIREPROOF_KIDNEY.get(),
                        InitItem.ENDER_KIDNEY.get(),
                        InitItem.ROTTEN_KIDNEY.get(),
                        InitItem.DRAGON_KIDNEY.get(),
                        InitItem.ELDER_KIDNEY.get()
                );

        // 脊柱
        tag(ItemTagManager.SPINE)
                .add(
                        InitItem.SPINE.get(),
                        InitItem.ANIMAL_SPINE.get(),
                        InitItem.SMALL_ANIMAL_SPINE.get(),
                        InitItem.FIREPROOF_SPINE.get(),
                        InitItem.ENDER_SPINE.get(),
                        InitItem.ROTTEN_SPINE.get(),
                        InitItem.WITHERED_SPINE.get(),
                        InitItem.DRAGON_SPINE.get(),
                        InitItem.SCULK_SPINE.get(),
                        InitItem.FISH_SPINE.get(),
                        InitItem.SMALL_FISH_SPINE.get(),
                        InitItem.ELDER_FISH_SPINE.get(),
                        InitItem.ELDER_SPINE.get(),
                        InitItem.GOLEM_CABLE.get()
                );

        // 肝脏
        tag(ItemTagManager.LIVER)
                .add(
                        InitItem.LIVER.get(),
                        InitItem.ANIMAL_LIVER.get(),
                        InitItem.SMALL_ANIMAL_LIVER.get(),
                        InitItem.FIREPROOF_LIVER.get(),
                        InitItem.ENDER_LIVER.get(),
                        InitItem.ROTTEN_LIVER.get(),
                        InitItem.DRAGON_LIVER.get(),
                        InitItem.ELDER_LIVER.get()
                );

        // 肠子
        tag(ItemTagManager.INTESTINE)
                .add(
                        InitItem.INTESTINE.get(),
                        InitItem.ANIMAL_INTESTINE.get(),
                        InitItem.SMALL_ANIMAL_INTESTINE.get(),
                        InitItem.FIREPROOF_INTESTINE.get(),
                        InitItem.ENDER_INTESTINE.get(),
                        InitItem.ROTTEN_INTESTINE.get(),
                        InitItem.FISH_INTESTINE.get(),
                        InitItem.SMALL_FISH_INTESTINE.get(),
                        InitItem.CARNIVORE_INTESTINE.get(),
                        InitItem.SMALL_CARNIVORE_INTESTINE.get(),
                        InitItem.HERBIVORE_INTESTINE.get(),
                        InitItem.SMALL_HERBIVORE_INTESTINE.get(),
                        InitItem.ARTHROPOD_INTESTINE.get(),
                        InitItem.ELDER_INTESTINE.get(),
                        InitItem.ELDER_FISH_INTESTINE.get()
                );

        // 胃
        tag(ItemTagManager.STOMACH)
                .add(
                        InitItem.STOMACH.get(),
                        InitItem.ANIMAL_STOMACH.get(),
                        InitItem.SMALL_ANIMAL_STOMACH.get(),
                        InitItem.FIREPROOF_STOMACH.get(),
                        InitItem.ENDER_STOMACH.get(),
                        InitItem.ROTTEN_STOMACH.get(),
                        InitItem.CARNIVORE_STOMACH.get(),
                        InitItem.SMALL_CARNIVORE_STOMACH.get(),
                        InitItem.HERBIVORE_RUMEN.get(),
                        InitItem.HERBIVORE_STOMACH.get(),
                        InitItem.SMALL_HERBIVORE_STOMACH.get(),
                        InitItem.GHAST_STOMACH.get(),
                        InitItem.SLIME_STOMACH.get(),
                        InitItem.MAGMA_STOMACH.get(),
                        InitItem.ARTHROPOD_STOMACH.get(),
                        InitItem.ARTHROPOD_CAECUM.get(),
                        InitItem.INNER_FURNACE.get(),
                        InitItem.ELDER_STOMACH.get()
                );

        // 特殊器官
        tag(ItemTagManager.SPECIAL)
                .add(
                        InitItem.SILK_GLAND.get(),
                        InitItem.VENOM_GLAND.get(),
                        InitItem.ACTIVE_BLAZE_ROD.get(),
                        InitItem.ACTIVE_BREEZE_ROD.get(),
                        InitItem.MANA_REACTOR.get(),
                        InitItem.ELDER_MANA_REACTOR.get(),
                        InitItem.GUARDIAN_EYE.get(),
                        InitItem.ELDER_GUARDIAN_EYE.get()
                );

        // 骨质器官
        tag(ItemTagManager.BONE)
                .add(
                        InitItem.RIB.get(),
                        InitItem.ANIMAL_RIB.get(),
                        InitItem.SMALL_ANIMAL_RIB.get(),
                        InitItem.FIREPROOF_RIB.get(),
                        InitItem.ENDER_RIB.get(),
                        InitItem.ROTTEN_RIB.get(),
                        InitItem.WITHERED_RIB.get(),
                        InitItem.DRAGON_RIB.get(),
                        InitItem.SCULK_RIB.get(),
                        InitItem.FISH_BONE.get(),
                        InitItem.SMALL_FISH_BONE.get(),
                        InitItem.SPINE.get(),
                        InitItem.ANIMAL_SPINE.get(),
                        InitItem.SMALL_ANIMAL_SPINE.get(),
                        InitItem.FIREPROOF_SPINE.get(),
                        InitItem.ENDER_SPINE.get(),
                        InitItem.ROTTEN_SPINE.get(),
                        InitItem.WITHERED_SPINE.get(),
                        InitItem.DRAGON_SPINE.get(),
                        InitItem.SCULK_SPINE.get(),
                        InitItem.FISH_SPINE.get(),
                        InitItem.SMALL_FISH_SPINE.get(),
                        InitItem.ELDER_FISH_SPINE.get(),
                        InitItem.ELDER_FISH_BONE.get(),
                        InitItem.ELDER_RIB.get(),
                        InitItem.ELDER_SPINE.get()
                );

        // 腐烂器官
        tag(ItemTagManager.ROTTEN)
                .add(
                        InitItem.ROTTEN_HEART.get(),
                        InitItem.ROTTEN_LUNG.get(),
                        InitItem.ROTTEN_STOMACH.get(),
                        InitItem.ROTTEN_INTESTINE.get(),
                        InitItem.ROTTEN_KIDNEY.get(),
                        InitItem.ROTTEN_SPLEEN.get(),
                        InitItem.ROTTEN_LIVER.get(),
                        InitItem.ROTTEN_APPENDIX.get(),
                        InitItem.ROTTEN_MUSCLE.get(),
                        InitItem.ROTTEN_RIB.get(),
                        InitItem.ROTTEN_SPINE.get()
                );

        // 铁质器官
        tag(ItemTagManager.IRON)
                .add(
                        InitItem.GOLEM_CORE.get(),
                        InitItem.GOLEM_CABLE.get(),
                        InitItem.GOLEM_ARMOR_PLATE.get(),
                        InitItem.PISTON_MUSCLE.get()
                );

        // 器官
        tag(ItemTagManager.ORGANS)
                .addTags(
                        ItemTagManager.HEART,
                        ItemTagManager.LUNG,
                        ItemTagManager.MUSCLE,
                        ItemTagManager.RIB,
                        ItemTagManager.APPENDIX,
                        ItemTagManager.SPLEEN,
                        ItemTagManager.KIDNEY,
                        ItemTagManager.SPINE,
                        ItemTagManager.LIVER,
                        ItemTagManager.INTESTINE,
                        ItemTagManager.STOMACH,
                        ItemTagManager.SPECIAL,
                        ItemTagManager.IRON,
                        ItemTagManager.BONE,
                        ItemTagManager.ROTTEN
                )
                .add(Items.NETHER_STAR);
    }
}
