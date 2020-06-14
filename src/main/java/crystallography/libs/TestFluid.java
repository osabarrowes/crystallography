package crystallography.libs;

import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.WaterFluid;

public class TestFluid extends WaterFluid {
    @Override
    public boolean isSource(IFluidState state) {
        return state.getFluid()==this;
    }

    @Override
    public int getLevel(IFluidState fluidState) {
        if(isSource(fluidState))
            return 8;
        else
            return fluidState.get(LEVEL_1_8);
    }

}
