package crystallography.common.crafting;

import crystallography.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

/**
 * Contains lists of all items which are valid solutes for the vat. A solute is any item which can be dissolved into solution.
 * @author xenonni
 */
public class Solutes {
    /**
     * Don't let anyone instantiate this class.
     */
    private Solutes(){}

    // TODO use tags (once called OreDict) to determine these things instead of hard coding them

    public static boolean isAcceptedOre(Item item)
    {
        return item == Items.IRON_ORE;
    }

    public static boolean isAcceptedCatalyst(Item item) {
        return item == ModItems.EXAMPLE_ITEM;
    }

    public static boolean isAcceptedMisc(Item item) {
        return false;
    }

    public static int countAllSolutes()
    {
        // TODO return a count of all registered solutes
        return 2;
    }
}
