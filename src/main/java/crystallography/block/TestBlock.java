package crystallography.block;

import crystallography.libs.Util;
import crystallography.libs.multiblock.MultiBlockComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Used to make experimental changes to blocks. Mostly for figuring out how to do things.
 */
public class TestBlock extends MultiBlockComponent {

    private static final Logger LOGGER = LogManager.getLogger();

    public TestBlock(Properties properties) {
        super(properties);
    }

    // The world this block exists in. This is here because I want to check neighbors for isValid without changing it's method signature.
    private World worldIn;
    // The position of this block. this is also here for isValid
    private BlockPos thisPos;

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        this.worldIn = worldIn;
        thisPos = pos;
        if(!worldIn.isRemote) {

            LOGGER.info("I have " + Util.countAirNeighbors(worldIn, pos) + " air neighbors.");
            LOGGER.info("I have " + Util.countAirAxes(worldIn, pos) + " air axes.");
            //LOGGER.info("Validity check: " + isValid());
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean isValid() {
        // return true if this block is a valid cuboid category
        Util.CuboidCategory result = Util.cuboidCategorize(worldIn, thisPos);
        if (result.equals(Util.CuboidCategory.ILLEGAL)) {
            return false;
        }
        return true;

    }
}
