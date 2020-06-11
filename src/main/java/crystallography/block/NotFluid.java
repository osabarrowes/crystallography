package crystallography.block;

import crystallography.libs.multiblock.MultiBlockComponent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Set;

/**
 * Placeholder block for the universal solvent until I can figure out how to use fluids correctly
 */
public class NotFluid extends MultiBlockComponent {
    public NotFluid(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValid(World worldIn, BlockPos pos, Set<BlockPos> structure) {
        return true;
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0;
    }



}
