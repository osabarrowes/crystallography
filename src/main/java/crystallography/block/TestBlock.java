package crystallography.block;

import crystallography.init.ModBlocks;
import crystallography.libs.multiblock.ControllerBlock;
import crystallography.libs.multiblock.MultiBlockComponent;
import crystallography.libs.tileentity.MultiBlockComponentTileEntity;
import crystallography.tileentity.NucleationBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Used to make experimental changes to blocks. Mostly for figuring out how to do things.
 *
 * @author xenonni
 */
public class TestBlock extends MultiBlockComponent {

    private static final Logger LOGGER = LogManager.getLogger();

    public TestBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MultiBlockComponentTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        // DEBUG
        if (worldIn.getBlockState(pos).get(VALID))
        {
            onBlockHarvested(worldIn, pos, state, player, true);
        }
        else
        {
            // save your data

            ((MultiBlockComponentTileEntity)worldIn.getTileEntity(pos)).setControllerPos(pos); // this is a lie for debugging purposes

            worldIn.setBlockState(pos, worldIn.getBlockState(pos).with(VALID, !worldIn.getBlockState(pos).get(VALID)), 2); // toggle the blockstate
        }


        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean isValid(World worldIn, BlockPos pos, Set<BlockPos> structure) {

        Collection<Block> blacklist = new ArrayList<>(), whitelist = new ArrayList<>();
        blacklist.add(ModBlocks.NOT_FLUID.get());
        CuboidCategory c = CuboidCategory.categorize(worldIn, pos, whitelist, blacklist);
        boolean returnValue;
        if (c.equals(CuboidCategory.ILLEGAL))
            returnValue = false;
        else
            returnValue = true;

        // find your controller
        for (BlockPos p : structure)
        {
            // correct structures only have one controller. if the structure fails validtion, I'll potentially remember the wrong controller
            // but it doesn't matter as long as we do validity checks whenever asking for the controller location.
            if (worldIn.getBlockState(p).getBlock()instanceof ControllerBlock) {
                TileEntity myTE = worldIn.getTileEntity(pos);
                if(myTE instanceof MultiBlockComponentTileEntity)
                {
                    ((MultiBlockComponentTileEntity) myTE).setControllerPos(p);
                    break; // preemptive optimisation is the root of all evil
                }
            }

        }

        return returnValue;

    }

    // @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player, boolean flag) {
        if (state.get(VALID)) {
            TileEntity myTE = worldIn.getTileEntity(pos);
            if (myTE instanceof MultiBlockComponentTileEntity) {
                ((MultiBlockComponentTileEntity) myTE).invalidateController();
            }
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }
}
