package crystallography.libs;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

import static net.minecraft.block.Blocks.*;

public class Util {

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

    public static boolean validVatIngredient(Entity entityIn) {
        return true;
    }
}
