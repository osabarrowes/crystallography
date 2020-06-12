package crystallography.tileentity;

import crystallography.init.ModBlocks;
import crystallography.init.ModItems;
import crystallography.init.ModTileEntityTypes;
import crystallography.libs.Util;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TestControllerBlockTileEntity extends TileEntity implements ITickableTileEntity {

    private static final Logger LOGGER = LogManager.getLogger();

    private Collection<BlockPos> structure;

    private HashMap<Item, Integer> data = new HashMap<>();

    public TestControllerBlockTileEntity() {
        super(ModTileEntityTypes.TEST_CONTROLLER_BLOCK_TILE_ENTITY.get());
    }

    public void setStructure(Collection<BlockPos> structure)
    {
        this.structure = structure;
    }

    public Collection<BlockPos> getStructure()
    {
        if(structure == null)
            return new ArrayList<>(); // why are you calling me without having a structure?
        return structure;
    }

    public void addItem(Item item, int count)
    {
        // add or increment an entry in data
        if(data.containsKey(item))
            data.put(item, data.get(item) + count);
        else
            data.put(item, count);

        LOGGER.info("I received data. Item: " + item + ", count: " + count);
    }

    //DEBUG
    public void reportContents()
    {
        for(Item item : data.keySet())
        LOGGER.info("I contain Item: " + item + ", count: " + data.get(item));
    }

    @Override
    public void tick() {
//        if (world.isRemote())
//            LOGGER.info("I am a tile entity located at " + this.pos);

    }

    //DEBUG
    public void craft() {
        // if I have at least one iron ore and at least one example item, place an iron crystal block on one of the fluid (or NotFluid) neighbors of any nucleation block
        if(data.get(Items.IRON_ORE) != null && data.get(Items.IRON_ORE) > 0 && data.get(ModItems.EXAMPLE_ITEM) != null && data.get(ModItems.EXAMPLE_ITEM) > 0)
        {
            BlockPos crystallizingPos;

            for (BlockPos p : structure)
            {
                if (this.getWorld().getBlockState(p).getBlock().equals(ModBlocks.NUCLEATION_BLOCK.get()))
                {
                    // check for any eligible neighbors, which include water or NotFluid
                    Map<Direction, Block> neighbors = Util.getNeighbors(this.getWorld(), p);
                    for(Direction d : neighbors.keySet())
                    {
                        if(neighbors.get(d).equals(Blocks.WATER) || neighbors.get(d).equals(ModBlocks.NOT_FLUID.get()))
                        {
                            crystallizingPos = p.offset(d);

                            this.getWorld().setBlockState(crystallizingPos, ModBlocks.IRON_CRYSTAL_BLOCK.get().getDefaultState());
                            data.put(Items.IRON_ORE, data.get(Items.IRON_ORE) - 1);
                            data.put(ModItems.EXAMPLE_ITEM, data.get(ModItems.EXAMPLE_ITEM) - 1);
                            return;
                        }
                    }
                }
            }
            LOGGER.info("No valid crystallizing position was found.");
            return;
        }
        LOGGER.info("Insufficient crafting materials for recipe: IRON_CRYSTAL_BLOCK");

    }
}
