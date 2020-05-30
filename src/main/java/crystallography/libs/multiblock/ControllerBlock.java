package crystallography.libs.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

/**
 * Controller blocks exist as components of a multiblock and serve as the overseer or master. Logic which applies to the
 * entire multiblock structure should be handled by the ControllerBlock.
 *
 * Exactly one controllerBlock is allowed per multiblock structure.
 */
public abstract class ControllerBlock extends MultiBlockComponent {

    // A list containing all the multiblocks in this structure.
    // CLEANME We do this because recursion. If a block is in the set already, then it has already been visited. No additional visitation is required.
    // TODO ensure only valid IMultiBlockComponents are contained in this list.
    private Set<BlockPos> structure;

    public ControllerBlock(Properties properties) {
        super(properties);
    }

    /*
     * Exactly one controllerBlock is allowed per multiblock structure.
     *
      */
    @Override
    public boolean isValid(World worldIn, BlockPos pos)
    {
        for(BlockPos comp : structure)
        {
            if(worldIn.getBlockState(comp).getBlock() instanceof ControllerBlock)
                return false;
        }
        structure.add(pos);
        // return imValid(worldIn, pos, structure); // FIXME
        return true;
    }
    // ControllerBlock will start the algorithm initially, but any MultiBlockComponent should be able to do so.

}
