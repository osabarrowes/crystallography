package crystallography.init;

import crystallography.Crystallography;
import crystallography.common.crafting.RecipeVatReaction;
import crystallography.misc.IRecipeVatReaction;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeTypes {

    public static final IRecipeType<IRecipeVatReaction> VAT_REACTION_TYPE = new RecipeType<>();
    public static final IRecipeSerializer<RecipeVatReaction> VAT_REACTION_SERIALIZER = new RecipeVatReaction.Serializer();
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_TYPES = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, Crystallography.MOD_ID);


    /*
     * convenience(?) class, copied from botania's ModRecipeTypes
     */
    private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
        @Override
        public String toString() {
            return Registry.RECIPE_TYPE.getKey(this).toString();
        }
    }
}
