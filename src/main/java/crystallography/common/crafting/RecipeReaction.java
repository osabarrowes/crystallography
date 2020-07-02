package crystallography.common.crafting;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import crystallography.init.ModRecipeTypes;
import crystallography.misc.IRecipeReaction;
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

public class RecipeReaction implements IRecipeReaction {

    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient input;
    private final int mana;
    @Nullable
    private final BlockState catalystState;
    private final String group;

    public RecipeReaction(ResourceLocation id, ItemStack output, Ingredient input, int mana,
                              @Nullable String group, @Nullable BlockState catalystState) {
        this.id = id;
        this.output = output;
        this.input = input;
        this.mana = mana;
        // Preconditions.checkArgument(mana < 100000); // FIXME
        this.group = group == null ? "" : group;
        this.catalystState = catalystState;
    }

    @Override
    public boolean matches(ItemStack itemStack) {
        return input.test(itemStack); // TODO what is this
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
        return ModRecipeTypes.REACTION_SERIALIZER;
    }

    // Copied from Botania's RecipeManaInfusion
    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeReaction> {

        @Nonnull
        @Override
        public RecipeReaction read(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
            JsonElement input = Objects.requireNonNull(json.get("input"));
            Ingredient ing = Ingredient.deserialize(input);
            ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "output"), true);
            int mana = JSONUtils.getInt(json, "mana");
            String group = JSONUtils.getString(json, "group", "");
            BlockState catalystState = null;
            if (json.has("catalyst")) {
                if (json.get("catalyst").isJsonPrimitive()) {
                    String s = JSONUtils.getString(json, "catalyst");
                    ResourceLocation catalystId = ResourceLocation.tryCreate(s);
                    if (catalystId == null) {
                        throw new IllegalArgumentException("Invalid catalyst ID: " + s);
                    }
                    Optional<Block> cat = Registry.BLOCK.getValue(catalystId);
                    if (!cat.isPresent()) {
                        throw new IllegalArgumentException("Unknown catalyst: " + s);
                    }
                    catalystState = cat.get().getDefaultState();
                } else {
                    // FIXME
                    // catalystState = StateIngredientHelper.readBlockState(JSONUtils.getJsonObject(json, "catalyst"));
                }
            }

            RecipeReaction ret = new RecipeReaction(id, output, ing, mana, group, catalystState);
            return ret;
        }

        @Nullable
        @Override
        public RecipeReaction read(@Nonnull ResourceLocation id, @Nonnull PacketBuffer buf) {
            Ingredient input = Ingredient.read(buf);
            ItemStack output = buf.readItemStack();
            int mana = buf.readVarInt();
            int catalystId = buf.readInt();
            String group = buf.readString();
            BlockState catalystState = catalystId == -1 ? null : Block.getStateById(catalystId);
            return new RecipeReaction(id, output, input, mana, group, catalystState);
        }

        @Override
        public void write(@Nonnull PacketBuffer buf, @Nonnull RecipeReaction recipe) {
            recipe.getIngredients().get(0).write(buf);
            buf.writeItemStack(recipe.getRecipeOutput(), false);
            // buf.writeVarInt(recipe.getManaToConsume()); // FIXME
            buf.writeInt(recipe.getCatalyst() == null ? -1 : Block.getStateId(recipe.getCatalyst()));
            buf.writeString(recipe.getGroup());
        }
    }
}
