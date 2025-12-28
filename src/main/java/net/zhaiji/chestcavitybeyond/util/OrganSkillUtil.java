package net.zhaiji.chestcavitybeyond.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public class OrganSkillUtil {
    /**
     * 食草的基本食物属性
     */
    public static final FoodProperties GRASS_FOOD = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.5F)
            .build();

    /**
     * 将目标传送到周围随机位置
     * <p>
     * {@link EnderMan//#teleport}
     * </p>
     *
     * @param entity 传送目标
     * @return 传送是否成功
     */
    public static boolean randomTeleport(LivingEntity entity) {
        // TODO 尝试循环次数写入配置
        for (int i = 0; i < 16; i++) {
            if (TeleportUtil.randomTeleport(entity)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 让玩家沿着视线方向传送
     */
    public static void teleport(Player player) {
        TeleportUtil.teleport(player);
    }

    /**
     * 吃草
     */
    public static void graze(Player player) {
        Vec3 from = player.getEyePosition();
        Vec3 to = from.add(player.getLookAngle().normalize().scale(player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue()));
        ClipContext clipContext = new ClipContext(
                from,
                to,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                CollisionContext.empty()
        );
        Level level = player.level();
        BlockHitResult blockHitResult = level.clip(clipContext);
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = blockHitResult.getBlockPos();
            BlockState state = level.getBlockState(pos);
            if (state.is(Blocks.GRASS_BLOCK)) {
                player.getFoodData().eat(GRASS_FOOD);
                player.gameEvent(GameEvent.EAT);
                level.levelEvent(2001, pos, Block.getId(state));
                level.setBlock(blockHitResult.getBlockPos(), Blocks.DIRT.defaultBlockState(), 2);
            }
        }
    }
}
