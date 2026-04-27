package net.zhaiji.chestcavitybeyond.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;

/**
 * 器官转换事件
 * <p>
 * 当实体发生转换时触发（如村民→僵尸村民），允许外部模组介入器官转换逻辑。
 * </p>
 */
public abstract class OrganConversionEvent extends Event {
    private final LivingEntity original;
    private final LivingEntity outcome;
    private final ChestCavityData oldData;
    private final ChestCavityData newData;

    public OrganConversionEvent(LivingEntity original, LivingEntity outcome, ChestCavityData oldData, ChestCavityData newData) {
        this.original = original;
        this.outcome = outcome;
        this.oldData = oldData;
        this.newData = newData;
    }

    /**
     * 获取原始实体（可能已被标记为移除）
     */
    public LivingEntity getOriginal() {
        return original;
    }

    /**
     * 获取转换后的新实体
     */
    public LivingEntity getOutcome() {
        return outcome;
    }

    /**
     * 获取原始实体的旧胸腔数据
     */
    public ChestCavityData getOldData() {
        return oldData;
    }

    /**
     * 获取转换后实体的新胸腔数据
     * <p>监听器可以直接修改此数据中的器官来实现自定义转换</p>
     */
    public ChestCavityData getNewData() {
        return newData;
    }

    /**
     * OrganConversionEvent.Pre 在默认转换逻辑执行之前触发。
     * <p>
     * 取消此事件将跳过 {@link ChestCavityData#convertOrgans(ChestCavityType)} 的默认转换逻辑。
     * </p>
     */
    public static class Pre extends OrganConversionEvent implements ICancellableEvent {
        public Pre(LivingEntity original, LivingEntity outcome, ChestCavityData oldData, ChestCavityData newData) {
            super(original, outcome, oldData, newData);
        }
    }

    /**
     * OrganConversionEvent.Post 在默认转换逻辑执行之后触发。
     * 此时新实体的器官已完成默认映射和属性重算。
     */
    public static class Post extends OrganConversionEvent {
        public Post(LivingEntity original, LivingEntity outcome, ChestCavityData oldData, ChestCavityData newData) {
            super(original, outcome, oldData, newData);
        }
    }
}
