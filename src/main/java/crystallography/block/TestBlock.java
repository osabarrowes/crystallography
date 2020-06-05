package crystallography.block;

import crystallography.libs.multiblock.MultiBlockComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashSet;
import java.util.Set;

/**
 * Used to make experimental changes to blocks. Mostly for figuring out how to do things.
 *
 * @author xenonni
 */
public class TestBlock extends MultiBlockComponent {

    private static final Logger LOGGER = LogManager.getLogger();

    public TestBlock(Properties properties) {
        super(properties);
    }

    // IProperties are used in the construction of BlockStates (not to be confused with .json files)
    public static final BooleanProperty VALID = BooleanProperty.create("valid");

    // Called when the block is right clicked.
//    @Override
//    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
//    {
//        if(!worldIn.isRemote) {
//            LOGGER.info("Cuboid category: " + CuboidCategory.categorize(worldIn, pos));
//            Set<BlockPos> structure = new HashSet<>();
//            imValid(worldIn, pos, structure);
//        }
//        return ActionResultType.SUCCESS; // imValid can help determine what the return type should be, but I don't know how return types for this works right now
//    }

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

    @Override
    public boolean isValid(World worldIn, BlockPos pos, Set<BlockPos> structure) {

        CuboidCategory result = CuboidCategory.categorize(worldIn, pos);
        boolean isCuboid;
        if (result.equals(CuboidCategory.ILLEGAL)) {
            isCuboid = false;
        }
        else
        {
            isCuboid = true;
        }
        // Flag 2: send the change to clients
        return isCuboid;

    }
}
