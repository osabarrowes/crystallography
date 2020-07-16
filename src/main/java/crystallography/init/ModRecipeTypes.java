package crystallography.init;

import crystallography.Crystallography;
import crystallography.common.crafting.RecipeVatReaction;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeTypes {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_TYPES = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, Crystallography.MOD_ID);

    public static final IRecipeSerializer<RecipeVatReaction> VAT_REACTION_SERIALIZER = new RecipeVatReaction.Serializer();
    public static final RegistryObject<IRecipeSerializer<RecipeVatReaction>> RECIPE_VAT_REACTION = RECIPE_TYPES.register("recipe_vat_reaction_serializer", RecipeVatReaction.Serializer::new);

    // I'm not sure how neccessary this is, but it seems each recipe needs a separate type and serializer to be registered
    public static final IRecipeType<RecipeVatReaction> RECIPE_VAT_REACTION_TYPE = register("recipe_vat_reaction");

    static <T extends IRecipe<?>> IRecipeType<T> register(final String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(key), new IRecipeType<T>() {
            public String toString() {
                return key;
            }
        });
    }

}
