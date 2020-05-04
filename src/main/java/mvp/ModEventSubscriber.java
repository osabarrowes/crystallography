package mvp;

import com.google.common.eventbus.Subscribe;
import mvp.MVPMod;
import mvp.init.ModBlocks;
import mvp.init.ModItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * The @EventBusSubscriber annotation indicates to Forge that this class
 * contains methods that should be subscribed to handle events.
 * It contains the modid, bus and value parameters.
 *
 * https://cadiboo.github.io/tutorials/1.15.1/forge/1.5-first-item/
 */
@Mod.EventBusSubscriber(modid = MVPMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {

    private static final Logger LOGGER = LogManager.getLogger(MVPMod.MOD_ID + " Mod Event Subscriber");

    // The method signature here is boilerplate code to subscribe to Forge's event bus for Item registration.
    /**
     * This method will be called by Forge when it is time for the mod to register its Items.
     * This method will always be called after the Block registry method.
     *
     * Code copied from Cadiboo's example mod.
     */
    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();
        // Automatically register BlockItems for all our Blocks
        ModBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                // You can do extra filtering here if you don't want some blocks to have an BlockItem automatically registered for them
                // .filter(block -> needsItemBlock(block))
                // Register the BlockItem for the block
                .forEach(block -> {
                    // Make the properties, and make it so that the item will be on our ItemGroup (CreativeTab)
                    final Item.Properties properties = new Item.Properties().group(ModItemGroups.MOD_ITEM_GROUP);
                    // Create the new BlockItem with the block and it's properties
                    final BlockItem blockItem = new BlockItem(block, properties);
                    // Set the new BlockItem's registry name to the block's registry name
                    blockItem.setRegistryName(block.getRegistryName());
                    // Register the BlockItem
                    registry.register(blockItem);
                });
        LOGGER.debug("Registered BlockItems");

        // register normal items
        event.getRegistry().registerAll(
                // Here you can register items.
                setup(new Item(new Item.Properties().group(ModItemGroups.MOD_ITEM_GROUP)), "example_item"),
                setup(new Item(new Item.Properties().group(ModItemGroups.MOD_ITEM_GROUP)), "example_item_2")
        );
    }

    // Item registration done "correctly"
    public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {
        return setup(entry, new ResourceLocation(MVPMod.MOD_ID, name));
    }

    public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
        entry.setRegistryName(registryName);
        return entry;
    }

}
