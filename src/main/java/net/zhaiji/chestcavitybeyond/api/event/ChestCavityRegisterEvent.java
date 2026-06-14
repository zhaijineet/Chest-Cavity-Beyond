package net.zhaiji.chestcavitybeyond.api.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.api.TargetResolver;
import net.zhaiji.chestcavitybeyond.api.function.TaskDeserializer;
import net.zhaiji.chestcavitybeyond.manager.ChestCavityTypeManager;
import net.zhaiji.chestcavitybeyond.manager.TaskManager;

import java.util.function.Function;

/**
 * 注册胸腔事件
 */
public class ChestCavityRegisterEvent extends Event implements IModBusEvent {
    /**
     * 注册胸腔类型
     */
    public ChestCavityType registerType(ResourceLocation name) {
        return ChestCavityTypeManager.register(name);
    }

    /**
     * 为实体注册胸腔类型
     */
    public void registerEntity(EntityType<? extends LivingEntity> entityType, ChestCavityType chestCavityType) {
        ChestCavityTypeManager.registerEntity(entityType, chestCavityType);
    }

    /**
     * 注册可序列化的task类型
     *
     * @param type         Task类型
     * @param deserializer 反序列化器
     */
    public void registerTask(ResourceLocation type, TaskDeserializer deserializer) {
        TaskManager.registerTask(type, deserializer);
    }

    /**
     * 注册目标解析器，用于将射线追踪命中的非 LivingEntity 子部件实体解析为父 LivingEntity。
     * <p>
     * 解析函数必须在两端返回同一实体，否则交互无法被正确取消，导致 {@code Item.use()} 不被调用，开胸逻辑无法执行。
     * </p>
     * 参见 {@link TargetResolver} 的类注释。
     *
     * @param resolver 解析函数：输入命中的实体，返回对应的 LivingEntity 父实体，无法解析则返回 null
     */
    public void registerTargetResolver(Function<Entity, LivingEntity> resolver) {
        TargetResolver.register(resolver);
    }
}
