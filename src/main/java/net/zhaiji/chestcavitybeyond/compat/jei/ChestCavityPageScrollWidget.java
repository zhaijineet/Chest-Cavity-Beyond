package net.zhaiji.chestcavitybeyond.compat.jei;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.inputs.IJeiInputHandler;
import mezz.jei.api.gui.inputs.IJeiUserInput;
import mezz.jei.api.gui.inputs.RecipeSlotUnderMouse;
import mezz.jei.api.gui.widgets.ISlottedRecipeWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.AttributeBonus;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.api.TooltipsKeyContext;
import net.zhaiji.chestcavitybeyond.api.capability.Organ;
import net.zhaiji.chestcavitybeyond.attachment.ChestCavityData;
import net.zhaiji.chestcavitybeyond.util.ChestCavityUtil;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Optional;

public class ChestCavityPageScrollWidget implements ISlottedRecipeWidget, IJeiInputHandler {
    // 布局常量
    static final int SLOT_SIZE = 18;
    static final int GRID_COLS = 9;
    static final int BG_WIDTH = 179;
    // 3~6排
    static final int[] BG_HEIGHTS = {
        87,
        105,
        123,
        141
    };
    static final int ENTITY_GAP = 4;

    // 背景图内slot格起始偏移
    static final int FIRST_SLOT_INNER_X = 8;
    static final int FIRST_SLOT_Y = 18;
    // 可见区域
    static final int VISIBLE_HEIGHT = 127;
    static final int WIDGET_WIDTH = 213;
    static final int BG_OFFSET_X = (WIDGET_WIDTH - BG_WIDTH) / 2;
    private static final int ATTR_ICON_X = BG_OFFSET_X + 143;
    private static final int HEALTH_ICON_X = BG_OFFSET_X + 152;
    private static final int BREATH_ICON_X = BG_OFFSET_X + 161;
    // 滚动条
    private static final int SCROLLBAR_WIDTH = 14;
    private static final int SCROLLBAR_PADDING = 2;
    private static final int MIN_SCROLL_MARKER_HEIGHT = 14;
    private static final int SCROLLBAR_COLOR = 0xFFC6C6C6;
    private static final int SCROLLBAR_X = WIDGET_WIDTH - SCROLLBAR_WIDTH;
    private static final int ICON_SIZE = 7;
    private static final int ICON_Y = 7;
    // 图标纹理（sprite sheet: 3列×2行，每格7×7）
    // Row0: [0,0]需要呼吸 [7,0]需要健康 [14,0]额外属性
    // Row1: [0,7]不需要呼吸 [7,7]不需要健康 [14,7]空白
    private static final ResourceLocation ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(
        ChestCavityBeyond.MOD_ID,
        "textures/gui/jei/jei_chest_cavity_widget.png"
    );
    private static final int ICON_TEX_WIDTH = 21;
    private static final int ICON_TEX_HEIGHT = 14;
    // 滚动翻页追踪
    private static final int CONTINUOUS_SCROLL_PAGE_TICKS = 8;
    private static final int SCROLL_INTERRUPT_TICKS = 2;

    private static @Nullable ChestCavityTypeDisplay currentHoveredDisplay;
    private static int currentHoveredOrganIndex = -1;
    private static @Nullable ChestCavityType cachedType;
    private static @Nullable ChestCavityData cachedData;
    /**
     * widget 最近一次渲染的游戏刻，用于校验悬停状态是否仍然有效
     */
    private static long lastActiveTick = Long.MIN_VALUE;
    private final ChestCavityTypeDisplay display;
    private final IDrawable[] backgrounds;
    private final IDrawableStatic slotBackground;
    private final List<IRecipeSlotDrawable> organSlots;
    private final List<IRecipeSlotDrawable> entitySlots;
    private final int organRows;
    private final int bgHeight;
    private final int totalContentHeight;
    private final int hiddenAmount;
    // 保存每个slot的原始内容坐标位置
    // organSlotPositions[i*2]=contentX, organSlotPositions[i*2+1]=contentY
    private final int[] organSlotPositions;
    private final int[] entitySlotPositions;
    // 滚动状态
    private float scrollOffsetY = 0;
    private double dragOriginY = -1;
    private int scrollDirection = 0;
    private int continuousScrollTicks = 0;
    private int idleTicks = 0;
    private boolean hadScrollThisTick = false;

