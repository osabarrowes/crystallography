package crystallography.block;

import crystallography.init.ModBlocks;
import crystallography.libs.Util;
import crystallography.libs.multiblock.ControllerBlock;
import crystallography.libs.multiblock.MultiBlockComponent;
import crystallography.tileentity.NucleationBlockTileEntity;
import crystallography.tileentity.TestControllerBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

/**
 * Picks up nearby valid items in the world and then places crystals. Only does this when valid.
 */
public class NucleationBlock extends MultiBlockComponent{

    public NucleationBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new NucleationBlockTileEntity();
    }

    @Override
    public boolean isValid(World worldIn, BlockPos pos, Set<BlockPos> structure) {
        Map<Direction, Block> neighbors = Util.getNeighbors(worldIn, pos);
        int fluidCount = 0;
        for (Direction d : neighbors.keySet())
        {
            // FIXME for now, we'll only require that the nucleation be touching at least one block of fluid. This does not guarantee the fluid is actually within the vat, however.
            if(neighbors.get(d).equals(Blocks.WATER) || neighbors.get(d).equals(ModBlocks.NOT_FLUID.get()))
                fluidCount++;
        }

        // find your controller
        for (BlockPos p : structure)
        {
            // correct structures only have one controller. if the structure fails validtion, I'll potentially remember the wrong controller
            // but it doesn't matter as long as we do validity checks whenever asking for the controller location.
            if (worldIn.getBlockState(p).getBlock()instanceof ControllerBlock) {
                TileEntity myTE = worldIn.getTileEntity(pos);
                if(myTE instanceof NucleationBlockTileEntity)
                {
                    ((NucleationBlockTileEntity) myTE).setControllerPos(p);
                    break; // preemptive optimisation is the root of all evil
                }
            }

        }

        return fluidCount > 0;

    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.getBlockState(pos).get(VALID) && !worldIn.isRemote())
        {
            TileEntity myTE = worldIn.getTileEntity(pos);
            if(myTE instanceof NucleationBlockTileEntity)
            {
                LOGGER.info("My controller is at " + ((NucleationBlockTileEntity) myTE).getControllerPos());
            }

        }
        return ActionResultType.SUCCESS;
    }


}
