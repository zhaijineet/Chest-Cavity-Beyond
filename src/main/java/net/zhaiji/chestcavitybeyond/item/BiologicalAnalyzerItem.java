package net.zhaiji.chestcavitybeyond.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.zhaiji.chestcavitybeyond.api.TargetResolver;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;

public class BiologicalAnalyzerItem extends Item {
    public BiologicalAnalyzerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            // 射线检测视线中的实体
            HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(
                player,
                entity -> entity != player && TargetResolver.resolve(entity) instanceof LivingEntity,
                player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE)
            );

            if (hitResult instanceof EntityHitResult entityHitResult
                && TargetResolver.resolve(entityHitResult.getEntity()) instanceof LivingEntity target) {
                // 查看目标属性
                TooltipUtil.sendAttributeDisplay(serverPlayer, target);
            } else {
                // 查看自身属性
                TooltipUtil.sendAttributeDisplay(serverPlayer, serverPlayer);
            }

            // 添加1秒cd防止多次触发
            player.getCooldowns().addCooldown(this, 20);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
