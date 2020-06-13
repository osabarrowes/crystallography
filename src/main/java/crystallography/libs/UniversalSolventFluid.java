package crystallography.libs;

import crystallography.Crystallography;
import crystallography.init.ModBlocks;
import crystallography.init.ModFluids;
import crystallography.init.ModItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IProperty;
import net.minecraft.state.IStateHolder;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author xenonni
 */
public class UniversalSolventFluid extends FlowingFluid {

//    protected UniversalSolventFluid flowing;
//    protected UniversalSolventFluid source;
//    public Block block;
//    protected Item bucket;
//    protected FluidAttributes attributes;

    // public static final Collection<UniversalSolventFluid> IE_FLUIDS = new ArrayList<>(); // CLEANME remove?
    protected final String fluidName;
    protected final ResourceLocation stillTex;
    protected final ResourceLocation flowingTex;
    protected UniversalSolventFluid flowing;
    public UniversalSolventFluid source;
    @Nullable
    protected final Consumer<FluidAttributes.Builder> buildAttributes;
    public Block block;
    protected Item bucket;

    // A lot of these parameters are copied from water, and the overall structure is based on immersive engineering's IEFluid

    // TODO figure out constructor
//    public UniversalSolventFluid()
//    {
//        super();
//        flowing = this;
//        source = this;
//        block = ModBlocks.UNIVERSAL_SOLVENT_FLUID_BLOCK.get();
//        bucket = new BucketItem(() -> this.source, new Item.Properties()
//                .maxStackSize(1)
//                .group(ModItemGroups.MOD_ITEM_GROUP));
//        // attributes = this.getAttributes();
//        // this.setRegistryName("universal_solvent");
//    }

    public UniversalSolventFluid(String fluidName, ResourceLocation stillTex, ResourceLocation flowingTex, @Nullable Consumer<FluidAttributes.Builder> buildAttributes, boolean isSource)
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
//                    builder.add(UniversalSolventFluid.this.getStateContainer().getProperties().toArray(new IProperty[0]));
//                }
//
//                @Override
//                public IFluidState getFluidState(BlockState state)
//                {
//                    IFluidState baseState = super.getFluidState(state);
//                    for(IProperty<?> prop : UniversalSolventFluid.this.getStateContainer().getProperties())
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
//            ModBlocks.BLOCKS.register("universal_solvent_fluid_block", () -> this.block);

            // TODO make your own damn buckets

            flowing = createFlowingVariant();
        }
    }

    // lifted right out of IEFluid
    protected UniversalSolventFluid createFlowingVariant()
    {
        UniversalSolventFluid ret = new UniversalSolventFluid(fluidName, stillTex, flowingTex, buildAttributes, false)
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
