package net.zhaiji.chestcavitybeyond.datagen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.register.InitEnchantment;

import java.util.function.BiConsumer;

public class ArchaeologyLootProvider implements LootTableSubProvider {
    public static final ResourceKey<LootTable> PRIMAL_REVERSION_ENCHANTED_BOOK = ResourceKey.create(
        Registries.LOOT_TABLE, ChestCavityBeyond.of("archaeology/primal_reversion_enchanted_book")
    );

    private final HolderLookup.Provider registries;

    public ArchaeologyLootProvider(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(PRIMAL_REVERSION_ENCHANTED_BOOK, LootTable.lootTable()
            .withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(Items.BOOK)
                    .apply(new SetEnchantmentsFunction.Builder()
                        .withEnchantment(
                            registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(InitEnchantment.PRIMAL_REVERSION),
                            ConstantValue.exactly(1.0F)
                        )))
            )
        );
    }
}
