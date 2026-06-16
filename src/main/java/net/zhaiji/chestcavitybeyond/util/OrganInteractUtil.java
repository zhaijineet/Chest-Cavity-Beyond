package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.api.OrganInteractContext;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;

public class OrganInteractUtil {
    /**
     * 傀儡装甲板——玩家手持铁锭右键时修复该实体
     */
    public static void ironRepair(ChestCavitySlotContext context, OrganInteractContext interactContext) {
        LivingEntity entity = context.entity();
        Player player = interactContext.getPlayer();
        ItemStack heldItem = interactContext.getHeldItem();
        if (!heldItem.is(Items.IRON_INGOT)) return;
        double ironRepair = context.data().getCurrentValue(InitAttribute.IRON_REPAIR);
        if (ironRepair <= 0) return;
        if (entity.getHealth() >= entity.getMaxHealth()) return;
        // 排除铁傀儡：效果与 IronGolemMixin 重复
        if (entity instanceof IronGolem) return;

        if (!entity.level().isClientSide()) {
            entity.heal((float) (2.5 * ironRepair));
            if (!player.getAbilities().instabuild) {
                heldItem.consume(1, player);
            }
            Level level = entity.level();
            level.playSound(
                null,
                entity.blockPosition(),
                SoundEvents.IRON_GOLEM_REPAIR,
                entity.getSoundSource(),
                1.0F,
                1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F
            );
        }

        interactContext.consume();
        interactContext.skipSameType();
    }
}
