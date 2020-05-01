package mvp.common.core;

import mvp.common.item.ModItems;
import mvp.common.lib.LibReferences;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MVPCreativeTab extends ItemGroup {

    public static final MVPCreativeTab INSTANCE = new MVPCreativeTab();

    public MVPCreativeTab() {
        super(LibReferences.MOD_ID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.testItem);
    }
}
