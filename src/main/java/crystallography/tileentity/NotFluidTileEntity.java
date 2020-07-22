package crystallography.tileentity;

import crystallography.init.ModTileEntityTypes;
import crystallography.libs.Util;
import crystallography.libs.tileentity.MultiBlockComponentTileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotFluidTileEntity extends MultiBlockComponentTileEntity {

    private static final Logger LOGGER = LogManager.getLogger();

    public NotFluidTileEntity() {
        super(ModTileEntityTypes.NOT_FLUID_TILE_ENTITY.get());
    }

    public void onEntityCollision(Entity entityIn) {
        if (entityIn instanceof ItemEntity) {

            if (Util.validVatIngredient(entityIn)) {

                //inform the controller
                ItemStack itemstack = ((ItemEntity) entityIn).getItem();

                if(controllerPos==null)
                    return; // stop, you have violated the law
                /* FIXME
                 * It would probably be good to know why controllerPos is null on the second time around.
                 * It would probably be good to know why there's a second time around in the first place.
                 * What I mean is that onEntityCollision is called twice when an entity colldies with the block, and for
                 * some reason controllerPos is null on the second time.
                 */

                TileEntity controllerTE = this.getWorld().getTileEntity(controllerPos);
                if(controllerTE instanceof TestControllerBlockTileEntity)
                {
                    itemstack = ((TestControllerBlockTileEntity) controllerTE).addItem(itemstack);
                }
                else {
                    LOGGER.warn("this is bad. controllerPos pointed to something that was not a controller...");
                }

                if(itemstack.isEmpty()) {
                    ((ItemEntity) entityIn).setItem(ItemStack.EMPTY);
                    entityIn.remove();
                }
                else {
                    ((ItemEntity) entityIn).setItem(itemstack);
                    LOGGER.info("Item=" + itemstack.getItem() + ", count=" + itemstack.getCount());
                }
            }
        }
    }


    //TODO update to 1.16
    /**
     * Read saved data from disk into the tile entity.
     */
//    @Override
//    public void read(CompoundNBT tag) {
//        // super.read(tag);
//        byte data[] = tag.getByteArray("controllerPos");
//        controllerPos = new BlockPos(data[0], data[1], data[2]);
//        // FIXME I'm getting a fatal IndexOutOfBoundsException 0 from this line, and overriding the method onLoad may help here.
//    }

    /**
     * Write data from the tile entity into a compound tag for saving to disk.
     */
    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        byte data[] = {(byte)controllerPos.getX(), (byte)controllerPos.getY(), (byte)controllerPos.getZ()};
        tag.putByteArray("controllerPos", data);
        return tag;
    }

}
