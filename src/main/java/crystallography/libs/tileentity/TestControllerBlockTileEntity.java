package crystallography.libs.tileentity;

import crystallography.init.ModTileEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

public class TestControllerBlockTileEntity extends TileEntity implements ITickableTileEntity {

    private static final Logger LOGGER = LogManager.getLogger();

    private Collection<BlockPos> structure;

    public TestControllerBlockTileEntity() {
        super(ModTileEntityTypes.TEST_CONTROLLER_BLOCK_TILE_ENTITY.get());
    }

    public void setStructure(Collection<BlockPos> structure)
    {
        this.structure = structure;
    }

    public Collection<BlockPos> getStructure()
    {
        if(structure == null)
            return new ArrayList<>(); // why are you calling me without having a structure?
        return structure;
    }

    @Override
    public void tick() {
//        if (world.isRemote())
//            LOGGER.info("I am a tile entity located at " + this.pos);

    }



}
