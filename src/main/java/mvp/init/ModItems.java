package mvp.init;

import mvp.MVPMod;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.registries.ObjectHolder;

/*
 * When you put the @ObjectHolder annotation on a class, Forge will look
 *  at every field in the class and set the value of each field. The value
 *  of the field will be set to the object in the field type’s registry that
 *  has a registry name made up of the parameter of the annotation and the
 *  field’s name (in lowercase).
 * For example a field with a type of Item and a name of EXAMPLE_ITEM
 *  (public static final Item EXAMPLE_ITEM = null;) in a class annotated
 *  with @ObjectHolder(ExampleMod.MODID) will be filled with the the Item whose
 *  registry name is examplemod:example_item
 *
 * https://cadiboo.github.io/tutorials/1.15.1/forge/1.7-itemgroup/
 */
@ObjectHolder(MVPMod.MOD_ID)
/**
 * Contains static references to Mod Items, which is useful if you want to reference them somewhere.
 * For example, you could use an item for the mod's ItemGroup.
 */
public class ModItems {
    // while set to null initially, the item will be set to the appropriate reference upon registration.
    public static final Item EXAMPLE_ITEM = null;
    public static final Item EXAMPLE_ITEM_2 = null;
}
