package net.zhaiji.chestcavitybeyond.api.client.task;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import net.zhaiji.chestcavitybeyond.api.task.IChestCavityTask;

public interface IRenderTask extends IChestCavityTask {
    /**
     * 渲染调用
     */
    default void render(Player player, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource) {
    }
}