    public ChestCavityPageScrollWidget(
        ChestCavityTypeDisplay display,
        IDrawable[] backgrounds,
        IDrawableStatic slotBackground,
        List<IRecipeSlotDrawable> organSlots,
        List<IRecipeSlotDrawable> entitySlots
    ) {
        this.display = display;
        this.backgrounds = backgrounds;
        this.slotBackground = slotBackground;
        this.organSlots = organSlots;
        this.entitySlots = entitySlots;

        organRows = display.getSlots() / GRID_COLS;
        bgHeight = getBgHeight(organRows);

        int entityRows = entitySlots.isEmpty() ? 0 : (int) Math.ceil((double) entitySlots.size() / GRID_COLS);
        int entityAreaHeight = entityRows > 0 ? ENTITY_GAP + entityRows * SLOT_SIZE : 0;
        totalContentHeight = bgHeight + entityAreaHeight;
        hiddenAmount = Math.max(totalContentHeight - VISIBLE_HEIGHT, 0);

        // 保存slot原始位置
        organSlotPositions = new int[organSlots.size() * 2];
        for (int i = 0; i < organSlots.size(); i++) {
            @SuppressWarnings("removal")
            Rect2i rect = organSlots.get(i).getRect();
            organSlotPositions[i * 2] = rect.getX();
            organSlotPositions[i * 2 + 1] = rect.getY();
        }
        entitySlotPositions = new int[entitySlots.size() * 2];
        for (int i = 0; i < entitySlots.size(); i++) {
            @SuppressWarnings("removal")
            Rect2i rect = entitySlots.get(i).getRect();
            entitySlotPositions[i * 2] = rect.getX();
            entitySlotPositions[i * 2 + 1] = rect.getY();
        }
    }

    /**
     * 尝试为 JEI 胸腔类型页面处理 tooltip，如果当前不在 JEI 胸腔页面则返回 false。
     */
    public static boolean handleJeiTooltip(
        Organ organ,
        ItemStack stack,
        Item.TooltipContext context,
        List<Component> tooltip,
        TooltipFlag flags
    ) {
        if (currentHoveredDisplay == null) return false;
        Level level = Minecraft.getInstance().level;
        if (level == null) return false;
        // JEI 页面关闭后悬停状态不再被刷新，超过阈值视为失效
        if (level.getGameTime() - lastActiveTick > 3) {
            currentHoveredDisplay = null;
            currentHoveredOrganIndex = -1;
            return false;
        }
        NonNullList<Item> organs = currentHoveredDisplay.getOrgans();
        if (currentHoveredOrganIndex < 0 || currentHoveredOrganIndex >= organs.size()) return false;
        if (organs.get(currentHoveredOrganIndex) != stack.getItem()) return false;
        ChestCavityData data = getOrCreateCachedData(currentHoveredDisplay);
        if (data == null) return true;
        TooltipsKeyContext keyContext = new TooltipsKeyContext(Screen.hasShiftDown(), Screen.hasControlDown());
        organ.organTooltip(
            ChestCavityUtil.createContext(data, currentHoveredOrganIndex, stack),
            keyContext,
            context,
            tooltip,
            flags
        );
        return true;
    }

    /**
     * 获取或创建指定胸腔类型页面的虚拟胸腔数据，用于 JEI tooltip 计算。
     * <p>
     * 遍历 display 的实体列表，取第一个能 create 成功的 LivingEntity；
     * 虚拟实体不加入 Level，缓存失效后由 GC 回收。
     * </p>
     */
    private static @Nullable ChestCavityData getOrCreateCachedData(ChestCavityTypeDisplay display) {
        ChestCavityType type = display.getType();
        if (cachedType == type && cachedData != null) return cachedData;
        cachedType = null;
        cachedData = null;

        Level level = Minecraft.getInstance().level;
        LivingEntity livingEntity = null;
        for (EntityType<?> entityType : display.getEntities()) {
            Entity candidate = entityType.create(level);
            if (candidate instanceof LivingEntity living) {
                livingEntity = living;
                break;
            }
        }
        if (livingEntity == null) return null;

        ChestCavityData data = ChestCavityUtil.getData(livingEntity);
        data.init();
        cachedType = type;
        cachedData = data;
        return data;
    }

    /**
     * 清空虚拟实体缓存，供玩家登出等场景调用。
     * <p>
     * 虚拟实体不加入 Level，无需 discard，置空引用后由 GC 回收。
     * </p>
     */
    public static void invalidate() {
        cachedType = null;
        cachedData = null;
    }

    static int getBgHeight(int rows) {
        int index = rows - 3;
        if (index >= 0 && index < BG_HEIGHTS.length) {
            return BG_HEIGHTS[index];
        }
        return BG_HEIGHTS[3];
    }

    @Override
    public ScreenPosition getPosition() {
        return new ScreenPosition(0, 0);
    }

