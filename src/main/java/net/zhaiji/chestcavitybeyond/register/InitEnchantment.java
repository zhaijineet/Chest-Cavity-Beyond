package net.zhaiji.chestcavitybeyond.register;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.builder.EnchantmentDefinitionBuilder;
import net.zhaiji.chestcavitybeyond.manager.ItemTagManager;

public class InitEnchantment {
    public static final ResourceKey<Enchantment> TELEOPERATION = ResourceKey.create(Registries.ENCHANTMENT, ChestCavityBeyond.of("teleoperation"));

    public static final ResourceKey<Enchantment> ADVANCED_SURGERY = ResourceKey.create(Registries.ENCHANTMENT, ChestCavityBeyond.of("advanced_surgery"));

    public static final ResourceKey<Enchantment> SAFE_SURGERY = ResourceKey.create(Registries.ENCHANTMENT, ChestCavityBeyond.of("safe_surgery"));

    public static final ResourceKey<Enchantment> PRUDENT_SURGERY = ResourceKey.create(Registries.ENCHANTMENT, ChestCavityBeyond.of("prudent_surgery"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> itemLookup = context.lookup(Registries.ITEM);

        HolderSet<Item> chestOpeners = itemLookup.getOrThrow(ItemTagManager.CHEST_OPENERS);

        register(
                context,
                TELEOPERATION,
                Enchantment.enchantment(
                        EnchantmentDefinitionBuilder.builder()
                                .setSupportedItems(chestOpeners)
                                .setPrimaryItems(chestOpeners)
                                .setMinCost(1, 11)
                                .setMaxCost(12, 11)
                                .setMaxLevel(4)
                                .build()
                )
        );

        register(
                context,
                ADVANCED_SURGERY,
                Enchantment.enchantment(
                        EnchantmentDefinitionBuilder.builder()
                                .setSupportedItems(chestOpeners)
                                .setPrimaryItems(chestOpeners)
                                .setMinCost(1, 11)
                                .setMaxCost(12, 11)
                                .setMaxLevel(3)
                                .build()
                )
        );

        register(
                context,
                SAFE_SURGERY,
                Enchantment.enchantment(
                        EnchantmentDefinitionBuilder.builder()
                                .setSupportedItems(chestOpeners)
                                .setPrimaryItems(chestOpeners)
                                .setMinCost(1, 11)
                                .setMaxCost(12, 11)
                                .setMaxLevel(2)
                                .build()
                )
        );

        register(
                context,
                PRUDENT_SURGERY,
                Enchantment.enchantment(
                        EnchantmentDefinitionBuilder.builder()
                                .setSupportedItems(chestOpeners)
                                .setPrimaryItems(chestOpeners)
                                .setMinCost(1, 11)
                                .setMaxCost(12, 11)
                                .setMaxLevel(3)
                                .build()
                )
        );
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }
}
