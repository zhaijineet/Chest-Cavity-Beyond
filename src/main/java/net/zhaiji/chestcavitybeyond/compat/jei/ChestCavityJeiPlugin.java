package net.zhaiji.chestcavitybeyond.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.zhaiji.chestcavitybeyond.ChestCavityBeyond;
import net.zhaiji.chestcavitybeyond.api.ChestCavityType;
import net.zhaiji.chestcavitybeyond.manager.ChestCavityTypeManager;
import net.zhaiji.chestcavitybeyond.manager.ItemTagManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在 JEI 中注册胸腔类型信息页面，让玩家能浏览所有胸腔类型的器官布局和附加实体。
 */
@JeiPlugin
public class ChestCavityJeiPlugin implements IModPlugin {
    public static final RecipeType<ChestCavityTypeDisplay> CHEST_CAVITY_TYPE = RecipeType.create(
        ChestCavityBeyond.MOD_ID,
        "chest_cavity_type",
        ChestCavityTypeDisplay.class
    );

    @Override
    public ResourceLocation getPluginUid() {
        return ChestCavityBeyond.of("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
            new ChestCavityTypeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // 构建反向映射：ChestCavityType → List<EntityType<?>>
        Map<ChestCavityType, List<EntityType<?>>> typeToEntities = new HashMap<>();

        for (Map.Entry<EntityType<? extends LivingEntity>, ChestCavityType> entry :
            ChestCavityTypeManager.ENTITY_CHEST_CAVITY_TYPE_MAP.entrySet()) {
            typeToEntities.computeIfAbsent(entry.getValue(), k -> new ArrayList<>()).add(entry.getKey());
        }

        // 为每个注册的胸腔类型创建 Display
        List<ChestCavityTypeDisplay> displays = new ArrayList<>();
        for (Map.Entry<ResourceLocation, ChestCavityType> entry :
            ChestCavityTypeManager.CHEST_CAVITY_TYPE_REGISTRY.entrySet()) {
            ResourceLocation typeId = entry.getKey();
            ChestCavityType type = entry.getValue();
            List<EntityType<?>> entities = typeToEntities.getOrDefault(type, List.of());
            displays.add(new ChestCavityTypeDisplay(typeId, type, entities));
        }

        registration.addRecipes(CHEST_CAVITY_TYPE, displays);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // 遍历 chest_openers tag，将所有开胸器注册为催化剂
        BuiltInRegistries.ITEM.getTag(ItemTagManager.CHEST_OPENERS)
            .ifPresent(holderSet -> {
                for (Holder<Item> holder : holderSet) {
                    registration.addRecipeCatalyst(
                        holder.value().getDefaultInstance(),
                        CHEST_CAVITY_TYPE
                    );
                }
            });
    }
}
