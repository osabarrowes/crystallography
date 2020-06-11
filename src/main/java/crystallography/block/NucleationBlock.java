package crystallography.block;

import crystallography.init.ModBlocks;
import crystallography.libs.Util;
import crystallography.libs.multiblock.ControllerBlock;
import crystallography.libs.multiblock.MultiBlockComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPosWrapper;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Picks up nearby valid items in the world and then places crystals. Only does this when valid.
 */
public class NucleationBlock extends MultiBlockComponent{

    public NucleationBlock(Properties properties) {
        super(properties);
    }

    private BlockPos masterLocation;

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
            // but it doesn't matter as long as we do validity checks whenever using masterLocation.
            if (worldIn.getBlockState(p).getBlock()instanceof ControllerBlock) {
                masterLocation = p;
                break; // preemptive optimisation is the root of all evil
            }

        }

        return fluidCount > 0;

    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.getBlockState(pos).get(VALID) && !worldIn.isRemote())
            LOGGER.info("My controller is at " + masterLocation);
        return ActionResultType.SUCCESS;
    }


}
