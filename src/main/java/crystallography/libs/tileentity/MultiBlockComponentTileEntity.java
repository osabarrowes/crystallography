package crystallography.libs.tileentity;

import crystallography.block.TestControllerBlock;
import crystallography.init.ModTileEntityTypes;
import crystallography.libs.multiblock.MultiBlockComponent;
import crystallography.tileentity.TestControllerBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StructureBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static crystallography.libs.multiblock.MultiBlockComponent.VALID;
import static net.minecraft.block.Block.replaceBlock;

public class MultiBlockComponentTileEntity extends TileEntity {

    // default behavior
    public MultiBlockComponentTileEntity() {
        super(ModTileEntityTypes.TEST_BLOCK_TILE_ENTITY.get());
    }

    public MultiBlockComponentTileEntity(TileEntityType<? extends TileEntity> type)
    {
        super(type);
    }

    protected BlockPos controllerPos;

    private static final Logger LOGGER = LogManager.getLogger();

    public void setControllerPos(BlockPos pos)
    {
        controllerPos = pos;
        // write(new CompoundNBT());
    }

    public BlockPos getControllerPos()
    {
        // if (controllerPos == null)
        return controllerPos;
    }

//    @Override
//    public CompoundNBT write(CompoundNBT compound) {
//        compound.putInt("controller_x", controllerPos.getX());
//        compound.putInt("controller_y", controllerPos.getY());
//        compound.putInt("controller_z", controllerPos.getZ());
//        return super.write(compound);
//    }

    /**
     * This method should be called when the associated block is broken in order to let the controller know the structure
     * may no longer be valid.
     */
    public void invalidateController()
    {
        // return controllerPos;

        Block controller = world.getBlockState(controllerPos).getBlock();

        // DEBUG copied from the onBlockActivated method
        Set<BlockPos> structure = new HashSet<>();
        BlockState newState;
        if(controller instanceof TestControllerBlock) // safety check
        {
            if(((TestControllerBlock)controller).imValid(world, pos, structure))
            {
                for(BlockPos component : structure)
                {
                    newState = world.getBlockState(component).with(VALID, true);
                    world.setBlockState(component, newState, 2); // Flag 2: send the change to clients
                }
            }
            else
            {
                for(BlockPos component : structure)
                {
                    newState = world.getBlockState(component).with(VALID, false);
                    world.setBlockState(component, newState, 2);
                }
            }

        }
        else
        {
            LOGGER.info("your controller is not a controller! I will assume it has been destroyed!");
        }
    }
}
