package net.zhaiji.chestcavitybeyond.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.register.InitItem;

import java.util.function.Supplier;

public class ItemModelProvider extends net.neoforged.neoforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChestCavityBeyond.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        InitItem.ITEM.getEntries().stream().map(Supplier::get).forEach(this::basicItem);
    }
}
