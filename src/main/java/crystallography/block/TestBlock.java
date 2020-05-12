package crystallography.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

/**
 * Used to make experimental changes to blocks. Mostly for figuring out how to do things.
 */
public class TestBlock extends Block {

    private static final Direction[] NEIGHBORS = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP};

    private static final Logger LOGGER = LogManager.getLogger();

    public TestBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if(!worldIn.isRemote) {
            LOGGER.info("Testing, one two three!");

            BlockPos.Mutable cursor = new BlockPos.Mutable();

            LOGGER.info("My neighbors are:");
            for(Direction direction : NEIGHBORS) {
                LOGGER.info(worldIn.getBlockState(cursor.setPos(pos).move(direction)).getBlock().getRegistryName() + " at coordinate" + cursor.toString() + ", direction " + direction);
            }
        }
        return ActionResultType.SUCCESS;
    }

    private Set<Block> getNeighborsHardCoded() {

        return null;
    }
}
