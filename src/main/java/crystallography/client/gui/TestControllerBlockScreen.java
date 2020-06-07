package crystallography.client.gui;

import crystallography.init.ModBlocks;
import crystallography.libs.tileentity.TestControllerBlockTileEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public class TestControllerBlockScreen extends Screen {

    protected TestControllerBlockScreen(final TestControllerBlockTileEntity tileEntity) {
        super(ModBlocks.TEST_CONTROLLER_BLOCK.get().getNameTextComponent());
    }


}
