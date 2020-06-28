package crystallography.libs.tileentity;

import crystallography.block.TestControllerBlock;
import crystallography.init.ModTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.StructureBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
        if(controller instanceof TestControllerBlock) // safety check
        {
            Set<BlockPos> structure = new HashSet<>();
            ((TestControllerBlock) controller).imValid(world, controllerPos, structure);
        }
        // else // do nothing
    }
}
