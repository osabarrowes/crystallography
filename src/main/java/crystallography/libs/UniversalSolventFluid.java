package crystallography.libs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;

/**
 * @author xenonni
 */
public class UniversalSolventFluid extends FlowingFluid {

    protected UniversalSolventFluid flowing;
    protected UniversalSolventFluid source;
    public Block block;
    protected Item bucket;

    // A lot of these parameters are copied from water, and the overall structure is based on immersive engineering's IEFluid

    // TODO figure out constructor


    // Mod Fluids must override createAttributes.
    @Override
    protected FluidAttributes createAttributes() {
        source = this; // maybe?
        ResourceLocation stillTexture = new ResourceLocation("crystallography:block/universal_solvent_still");
        ResourceLocation flowingTexture = new ResourceLocation("crystallography:block/universal_solvent_flowing");
        FluidAttributes.Builder builder = FluidAttributes.builder(stillTexture, flowingTexture);
        return builder.build(this);
    }

    @Override
    public Fluid getFlowingFluid() {
        return flowing;
    }

    @Override
    public Fluid getStillFluid() {
        return source;
    }

    @Override
    protected boolean canSourcesMultiply() {
        return false;
    }

    @Override
    protected void beforeReplacingBlock(IWorld worldIn, BlockPos pos, BlockState state) {

    }

    @Override
    protected int getSlopeFindDistance(IWorldReader worldIn) {
        return 4;
    }

    @Override
    protected int getLevelDecreasePerBlock(IWorldReader worldIn) {
        return 1;
    }

    @Override
    public Item getFilledBucket() {
        return bucket;
    }

    @Override
    protected boolean canDisplace(IFluidState state, IBlockReader blockReader, BlockPos pos, Fluid fluidIn, Direction direction) {
        return direction==Direction.DOWN&&!isEquivalentTo(fluidIn);
    }

    @Override
    public int getTickRate(IWorldReader worldReader) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100;
    }

    @Override
    protected BlockState getBlockState(IFluidState state) {
        return block.getDefaultState().with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
    }

    @Override
    public boolean isSource(IFluidState state) {
        return state.getFluid()==source;
    }

    @Override
    public int getLevel(IFluidState fluidState) {
        if(isSource(fluidState))
            return 8;
        else
            return fluidState.get(LEVEL_1_8);
    }

}