    @Override
    public void drawWidget(GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            lastActiveTick = level.getGameTime();
        }
        int scrollPixels = Math.round(hiddenAmount * scrollOffsetY);

        // 绘制滚动条
        drawScrollbar(guiGraphics);

        // scissor裁剪到背景图区域
        PoseStack poseStack = guiGraphics.pose();
        Matrix4f pose = poseStack.last().pose();
        int screenX = (int) (pose.m30());
        int screenY = (int) (pose.m31());
        guiGraphics.enableScissor(
            screenX + BG_OFFSET_X,
            screenY,
            screenX + BG_OFFSET_X + BG_WIDTH,
            screenY + VISIBLE_HEIGHT
        );

        // 绘制背景图
        int bgIndex = organRows - 3;
        if (bgIndex >= 0 && bgIndex < backgrounds.length) {
            backgrounds[bgIndex].draw(guiGraphics, BG_OFFSET_X, -scrollPixels);
        }

        // 绘制标题
        Component title = ChestCavityTypeDisplay.getTranslatedName(display.getTypeId());
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.drawString(minecraft.font, title, BG_OFFSET_X + FIRST_SLOT_INNER_X + 1, 5 - scrollPixels, 0x404040, false);

        // 绘制属性图标
        int iconY = ICON_Y - scrollPixels;
        List<AttributeBonus> typeBonuses = display.getType().getTypeDefaultBonuses();
        if (!typeBonuses.isEmpty()) {
            guiGraphics.blit(ICON_TEXTURE, ATTR_ICON_X, iconY, 14, 0, ICON_SIZE, ICON_SIZE, ICON_TEX_WIDTH, ICON_TEX_HEIGHT);
        }

        // 绘制呼吸图标
        int breathV = display.getNeedBreath() ? 0 : 7;
        guiGraphics.blit(ICON_TEXTURE, BREATH_ICON_X, iconY, 0, breathV, ICON_SIZE, ICON_SIZE, ICON_TEX_WIDTH, ICON_TEX_HEIGHT);

        // 绘制健康图标
        int healthV = display.getNeedHealth() ? 0 : 7;
        guiGraphics.blit(ICON_TEXTURE, HEALTH_ICON_X, iconY, 7, healthV, ICON_SIZE, ICON_SIZE, ICON_TEX_WIDTH, ICON_TEX_HEIGHT);

        // 绘制器官网格slots
        for (int i = 0; i < organSlots.size(); i++) {
            int contentX = organSlotPositions[i * 2];
            int contentY = organSlotPositions[i * 2 + 1];
            int visibleY = contentY - scrollPixels;

            if (visibleY + SLOT_SIZE > 0 && visibleY < VISIBLE_HEIGHT) {
                IRecipeSlotDrawable slot = organSlots.get(i);
                slot.setPosition(contentX, visibleY);
                slot.draw(guiGraphics);
            }
        }

        // 绘制实体网格slots
        for (int i = 0; i < entitySlots.size(); i++) {
            int contentX = entitySlotPositions[i * 2];
            int contentY = entitySlotPositions[i * 2 + 1];
            int visibleY = contentY - scrollPixels;

            if (visibleY + SLOT_SIZE > 0 && visibleY < VISIBLE_HEIGHT) {
                // 绘制slot背景
                slotBackground.draw(guiGraphics, contentX - 1, visibleY - 1);
                IRecipeSlotDrawable slot = entitySlots.get(i);
                slot.setPosition(contentX, visibleY);
                slot.draw(guiGraphics);
            }
        }

