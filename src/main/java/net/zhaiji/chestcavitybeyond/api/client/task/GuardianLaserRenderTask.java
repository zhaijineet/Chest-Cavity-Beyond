package net.zhaiji.chestcavitybeyond.api.client.task;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class GuardianLaserRenderTask implements IRenderTask {
    private static final ResourceLocation GUARDIAN_BEAM_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/guardian_beam.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(GUARDIAN_BEAM_LOCATION);

    private final LivingEntity attacker;
    private final LivingEntity target;
    private final int maxTime;
    private int timer = 0;

    public GuardianLaserRenderTask(LivingEntity attacker, LivingEntity target, boolean elder) {
        this.attacker = attacker;
        this.target = target;
        maxTime = elder ? 60 : 80;
    }

    private static Vec3 getPosition(LivingEntity livingEntity, double yOffset, float partialTick) {
        double d0 = Mth.lerp(partialTick, livingEntity.xOld, livingEntity.getX());
        double d1 = Mth.lerp(partialTick, livingEntity.yOld, livingEntity.getY()) + yOffset;
        double d2 = Mth.lerp(partialTick, livingEntity.zOld, livingEntity.getZ());
        return new Vec3(d0, d1, d2);
    }

    private static void vertex(
            VertexConsumer consumer,
            PoseStack.Pose pose,
            float x,
            float y,
            float z,
            int red,
            int green,
            int blue,
            float u,
            float v
    ) {
        consumer.addVertex(pose, x, y, z)
                .setColor(red, green, blue, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(15728880)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public void tick(LivingEntity entity) {
        // TODO 泡泡粒子
        timer++;
    }

    @Override
    public void render(Player player, float partialTick, PoseStack poseStack, MultiBufferSource buffer) {
        float f = (timer + partialTick) / maxTime;
        float f1 = timer + partialTick;
        float f2 = f1 * 0.5F % 1.0F;
        float f3 = attacker.getEyeHeight();
        poseStack.pushPose();
        poseStack.translate(0, -f3 / 3, 0);
        Vec3 vec3 = getPosition(target, target.getBbHeight(), partialTick);
        Vec3 vec31 = getPosition(attacker, f3, partialTick);
        Vec3 vec32 = vec3.subtract(vec31);
        float f4 = (float) (vec32.length() + 1.0);
        vec32 = vec32.normalize();
        float f5 = (float) Math.acos(vec32.y);
        float f6 = (float) Math.atan2(vec32.z, vec32.x);
        poseStack.mulPose(Axis.YP.rotationDegrees(((float) (Math.PI / 2) - f6) * (180.0F / (float) Math.PI)));
        poseStack.mulPose(Axis.XP.rotationDegrees(f5 * (180.0F / (float) Math.PI)));
        float f7 = f1 * 0.05F * -1.5F;
        float f8 = f * f;
        int j = 64 + (int) (f8 * 191.0F);
        int k = 32 + (int) (f8 * 191.0F);
        int l = 128 - (int) (f8 * 64.0F);
        float f9 = 0.2F;
        float f10 = 0.282F;
        float f11 = Mth.cos(f7 + (float) (Math.PI * 3.0 / 4.0)) * f10;
        float f12 = Mth.sin(f7 + (float) (Math.PI * 3.0 / 4.0)) * f10;
        float f13 = Mth.cos(f7 + (float) (Math.PI / 4)) * f10;
        float f14 = Mth.sin(f7 + (float) (Math.PI / 4)) * f10;
        float f15 = Mth.cos(f7 + ((float) Math.PI * 5.0F / 4.0F)) * f10;
        float f16 = Mth.sin(f7 + ((float) Math.PI * 5.0F / 4.0F)) * f10;
        float f17 = Mth.cos(f7 + ((float) Math.PI * 7.0F / 4.0F)) * f10;
        float f18 = Mth.sin(f7 + ((float) Math.PI * 7.0F / 4.0F)) * f10;
        float f19 = Mth.cos(f7 + (float) Math.PI) * f9;
        float f20 = Mth.sin(f7 + (float) Math.PI) * f9;
        float f21 = Mth.cos(f7 + 0.0F) * f9;
        float f22 = Mth.sin(f7 + 0.0F) * f9;
        float f23 = Mth.cos(f7 + (float) (Math.PI / 2)) * f9;
        float f24 = Mth.sin(f7 + (float) (Math.PI / 2)) * f9;
        float f25 = Mth.cos(f7 + (float) (Math.PI * 3.0 / 2.0)) * f9;
        float f26 = Mth.sin(f7 + (float) (Math.PI * 3.0 / 2.0)) * f9;
        float f27 = 0.0F;
        float f28 = 0.4999F;
        float f29 = -1.0F + f2;
        float f30 = f4 * 2.5F + f29;
        VertexConsumer vertexconsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(BEAM_RENDER_TYPE);
        PoseStack.Pose posestack$pose = poseStack.last();
        vertex(vertexconsumer, posestack$pose, f19, f4, f20, j, k, l, f28, f30);
        vertex(vertexconsumer, posestack$pose, f19, f27, f20, j, k, l, f28, f29);
        vertex(vertexconsumer, posestack$pose, f21, f27, f22, j, k, l, f27, f29);
        vertex(vertexconsumer, posestack$pose, f21, f4, f22, j, k, l, f27, f30);
        vertex(vertexconsumer, posestack$pose, f23, f4, f24, j, k, l, f28, f30);
        vertex(vertexconsumer, posestack$pose, f23, f27, f24, j, k, l, f28, f29);
        vertex(vertexconsumer, posestack$pose, f25, f27, f26, j, k, l, f27, f29);
        vertex(vertexconsumer, posestack$pose, f25, f4, f26, j, k, l, f27, f30);
        float f31 = 0.0F;
        if (attacker.tickCount % 2 == 0) {
            f31 = 0.5F;
        }
        vertex(vertexconsumer, posestack$pose, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
        vertex(vertexconsumer, posestack$pose, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
        vertex(vertexconsumer, posestack$pose, f17, f4, f18, j, k, l, 1.0F, f31);
        vertex(vertexconsumer, posestack$pose, f15, f4, f16, j, k, l, 0.5F, f31);
        poseStack.popPose();
    }

    @Override
    public boolean canRemove(LivingEntity entity) {
        // TODO 距离写入配置
        return timer > maxTime || entity.isRemoved() || target.isRemoved() || entity.distanceTo(target) > 16;
    }
}
