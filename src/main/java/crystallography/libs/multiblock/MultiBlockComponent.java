package crystallography.libs.multiblock;

import crystallography.libs.Util;
import crystallography.libs.tileentity.MultiBlockComponentTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
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
    public abstract boolean isValid(World worldIn, BlockPos pos, Set<BlockPos> structure);

    /**
     * IProprety used to check validity.
     */
    public static final BooleanProperty VALID = BooleanProperty.create("valid"); // This has the same name as a property in TestBlock. Hopefully that doesn't cause problems

    // All IProperties used in a BlockState are added here.
    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(VALID);
    }

    // The default state of all IProperties is set here.
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(VALID, false);
    }

    /**
     * Returns true if this block's specific logic is satisfied && neighboring MultiBlockComponents are valid
     *
     *
     * @return
     */
    public boolean imValid(World worldIn, BlockPos centerPos, Set<BlockPos> structure) {
        // structure contains all the visited blocks. if all visited blocks are valid, then the structure is valid.
        // if the structure is invalid at any point during the algorithm, we should throw structure away
        structure.add(centerPos);

        if(isNeighborsValid(worldIn, centerPos, structure) == false) {
            return  false;
        }

        if (isValid(worldIn, centerPos, structure) == false) {
            return false;
        }

        return true;
    }

    /**
     * Returns true if all the neighbors report they are valid.
     * @param structure all the blocks which have been queried for this multiblock validation.
     * @param centerPos the block whose neighbors are being queried.
     * @return
     */
    public boolean isNeighborsValid (World worldIn, BlockPos centerPos, Set<BlockPos> structure) {
        //for each neighbor if neighbor is a multiblock component ask neighbor
        //if it is valid.
        Map<Direction, Block> neighbors = Util.getNeighbors( worldIn, centerPos);

        for(Direction direction : neighbors.keySet())
        {
            if(neighbors.get(direction) instanceof MultiBlockComponent)
            {
                // only visit neighbors that are not in structure. blocks in structure have already asked for validity
                if (structure.contains(centerPos.offset(direction))) {
                    continue;
                }

                MultiBlockComponent mbc = (MultiBlockComponent) (worldIn.getBlockState(centerPos.offset(direction)).getBlock());
                if (mbc.imValid(worldIn, centerPos.offset(direction), structure) == false) {
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
     * Returns the number of axes which contain at least one MultiBlockComponent.
     * This implementation uses a whitelist and a blacklist.
     * @param worldIn the world of the block which has neighbors
     * @param pos the position of the block which has neighbors
     */
    public static <T extends Block> int countRecognizedNeighborAxes(World worldIn, BlockPos pos, Collection<T> whitelist, Collection<T> blacklist) {
        int count = 0;
        Map<Direction, Block> neighbors = Util.getNeighbors(worldIn, pos);
        if (isOk(neighbors.get(Direction.EAST), whitelist, blacklist)|| isOk(neighbors.get(Direction.WEST), whitelist, blacklist)){
            count++;
        }
        if (isOk(neighbors.get(Direction.NORTH), whitelist, blacklist)|| isOk(neighbors.get(Direction.SOUTH), whitelist, blacklist)){
            count++;
        }
        if (isOk(neighbors.get(Direction.UP), whitelist, blacklist)|| isOk(neighbors.get(Direction.DOWN), whitelist, blacklist)){
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
     * Counts the number of neighbors which are MultiBlockComponents.
     * This implementation uses a whitelist and a blacklist.
     * @param worldIn the world of the block which has neighbors
     * @param pos the position of the block which has neighbors
     */
    public static <T extends Block> int countRecognizedNeighbors(World worldIn, BlockPos pos, Collection<T> whitelist, Collection<T> blacklist)
    {
        int count = 0;
        Map<Direction, Block> neighbors = Util.getNeighbors(worldIn, pos);
        for(Direction d : neighbors.keySet()) {
            if(isOk(neighbors.get(d), whitelist, blacklist))
                count++;
        }
        return count;
    }

    /**
     * Called when a neighbor of the block changes.
     * @param state my blockState
     * @param pos my position
     * @param blockIn the block that changed, before it changed
     * @param fromPos the position of the block that changed
     */
    // Overriding deprecated methods is bad practice, but this method is called by BlockState#neighborChanged, so...
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (state.get(VALID)) {
            TileEntity myTE = worldIn.getTileEntity(pos);
            if (myTE instanceof MultiBlockComponentTileEntity) {
                LOGGER.info("hello from neighborChanged~ my position is " + pos);
                LOGGER.info("beginning revalidation process...");
                // TileEntity t = worldIn.getTileEntity(pos);
                ((MultiBlockComponentTileEntity) myTE).invalidateController();
            }
        }

        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }

    /**
     * A MultiBlockComponent can be one of these categories in a cuboid structure.
     */
    public enum CuboidCategory {
        CORNER, LIP, EDGE, FACE, ILLEGAL;

        /**
         * Returns the cuboid category of the specified block. Used in ensuring a block is part of a cuboid multiblock structure.
         * By default, any MultiBlockComponent is considered a neighbor.
         * @param worldIn the world of the block
         * @param pos the position of the block
         * @return the CuboidCategory of the block.
         */
        public static CuboidCategory categorize(World worldIn, BlockPos pos)
        {
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
            if(countRecognizedNeighbors(worldIn, pos, whitelist, blacklist) == 3) {

                if (countRecognizedNeighborAxes(worldIn, pos, whitelist, blacklist) == 3) {
                    return CuboidCategory.CORNER;
                }
                else if(countRecognizedNeighborAxes(worldIn, pos, whitelist, blacklist) == 2){
                    if(isOk(Util.getNeighbors(worldIn, pos).get(Direction.UP), whitelist, blacklist) || !isOk(Util.getNeighbors(worldIn, pos).get(Direction.DOWN),whitelist ,blacklist))
                    {
                        return CuboidCategory.ILLEGAL;
                    }
                    return CuboidCategory.LIP;
                }
                else {
                    return CuboidCategory.ILLEGAL;
                }

            }
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

        /**
         * Returns false is the cuboid category is ILLEGAL. Convenience method.
         * @param worldIn the world of the block
         * @param pos the position of the block
         */
        public static boolean isCuboid(World worldIn, BlockPos pos) {
            CuboidCategory result = CuboidCategory.categorize(worldIn, pos);
            if (result.equals(CuboidCategory.ILLEGAL)) {
                return false;
            }
            else
            {
                return true;
            }

        }



    }
    // Helper method for logic of the whitelist and blacklist. The whitelist trumps the blacklist.
    protected static <T extends Block> boolean isOk(Block b, Collection<T> whitelist, Collection<T> blacklist)
    {
        if(whitelist.contains(b))
            return true;
        if(blacklist.contains(b))
            return false;
        if(b instanceof MultiBlockComponent)
            return true;
        return false;
    }

}
