package net.zhaiji.chestcavitybeyond.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
import net.zhaiji.chestcavitybeyond.api.TargetResolver;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.manager.DamageSourceManager;
import net.zhaiji.chestcavitybeyond.network.client.packet.ChestOpenerMessagePacket;
import net.zhaiji.chestcavitybeyond.network.client.packet.UnopenableChestCavityMessagePacket;
import net.zhaiji.chestcavitybeyond.register.InitEnchantment;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.EnchantmentUtil;

public class ChestOpenerItem extends Item {
    private final float baseDamage;

    public ChestOpenerItem(Item.Properties properties) {
        super(properties);
        baseDamage = 4;
    }

    public ChestOpenerItem(Item.Properties properties, float baseDamage) {
        super(properties);
        this.baseDamage = baseDamage;
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
            entity -> entity != player && TargetResolver.resolve(entity) instanceof LivingEntity,
            EnchantmentUtil.calculateOpenDistance(level, stack, player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE))
        );
        DamageSource source = DamageSourceManager.openChest(level, player);
        float damage = EnchantmentUtil.calculateOpenDamage(level, stack, baseDamage);
        boolean hasDoor;
        if (hitResult instanceof EntityHitResult entityHitResult && TargetResolver.resolve(entityHitResult.getEntity()) instanceof LivingEntity target) {
            ChestCavityData data = ChestCavityUtil.getData(target);
            // 检查胸腔类型是否可开胸
            if (!player.isCreative() && !data.getType().canOpen(player, target)) {
                if (player instanceof ServerPlayer serverPlayer) {
                    PacketDistributor.sendToPlayer(serverPlayer, new UnopenableChestCavityMessagePacket(target.getId()));
                }
                return InteractionResultHolder.fail(stack);
            }
            boolean isOwner = target instanceof OwnableEntity ownable && ownable.getOwner() == player;
            int safeSurgeryLevel = EnchantmentUtil.getEnchantmentLevel(level, stack, InitEnchantment.SAFE_SURGERY);
            if (!player.isCreative() && isOwner && !(safeSurgeryLevel == 0 || (safeSurgeryLevel == 1 && player.isShiftKeyDown()))) {
                return InteractionResultHolder.fail(stack);
            }
            boolean canOpenCavity = player.isCreative() || isOwner || EnchantmentUtil.canOpenChestCavity(
                level,
                stack,
                target.getMaxHealth(),
                target.getHealth()
            );
            hasDoor = data.hasOrgan(ItemTags.DOORS);
            boolean hasChestPlate = ChestCavityBeyondConfig.chestplateBlocksChestOpener
                                    && !target.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
            // 液压钳：消耗胸甲耐久，如果胸甲被破坏则视为无胸甲
            if (hasChestPlate && !player.isCreative()) {
                if (EnchantmentUtil.getEnchantmentLevel(level, stack, InitEnchantment.HYDRAULIC_CLAMP) > 0) {
                    hasChestPlate = !EnchantmentUtil.applyHydraulicClamp(level, stack, target);
                    // 触发液压钳，添加3秒冷却
                    player.getCooldowns().addCooldown(this, 60);
                }
            }
            if ((!canOpenCavity && !hasDoor) || (hasChestPlate && !player.isCreative())) {
                if (player instanceof ServerPlayer serverPlayer) {
                    PacketDistributor.sendToPlayer(serverPlayer, new ChestOpenerMessagePacket(hasChestPlate));
                } else {
                    if (hasChestPlate) {
                        player.playNotifySound(SoundEvents.CHAIN_HIT, player.getSoundSource(), 0.75F, 1);
                    } else {
                        player.playNotifySound(SoundEvents.ARMOR_EQUIP_TURTLE.value(), player.getSoundSource(), 0.75F, 1);
                    }
                }
                return InteractionResultHolder.fail(stack);
            } else {
                if (!hasDoor && !player.isCreative()) target.hurt(source, damage);
                if (target.isAlive()) {
                    ChestCavityUtil.openChestCavity(player, target, stack);
                    EnchantmentUtil.applyAnesthesiaSurgery(level, stack, target);
                }
            }
        } else {
            int safeSurgeryLevel = EnchantmentUtil.getEnchantmentLevel(level, stack, InitEnchantment.SAFE_SURGERY);
            if (safeSurgeryLevel == 0 || (safeSurgeryLevel == 1 && player.isShiftKeyDown())) {
                hasDoor = ChestCavityUtil.getData(player).hasOrgan(ItemTags.DOORS);
                if (!hasDoor && !player.isCreative()) player.hurt(source, damage);
                if (player.isAlive()) {
                    ChestCavityUtil.openChestCavity(player, player, stack);
                    EnchantmentUtil.applyAnesthesiaSurgery(level, stack, player);
                }
            } else {
                return InteractionResultHolder.fail(stack);
            }
        }
        if (level.isClientSide() && hasDoor) {
            player.playNotifySound(SoundEvents.CHEST_OPEN, player.getSoundSource(), 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getMaxStackSize() == 1;
    }
}
