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
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.zhaiji.chestcavitybeyond.register.InitLootModifier;

/**
 * 全局战利品修改器：用指定战利品表的产出<b>替换</b>当前产出列表。
 * <p>
 * 与 NeoForge 的 {@link AddTableLootModifier} 不同，
 * 本修改器会先清空已有产出再注入目标表的结果，适用于「只保留单个产出」的场景
 * （如考古可疑方块 {@code BrushableBlockEntity} 仅取产出列表的第 0 项）。
 * </p>
 * <p>
 * 掉落概率应通过 {@link LootItemRandomChanceCondition} 等条件控制：条件不满足时本修改器整体不执行，原产出保持不变。
 * </p>
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
