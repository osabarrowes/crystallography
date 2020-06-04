package crystallography.block;

import crystallography.libs.multiblock.ControllerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

/**
 * Used to make experimental changes to blocks. Mostly for figuring out how to do things.
 *
 * @author xenonni
 */
public class TestControllerBlock extends ControllerBlock {

    public TestControllerBlock(Properties properties) { super(properties); }

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

    @Override
    public boolean isValid(World worldIn, BlockPos pos, Set<BlockPos> structure)
    {
        CuboidCategory result = CuboidCategory.categorize(worldIn, pos);
        final BlockState newState;
        if (result.equals(CuboidCategory.ILLEGAL)) {
            newState = worldIn.getBlockState(pos).with(VALID, false);
        }
        else
        {
            newState = worldIn.getBlockState(pos).with(VALID, true);
        }
        // Flag 2: send the change to clients
        worldIn.setBlockState(pos, newState, 2);

        return newState.get(VALID) && super.isValid(worldIn, pos, structure);
    }
}
