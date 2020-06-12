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

            if (/* valid item */ true) {

                LOGGER.info("Tile entity has seen an item");
                //inform the controller
                ItemStack itemstack = ((ItemEntity) entityIn).getItem();
                Item item = itemstack.getItem();
                int count = itemstack.getCount();
                LOGGER.info("Item information parsed.");

                TileEntity controllerTE = this.getWorld().getTileEntity(controllerPos);
                LOGGER.info("Controller tile entity accessed.");

                if(controllerTE instanceof TestControllerBlockTileEntity)
                {
                    ((TestControllerBlockTileEntity) controllerTE).addItem(item, count);
                    LOGGER.info("Information was sent to the controller.");
                }
                else {
                    // this is bad
                    LOGGER.warn("this is bad.");
                }

                //consume the item
                entityIn.remove();
                LOGGER.info("item removed.");
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
