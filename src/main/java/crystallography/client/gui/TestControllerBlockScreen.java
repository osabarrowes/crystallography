package crystallography.client.gui;

import crystallography.init.ModBlocks;
import crystallography.tileentity.TestControllerBlockTileEntity;
import net.minecraft.client.gui.screen.Screen;

public class TestControllerBlockScreen extends Screen {

    protected TestControllerBlockScreen(final TestControllerBlockTileEntity tileEntity) {
        super(ModBlocks.TEST_CONTROLLER_BLOCK.get().func_235333_g_()); // getNameTextComponent()
    }


}
