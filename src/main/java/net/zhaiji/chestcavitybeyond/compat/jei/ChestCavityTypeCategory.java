package net.zhaiji.chestcavitybeyond.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.AttributeBonus;
import net.zhaiji.chestcavitybeyond.register.InitItem;
import net.zhaiji.chestcavitybeyond.util.TooltipUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * JEI 胸腔类型显示类别，负责渲染一个胸腔类型的完整信息页面。
 * <p>
 * 整个页面由 {@link ChestCavityPageScrollWidget} 统一管理，支持全页面滚动。
 */
public class ChestCavityTypeCategory extends AbstractRecipeCategory<ChestCavityTypeDisplay> {
    private static final String ENTITY_SLOT_PREFIX = "entity_";

    private final IDrawable[] backgrounds;
    private final IDrawableStatic slotBackground;

    public ChestCavityTypeCategory(IGuiHelper guiHelper) {
        super(
            ChestCavityJeiPlugin.CHEST_CAVITY_TYPE,
            Component.translatable("jei." + ChestCavityBeyond.MOD_ID + ".chest_cavity_type"),
            guiHelper.createDrawableItemStack(InitItem.CHEST_OPENER.get().getDefaultInstance()),
            ChestCavityPageScrollWidget.WIDGET_WIDTH,
            ChestCavityPageScrollWidget.VISIBLE_HEIGHT
        );
        backgrounds = new IDrawable[4];
        for (int i = 0; i < 4; i++) {
            int rows = i + 3;
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                ChestCavityBeyond.MOD_ID,
                "textures/gui/jei/jei_chest_cavity_" + rows + ".png"
            );
            backgrounds[i] = guiHelper.drawableBuilder(
                    texture,
                    0,
                    0,
                    ChestCavityPageScrollWidget.BG_WIDTH,
                    ChestCavityPageScrollWidget.BG_HEIGHTS[i]
                )
                .setTextureSize(256, 256)
                .build();
        }
        slotBackground = guiHelper.getSlotDrawable();
    }

    /**
     * 解析实体的展示物品：优先取实体自身的拾取结果（如盔甲架），其次刷怪蛋，玩家等无对应物品的返回空
     *
     * @param entityType 实体类型
     * @return 用于 JEI slot 的物品栈
     */
    private static ItemStack resolveEntityItemStack(EntityType<?> entityType) {
        return JeiEntityCache.getOrCreate(entityType)
            .map(LivingEntity::getPickResult)
            .orElseGet(() -> {
                SpawnEggItem spawnEgg = SpawnEggItem.byId(entityType);
                return spawnEgg != null ? spawnEgg.getDefaultInstance() : ItemStack.EMPTY;
            });
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChestCavityTypeDisplay display, IFocusGroup focuses) {
        // 器官网格
        NonNullList<Item> organs = display.getOrgans();
        int slots = display.getSlots();
        int rows = slots / ChestCavityPageScrollWidget.GRID_COLS;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < ChestCavityPageScrollWidget.GRID_COLS; col++) {
                int index = row * ChestCavityPageScrollWidget.GRID_COLS + col;
                if (index >= slots) break;
                Item organ = organs.get(index);
                if (organ != Items.AIR) {
                    int slotX = ChestCavityPageScrollWidget.BG_OFFSET_X + ChestCavityPageScrollWidget.FIRST_SLOT_INNER_X + col * ChestCavityPageScrollWidget.SLOT_SIZE;
                    int slotY = ChestCavityPageScrollWidget.FIRST_SLOT_Y + row * ChestCavityPageScrollWidget.SLOT_SIZE;
                    IRecipeSlotBuilder slotBuilder = builder.addOutputSlot(slotX, slotY)
                        .addItemStack(organ.getDefaultInstance());
                    List<AttributeBonus> bonuses = display.type().getAttributeBonuses(organ);
                    if (!bonuses.isEmpty()) {
                        slotBuilder.addRichTooltipCallback((slotView, tooltip) -> {
                            tooltip.add(Component.translatable("jei." + ChestCavityBeyond.MOD_ID + ".type_bonus_header")
                                .withStyle(ChatFormatting.GOLD));
                            tooltip.addAll(TooltipUtil.attributeBonusTooltip(bonuses));
                        });
                    }
                }
            }
        }

        // 实体列表
        int bgHeight = ChestCavityPageScrollWidget.getBgHeight(rows);
        int entityStartY = bgHeight + ChestCavityPageScrollWidget.ENTITY_GAP;

        List<EntityType<?>> entities = display.entities();
        for (int i = 0; i < entities.size(); i++) {
            EntityType<?> entityType = entities.get(i);

            int row = i / ChestCavityPageScrollWidget.ENTITY_COLS;
            int column = i % ChestCavityPageScrollWidget.ENTITY_COLS;
            int slotX = ChestCavityPageScrollWidget.ENTITY_AREA_X + column * ChestCavityPageScrollWidget.ENTITY_SLOT_SIZE;
            int slotY = entityStartY + row * ChestCavityPageScrollWidget.ENTITY_SLOT_SIZE;

            builder.addOutputSlot(slotX, slotY)
                .addItemStack(resolveEntityItemStack(entityType))
                .addRichTooltipCallback((slotView, tooltip) -> tooltip.add(entityType.getDescription()))
                .setCustomRenderer(VanillaTypes.ITEM_STACK, EmptyEntityRenderer.INSTANCE)
                .setSlotName(ENTITY_SLOT_PREFIX + i);
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, ChestCavityTypeDisplay display, IFocusGroup focuses) {
        // 按名称前缀将slot分为器官和实体两组
        List<IRecipeSlotDrawable> organSlots = new ArrayList<>();
        List<IRecipeSlotDrawable> entitySlots = new ArrayList<>();
        List<IRecipeSlotDrawable> allSlots = new ArrayList<>();

        for (IRecipeSlotDrawable slot : builder.getRecipeSlots().getSlots()) {
            boolean isEntity = slot.getSlotName()
                .map(name -> name.startsWith(ENTITY_SLOT_PREFIX))
                .orElse(false);
            if (isEntity) {
                entitySlots.add(slot);
            } else {
                organSlots.add(slot);
            }
            allSlots.add(slot);
        }

        // 创建全页面滚动widget
        ChestCavityPageScrollWidget widget = new ChestCavityPageScrollWidget(
            display, backgrounds, slotBackground, organSlots, entitySlots
        );

        // widget接管所有slot的管理，并处理滚动输入
        builder.addSlottedWidget(widget, allSlots);
        builder.addInputHandler(widget);
    }

    @Override
    public void draw(
        ChestCavityTypeDisplay display,
        IRecipeSlotsView recipeSlotsView,
        GuiGraphics guiGraphics,
        double mouseX,
        double mouseY
    ) {
    }

    @Override
    public void getTooltip(
        ITooltipBuilder tooltip,
        ChestCavityTypeDisplay display,
        IRecipeSlotsView recipeSlotsView,
        double mouseX,
        double mouseY
    ) {
    }
}
