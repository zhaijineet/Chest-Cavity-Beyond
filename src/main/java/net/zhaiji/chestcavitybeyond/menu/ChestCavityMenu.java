package net.zhaiji.chestcavitybeyond.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.register.InitMenuType;

public class ChestCavityMenu extends AbstractContainerMenu {
    private final ItemStackHandler container;

    // 客户端使用的构造函数
    // 仅需要容器的槽位数量
    public ChestCavityMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, new ItemStackHandler(extraData.readInt()));
    }

    public ChestCavityMenu(int containerId, Inventory playerInventory, ItemStackHandler container) {
        super(InitMenuType.CHEST_CAVITY.get(), containerId);
        this.container = container;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new SlotItemHandler(container, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 85 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 143));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 27) {
                if (!this.moveItemStackTo(itemstack1, 27, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 27, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            // 因为moveItemStackTo方法使用shrink减少是物品数量，所以当选择的是胸腔槽位时，需要额外更新属性
            if (index < 27 && container instanceof ChestCavityData data) {
                data.updateOrganAttribute(itemstack, itemstack1);
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.level().isClientSide()
                || container instanceof ChestCavityData data
                && data.getOwner() instanceof LivingEntity entity
                && entity.isAlive()
                // 最大距离为实体交互距离的2倍
                && player.canInteractWithEntity(entity, player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).getValue() * 2);
    }
}
