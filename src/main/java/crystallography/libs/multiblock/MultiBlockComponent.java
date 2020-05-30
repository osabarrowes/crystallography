package crystallography.libs.multiblock;

import crystallography.block.TestBlock;
import crystallography.libs.Util;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This interface is can be inherited by any block that is part of a multiblock structure for which you want to run
 * validation algorithms to ensure its appropriatness
 */
public abstract class MultiBlockComponent extends Block{

    public MultiBlockComponent(Properties properties) {
        super(properties);
    }

    /**
     * Contains specific logic for this block's validity. Varies based on implementation.
     * @return
     */
    public abstract boolean isValid(World worldIn, BlockPos pos);

    /**
     * Returns true if this block's specific logic is satisfied && neighboring MultiBlockComponents are valid
     *
     * The first block that starts the algorithm passes 'this' as the parameter.
     * @return
     */
    public boolean imValid(World worldIn, BlockPos pos,  Set<MultiBlockComponent> structure) {

        if(isNeighborsValid(worldIn, pos, structure) == false) {
            return  false;
        }

        if (isValid(worldIn, pos) == false) {
            return false;
        }

        structure.add(this);
        return true;
    }

    /**
     * Returns true if all the neighbors report they are valid.
     * @param structure
     * @return
     */
    public boolean isNeighborsValid (World worldIn, BlockPos pos,  Set<BlockPos> structure) {
        //for each neighbor if neighbor is a multiblock component ask neighbor
        //if it is valid.
        //
        Collection<Block> neighborhood = new HashSet<>(); // TODO get all blocks which share a face with this block, put them in this set
        //FIXME getNeighbors provides what we want here, but requires a world and a blockpos. See issue https://github.com/xenonni/crystallography/issues/4#issue-619867435

        Map<Direction, Block> neighbors = Util.getNeighbors( worldIn, pos);
        neighborhood = neighbors.values();

        for(Block neighbor : neighborhood)
        {
            if(neighbor instanceof MultiBlockComponent)
            {

                // only visit neighbors that are not in structure
                if (structure.contains(neighbor)) {
                    continue;
                }

                // don't visit the block who asked
                if (neighbor instanceof TestBlock) { // DEBUG
//                    if ( ((TestBlock) neighbor).getWorldIn().getBlockState( ((TestBlock) neighbor).getPos() ).get(TestBlock.VALID) ) // don't visit the blocks which are valid
//                    {
//                        LOGGER.info("Visited block " + neighbor + " , state was " + ((TestBlock) neighbor).getWorldIn().getBlockState( ((TestBlock) neighbor).getPos() ));
//                        continue;
//                    }

                }

                if (((MultiBlockComponent) neighbor).imValid(worldIn, pos, structure) == false) { // check the parameters here are being recursive correctly
                    return false;
                }

            }
        }
        return true;
    }

    /**
     * Returns the number of axes which contain at least one MultiBlockComponent.
     * @param worldIn the world of the block which has neighbors
     * @param pos the position of the block which has neighbors
     */
    public static <T extends Block> int countRecognizedNeighborAxes(World worldIn, BlockPos pos) {
        // It would also be nice if you told me which axes contained no MultiBlockComponents
        int count = 0;
        Map<Direction, Block> neighbors = Util.getNeighbors(worldIn, pos);
        if (neighbors.get(Direction.EAST) instanceof MultiBlockComponent || neighbors.get(Direction.WEST) instanceof MultiBlockComponent){
            count++;
        }
        if (neighbors.get(Direction.NORTH) instanceof MultiBlockComponent || neighbors.get(Direction.SOUTH) instanceof MultiBlockComponent){
            count++;
        }
        if (neighbors.get(Direction.UP) instanceof MultiBlockComponent || neighbors.get(Direction.DOWN) instanceof MultiBlockComponent){
            count++;
        }
        return count;
    }

    /**
     * Counts the number of neighbors which are MultiBlockComponents.
     * @param worldIn the world of the block which has neighbors
     * @param pos the position of the block which has neighbors
     */
    public static <T extends Block> int countRecognizedNeighbors(World worldIn, BlockPos pos)
    {
        int count = 0;
        Map<Direction, Block> neighbors = Util.getNeighbors(worldIn, pos);
        for(Direction d : neighbors.keySet()) {
            if(neighbors.get(d) instanceof MultiBlockComponent)
                count++;
        }
        return count;
    }

    /**
     * A MultiBlockComponent can be one of these categories in a cuboid structure.
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
            // Idea: make a helper class which contains a boolean and a CuboidCategory to package the return of this method.
            // That way, you get a boolean for whether or not it's legal rather than having to check explicitly if the
            // cuboid category is illegal.
            // I don't know if this is a good idea but it's an idea

            if(countRecognizedNeighbors(worldIn, pos) == 3) {

                if (countRecognizedNeighborAxes(worldIn, pos) == 3) {
                    return CuboidCategory.CORNER;
                }
                else if(countRecognizedNeighborAxes(worldIn, pos) == 2){
                    // maybe a lip. Let's see.
                    if(Util.getNeighbors(worldIn, pos).get(Direction.UP) instanceof MultiBlockComponent || !(Util.getNeighbors(worldIn, pos).get(Direction.DOWN) instanceof MultiBlockComponent))
                    {
                        return CuboidCategory.ILLEGAL; // Lips cannot have MultiBlockComponents above them, and they must have one beneath them.
                    }
                    return CuboidCategory.LIP;
                }
                else {
                    return CuboidCategory.ILLEGAL;
                }

            }
            // 4 neighbors means (6 - 4) face sharing air blocks
            else if(countRecognizedNeighbors(worldIn, pos) == 4)
            {
                if (countRecognizedNeighborAxes(worldIn, pos) == 3) {
                    return CuboidCategory.EDGE;
                }
                else if(countRecognizedNeighborAxes(worldIn, pos) == 2) {
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
