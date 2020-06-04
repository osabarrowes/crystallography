package crystallography.libs.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

/**
 * Controller blocks exist as components of a multiblock and serve as the overseer or master. Logic which applies to the
 * entire multiblock structure should be handled by the ControllerBlock.
 *
 * Exactly one controllerBlock is required per multiblock structure.
 */
public abstract class ControllerBlock extends MultiBlockComponent {

    public ControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValid(World worldIn, BlockPos pos, Set<BlockPos> structure)
    {
        int controllerCount = 0;
        int cornerCount = 0;
        for(BlockPos comp : structure)
        {
            if(worldIn.getBlockState(comp).getBlock() instanceof ControllerBlock)
            {
                controllerCount++;
            }

            if(CuboidCategory.categorize(worldIn, comp).equals(CuboidCategory.CORNER))
            {
                cornerCount++;
            }
        }

        if(controllerCount != 1 || cornerCount != 8) {
            return false;
        }

        return true;
    }
}
