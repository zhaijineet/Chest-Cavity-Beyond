package net.zhaiji.chestcavitybeyond.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/**
 * 器官交互上下文
 */
public class OrganInteractContext {
    private final Player player;
    private final InteractionHand hand;
    private final BlockPos pos;
    private final Vec3 localPos;

    // 是否交互成功至少一次
    private boolean consumed;

    // 是否停止此后所有的器官交互
    private boolean stopAll;

    // 是否跳过当前器官种类的后续触发
    private boolean skipSameType;

    public OrganInteractContext(Player player, InteractionHand hand, BlockPos pos, Vec3 localPos) {
        this.player = player;
        this.hand = hand;
        this.pos = pos;
        this.localPos = localPos;
    }

    /**
     * 获取执行交互的玩家
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * 获取交互手
     */
    public InteractionHand getHand() {
        return hand;
    }

    /**
     * 获取交互位置
     */
    public BlockPos getPos() {
        return pos;
    }

    /**
     * 获取本地交互位置
     */
    public Vec3 getLocalPos() {
        return localPos;
    }

    /**
     * 获取玩家手持物品
     */
    public ItemStack getHeldItem() {
        return player.getItemInHand(hand);
    }

    /**
     * 标记交互成功
     * <p>
     * 框架遍历所有器官结束后，如果有任意回调标记了成功，
     * 则取消交互事件并返回 {@link net.minecraft.world.InteractionResult#SUCCESS}（触发挥手动画）。
     * </p>
     */
    public void consume() {
        consumed = true;
    }

    /**
     * 是否有任何回调标记了交互成功
     */
    public boolean isConsumed() {
        return consumed;
    }

    /**
     * 停止所有后续器官的遍历
     */
    public void stopAll() {
        stopAll = true;
    }

    /**
     * 是否应停止遍历
     */
    public boolean shouldStopAll() {
        return stopAll;
    }

    /**
     * 跳过当前器官种类的后续触发
     */
    public void skipSameType() {
        skipSameType = true;
    }

    /**
     * @return 本次回调是否要求跳过同类器官
     */
    public boolean consumeSkipSameTypeFlag() {
        boolean value = skipSameType;
        skipSameType = false;
        return value;
    }
}