        guiGraphics.disableScissor();
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, double mouseX, double mouseY) {
        int scrollPixels = Math.round(hiddenAmount * scrollOffsetY);
        // 将鼠标坐标转换为内容坐标
        double contentMouseY = mouseY + scrollPixels;

        String modId = ChestCavityBeyond.MOD_ID;

        // 属性加成方块区域检测
        List<AttributeBonus> typeBonuses = display.getType().getTypeDefaultBonuses();
        if (!typeBonuses.isEmpty()
            && mouseX >= ATTR_ICON_X && mouseX < ATTR_ICON_X + ICON_SIZE
            && contentMouseY >= ICON_Y && contentMouseY < ICON_Y + ICON_SIZE) {
            if (ICON_Y + ICON_SIZE > scrollPixels && ICON_Y < scrollPixels + VISIBLE_HEIGHT) {
                tooltip.add(Component.translatable("jei." + modId + ".type_default_bonus_header")
                    .withStyle(ChatFormatting.GOLD));
                tooltip.addAll(TooltipUtil.attributeBonusTooltip(typeBonuses));
            }
        }

        // 呼吸方块区域检测
        if (mouseX >= BREATH_ICON_X && mouseX < BREATH_ICON_X + ICON_SIZE
            && contentMouseY >= ICON_Y && contentMouseY < ICON_Y + ICON_SIZE
            && ICON_Y + ICON_SIZE > scrollPixels && ICON_Y < scrollPixels + VISIBLE_HEIGHT) {
            String key = display.getNeedBreath()
                         ? "jei." + modId + ".need_breath"
                         : "jei." + modId + ".no_breath";
            tooltip.add(Component.translatable(key));
        }

        // 健康方块区域检测
        if (mouseX >= HEALTH_ICON_X && mouseX < HEALTH_ICON_X + ICON_SIZE
            && contentMouseY >= ICON_Y && contentMouseY < ICON_Y + ICON_SIZE
            && ICON_Y + ICON_SIZE > scrollPixels && ICON_Y < scrollPixels + VISIBLE_HEIGHT) {
            String key = display.getNeedHealth()
                         ? "jei." + modId + ".need_health"
                         : "jei." + modId + ".no_health";
            tooltip.add(Component.translatable(key));
        }
    }

    @Override
    public void tick() {
        if (hiddenAmount <= 0) {
            return;
        }

        if (hadScrollThisTick) {
            idleTicks = 0;
            if (scrollDirection != 0) {
                if (scrollOffsetY <= 0.0F || scrollOffsetY >= 1.0F) {
                    continuousScrollTicks++;
                }
            }
        } else {
            idleTicks++;
            if (idleTicks > SCROLL_INTERRUPT_TICKS) {
                // 滚动已中断，重置连续滚动追踪
                continuousScrollTicks = 0;
                scrollDirection = 0;
            }
        }
        hadScrollThisTick = false;
    }

    private int getScrollMarkerHeight() {
        int totalSpace = VISIBLE_HEIGHT - 2;
        int h = Math.round(totalSpace * ((float) VISIBLE_HEIGHT / (VISIBLE_HEIGHT + hiddenAmount)));
        return Math.max(h, MIN_SCROLL_MARKER_HEIGHT);
    }

    private void drawScrollbar(GuiGraphics guiGraphics) {
        if (hiddenAmount <= 0) {
            return;
        }

        int scrollbarBgX = SCROLLBAR_X + SCROLLBAR_PADDING;
        int scrollbarBgWidth = SCROLLBAR_WIDTH - SCROLLBAR_PADDING * 2;
        guiGraphics.fill(
            scrollbarBgX, 0,
            scrollbarBgX + scrollbarBgWidth, VISIBLE_HEIGHT,
            0xFF000000
        );

        int totalSpace = VISIBLE_HEIGHT - 2;
        int scrollMarkerWidth = SCROLLBAR_WIDTH - SCROLLBAR_PADDING * 2 - 2;
        int scrollMarkerHeight = getScrollMarkerHeight();
        int scrollMarkerY = Math.round((totalSpace - scrollMarkerHeight) * scrollOffsetY);

        guiGraphics.fill(
            SCROLLBAR_X + SCROLLBAR_PADDING + 1,
            1 + scrollMarkerY,
            SCROLLBAR_X + SCROLLBAR_PADDING + 1 + scrollMarkerWidth,
            1 + scrollMarkerY + scrollMarkerHeight,
            SCROLLBAR_COLOR
        );
    }

    @Override
    public Optional<RecipeSlotUnderMouse> getSlotUnderMouse(double mouseX, double mouseY) {
        int scrollPixels = Math.round(hiddenAmount * scrollOffsetY);

        // 器官slot
        for (int i = 0; i < organSlots.size(); i++) {
            int contentY = organSlotPositions[i * 2 + 1];
            int visibleY = contentY - scrollPixels;
            if (visibleY + SLOT_SIZE > 0 && visibleY < VISIBLE_HEIGHT) {
                IRecipeSlotDrawable slot = organSlots.get(i);
                if (slot.isMouseOver(mouseX, mouseY)) {
                    currentHoveredOrganIndex = i;
                    currentHoveredDisplay = this.display;
                    return Optional.of(new RecipeSlotUnderMouse(slot, getPosition()));
                }
            }
        }

        // 实体slot
        for (int i = 0; i < entitySlots.size(); i++) {
            int contentY = entitySlotPositions[i * 2 + 1];
            int visibleY = contentY - scrollPixels;
            if (visibleY + SLOT_SIZE > 0 && visibleY < VISIBLE_HEIGHT) {
                IRecipeSlotDrawable slot = entitySlots.get(i);
                if (slot.isMouseOver(mouseX, mouseY)) {
                    currentHoveredOrganIndex = -1;
                    currentHoveredDisplay = this.display;
                    return Optional.of(new RecipeSlotUnderMouse(slot, getPosition()));
                }
            }
        }

        currentHoveredOrganIndex = -1;
        currentHoveredDisplay = null;
        return Optional.empty();
    }

    @Override
    public ScreenRectangle getArea() {
        return new ScreenRectangle(0, 0, WIDGET_WIDTH, VISIBLE_HEIGHT);
    }

    @Override
    public boolean handleInput(double mouseX, double mouseY, IJeiUserInput userInput) {
        InputConstants.Key key = userInput.getKey();
        if (key.getType() != InputConstants.Type.MOUSE || key.getValue() != InputConstants.MOUSE_BUTTON_LEFT) {
            return false;
        }

        if (!userInput.isSimulate()) {
            dragOriginY = -1;
        }

        if (mouseX < SCROLLBAR_X || mouseX > SCROLLBAR_X + SCROLLBAR_WIDTH
            || mouseY < 0 || mouseY > VISIBLE_HEIGHT) {
            return false;
        }

        if (hiddenAmount <= 0) {
            return false;
        }

        if (userInput.isSimulate()) {
            int totalSpace = VISIBLE_HEIGHT - 2;
            int scrollMarkerHeight = getScrollMarkerHeight();
            int scrollMarkerY = Math.round((totalSpace - scrollMarkerHeight) * scrollOffsetY);

            double markerTop = 1 + scrollMarkerY;
            double markerBottom = markerTop + scrollMarkerHeight;

            if (mouseY < markerTop || mouseY > markerBottom) {
                double topY = mouseY - scrollMarkerHeight / 2.0;
                double relativeY = topY - 1;
                scrollOffsetY = (float) (relativeY / (totalSpace - scrollMarkerHeight));
                scrollOffsetY = Mth.clamp(scrollOffsetY, 0.0F, 1.0F);

                scrollMarkerY = Math.round((totalSpace - scrollMarkerHeight) * scrollOffsetY);
                markerTop = 1 + scrollMarkerY;
            }
            dragOriginY = mouseY - markerTop;
        }
        return true;
    }

    @Override
    public boolean handleMouseScrolled(double mouseX, double mouseY, double scrollDeltaX, double scrollDeltaY) {
        if (hiddenAmount > 0) {
            int direction = scrollDeltaY > 0 ? 1 : (scrollDeltaY < 0 ? -1 : 0);

            if (direction != 0) {
                // 边界处翻页：停顿后向外滚 或 持续同方向滚到边界 → 翻页
                boolean atBottom = scrollOffsetY >= 1.0F;
                boolean atTop = scrollOffsetY <= 0.0F;
                if ((atBottom && direction == -1) || (atTop && direction == 1)) {
                    if (idleTicks > SCROLL_INTERRUPT_TICKS
                        || continuousScrollTicks >= CONTINUOUS_SCROLL_PAGE_TICKS) {
                        resetScrollTracking();
                        return false;
                    }
                }

                // 方向变化时重置连续计数
                if (direction != scrollDirection) {
                    scrollDirection = direction;
                    continuousScrollTicks = 0;
                }

                hadScrollThisTick = true;
                idleTicks = 0;
            }

            // 正常滚动
            float scrollAmount = (float) (scrollDeltaY * SLOT_SIZE / (double) hiddenAmount);
            scrollOffsetY -= scrollAmount;
            scrollOffsetY = Mth.clamp(scrollOffsetY, 0.0F, 1.0F);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleMouseDragged(double mouseX, double mouseY, InputConstants.Key mouseKey, double dragX, double dragY) {
        if (dragOriginY < 0 || mouseKey.getValue() != InputConstants.MOUSE_BUTTON_LEFT) {
            return false;
        }
        if (hiddenAmount <= 0) {
            return false;
        }

        int totalSpace = VISIBLE_HEIGHT - 2;
        int scrollMarkerHeight = getScrollMarkerHeight();

        double topY = mouseY - dragOriginY;
        double relativeY = topY - 1;
        scrollOffsetY = (float) (relativeY / (totalSpace - scrollMarkerHeight));
        scrollOffsetY = Mth.clamp(scrollOffsetY, 0.0F, 1.0F);
        return true;
    }

    private void resetScrollTracking() {
        scrollDirection = 0;
        continuousScrollTicks = 0;
        idleTicks = 0;
        hadScrollThisTick = false;
    }

}
