package net.zhaiji.chestcavitybeyond.api;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.entity.PartEntity;
import net.zhaiji.chestcavitybeyond.api.event.ChestCavityRegisterEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 开胸器目标解析器，用于将射线追踪命中的非 LivingEntity 实体（如多碰撞箱子部件）解析为父 LivingEntity。
 * <p>
 * NeoForge 的 {@link PartEntity} 自动由内置逻辑处理。第三方多碰撞箱实现需通过 {@link ChestCavityRegisterEvent#registerTargetResolver} 注册自定义解析器。
 * </p>
 * <strong>为什么解析器必须在两端返回同一实体</strong><br>
 * {@code CommonEventHandler} 中的交互事件处理器通过 {@link #resolve} 判断是否取消实体交互。
 * <p>
 * 如果自定义解析器在客户端解析失败（返回 null，导致 {@link #resolve} 回退为返回非 LivingEntity 的原实体）而服务端正常，客户端事件不会取消，实体的 {@code interactAt()} 直接返回 SUCCESS 消费掉交互，{@code Item.use()} 不被调用，服务端收不到使用物品的网络封包，胸腔无法打开。
 * </p>
 * 自定义解析器必须使用客户端和服务端都能正确查找实体的方式（如通过 UUID）。
 */
public class TargetResolver {
    private static final List<Function<Entity, LivingEntity>> RESOLVERS = new ArrayList<>();

    /**
     * 注册外部目标解析器。
     *
     * @param resolver 解析函数：输入命中的实体，返回对应的 LivingEntity 父实体，无法解析则返回 null
     */
    public static void register(Function<Entity, LivingEntity> resolver) {
        RESOLVERS.add(resolver);
    }

    /**
     * 将射线追踪命中的实体解析为实际目标 LivingEntity。
     * <p>
     * 解析失败时返回输入实体本身（而非 null），调用方通过 {@code instanceof LivingEntity} 判断是否解析成功。
     * </p>
     *
     * @param entity 射线追踪命中的实体
     * @return 解析后的 LivingEntity 目标，或无法解析时返回输入实体
     */
    public static Entity resolve(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) return livingEntity;
        if (entity instanceof PartEntity<?> part && part.getParent() instanceof LivingEntity livingEntity) return livingEntity;
        for (Function<Entity, LivingEntity> resolver : RESOLVERS) {
            LivingEntity livingEntity = resolver.apply(entity);
            if (livingEntity != null) return livingEntity;
        }
        return entity;
    }
}
