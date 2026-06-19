package net.zhaiji.chestcavitybeyond.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.datagen.loot.ArchaeologyLootProvider;
import net.zhaiji.chestcavitybeyond.loot.ReplaceTableLootModifier;

import java.util.concurrent.CompletableFuture;

public class AllLootModifierProvider extends GlobalLootModifierProvider {
    public AllLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, ChestCavityBeyond.MOD_ID);
    }

    @Override
    protected void start() {
        // 原版当前所有考古可疑方块的战利品表 ID（minecraft 命名空间）。
        // 若原版未来新增考古结构/可疑方块，需在此列表手动补充对应表 ID。
        ResourceLocation[] archaeologyTables = {
            ResourceLocation.withDefaultNamespace("archaeology/desert_well"),
            ResourceLocation.withDefaultNamespace("archaeology/desert_pyramid"),
            ResourceLocation.withDefaultNamespace("archaeology/trail_ruins_common"),
            ResourceLocation.withDefaultNamespace("archaeology/trail_ruins_rare"),
            ResourceLocation.withDefaultNamespace("archaeology/ocean_ruin_warm"),
            ResourceLocation.withDefaultNamespace("archaeology/ocean_ruin_cold")
        };

        for (ResourceLocation tableId : archaeologyTables) {
            add(
                "primal_reversion_" + tableId.getPath().replace("/", "_"),
                new ReplaceTableLootModifier(
                    new LootItemCondition[]{
                        new LootTableIdCondition.Builder(tableId).build(),
                        LootItemRandomChanceCondition.randomChance(0.25F).build()
                    },
                    ArchaeologyLootProvider.PRIMAL_REVERSION_ENCHANTED_BOOK
                )
            );
        }
    }
}
