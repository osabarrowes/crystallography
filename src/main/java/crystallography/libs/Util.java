package crystallography.libs;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class Util {

    /**
     * Don't let anyone instantiate this class.
     */
    private Util(){}

    /**
     * Helper array specifying directions from minecraft's Direction class. This way, we don't have to use pos.add(1, 0, 0), pos.add(0, 1, 0), etc.
     */
    public static final Direction[] NEIGHBORS = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP};

    /**
     * Helper method for getting the neighbors of a Block.
     * @param worldIn the world of the block
     * @param centerBlockPos the position of the center block
     * @return Dictionary mapping each of the 6 directions to the block in that direction.
     */
    public static Map<Direction, Block> getNeighbors(World worldIn, BlockPos centerBlockPos)
    {
        Map<Direction, Block> neighborMap = new Hashtable<>();
        BlockPos.Mutable cursor = new BlockPos.Mutable();

        for(Direction direction : NEIGHBORS) {
             neighborMap.put(direction, worldIn.getBlockState(cursor.setPos(centerBlockPos).move(direction)).getBlock());
        }
        return neighborMap;
    }


}
