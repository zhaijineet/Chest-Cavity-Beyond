package net.zhaiji.chestcavitybeyond.compat.jei;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * 在 JEI 页面槽位内渲染生物 3D 模型，尺寸按碰撞箱统一基准缩放，头部跟随鼠标转动
 */
public class JeiEntitySlotRenderer {
    /**
     * 在指定槽位区域内渲染生物模型。
     *
     * @param livingEntity 要渲染的生物
     * @param slotX        槽位左上角 X（相对 widget）
     * @param slotY        槽位左上角 Y（相对 widget）
     * @param slotSize     槽位边长
     * @param margin       槽位内边距
     * @param guiGraphics  绘图上下文
     * @param mouseX       鼠标 X（相对 widget，控制头部跟随转动）
     * @param mouseY       鼠标 Y（相对 widget）
     */
    public static void render(
        LivingEntity livingEntity,
        int slotX,
        int slotY,
        int slotSize,
        int margin,
        GuiGraphics guiGraphics,
        double mouseX,
        double mouseY
    ) {
        EntityType<?> entityType = livingEntity.getType();
        EntityDimensions dimensions = entityType.getDimensions();
        float scale = calculateScale(dimensions, slotSize, margin);
        if (entityType == EntityType.ENDER_DRAGON) {
            scale *= 2F;
        }

        float centerX = slotX + slotSize / 2.0F;
        float centerY = slotY + slotSize / 2.0F;

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
     * 计算实体的统一基准缩放倍数，使渲染大小与碰撞箱成正比，保留实体间相对大小差异。
     * <p>
     * 以玩家高度 1.8 米为参考占槽位可用空间的 75%，使用 0.8 次方指数曲线压缩大小范围，
     * 让小实体不至于太小、大实体不至于缩太狠，最小实体至少占 35%。
     */
    private static float calculateScale(EntityDimensions dimensions, int slotSize, int margin) {
        int available = slotSize - margin * 2;

        float entityMaxDim = Math.max(dimensions.height(), dimensions.width());
        float normalizedSize = entityMaxDim / 1.8F;
        float compressed = (float) Math.pow(normalizedSize, 0.8);
        float targetRenderSize = compressed * (available * 0.75F);

        float minSize = available * 0.35F;
        float clampedSize = Math.clamp(targetRenderSize, minSize, available);
        return clampedSize / entityMaxDim;
    }
}
