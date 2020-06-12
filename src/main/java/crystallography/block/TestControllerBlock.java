package crystallography.block;

import crystallography.init.ModBlocks;
import crystallography.libs.multiblock.ControllerBlock;
import crystallography.tileentity.TestControllerBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Used to make experimental changes to blocks. Mostly for figuring out how to do things.
 *
 * @author xenonni
 */
public class TestControllerBlock extends ControllerBlock {

    public TestControllerBlock(Properties properties) { super(properties); }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TestControllerBlockTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if(!worldIn.isRemote) {

            //debug
            if ((player.isCrouching()) && worldIn.getBlockState(pos).get(VALID))
            {
                TileEntity myTE = worldIn.getTileEntity(pos);
                if(myTE instanceof TestControllerBlockTileEntity) // SAFETY
                {
                    for (BlockPos component : ((TestControllerBlockTileEntity) myTE).getStructure())
                        LOGGER.info("Component " + worldIn.getBlockState(component).getBlock() + " found at " + component);
                }
                LOGGER.info("Found a total of " + ((TestControllerBlockTileEntity) myTE).getStructure().size() + " components");
                return ActionResultType.SUCCESS;
            }
            //end debug

            Set<BlockPos> structure = new HashSet<>();
            BlockState newState;

            if(imValid(worldIn, pos, structure))
            {
                for(BlockPos component : structure)
                {
                    newState = worldIn.getBlockState(component).with(VALID, true);
                    worldIn.setBlockState(component, newState, 2); // Flag 2: send the change to clients
                }
            }
            else
            {
                for(BlockPos component : structure)
                {
                    newState = worldIn.getBlockState(component).with(VALID, false);
                    worldIn.setBlockState(component, newState, 2);
                }
            }

            TileEntity myTE = worldIn.getTileEntity(pos);
            if(myTE instanceof TestControllerBlockTileEntity) // SAFETY
            {
                ((TestControllerBlockTileEntity) myTE).setStructure(structure);
            }
        }
        return ActionResultType.SUCCESS; // imValid can help determine what the return type should be, but I don't know how return types for this works right now
    }

    //TODO structure invalidation needs to occur whenever a block is broken
    // design question: should breaking a block cause the entire structure to invalidate, regardless of whether or not the resulting structure is valid?
//    @Override
//    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
//    }
//    @Override
//    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
//        worldIn.playEvent(player, 2001, pos, getStateId(state));
//    }
    // neighborChanged
    // updateState
    // onReplaced? what does this method do

    @Override
    public boolean isValid(World worldIn, BlockPos pos, Set<BlockPos> structure)
    {
        Collection<Block> blacklist = new ArrayList<>(), whitelist = new ArrayList<>();
        blacklist.add(ModBlocks.NOT_FLUID.get());
        CuboidCategory c = CuboidCategory.categorize(worldIn, pos, whitelist, blacklist);
        boolean value;
        if (c.equals(CuboidCategory.ILLEGAL))
            value = false;
        else
            value = true;

        return value && super.isValid(worldIn, pos, structure);
    }
}
