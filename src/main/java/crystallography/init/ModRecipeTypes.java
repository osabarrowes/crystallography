package crystallography.init;

import crystallography.Crystallography;
import crystallography.common.crafting.RecipeVatReaction;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeTypes {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_TYPES = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, Crystallography.MOD_ID);

    public static final IRecipeSerializer<RecipeVatReaction> VAT_REACTION_SERIALIZER = new RecipeVatReaction.Serializer();

    public static final RegistryObject<IRecipeSerializer<RecipeVatReaction>> RECIPE_VAT_REACTION = RECIPE_TYPES.register("recipe_vat_reaction", RecipeVatReaction.Serializer::new);

}
