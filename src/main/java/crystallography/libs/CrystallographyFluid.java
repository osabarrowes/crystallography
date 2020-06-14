package crystallography.libs;

import crystallography.Crystallography;
import crystallography.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;

import javax.annotation.Nullable;

/**
 * @author xenonni
 */
public class CrystallographyFluid extends FlowingFluid {

    protected final String fluidName;
    protected final ResourceLocation stillTex;
    protected final ResourceLocation flowingTex;
    protected CrystallographyFluid flowing;
    public CrystallographyFluid source;
    @Nullable // what do you do when this is null? probably should have some default value, like the properties of water for instance
    protected final FluidAttributes.Builder buildAttributes;
    public Block block;
    protected Item bucket;

    // A lot of these parameters are copied from water, and the overall structure is based on immersive engineering's IEFluid

    public CrystallographyFluid(String fluidName, ResourceLocation stillTex, ResourceLocation flowingTex, @Nullable FluidAttributes.Builder buildAttributes, boolean isSource)
    {
        this.fluidName = fluidName;
        this.stillTex = stillTex;
        this.flowingTex = flowingTex;
        this.buildAttributes = buildAttributes;
        if(!isSource)
        {
            flowing = this;
            setRegistryName(Crystallography.MOD_ID, fluidName+"_flowing");
        }
        else
        {
            source = this;
            this.block = ModBlocks.UNIVERSAL_SOLVENT_FLUID_BLOCK.get();
//            this.block = new FlowingFluidBlock(() -> this.source, Block.Properties.create(Material.WATER))
//            {
//                @Override
//                protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//                {
//                    super.fillStateContainer(builder);
//                    builder.add(CrystallographyFluid.this.getStateContainer().getProperties().toArray(new IProperty[0]));
//                }
//
//                @Override
//                public IFluidState getFluidState(BlockState state)
//                {
//                    IFluidState baseState = super.getFluidState(state);
//                    for(IProperty<?> prop : CrystallographyFluid.this.getStateContainer().getProperties())
//                        if(prop!=FlowingFluidBlock.LEVEL)
//                            baseState = withCopiedValue(prop, baseState, state);
//                    return baseState;
//                }
//
//                private <T extends IStateHolder<T>, S extends Comparable<S>>
//                T withCopiedValue(IProperty<S> prop, T oldState, IStateHolder<?> copyFrom)
//                {
//                    return oldState.with(prop, copyFrom.get(prop));
//                }
//            };
//            this.block.setRegistryName(Crystallography.MOD_ID, fluidName+"_fluid_block");
//            ModBlocks.BLOCKS.register("universal_solvent_fluid_block", () -> this.block)

            // this.bucket = ModItems.UNIVERSAL_SOLVENT_FLUID_BUCKET;
            // TODO make buckets in a generalized way, so we're not hard coding them for each fluid

            flowing = createFlowingVariant();
        }
    }

    // lifted right out of IEFluid
    protected CrystallographyFluid createFlowingVariant()
    {
        CrystallographyFluid ret = new CrystallographyFluid(fluidName, stillTex, flowingTex, buildAttributes, false)
        {
            @Override
            protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder)
            {
                super.fillStateContainer(builder);
                builder.add(LEVEL_1_8);
            }
        };
        ret.source = this;
        ret.bucket = bucket;
        ret.block = block;
        ret.setDefaultState(ret.getStateContainer().getBaseState().with(LEVEL_1_8, 7));
        return ret;
    }

    // Mod Fluids must override createAttributes.
    @Override
    protected FluidAttributes createAttributes() {
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
