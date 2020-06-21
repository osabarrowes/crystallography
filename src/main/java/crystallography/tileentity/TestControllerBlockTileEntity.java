package crystallography.tileentity;

import crystallography.init.ModBlocks;
import crystallography.init.ModItems;
import crystallography.init.ModTileEntityTypes;
import crystallography.libs.Util;
import crystallography.libs.multiblock.MultiBlockComponent;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TestControllerBlockTileEntity extends TileEntity implements ITickableTileEntity {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String INVENTORY_TAG = "vat_data";
    private static final int IRON_ORE_SLOT = 0;
    private static final int IRON_CATALYST_SLOT = 1;

    /**
     * Keeps track of all currently progressing reactions in the vat.
     */
    private ArrayList<Integer> reactions = new ArrayList<>();

    private Collection<BlockPos> structure;

    // Store the capability lazy optionals as fields to keep the amount of objects we use to a minimum
    private final LazyOptional<ItemStackHandler> inventoryCapabilityExternal = LazyOptional.of(() -> this.inventory);


    // the size should be enough to accommodate all the potential ores and catalysts
    public final ItemStackHandler inventory = new ItemStackHandler( 2) {
        @Override
        public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
            return stack.getItem() == Items.IRON_ORE || stack.getItem() == ModItems.EXAMPLE_ITEM; // FIXME return true if it is an ore or a catalyst
        }

        @Override
        protected void onContentsChanged(final int slot) {
            super.onContentsChanged(slot);
            // Mark the tile entity as having changed whenever its inventory changes.
            // "markDirty" tells vanilla that the chunk containing the tile entity has
            // changed and means the game will save the chunk to disk later.
            markDirty();
        }

        @Override
        public int getSlotLimit(int slot) {
            if (getStackInSlot(slot).getItem().equals(Items.IRON_ORE)) {
                return Util.fluidCount(structure, world);
            }
            if (getStackInSlot(slot).getItem().equals(ModItems.EXAMPLE_ITEM)) {
                return Util.fluidCount(structure, world) * 8; // FIXME ItemStacks which have a max stack size of 64 aren't able to exceed that, even though it should according to this
                // Might have to make additional slots based on the size of the vat in order to accommodate this
            }
            return 64;

        }
    };

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

    /**
     * Attempts to place the passed stack into the vat. Returns stack of leftover items.
     */
    public ItemStack addItem(ItemStack stack)
    {
        if(stack.getItem() == Items.IRON_ORE)
            stack = inventory.insertItem(IRON_ORE_SLOT, stack, false);
        if(stack.getItem() == ModItems.EXAMPLE_ITEM)
            stack = inventory.insertItem(IRON_CATALYST_SLOT, stack, false);
        return stack;
    }

    //DEBUG
    public void reportContents()
    {
        LOGGER.info("ItemStackHandler Contents");
        for(int i = 0; i < inventory.getSlots(); i++)
            LOGGER.info("Item=" + inventory.getStackInSlot(i).getItem() + ", count=" + inventory.getStackInSlot(i).getCount());
    }

    /**
     * Read saved data from disk into the tile entity.
     */
    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        this.inventory.deserializeNBT(tag.getCompound(INVENTORY_TAG));
    }

    /**
     * Write data from the tile entity into a compound tag for saving to disk.
     */
    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.put(INVENTORY_TAG, inventory.serializeNBT());
        return tag;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventoryCapabilityExternal.cast();
        }
        return super.getCapability(cap, side);
    }

    // copied from cadiboo, this will probably be helpful for determining reaction time
//    /**
//     * Mimics the code in {@link AbstractFurnaceTileEntity#func_214005_h()}
//     *
//     * @return The custom smelt time or 200 if there is no recipe for the input
//     */
//    private short getSmeltTime(final ItemStack input) {
//        return getRecipe(input)
//                .map(AbstractCookingRecipe::getCookTime)
//                .orElse(200)
//                .shortValue();
//    }

    /**
     * Called once per tick.
     */
    @Override
    public void tick() {
        if(!world.isRemote) {
            LOGGER.info(world.getGameTime());
        }

    }



    //DEBUG
//    public void craft() {
//        if(!inventory.getStackInSlot(IRON_ORE_SLOT).isEmpty() && !inventory.getStackInSlot(IRON_CATALYST_SLOT).isEmpty())
//        {
//            BlockPos crystallizingPos;
//
//            for (BlockPos p : structure)
//            {
//                if (getWorld().getBlockState(p).getBlock().equals(ModBlocks.NUCLEATION_BLOCK.get()))
//                {
//                    // check for any eligible neighbors, which include water or NotFluid
//                    Map<Direction, Block> neighbors = Util.getNeighbors(getWorld(), p);
//                    for(Direction d : neighbors.keySet())
//                    {
//                        if(neighbors.get(d).equals(Blocks.WATER) || neighbors.get(d).equals(ModBlocks.NOT_FLUID.get()))
//                        {
//                            crystallizingPos = p.offset(d);
//
//                            getWorld().setBlockState(crystallizingPos, ModBlocks.IRON_CRYSTAL_BLOCK.get().getDefaultState());
//                            items.put(Items.IRON_ORE, items.get(Items.IRON_ORE) - 1);
//                            items.put(ModItems.EXAMPLE_ITEM, items.get(ModItems.EXAMPLE_ITEM) - 1);
//                            return;
//                        }
//                    }
//                }
//            }
//            LOGGER.info("No valid crystallizing position was found.");
//            return;
//        }
//        LOGGER.info("Insufficient crafting materials for recipe: IRON_CRYSTAL_BLOCK");
//
//    }
}

