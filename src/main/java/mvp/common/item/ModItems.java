package mvp.common.item;

import mvp.common.core.MVPCreativeTab;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;

public class ModItems {

    public static IItemProvider testItem;

    public static Item.Properties defaultBuilder() {
        return new Item.Properties().group(MVPCreativeTab.INSTANCE);
    }
}
