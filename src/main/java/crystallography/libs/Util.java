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
     * Use {@link Direction.values()} instead
     */
    @Deprecated
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

        for(Direction direction : Direction.values()) {
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
       // Idea: make a helper class which contains a boolean and a CuboidCategory to package the return of this method.
       // That way, you get a boolean for whether or not it's legal rather than having to check explicitly if the
       // cuboid category is illegal.

       // FIXME for now we'll just assume that any non-air block is a neighbor.

       // 3 neighbors means (6 - 3) face sharing air blocks
       if(countAirNeighbors(worldIn, pos) == 3) {
           // TODO check the lip-corner clause

           if (countAirAxes(worldIn, pos) == 0) {
               return CuboidCategory.CORNER;
           }
           else if(countAirAxes(worldIn, pos) == 1){
               return CuboidCategory.LIP;
           }
           else {
               return CuboidCategory.ILLEGAL;
           }

       }
       // 4 neighbors means (6 - 4) face sharing air blocks
       else if(countAirNeighbors(worldIn, pos) == 2)
       {
           if (countAirAxes(worldIn, pos) == 0) {
               return CuboidCategory.EDGE;
           }
           else if(countAirAxes(worldIn, pos) == 1) {
               return CuboidCategory.FACE;
           }
           else {
               return CuboidCategory.ILLEGAL;
           }
       }
       else{
           return CuboidCategory.ILLEGAL;
       }
    }

    /**
     * For a given block, returns the number of axes for which the neighbors consist only of air.
     * @param worldIn the world of the block
     * @param pos the position of the block
     */
    public static int countAirAxes(World worldIn, BlockPos pos) {

        // It would also be nice if you told me which axes were air
        int count = 0;
        Map<Direction, Block> neighbors = Util.getNeighbors(worldIn, pos);
        if (neighbors.get(Direction.EAST) == Blocks.AIR && neighbors.get(Direction.WEST) == Blocks.AIR){
            count++;
        }
        if (neighbors.get(Direction.NORTH) == Blocks.AIR && neighbors.get(Direction.SOUTH) == Blocks.AIR){
            count++;
        }
        if (neighbors.get(Direction.UP) == Blocks.AIR && neighbors.get(Direction.DOWN) == Blocks.AIR){
            count++;
        }
        return count;
    }

    /**
     * Counts the number of neighbors which are minecraft:air
     * @param worldIn the world of the block
     * @param pos the position of the block
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
