package crystallography.libs.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
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
    public abstract boolean isValid();

    /**
     * Returns true if this block's specific logic is satisfied && neighboring MultiBlockComponents are valid
     *
     * The first block that starts the algorithm passes this as the parameter
     * @return
     */
    public boolean imValid(MultiBlockComponent blockThatAsked) {

        if(isNeighborsValid() == false) {
            return  false;
        }

        if (isValid() == false) {
            return false;
        }

        return true;
    }

    public boolean isNeighborsValid () {
        //for each neighbor if neighbor is a multiblock component ask neighbor
        //if it is valid.
        //
        Set<MultiBlockComponent> neighborhood = new HashSet<>(); // TODO get all blocks which share a face with this block, put them in this set

        // FIXME Don't call imValid on the block who asked if you're valid

        for(MultiBlockComponent neighbor : neighborhood)
        {
            if (neighbor.imValid(this) == false)
            {
                return false;
            }
        }
        // foreach neighbor
        //  call neighbors imvalid.
        //  get block position of neighbor
        //  block position to block
        //  block.imValid();
        return true;
    }


}
