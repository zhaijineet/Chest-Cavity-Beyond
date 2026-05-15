package net.zhaiji.chestcavitybeyond.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySize;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.register.InitMenuType;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;

public class ChestCavityMenu extends AbstractContainerMenu {
    private final ChestCavityData data;
    private final ChestCavitySize size;

    // 客户端使用的构造函数
    public ChestCavityMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(
            containerId,
            playerInventory,
            extraData.readEnum(ChestCavitySize.class),
            (LivingEntity) playerInventory.player.level().getEntity(extraData.readInt())
        );
    }

    public ChestCavityMenu(int containerId, Inventory playerInventory, ChestCavitySize size, LivingEntity entity) {
        super(InitMenuType.CHEST_CAVITY.get(), containerId);
        this.size = size;
        data = ChestCavityUtil.getData(entity);
        // 确保数据大小与菜单一致
        data.updateSize(size);
        int rows = size.getRows();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new SlotItemHandler(data, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

        int playerInvY = size.getPlayerInventoryY();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, playerInvY + i * 18));
            }
        }

        int hotbarY = size.getHotbarY();
        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, hotbarY));
        }
        // 触发胸腔打开回调
        ChestCavityUtil.chestCavityOpen(data, entity);
    }

    public ChestCavityData getData() {
        return data;
    }

    public ChestCavitySize getChestCavitySize() {
        return size;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int chestCavitySlots = size.getSlots();
            if (index < chestCavitySlots) {
                if (!this.moveItemStackTo(itemstack1, chestCavitySlots, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, chestCavitySlots, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                itemstack1 = ItemStack.EMPTY;
                slot.setByPlayer(itemstack1);
            } else {
                slot.setChanged();
            }
            // 因为moveItemStackTo使用shrink减少物品数量，所以当选择的是胸腔槽位时，需要额外更新器官
            if (index < chestCavitySlots) {
                ChestCavityUtil.changeOrgan(data, data.getOwner(), index, itemstack, itemstack1);
            }
        }
        return itemstack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        Level level = player.level();
        LivingEntity entity = data.getOwner();
        if (ChestCavityUtil.getData(entity).hasOrgan(ItemTags.DOORS)) {
            level.playSound(
                player,
                player.getOnPos(),
                SoundEvents.CHEST_CLOSE,
                player.getSoundSource(),
                0.5F,
                level.random.nextFloat() * 0.1F + 0.9F
            );
        }
        // 触发胸腔关闭回调
        ChestCavityUtil.chestCavityClose(data, entity);
    }

    @Override
    public boolean stillValid(Player player) {
        LivingEntity entity = data.getOwner();
        return player.level().isClientSide()
               || entity.isAlive()
                  // 最大距离为实体交互距离的2倍
                  && player.canInteractWithEntity(entity, player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).getValue() * 2);
    }
}
