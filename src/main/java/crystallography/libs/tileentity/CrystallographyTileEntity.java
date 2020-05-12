package crystallography.libs.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class CrystallographyTileEntity extends TileEntity implements ITickableTileEntity {

    public CrystallographyTileEntity(TileEntityType<?> thisTileEntityType) {
        super(thisTileEntityType);
    }

    @Override
    public void tick() {
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        return super.getCapability(capability, side);
    }
}
