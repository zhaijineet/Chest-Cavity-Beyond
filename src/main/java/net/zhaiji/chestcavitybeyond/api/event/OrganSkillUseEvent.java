package net.zhaiji.chestcavitybeyond.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.zhaiji.chestcavitybeyond.api.ChestCavitySlotContext;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;

import java.util.Objects;
import java.util.function.ToIntFunction;

/**
 * 器官技能使用事件
 */
public abstract class OrganSkillUseEvent extends Event {
    private final ChestCavityData data;
    private final int index;
    private final ItemStack stack;

    public OrganSkillUseEvent(ChestCavityData data, int index, ItemStack stack) {
        this.data = data;
        this.index = index;
        this.stack = stack;
    }

    /**
     * 获取胸腔数据
     */
    public ChestCavityData getData() {
        return data;
    }

    /**
     * 获取拥有胸腔的实体
     */
    public LivingEntity getEntity() {
        return data.getOwner();
    }

    /**
     * 获取槽位索引
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取器官物品
     */
    public ItemStack getStack() {
        return stack;
    }

    /**
     * OrganSkillUseEvent.Pre 在技能执行前触发。
     */
    public static class Pre extends OrganSkillUseEvent implements ICancellableEvent {
        private ToIntFunction<ChestCavitySlotContext> cooldownOverrideFunction = null;

        public Pre(ChestCavityData data, int index, ItemStack stack) {
            super(data, index, stack);
        }

        /**
         * 设置固定冷却时长
         */
        public void setCooldownTicks(int ticks) {
            setCooldownTicks(ignored -> ticks);
        }

        /**
         * 设置动态冷却回调，按上下文计算冷却时长
         */
        public void setCooldownTicks(ToIntFunction<ChestCavitySlotContext> cooldownOverrideFunction) {
            this.cooldownOverrideFunction = Objects.requireNonNull(cooldownOverrideFunction);
        }

        /**
         * 合并覆盖与默认冷却回调，返回最终冷却时长（非负）。
         *
         * @param context         胸腔槽位上下文
         * @param defaultFunction 默认冷却回调（未设置覆盖时使用）
         * @return 最终冷却时长
         */
        public int resolveCooldownTicks(
            ChestCavitySlotContext context,
            ToIntFunction<ChestCavitySlotContext> defaultFunction
        ) {
            int cooldown = cooldownOverrideFunction != null
                           ? cooldownOverrideFunction.applyAsInt(context)
                           : defaultFunction.applyAsInt(context);
            return Math.max(0, cooldown);
        }
    }

    /**
     * OrganSkillUseEvent.Post 在技能执行后触发。
     */
    public static class Post extends OrganSkillUseEvent {
        private final boolean success;
        private final int cooldownTicks;

        public Post(ChestCavityData data, int index, ItemStack stack, boolean success, int cooldownTicks) {
            super(data, index, stack);
            this.success = success;
            this.cooldownTicks = cooldownTicks;
        }

        /**
         * 获取技能是否执行成功
         */
        public boolean isSuccess() {
            return success;
        }

        /**
         * 获取最终使用的冷却时长，未加冷却时为 0
         */
        public int getCooldownTicks() {
            return cooldownTicks;
        }
    }
}
