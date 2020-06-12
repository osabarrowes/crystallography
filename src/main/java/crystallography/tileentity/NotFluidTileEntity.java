package crystallography.tileentity;

import crystallography.init.ModTileEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotFluidTileEntity extends TileEntity {

    private static final Logger LOGGER = LogManager.getLogger();

    public NotFluidTileEntity() {
        super(ModTileEntityTypes.NOT_FLUID_TILE_ENTITY.get());
    }

    private BlockPos controllerPos;

    public void onEntityCollision(Entity entityIn) {
        if (entityIn instanceof ItemEntity) {

            if (/* TODO valid item */ true) {

                //inform the controller
                ItemStack itemstack = ((ItemEntity) entityIn).getItem();
                Item item = itemstack.getItem();
                int count = itemstack.getCount();

                if(controllerPos==null)
                    return; // stop, you have violated the law
                // It would probably be good to know why controllerPos is null on the second time around.
                // It would probably be good to know why there's a second time around in the first place

                TileEntity controllerTE = this.getWorld().getTileEntity(controllerPos);
                if(controllerTE instanceof TestControllerBlockTileEntity)
                {
                    ((TestControllerBlockTileEntity) controllerTE).addItem(item, count);
                }
                else {
                    LOGGER.warn("this is bad. controllerPos pointed to something that was not a controller...");
                }

                //consume the item
                entityIn.remove();
            }
        }
    }

    public void setControllerPos(BlockPos pos)
    {
        controllerPos = pos;
    }

    public BlockPos getControllerPos()
    {
        return controllerPos;
    }

}
