package crystallography.libs.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This interface is can be inherited by any block that is part of a multiblock structure for which you want to run
 * validation algorithms to ensure its appropriatness
 */
public interface IMultiBlockComponent {

    /**
     * isValid reports whether the block is valid or not.
     * @param world
     * @param pos position of the block we are validating
     * @param state the blockstate of the block we are validating.
     *
     */

    boolean isValid(World world, BlockPos pos, BlockState state);

}
