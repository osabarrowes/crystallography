package crystallography.libs.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This interface is can be inherited by any block that is part of a multiblock structure for which you want to run
 * validation algorithms to ensure its appropriatness
 */
public abstract class MultiBlockComponent extends Block{

    public MultiBlockComponent(Properties properties) {
        super(properties);
    }

    public abstract boolean isValid(){
        // if all my neighbors valid
        //      if my specific algorithm says I am valid
        //          return true;

        // return false;

    }

//    void validateNeighbors();
//        for (neighbor : neighborhood)
//            if (neighbor instanceof MultiBlockComponent)
//                neighbor.Validate();
        // once all the neighbors are validated

    /**
     * Returns this block.
     * @return
     */
    public boolean isValidAlgorithm()
    {

    }


}
