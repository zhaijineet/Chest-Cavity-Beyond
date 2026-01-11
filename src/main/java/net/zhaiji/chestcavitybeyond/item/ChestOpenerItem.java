package net.zhaiji.chestcavitybeyond.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zhaiji.chestcavitybeyond.manager.DamageSourceManager;
import net.zhaiji.chestcavitybeyond.network.client.packet.ChestOpenerMessagePacket;
import net.zhaiji.chestcavitybeyond.register.InitEnchantment;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.EnchantmentUtil;

public class ChestOpenerItem extends Item {
    public ChestOpenerItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getMaxStackSize() == 1;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 14;
    }

    /**
     * 打开生物胸腔
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(
                player,
                entity -> entity != player,
                EnchantmentUtil.calculateOpenDistance(level, stack, player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE).getValue())
        );
        DamageSource source = DamageSourceManager.openChest(level, player);
        float damage = EnchantmentUtil.calculateOpenDamage(level, stack, 4);
        boolean hasDoor = false;
        if (hitResult instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof LivingEntity target) {
            boolean canOpenCavity = EnchantmentUtil.canOpenChestCavity(
                    level, stack, target.getMaxHealth(), target.getHealth()
            );
            hasDoor = ChestCavityUtil.getData(target).hasOrgan(ItemTags.DOORS);
            boolean hasChestPlate = !target.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
            if (!canOpenCavity && !hasDoor || hasChestPlate) {
                if (player instanceof ServerPlayer serverPlayer) {
                    PacketDistributor.sendToPlayer(serverPlayer, new ChestOpenerMessagePacket(hasChestPlate));
                } else {
                    if (hasChestPlate) {
                        player.playNotifySound(SoundEvents.CHAIN_HIT, player.getSoundSource(), 0.75F, 1);
                    } else {
                        player.playNotifySound(SoundEvents.ARMOR_EQUIP_TURTLE.value(), player.getSoundSource(), 0.75F, 1);
                    }
                }
            } else {
                ChestCavityUtil.openChestCavity(player, target);
                target.hurt(source, damage);
            }
        } else {
            int enchantmentLevel = EnchantmentUtil.getEnchantmentLevel(level, stack, InitEnchantment.SAFE_SURGERY);
            if (enchantmentLevel == 1 && player.isShiftKeyDown() || enchantmentLevel == 0) {
                ChestCavityUtil.openChestCavity(player);
                player.hurt(source, damage);
            }
        }
        if (hasDoor) {
            player.playNotifySound(SoundEvents.CHEST_OPEN, player.getSoundSource(), 0.75F, 1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
