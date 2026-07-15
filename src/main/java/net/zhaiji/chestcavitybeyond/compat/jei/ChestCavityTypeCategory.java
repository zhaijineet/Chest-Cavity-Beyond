package net.zhaiji.chestcavitybeyond.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.ChatFormatting;
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
import net.zhaiji.chestcavitybeyond.client.easter.EasterEggManager;
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
    private static final String ORGAN_SLOT_PREFIX = "organ_";
    private static final String ENTITY_SLOT_PREFIX = "entity_";

    private final IDrawable[] backgrounds;
    private final IDrawableStatic slotBackground;
    private final IIngredientRenderer<ItemStack> itemStackRenderer;
    private final JeiEntityRecipeSlotRenderer entityRecipeSlotRenderer;

    public ChestCavityTypeCategory(
        IGuiHelper guiHelper,
        IIngredientRenderer<ItemStack> itemStackRenderer
    ) {
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
        this.itemStackRenderer = itemStackRenderer;
        entityRecipeSlotRenderer = new JeiEntityRecipeSlotRenderer(slotBackground);
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
        IIngredientAcceptor<?> organInputs = builder.addInvisibleIngredients(RecipeIngredientRole.INPUT);

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < ChestCavityPageScrollWidget.GRID_COLS; column++) {
                int index = row * ChestCavityPageScrollWidget.GRID_COLS + column;
                if (index >= slots) break;
                Item organ = organs.get(index);
                if (organ != Items.AIR) {
                    int slotX = ChestCavityPageScrollWidget.getOrganSlotX(column);
                    int slotY = ChestCavityPageScrollWidget.getOrganSlotY(row);
                    ItemStack organStack = organ.getDefaultInstance();
                    organInputs.addItemStack(organStack);
                    IRecipeSlotBuilder slotBuilder = builder.addOutputSlot(slotX, slotY)
                        .addItemStack(organStack)
                        .setCustomRenderer(
                            VanillaTypes.ITEM_STACK,
                            new JeiOrganSlotRenderer(itemStackRenderer, display, index)
                        )
                        .setSlotName(ORGAN_SLOT_PREFIX + index);
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
        List<EntityType<?>> entities = display.entities();
        for (int i = 0; i < entities.size(); i++) {
            EntityType<?> entityType = entities.get(i);

            int row = i / ChestCavityPageScrollWidget.ENTITY_COLS;
            int column = i % ChestCavityPageScrollWidget.ENTITY_COLS;
            int slotX = ChestCavityPageScrollWidget.getEntitySlotX(column);
            int slotY = ChestCavityPageScrollWidget.getEntitySlotY(rows, row);

            JeiEntityIngredient entityIngredient = new JeiEntityIngredient(entityType);
            builder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY)
                .addIngredient(JeiEntityIngredient.TYPE, entityIngredient)
                .setCustomRenderer(JeiEntityIngredient.TYPE, entityRecipeSlotRenderer)
                .setSlotName(ENTITY_SLOT_PREFIX + i);
            builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT)
                .addIngredient(JeiEntityIngredient.TYPE, entityIngredient);

            ItemStack entityItemStack = resolveEntityItemStack(entityType);
            if (!entityItemStack.isEmpty()) {
                builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(entityItemStack);
                builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(entityItemStack);
            }
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, ChestCavityTypeDisplay display, IFocusGroup focuses) {
        EasterEggManager.rollHerobrine();
        List<ChestCavityPageScrollWidget.IndexedSlot> organSlots = new ArrayList<>();
        List<ChestCavityPageScrollWidget.IndexedSlot> entitySlots = new ArrayList<>();
        List<IRecipeSlotDrawable> managedSlots = new ArrayList<>();

        for (IRecipeSlotDrawable slot : builder.getRecipeSlots().getSlots()) {
            String slotName = slot.getSlotName().orElse(null);
            if (slotName == null) continue;

            if (slotName.startsWith(ENTITY_SLOT_PREFIX)) {
                int entityIndex = parseSlotIndex(slotName, ENTITY_SLOT_PREFIX);
                if (entityIndex < 0) continue;
                entitySlots.add(new ChestCavityPageScrollWidget.IndexedSlot(slot, entityIndex));
                managedSlots.add(slot);
            } else if (slotName.startsWith(ORGAN_SLOT_PREFIX)) {
                int organIndex = parseSlotIndex(slotName, ORGAN_SLOT_PREFIX);
                if (organIndex < 0) continue;
                organSlots.add(new ChestCavityPageScrollWidget.IndexedSlot(slot, organIndex));
                managedSlots.add(slot);
            }
        }

        ChestCavityPageScrollWidget widget = new ChestCavityPageScrollWidget(
            display, backgrounds, organSlots, entitySlots, entityRecipeSlotRenderer
        );

        builder.addSlottedWidget(widget, managedSlots);
        builder.addInputHandler(widget);
    }

    private static int parseSlotIndex(String slotName, String prefix) {
        return Integer.parseInt(slotName.substring(prefix.length()));
    }

    @Override
    public ResourceLocation getRegistryName(ChestCavityTypeDisplay display) {
        return display.typeId();
    }
}
