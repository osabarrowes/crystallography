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
