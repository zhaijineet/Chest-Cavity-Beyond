package net.zhaiji.chestcavitybeyond.api.task;

/**
 * 胸腔任务
 */
public interface IChestCavityTask {
    /**
     * 任务被添加到队列时调用
     */
    default void onAdded() {
    }

    /**
     * 每个tick都会被调用
     */
    default void tick() {
    }

    /**
     * 任务被移除队列时调用
     */
    default void onRemoved() {
    }

    /**
     * 检查任务是否要被移除
     */
    boolean canRemove();
}
