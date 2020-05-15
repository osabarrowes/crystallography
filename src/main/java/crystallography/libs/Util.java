package crystallography.libs;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

import static net.minecraft.block.Blocks.*;

public class Util {

    /**
     * Helper array specifying directions from minecraft's Direction class. This way, we don't have to use pos.add(1, 0, 0), pos.add(0, 1, 0), etc.
     * @deprecated Use {@link Direction#values()} instead
     */
    @Deprecated
    // CLEANME remove
    public static final Direction[] NEIGHBORS = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP};

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
     * Convenience overload for {@link #countRecognizedNeighborAxes(World, BlockPos, Collection, Collection)}.
     * Whitelist defaults to only Blocks.AIR. Blacklist defaults to empty.
     */
    public static <T extends Block> int countRecognizedNeighborAxes(World worldIn, BlockPos pos)
    {
        Collection<Block> whitelist = new HashSet<>(), blacklist = new HashSet<>();
        whitelist.add(Blocks.AIR);
        return countRecognizedNeighborAxes(worldIn, pos, whitelist, blacklist);
    }

    /**
     * For a given block, returns the number of axes which contain at least one recognized neighbor.
     * @param worldIn the world of the block
     * @param pos the position of the block
     */
    public static <T extends Block> int countRecognizedNeighborAxes(World worldIn, BlockPos pos, Collection<T> whitelist, Collection<T> blacklist) {

        // It would also be nice if you told me which axes contained no recognized neighbors
        int count = 0;
        Map<Direction, Block> neighbors = Util.getNeighbors(worldIn, pos);
        if (!whitelist.contains(neighbors.get(Direction.EAST)) || !whitelist.contains(neighbors.get(Direction.WEST))){
            count++;
        }
        if (!whitelist.contains(neighbors.get(Direction.NORTH)) || !whitelist.contains(neighbors.get(Direction.SOUTH))){
            count++;
        }
        if (!whitelist.contains(neighbors.get(Direction.UP)) || !whitelist.contains(neighbors.get(Direction.DOWN))){
            count++;
        }
        return count;
    }

    /**
     * Convenience overload for {@link #countRecognizedNeighbors(World, BlockPos, Collection, Collection)}.
     * Whitelist defaults to only Blocks.AIR. Blacklist defaults to empty.
     */
    public static <T extends Block> int countRecognizedNeighbors(World worldIn, BlockPos pos)
    {
        Collection<Block> whitelist = new HashSet<>(), blacklist = new HashSet<>();
        whitelist.add(Blocks.AIR);
        return countRecognizedNeighbors(worldIn, pos, whitelist, blacklist);
    }
    /**
     * Counts the number of neighbors which are recognized according to the whitelist and blacklist.
     * @param worldIn the world of the block
     * @param pos the position of the block
     */
    public static <T extends Block> int countRecognizedNeighbors(World worldIn, BlockPos pos, Collection<T> whitelist, @Nullable Collection<T> blacklist)
    {
        int count = 0;
        Map<Direction, Block> neighbors = Util.getNeighbors(worldIn, pos);
        for(Direction d : neighbors.keySet()) {
            if(!whitelist.contains(neighbors.get(d)))
                count++;
        }
        return count;
    }

    /**
     * A block can be one of these categories in a cuboid structure.
     */
    public enum CuboidCategory {
        CORNER, LIP, EDGE, FACE, ILLEGAL;

        /**
         * Convenience overload for {@link #categorize(World, BlockPos, Collection, Collection)}.
         * Whitelist defaults to only Blocks.AIR. Blacklist defaults to empty.
         */
        public static CuboidCategory categorize(World worldIn, BlockPos pos)
        {
            Collection<Block> whitelist = new HashSet<>(), blacklist = new HashSet<>();
            whitelist.add(Blocks.AIR);
            return categorize(worldIn, pos, whitelist, blacklist);
        }
        /**
         * Returns the cuboid category of the specified block. Used in ensuring a block is part of a cuboid multiblock structure.
         *
         * @param worldIn the world of the block
         * @param pos the position of the block
         * @param whitelist blocks which are recognized as neighbors
         * @param blacklist blocks which are not recognized as neighbors
         * @return the CuboidCategory of the block.
         */
        public static <T extends Block> CuboidCategory categorize(World worldIn, BlockPos pos, Collection<T> whitelist, Collection<T> blacklist)
        {
            // FIXME behavior is undefined when whitelist and blacklist overlap
            // TODO implement use of whitelist and blacklist

            // Idea: make a helper class which contains a boolean and a CuboidCategory to package the return of this method.
            // That way, you get a boolean for whether or not it's legal rather than having to check explicitly if the
            // cuboid category is illegal.

            // FIXME for now we'll say any block on the whitelist is a neighbor.

            // 3 neighbors means (6 - 3) face sharing air blocks
            if(countRecognizedNeighbors(worldIn, pos, whitelist, blacklist) == 3) {
                // TODO check the lip-corner clause

                if (countRecognizedNeighborAxes(worldIn, pos, whitelist, blacklist) == 3) {
                    return CuboidCategory.CORNER;
                }
                else if(countRecognizedNeighborAxes(worldIn, pos, whitelist, blacklist) == 2){
                    return CuboidCategory.LIP;
                }
                else {
                    return CuboidCategory.ILLEGAL;
                }

            }
            // 4 neighbors means (6 - 4) face sharing air blocks
            else if(countRecognizedNeighbors(worldIn, pos, whitelist, blacklist) == 4)
            {
                if (countRecognizedNeighborAxes(worldIn, pos, whitelist, blacklist) == 3) {
                    return CuboidCategory.EDGE;
                }
                else if(countRecognizedNeighborAxes(worldIn, pos, whitelist, blacklist) == 2) {
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
    }
}
