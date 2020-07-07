package crystallography.common.crafting;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import crystallography.Crystallography;
import crystallography.init.ModRecipeTypes;
import crystallography.misc.IRecipeVatReaction;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author xenonni
 */
public class RecipeVatReaction implements IRecipeVatReaction {

    private final ResourceLocation id;
    private final Block output;
    private final Ingredient input;
    private final int activationEnergy;
    @Nullable
    private final String group;

    public RecipeVatReaction(ResourceLocation id, Block output, Ingredient input, int activationEnergy,
                             @Nullable String group) {
        this.id = id;
        this.output = output;
        this.input = input;
        this.activationEnergy = activationEnergy;
        Preconditions.checkArgument(activationEnergy > 0);
        this.group = group == null ? "" : group; // groups are optional
    }

    @Override
    public int getActivationEnergy() {
        return activationEnergy;
    }

    @Override
    public Block getOutputBlock() {
        return output;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(output.asItem());
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

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeVatReaction> {
        private static final ResourceLocation NAME = new ResourceLocation(Crystallography.MOD_ID, "vat_reaction");
        @Nonnull
        @Override
        public RecipeVatReaction read(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
            JsonElement input = Objects.requireNonNull(json.get("input"));
            Ingredient ing = Ingredient.deserialize(input);
            ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "output"), true);
            Block outputBlock = getBlockFromItem(output);
            int defaultCookTime = JSONUtils.getInt(json, "defaultCookTime");
            RecipeVatReaction rxn = new RecipeVatReaction(id, outputBlock, ing, defaultCookTime, null);
            return rxn;
        }

        @Nullable
        @Override
        public RecipeVatReaction read(@Nonnull ResourceLocation id, @Nonnull PacketBuffer buf) {
            Ingredient input = Ingredient.read(buf);
            ItemStack output = buf.readItemStack();
            Block outputBlock = getBlockFromItem(output);
            int defaultCookTime = buf.readVarInt();
            return new RecipeVatReaction(id, outputBlock, input, defaultCookTime, null);
        }

        @Override
        public void write(@Nonnull PacketBuffer buf, @Nonnull RecipeVatReaction recipe) {
            recipe.getIngredients().get(0).write(buf);
            buf.writeItemStack(recipe.getRecipeOutput(), false);
            buf.writeVarInt(recipe.getActivationEnergy());
        }

        // because vanilla's IRecipe requires outputs to be of type ItemStack, output cannot be type Block.
        private Block getBlockFromItem(ItemStack output) throws JsonParseException {
            if (output.getItem() instanceof BlockItem)
                return ((BlockItem) output.getItem()).getBlock();
            else
                throw new JsonParseException("Output in vat recipes must be of type BlockItem");
        }
    }

}
