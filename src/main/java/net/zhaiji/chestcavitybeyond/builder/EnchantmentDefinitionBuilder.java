package net.zhaiji.chestcavitybeyond.builder;

import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnchantmentDefinitionBuilder {
    private HolderSet<Item> supportedItems = HolderSet.empty();
    private Optional<HolderSet<Item>> primaryItems = Optional.empty();
    private int weight = 10;
    private int maxLevel = 1;
    private Enchantment.Cost minCost = Enchantment.constantCost(1);
    private Enchantment.Cost maxCost = Enchantment.constantCost(10);
    private int anvilCost = 1;
    private List<EquipmentSlotGroup> slots = new ArrayList<>(List.of(EquipmentSlotGroup.ANY));

    public static EnchantmentDefinitionBuilder builder() {
        return new EnchantmentDefinitionBuilder();
    }

    public EnchantmentDefinitionBuilder setSupportedItems(HolderSet<Item> supportedItems) {
        this.supportedItems = supportedItems;
        return this;
    }

    public EnchantmentDefinitionBuilder setPrimaryItems(HolderSet<Item> primaryItems) {
        this.primaryItems = Optional.of(primaryItems);
        return this;
    }

    public EnchantmentDefinitionBuilder setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public EnchantmentDefinitionBuilder setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
        return this;
    }

    public EnchantmentDefinitionBuilder setMinCost(int base, int perLevel) {
        this.minCost = Enchantment.dynamicCost(base, perLevel);
        return this;
    }

    public EnchantmentDefinitionBuilder setMinCost(int minCost) {
        this.minCost = Enchantment.constantCost(minCost);
        return this;
    }

    public EnchantmentDefinitionBuilder setMaxCost(int base, int perLevel) {
        this.maxCost = Enchantment.dynamicCost(base, perLevel);
        return this;
    }

    public EnchantmentDefinitionBuilder setMaxCost(int maxCost) {
        this.maxCost = Enchantment.constantCost(maxCost);
        return this;
    }

    public EnchantmentDefinitionBuilder setAnvilCost(int anvilCost) {
        this.anvilCost = anvilCost;
        return this;
    }

    public EnchantmentDefinitionBuilder setSlots(EquipmentSlotGroup... slots) {
        this.slots = List.of(slots);
        return this;
    }

    public Enchantment.EnchantmentDefinition build() {
        return new Enchantment.EnchantmentDefinition(
                supportedItems,
                primaryItems,
                weight,
                maxLevel,
                minCost,
                maxCost,
                anvilCost,
                slots
        );
    }
}
