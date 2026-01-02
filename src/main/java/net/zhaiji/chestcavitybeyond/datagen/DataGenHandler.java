package net.zhaiji.chestcavitybeyond.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.register.InitDamageType;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ChestCavityBeyond.MOD_ID)
public class DataGenHandler {
    @SubscribeEvent
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

        generator.addProvider(event.includeClient(), new ItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new LanguageProvider(packOutput, LanguageProvider.EN_US));
        generator.addProvider(event.includeClient(), new LanguageProvider(packOutput, LanguageProvider.ZH_CN));

    }

    public static DatapackBuiltinEntriesProvider createDatapackBuiltinEntriesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder()
                .add(Registries.DAMAGE_TYPE, InitDamageType::bootstrap);
        return new DatapackBuiltinEntriesProvider(packOutput, lookupProvider, registrySetBuilder, Set.of(ChestCavityBeyond.MOD_ID));
    }
}
