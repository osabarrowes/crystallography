package crystallography.tileentity;

import crystallography.init.ModTileEntityTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class NucleationBlockTileEntity extends TileEntity {

    public NucleationBlockTileEntity() {
        super(ModTileEntityTypes.NUCLEATION_BLOCK_TILE_ENTITY.get());
    }

    private BlockPos controllerPos;

    public void setControllerPos(BlockPos pos)
    {
        controllerPos = pos;
    }

    public BlockPos getControllerPos()
    {
        return controllerPos;
    }
}
