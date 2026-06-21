package net.zhaiji.chestcavitybeyond.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.datagen.loot.ArchaeologyLootProvider;
import net.zhaiji.chestcavitybeyond.register.InitDamageType;
import net.zhaiji.chestcavitybeyond.register.InitEnchantment;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DataGenHandler {
    public static void handlerGatherDataEvent(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        DatapackBuiltinEntriesProvider datapackBuiltinEntriesProvider = createDatapackBuiltinEntriesProvider(packOutput, lookupProvider);

        generator.addProvider(event.includeServer(), datapackBuiltinEntriesProvider);
        // 重新获取注册后的lookupProvider
        lookupProvider = datapackBuiltinEntriesProvider.getRegistryProvider();
        generator.addProvider(event.includeServer(), new DamageTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new RecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new ItemTagProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new EnchantmentTagProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new AllLootModifierProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(ArchaeologyLootProvider::new, LootContextParamSets.CHEST)), lookupProvider)
        );

        generator.addProvider(event.includeClient(), new ItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new LanguageProvider(packOutput, LanguageProvider.EN_US));
        generator.addProvider(event.includeClient(), new LanguageProvider(packOutput, LanguageProvider.ZH_CN));

    }

    public static DatapackBuiltinEntriesProvider createDatapackBuiltinEntriesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder()
                .add(Registries.DAMAGE_TYPE, InitDamageType::bootstrap)
                .add(Registries.ENCHANTMENT, InitEnchantment::bootstrap);
        return new DatapackBuiltinEntriesProvider(packOutput, lookupProvider, registrySetBuilder, Set.of(ChestCavityBeyond.MOD_ID));
    }
}
