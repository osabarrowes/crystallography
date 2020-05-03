package mvp.init;

import mvp.MVPMod;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.function.Supplier;

// Previously called CreativeTabs, ItemGroups group items together on the creative tabs.
public class ModItemGroups {

    // Note: if there are no Items which use your ItemGroup, then your ItemGroup will not render.
    public static final ItemGroup MOD_ITEM_GROUP = new ModItemGroup(MVPMod.MOD_ID, () -> new ItemStack(Items.LIGHT_BLUE_BANNER));
    /*
     * Helper class to provide the icon for the tab. Usually, you use an item: but ItemGroups are created
     * before items, and so items are null at the time of an ItemGroup's creation. We use a supplier to
     * essentially promise that we will deliver an item when the tab actually needs to be rendered.
     */
    public static class ModItemGroup extends ItemGroup {

        private final Supplier<ItemStack> iconSupplier;

        public ModItemGroup(final String name, final Supplier<ItemStack> iconSupplier)
        {
            super(name);
            this.iconSupplier = iconSupplier;
        }

        @Override
        public ItemStack createIcon() {
            return iconSupplier.get();
        }
    }

}
