package crystallography.init;

import crystallography.Crystallography;
import crystallography.block.TestBlock;
import crystallography.block.TestControllerBlock;
import crystallography.block.Vat_component_functional_overseer_T1;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Holds a list of all our {@link Block}s.
 * Suppliers that create Blocks are added to the DeferredRegister.
 * The DeferredRegister is then added to our mod event bus in our constructor.
 * When the Block Registry Event is fired by Forge and it is time for the mod to
 * register its Blocks, our Blocks are created and registered by the DeferredRegister.
 * The Block Registry Event will always be called before the Item registry is filled.
 * Note: This supports registry overrides.
 * Documentation and structure copied from Cadiboo's example mod
 *
 * @author xenonni
 */
public class ModBlocks {

    // TODO blocks should have properties more indicative of their structure rather than all being the same material, hardness and resistance, etc.
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Crystallography.MOD_ID);

    // This block has the ROCK material, meaning it needs at least a wooden pickaxe to break it. It is very similar to Iron Ore
    public static final RegistryObject<Block> EXAMPLE_ORE = BLOCKS.register("example_ore", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> EXAMPLE_ORE_2 = BLOCKS.register("example_ore_2", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> LEACH = BLOCKS.register("leach", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> VAT_COMPONENT_STRUCTURAL_T1 = BLOCKS.register("vat_component_structural_t1", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> VAT_COMPONENT_STRUCTURAL_T1_TRANSPARENT = BLOCKS.register("vat_component_structural_t1_transparent", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> ENCHANTING_BLOCK = BLOCKS.register("enchanting_block", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> NUCLEATION_BLOCK = BLOCKS.register("nucleation_block", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)));

    public static final RegistryObject<Block> TEST_BLOCK = BLOCKS.register("test_block", () -> new TestBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)));
    public static final RegistryObject<Block> TEST_CONTROLLER_BLOCK = BLOCKS.register("test_controller_block", () -> new TestControllerBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)));
}
