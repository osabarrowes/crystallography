package crystallography.tileentity;

import crystallography.init.ModTileEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShapes;

public class NotFluidTileEntity extends TileEntity {
    public NotFluidTileEntity() {
        super(ModTileEntityTypes.NOT_FLUID_TILE_ENTITY.get());
    }


    public void onEntityCollision(Entity entityIn) {
        if (entityIn instanceof ItemEntity) {
            BlockPos blockpos = this.getPos();
            if (/* valid item */ true) {
                //consume the item
                //inform the controller of the consumed item and quantities
            }
        }
    }
}
