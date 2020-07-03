package crystallography.misc;

import crystallography.Crystallography;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Inspiration drawn from Botania's IManaInfusionRecipe, which I should note is placed in the API package of Botania
 */
public interface IRecipeVatReaction extends IRecipe<IInventory> {
    ResourceLocation TYPE_ID = new ResourceLocation(Crystallography.MOD_ID, "vat_reaction"); // don't hardcode vat_reaction. DRY code is best
    // ResourceLocation TYPE_ID = new ResourceLocation(BotaniaAPI.MODID, "mana_infusion");

    boolean matches (ItemStack itemStack);

    @Nullable
    BlockState getCatalyst();

    // int getManaToConsume();

    @Nonnull
    @Override
    default IRecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getValue(TYPE_ID).get();
    }

    // Ignored IRecipe stuff

    @Nonnull
    @Override
    default ItemStack getCraftingResult(@Nonnull IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    default boolean matches(@Nonnull IInventory inv, @Nonnull World world) {
        return false;
    }

    @Override
    default boolean canFit(int width, int height) {
        return false;
    }

}
