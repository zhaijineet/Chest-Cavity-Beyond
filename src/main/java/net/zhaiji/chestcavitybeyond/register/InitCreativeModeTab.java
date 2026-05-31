package net.zhaiji.chestcavitybeyond.register;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;

import java.util.function.Supplier;

public class InitCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, ChestCavityBeyond.MOD_ID);

    public static final String CHEST_CAVITY_BEYOND_TAB_TRANSLATABLE = "itemGroup.chestcavitybeyond";

    public static final Supplier<CreativeModeTab> CHEST_CAVITY_BEYOND_TAB = CREATIVE_MODE_TAB.register(
            "chest_cavity_beyond_tab",
            () -> CreativeModeTab.builder()
                                 .icon(() -> InitItem.CHEST_OPENER.get().getDefaultInstance())
                                 .title(Component.translatable(CHEST_CAVITY_BEYOND_TAB_TRANSLATABLE))
                                 .displayItems((parameters, output) -> {
                                     InitItem.ITEM.getEntries()
                                                  .stream()
                                                  .map(Supplier::get)
                                                  .forEach(item -> {
                                                      output.accept(item);
                                                      if (item == InitItem.CHEST_OPENER.get()) {
                                                          output.accept(EnchantedBookItem.createForEnchantment(
                                                                  new EnchantmentInstance(parameters.holders().lookupOrThrow(Registries.ENCHANTMENT)
                                                                                                    .getOrThrow(InitEnchantment.TELEOPERATION), 4)
                                                          ));
                                                          output.accept(EnchantedBookItem.createForEnchantment(
                                                                  new EnchantmentInstance(parameters.holders().lookupOrThrow(Registries.ENCHANTMENT)
                                                                                                    .getOrThrow(InitEnchantment.ADVANCED_SURGERY), 3)
                                                          ));
                                                          output.accept(EnchantedBookItem.createForEnchantment(
                                                                  new EnchantmentInstance(parameters.holders().lookupOrThrow(Registries.ENCHANTMENT)
                                                                                                    .getOrThrow(InitEnchantment.SAFE_SURGERY), 2)
                                                          ));
                                                          output.accept(EnchantedBookItem.createForEnchantment(
                                                                  new EnchantmentInstance(parameters.holders().lookupOrThrow(Registries.ENCHANTMENT)
                                                                                                    .getOrThrow(InitEnchantment.PRUDENT_SURGERY), 3)
                                                          ));
                                                          output.accept(EnchantedBookItem.createForEnchantment(
                                                                  new EnchantmentInstance(parameters.holders().lookupOrThrow(Registries.ENCHANTMENT)
                                                                                                    .getOrThrow(InitEnchantment.HYDRAULIC_CLAMP), 5)
                                                          ));
                                                      }
                                                  });
                                 })
                                 .build()
    );
}
