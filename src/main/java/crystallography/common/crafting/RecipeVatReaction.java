package crystallography.common.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import crystallography.Crystallography;
import crystallography.init.ModRecipeTypes;
import crystallography.misc.IRecipeVatReaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * @author xenonni
 */
public class RecipeVatReaction implements IRecipeVatReaction {

    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient input;
    private final int activationEnergy;
    @Nullable
    private final String group;

    public RecipeVatReaction(ResourceLocation id, ItemStack output, Ingredient input, int activationEnergy,
                             @Nullable String group) {
        this.id = id;
        this.output = output;
        this.input = input;
        this.activationEnergy = activationEnergy;
        // Preconditions.checkArgument(activationEnergy > 0);
        this.group = group == null ? "" : group; // groups are optional
    }

    @Override
    public boolean matches(ItemStack itemStack) {
        return input.test(itemStack); // TODO what is this and how is it used
    }

    @Nullable
    @Override
    public BlockState getCatalyst() {
        return null; // TODO what is this
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.from(null, input);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.VAT_REACTION_SERIALIZER;
    }

    // Copied from Botania's RecipeManaInfusion
    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeVatReaction> {
        private static final ResourceLocation NAME = new ResourceLocation(Crystallography.MOD_ID, "vat_reaction"); // Vanilla has NAME, but botania doesn't. why?
        // TODO this is how your JSON files are being serialized and deserialized, so don't copy botania's implementation
        @Nonnull
        @Override
        public RecipeVatReaction read(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
            JsonElement input = Objects.requireNonNull(json.get("input")); // why don't you need to do getItemStack?
            Ingredient ing = Ingredient.deserialize(input);
            ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "output"), true); // because vanilla's IRecipe requires outputs to be of type ItemStack, output cannot be type Block.
            int defaultCookTime = JSONUtils.getInt(json, "defaultCookTime");
            RecipeVatReaction ret = new RecipeVatReaction(id, output, ing, defaultCookTime, null);
            return ret;
        }

        @Nullable
        @Override
        public RecipeVatReaction read(@Nonnull ResourceLocation id, @Nonnull PacketBuffer buf) {
            Ingredient input = Ingredient.read(buf);
            ItemStack output = buf.readItemStack();
            int defaultCookTime = buf.readVarInt();
            return new RecipeVatReaction(id, output, input, defaultCookTime, null);
        }

        @Override
        public void write(@Nonnull PacketBuffer buf, @Nonnull RecipeVatReaction recipe) {
            recipe.getIngredients().get(0).write(buf);
            buf.writeItemStack(recipe.getRecipeOutput(), false);
            // buf.writeVarInt(recipe.getManaToConsume()); // FIXME
        }
    }
}
