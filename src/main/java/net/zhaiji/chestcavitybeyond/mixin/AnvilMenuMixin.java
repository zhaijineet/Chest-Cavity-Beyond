package net.zhaiji.chestcavitybeyond.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.zhaiji.chestcavitybeyond.manager.ItemTagManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {
    /**
     * 让开胸器通过铁砧的物品可损坏性检查，使其能在铁砧上合并附魔
     */
    @WrapOperation(
        method = "createResult",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isDamageableItem()Z",
            ordinal = 1
        )
    )
    private boolean chestCavityBeyond$allowChestOpenerCombine(ItemStack stack, Operation<Boolean> original) {
        return original.call(stack) || stack.is(ItemTagManager.CHEST_OPENERS);
    }
}
