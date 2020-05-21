package crystallography.block;

import crystallography.init.ModBlocks;
import crystallography.libs.Util;
import crystallography.libs.multiblock.MultiBlockComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;

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

    public static final BooleanProperty VALID = BooleanProperty.create("valid");


    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        this.worldIn = worldIn;
        thisPos = pos;
        if(!worldIn.isRemote) {
            LOGGER.info("I have " + countRecognizedNeighbors(worldIn, pos) + " recognized neighbors");
            LOGGER.info("I have " + countRecognizedNeighborAxes(worldIn, pos) + " axes containing at least one recognized neighbors");
            LOGGER.info("Cuboid category: " + CuboidCategory.categorize(worldIn, pos));
            LOGGER.info("Valid: " + isValid());
            final BlockState newState;
            if(isValid())
            {
                newState = worldIn.getBlockState(pos).with(VALID, true);
            }
            else
            {
                newState = worldIn.getBlockState(pos).with(VALID, false);
            }
            // Flag 2: send the change to clients
            worldIn.setBlockState(pos, newState, 2);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(VALID);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(VALID, false);
    }

    @Override
    public boolean isValid() {
        // return true if this block is a valid cuboid category
        CuboidCategory result = CuboidCategory.categorize(worldIn, thisPos);
        if (result.equals(CuboidCategory.ILLEGAL)) {
            return false;
        }
        return true;

    }
}
