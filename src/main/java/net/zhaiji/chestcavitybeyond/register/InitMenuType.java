package net.zhaiji.chestcavitybeyond.register;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.menu.ChestCavityMenu;

import java.util.function.Supplier;

public class InitMenuType {
    public static final DeferredRegister<MenuType<?>> MENU_TYPE = DeferredRegister.create(BuiltInRegistries.MENU, ChestCavityBeyond.MOD_ID);
    // 胸腔界面类型
    public static final Supplier<MenuType<ChestCavityMenu>> CHEST_CAVITY = MENU_TYPE.register(
            "chest_cavity",
            () -> IMenuTypeExtension.create(ChestCavityMenu::new)
    );
}
