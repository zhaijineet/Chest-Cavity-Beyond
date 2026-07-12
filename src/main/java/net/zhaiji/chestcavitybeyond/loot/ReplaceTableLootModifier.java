package net.zhaiji.chestcavitybeyond.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.zhaiji.chestcavitybeyond.register.InitLootModifier;

/**
 * 全局战利品修改器：用指定战利品表的产出替换当前产出列表。
 */
public class ReplaceTableLootModifier extends LootModifier {
    public static final MapCodec<ReplaceTableLootModifier> CODEC = RecordCodecBuilder.mapCodec(
        instance -> codecStart(instance).and(
                ResourceKey.codec(Registries.LOOT_TABLE)
                    .fieldOf("table")
                    .forGetter(modifier -> modifier.table)
            )
            .apply(instance, ReplaceTableLootModifier::new));

    private final ResourceKey<LootTable> table;

    public ReplaceTableLootModifier(LootItemCondition[] conditions, ResourceKey<LootTable> table) {
        super(conditions);
        this.table = table;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        context.getResolver().get(Registries.LOOT_TABLE, this.table).ifPresent(extraTable -> {
            generatedLoot.clear();
            extraTable.value().getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), generatedLoot::add));
        });
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return InitLootModifier.REPLACE_TABLE_LOOT_MODIFIER_TYPE.get();
    }
}
