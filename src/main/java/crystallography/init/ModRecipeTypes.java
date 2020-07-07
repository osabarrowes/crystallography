package crystallography.init;

import crystallography.Crystallography;
import crystallography.common.crafting.RecipeVatReaction;
import crystallography.misc.IRecipeVatReaction;
import crystallography.tileentity.TestControllerBlockTileEntity;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeTypes {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_TYPES = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, Crystallography.MOD_ID);

    // public static final IRecipeType<IRecipeVatReaction> VAT_REACTION_TYPE = new RecipeType<>();
    public static final IRecipeSerializer<RecipeVatReaction> VAT_REACTION_SERIALIZER = new RecipeVatReaction.Serializer();

    public static final RegistryObject<IRecipeSerializer<RecipeVatReaction>> RECIPE_VAT_REACTION = RECIPE_TYPES.register("recipe_vat_reaction", () -> {
        return new RecipeVatReaction.Serializer();
    });
    /*
     * convenience(?) class, copied from botania's ModRecipeTypes
     */
//    private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
//        @Override
//        public String toString() {
//            return Registry.RECIPE_TYPE.getKey(this).toString();
//        }
//    }
}
