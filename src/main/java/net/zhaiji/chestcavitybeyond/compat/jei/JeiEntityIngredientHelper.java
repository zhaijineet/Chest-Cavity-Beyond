package net.zhaiji.chestcavitybeyond.compat.jei;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class JeiEntityIngredientHelper implements IIngredientHelper<JeiEntityIngredient> {
    public static final JeiEntityIngredientHelper INSTANCE = new JeiEntityIngredientHelper();

    private JeiEntityIngredientHelper() {
    }

    @Override
    public IIngredientType<JeiEntityIngredient> getIngredientType() {
        return JeiEntityIngredient.TYPE;
    }

    @Override
    public String getDisplayName(JeiEntityIngredient ingredient) {
        return ingredient.entityType().getDescription().getString();
    }

    @SuppressWarnings("removal")
    @Override
    public String getUniqueId(JeiEntityIngredient ingredient, UidContext context) {
        return JeiEntityIngredient.TYPE.getUid() + "/" + ingredient.entityTypeResourceLocation();
    }

    @Override
    public Object getUid(JeiEntityIngredient ingredient, UidContext context) {
        return createUid(ingredient);
    }

    @Override
    public Object getGroupingUid(JeiEntityIngredient ingredient) {
        return createUid(ingredient);
    }

    private static EntityIngredientUid createUid(JeiEntityIngredient ingredient) {
        return new EntityIngredientUid(
            JeiEntityIngredient.TYPE.getUid(),
            ingredient.entityTypeResourceLocation()
        );
    }

    @Override
    public ResourceLocation getResourceLocation(JeiEntityIngredient ingredient) {
        return ingredient.entityTypeResourceLocation();
    }

    @Override
    public JeiEntityIngredient copyIngredient(JeiEntityIngredient ingredient) {
        return ingredient;
    }

    @Override
    public String getErrorInfo(@Nullable JeiEntityIngredient ingredient) {
        if (ingredient == null) {
            return "entity ingredient: null";
        }
        return "entity ingredient: " + ingredient.entityTypeResourceLocation();
    }

    private record EntityIngredientUid(String ingredientTypeUid, ResourceLocation entityTypeResourceLocation) {
    }
}
