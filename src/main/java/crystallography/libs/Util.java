package crystallography.libs;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class Util {

    /**
     * Helper array specifying directions from minecraft's Direction class. This way, we don't have to use pos.add(1, 0, 0), pos.add(0, 1, 0), etc.
     */
    public static final Direction[] NEIGHBORS = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP};

    /**
     * A block can be one of these categories in a cuboid structure.
     */
    public enum CuboidCategory {
        CORNER, LIP, EDGE, FACE, ILLEGAL
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private Util(){}


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

    /**
     * Returns the cuboid category of the specified block. Used in ensuring a block is part of a cuboid multiblock structure.
     * @param worldIn the world of the block
     * @param pos the position of the block
     * @return the cuboid category of the block.
     */
    public static CuboidCategory cuboidCategorize(World worldIn, BlockPos pos)
    {
        // TODO implement
        throw new NotImplementedException();
//
//        // Idea: make a helper class which contains a boolean and a CuboidCategory to package the return of this method.
//        // That way, you get a boolean for whether or not it's legal rather than having to check explicitly if the
//        // cuboid category is illegal.
//
//        // FIXME for now we'll just assume that any non-air block is a neighbor.
//
//        // 3 neighbors means (6 - 3) air blocks
//        if(countAirNeighbors(worldIn, pos) == 3) {
//            // TODO check the lip-corner clause
//            if (/*3 axes*/) {
//                return CuboidCategory.CORNER;
//            }
//            else if(/* 2 axes*/){
//                return CuboidCategory.LIP;
//            }
//            else {
//                return CuboidCategory.ILLEGAL;
//            }
//
//        }
//        else if(/*4 neighbors*/)
//        {
//            if (/*3 axes*/) {
//                return CuboidCategory.EDGE;
//            }
//            else if(/*2 axes*/) {
//                return CuboidCategory.FACE;
//            }
//            else {
//                return CuboidCategory.ILLEGAL;
//            }
//        }
//        else{
//            return CuboidCategory.ILLEGAL;
//        }
    }

    /**
     * Counts the number of neighbors which are minecraft:air
     */
    public static int countAirNeighbors(World worldIn, BlockPos pos)
    {
        int count = 0;
        Map<Direction, Block> neighbors = Util.getNeighbors(worldIn, pos);
        for(Direction d : neighbors.keySet()) {
            if(neighbors.get(d) == Blocks.AIR)
                count++;
        }
        return count;
    }

}
