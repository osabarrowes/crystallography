package crystallography.block;

import crystallography.libs.Util;
import crystallography.libs.multiblock.MultiBlockComponent;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    public boolean isValid(World worldIn, BlockPos pos, Set<BlockPos> structure) {
        Map<Direction, Block> neighbors = Util.getNeighbors(worldIn, pos);
        int fluidCount = 0;
        for (Direction d : neighbors.keySet())
        {
            // FIXME for now, we'll only require that the nucleation be touching at least one block of fluid. This does not guarantee the fluid is actually within the vat, however.
            if(neighbors.get(d).equals(Blocks.WATER))
                fluidCount++;
        }
        return fluidCount > 0;

    }
}
