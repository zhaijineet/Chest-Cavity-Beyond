package net.zhaiji.chestcavitybeyond.compat.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 缓存用于 JEI 页面渲染的生物实体实例，并提供自适应格子尺寸的模型渲染方法。
 * <p>
 * 玩家因 {@code EntityType.PLAYER.create()} 始终返回 null（原版 createNothing），
 * 故每次实时获取本地玩家实例而非缓存。
 */
public class JeiEntityModelCache {
    private static final Map<EntityType<?>, Optional<LivingEntity>> ENTITY_CACHE = new HashMap<>();

    /**
     * 获取要渲染的实体实例。
     * <p>
     * 玩家特殊处理：直接返回本地玩家，不放入缓存，避免持有旧世界引用。
     *
     * @param entityType 实体类型
     * @return 可渲染的 LivingEntity；empty 表示无法创建（如世界未加载）
     */
    public static Optional<LivingEntity> getOrCreate(EntityType<?> entityType) {
        if (entityType == EntityType.PLAYER) {
            return Optional.ofNullable(Minecraft.getInstance().player);
        }
        return ENTITY_CACHE.computeIfAbsent(entityType, JeiEntityModelCache::createEntity);
    }

    private static Optional<LivingEntity> createEntity(EntityType<?> entityType) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return Optional.empty();
        Entity entity = entityType.create(level);
        if (entity instanceof LivingEntity livingEntity) {
            return Optional.of(livingEntity);
        }
        return Optional.empty();
    }

    /**
     * 计算实体的统一基准缩放倍数，使渲染大小与碰撞箱成正比，保留实体间相对大小差异。
     * <p>
     * 以玩家高度 1.8 米为参考，占格子可用空间的 75%。使用 0.8 次方指数曲线压缩大小范围，
     * 让小实体不至于太小、大实体不至于缩太狠。最小实体至少占 35%。
     *
     * @param dimensions 实体碰撞箱尺寸
     * @return 渲染缩放倍数
     */
    private static float calculateScale(EntityDimensions dimensions) {
        int cellSize = ChestCavityPageScrollWidget.ENTITY_CELL_SIZE;
        int margin = ChestCavityPageScrollWidget.ENTITY_RENDER_MARGIN;
        int available = cellSize - margin * 2;

        float entityMaxDim = Math.max(dimensions.height(), dimensions.width());
        float normalizedSize = entityMaxDim / 1.8F;
        float compressed = (float) Math.pow(normalizedSize, 0.8);
        float targetRenderSize = compressed * (available * 0.75F);

        float minSize = available * 0.35F;
        float clampedSize = Math.clamp(targetRenderSize, minSize, available);
        return clampedSize / entityMaxDim;
    }

    /**
     * 在指定格子区域内渲染生物模型，尺寸根据实体碰撞箱按统一基准缩放，头部跟随鼠标转动。
     * <p>
     * 不使用 {@link InventoryScreen#renderEntityInInventoryFollowsMouse}，因为该方法内部
     * 的 {@code enableScissor} 期望屏幕绝对坐标，而 JEI widget 中 guiGraphics 的 pose 已被
     * 平移到 widget 原点，传入的相对坐标会导致裁剪区域计算错误。这里直接调用
     * {@link InventoryScreen#renderEntityInInventory}（不含 scissor），由外层 scissor 统一裁剪。
     *
     * @param livingEntity 要渲染的生物
     * @param cellX        格子左上角 X（相对 widget）
     * @param cellY        格子左上角 Y（相对 widget）
     * @param guiGraphics  绘图上下文
     * @param mouseX       鼠标 X（相对 widget，控制头部跟随转动）
     * @param mouseY       鼠标 Y（相对 widget）
     */
    public static void renderEntityInCell(
        LivingEntity livingEntity,
        int cellX,
        int cellY,
        GuiGraphics guiGraphics,
        double mouseX,
        double mouseY
    ) {
        EntityType<?> entityType = livingEntity.getType();
        EntityDimensions dimensions = entityType.getDimensions();
        float scale = calculateScale(dimensions);
        if (entityType == EntityType.ENDER_DRAGON) {
            scale *= 2F;
        }

        int cellSize = ChestCavityPageScrollWidget.ENTITY_CELL_SIZE;
        float centerX = cellX + cellSize / 2.0F;
        float centerY = cellY + cellSize / 2.0F;

        float angleX = (float) Math.atan((centerX - mouseX) / 40.0F);
        float angleY = (float) Math.atan((centerY - mouseY) / 40.0F);

        Quaternionf poseRotation = new Quaternionf().rotateZ((float) Math.PI);
        Quaternionf cameraOrientation = new Quaternionf().rotateX(angleY * 20.0F * (float) (Math.PI / 180.0));
        poseRotation.mul(cameraOrientation);

        float prevBodyRot = livingEntity.yBodyRot;
        float prevBodyRotO = livingEntity.yBodyRotO;
        float prevYRot = livingEntity.getYRot();
        float prevXRot = livingEntity.getXRot();
        float prevHeadRotO = livingEntity.yHeadRotO;
        float prevHeadRot = livingEntity.yHeadRot;

        livingEntity.yBodyRot = 180.0F + angleX * 20.0F;
        livingEntity.yBodyRotO = livingEntity.yBodyRot;
        livingEntity.setYRot(180.0F + angleX * 40.0F);
        livingEntity.setXRot(-angleY * 20.0F);
        livingEntity.yHeadRot = livingEntity.getYRot();
        livingEntity.yHeadRotO = livingEntity.getYRot();

        float entityScale = livingEntity.getScale();
        Vector3f translate = new Vector3f(0.0F, livingEntity.getBbHeight() / 2.0F + 0.0625F * entityScale, 0.0F);
        float renderScale = scale / entityScale;

        // 鱼等水生生物不在水中时渲染器会旋转90度侧躺，渲染前临时标记为在水中
        boolean prevWasTouchingWater = livingEntity.wasTouchingWater;
        livingEntity.wasTouchingWater = true;

        InventoryScreen.renderEntityInInventory(
            guiGraphics, centerX, centerY, renderScale, translate, poseRotation, cameraOrientation, livingEntity
        );

        livingEntity.wasTouchingWater = prevWasTouchingWater;
        livingEntity.yBodyRot = prevBodyRot;
        livingEntity.yBodyRotO = prevBodyRotO;
        livingEntity.setYRot(prevYRot);
        livingEntity.setXRot(prevXRot);
        livingEntity.yHeadRotO = prevHeadRotO;
        livingEntity.yHeadRot = prevHeadRot;
    }

    /**
     * 清理缓存的实体实例，在客户端世界卸载时调用，避免引用已卸载世界的幽灵实体。
     */
    public static void clear() {
        ENTITY_CACHE.clear();
    }
}
