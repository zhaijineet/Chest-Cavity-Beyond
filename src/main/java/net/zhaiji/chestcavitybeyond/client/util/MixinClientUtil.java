package net.zhaiji.chestcavitybeyond.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyondConfig;
import net.zhaiji.chestcavitybeyond.register.InitAttribute;

import java.util.List;

/**
 * Mixin 客户端业务逻辑工具类
 * <p>
 * 从客户端 Mixin 中提取的业务逻辑，与服务端代码分离避免类加载崩溃
 * </p>
 */
public class MixinClientUtil {
    /**
     * 渲染结晶光束
     *
     * @param crystal      末影水晶
     * @param partialTicks 部分刻
     * @param poseStack    位姿堆栈
     * @param buffer       多缓冲源
     * @param packedLight  光照值
     */
    public static void renderCrystalBeams(
        EndCrystal crystal,
        float partialTicks,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight
    ) {
        List<LivingEntity> list = crystal.level().getEntitiesOfClass(
            LivingEntity.class,
            crystal.getBoundingBox().inflate(ChestCavityBeyondConfig.crystalEffectSearchRange),
            entity -> !(entity instanceof EnderDragon)
                      && entity.getAttribute(InitAttribute.CRYSTALLIZATION).getValue() > 0
        );
        for (LivingEntity entity : list) {
            float x = (float) (Mth.lerp(partialTicks, entity.xo, entity.getX()) - crystal.getX());
            float y = (float) (Mth.lerp(partialTicks, entity.yo - 0.9, entity.getY() - 0.9) - crystal.getY());
            float z = (float) (Mth.lerp(partialTicks, entity.zo, entity.getZ()) - crystal.getZ());
            poseStack.pushPose();
            poseStack.translate(x, y, z);
            EnderDragonRenderer.renderCrystalBeams(
                -x,
                -y + EndCrystalRenderer.getY(crystal, partialTicks),
                -z,
                partialTicks,
                entity.tickCount,
                poseStack,
                buffer,
                packedLight
            );
            poseStack.popPose();
        }
    }
}
