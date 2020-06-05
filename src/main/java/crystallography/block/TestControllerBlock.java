package crystallography.block;

import crystallography.libs.multiblock.ControllerBlock;
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
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.HashSet;
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
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if(!worldIn.isRemote) {
            Set<BlockPos> structure = new HashSet<>();
            BlockState newState;

            if(imValid(worldIn, pos, structure))
            {
                for(BlockPos component : structure)
                {
                    newState = worldIn.getBlockState(component).with(VALID, true);
                    worldIn.setBlockState(component, newState, 2); // Flag 2: send the change to clients
                }
            }
            else
            {
                for(BlockPos component : structure)
                {
                    newState = worldIn.getBlockState(component).with(VALID, false);
                    worldIn.setBlockState(component, newState, 2);
                }
            }
        }
        return ActionResultType.SUCCESS; // imValid can help determine what the return type should be, but I don't know how return types for this works right now
    }

//    @Override
//    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
//        // TODO structure needs to be stored so that I can access all the blocks in the multiblock even after I've been destroyed
//        // TileEntity?
//    }
//    @Override
//    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
//        worldIn.playEvent(player, 2001, pos, getStateId(state));
//    }

    @Override
    public boolean isValid(World worldIn, BlockPos pos, Set<BlockPos> structure)
    {
        CuboidCategory result = CuboidCategory.categorize(worldIn, pos);
        boolean isCuboid;
        if (result.equals(CuboidCategory.ILLEGAL)) {
            isCuboid = false;
        }
        else
        {
            isCuboid = true;
        }
        return isCuboid && super.isValid(worldIn, pos, structure);
    }
}
