package net.zhaiji.chestcavitybeyond.api;

/**
 * 胸腔任务
 */
public interface ChestCavityTask {
    /**
     * 任务被添加到队列时调用
     */
    default void onAdded() {
    }

    /**
     * 每个tick都会被调用
     */
    default void tick(int tickCount) {
    }

    /**
     * 任务被移除队列时调用
     */
    default void onRemoved() {
    }

    /**
     * 检查任务是否应该被移除
     */
    default boolean shouldRemove() {
        return true;
    }
}
