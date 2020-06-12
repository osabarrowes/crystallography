package crystallography.libs.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public class MultiBlockComponentTileEntity extends TileEntity {

    // FIXME make more generic
    public MultiBlockComponentTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    // More thinking is required on how to structure this class. functions similar in concept to CrystallographyTileEntity

    private BlockPos masterLocation;

    public void setMaster(BlockPos pos)
    {
        masterLocation = pos;
    }

    public BlockPos getMaster()
    {
        return masterLocation;
    }
}
