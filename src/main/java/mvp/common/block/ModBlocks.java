package mvp.common.block;

import mvp.common.item.ModItems;
import mvp.common.lib.BlockNames;
import mvp.common.lib.LibReferences;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = LibReferences.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks{
    public static Block testBlock;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> evt) {

        IForgeRegistry<Block> r = evt.getRegistry();

        Block.Properties builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).sound(SoundType.STONE);
        register(r, new Block(builder), BlockNames.TEST_BLOCK);
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
        IForgeRegistry<Item> r = evt.getRegistry();
        Item.Properties props = ModItems.defaultBuilder();
        register(r, new BlockItem(testBlock, props), testBlock.getRegistryName());
    }
    // Helper methods copied from botania

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name) {
        reg.register(thing.setRegistryName(name));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name) {
        register(reg, thing, new ResourceLocation(LibReferences.MOD_ID, name));
    }
}
