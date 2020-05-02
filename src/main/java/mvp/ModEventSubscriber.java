package mvp;

import mvp.MVPMod;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

/*
 * The @EventBusSubscriber annotation indicates to Forge that this class
 * contains methods that should be subscribed to handle events.
 * It contains the modid, bus and value parameters.
 *
 * https://cadiboo.github.io/tutorials/1.15.1/forge/1.5-first-item/
 */
@Mod.EventBusSubscriber(modid = MVPMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {

    // The method signature here is boilerplate code to subscribe to Forge's event bus for Item registration.
    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();
        r.registerAll(
                // Here you can register items.
                setup(new Item(new Item.Properties()), "example_item"),
                setup(new Item(new Item.Properties()), "example_item2")
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
