package crystallography.init;

import crystallography.common.crafting.RecipeReaction;
import crystallography.misc.IRecipeReaction;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;

public class ModRecipeTypes {

    public static final IRecipeType<IRecipeReaction> REACTION_TYPE = new RecipeType<>();
    public static final IRecipeSerializer<RecipeReaction> REACTION_SERIALIZER = new RecipeReaction.Serializer();


    private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
        @Override
        public String toString() {
            return Registry.RECIPE_TYPE.getKey(this).toString();
        }
    }
}
