package net.zhaiji.chestcavitybeyond.register;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.loot.ReplaceTableLootModifier;

/**
 * 全局战利品修改器类型注册
 */
public class InitLootModifier {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ChestCavityBeyond.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ReplaceTableLootModifier>> REPLACE_TABLE_LOOT_MODIFIER_TYPE = LOOT_MODIFIER.register(
        "replace_table",
        () -> ReplaceTableLootModifier.CODEC
    );
}
