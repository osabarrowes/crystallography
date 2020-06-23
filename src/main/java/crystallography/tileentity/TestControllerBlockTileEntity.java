package crystallography.tileentity;

import com.mojang.datafixers.types.DynamicOps;
import crystallography.init.ModBlocks;
import crystallography.init.ModItems;
import crystallography.init.ModTileEntityTypes;
import crystallography.libs.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;

import java.util.HashSet;
import java.util.Map;

public class TestControllerBlockTileEntity extends TileEntity implements ITickableTileEntity {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String INVENTORY_TAG = "vat_data";
    private static final String STRUCTURE_TAG = "structure";
    private static final int IRON_ORE_SLOT = 0;
    private static final int IRON_CATALYST_SLOT = 1;
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
        int before = stack.getCount();

        if(stack.getItem() == Items.IRON_ORE)
            stack = inventory.insertItem(IRON_ORE_SLOT, stack, false);
        if(stack.getItem() == ModItems.EXAMPLE_ITEM)
            stack = inventory.insertItem(IRON_CATALYST_SLOT, stack, false);

        if(before != stack.getCount()) // something was inserted
            checkCraft();

        return stack;
    }

    private void checkCraft() {
        if(/*there enough ingredients to react*/ true) // TODO
        {
            // begin crafting the appropriate product
            // TODO keyword appropriate
            craft(IRON_ORE_SLOT);
        }
        return;
    }

    //DEBUG
    public void reportContents()
    {
        LOGGER.info("ItemStackHandler Contents");
        for(int i = 0; i < inventory.getSlots(); i++)
            LOGGER.info("Item=" + inventory.getStackInSlot(i).getItem() + ", count=" + inventory.getStackInSlot(i).getCount());
        LOGGER.info("Structure size:" + structure.size());
    }

    /**
     * Read saved data from disk into the tile entity.
     */
    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        this.inventory.deserializeNBT(tag.getCompound(INVENTORY_TAG));
        this.structure = structureDeserialize(tag.getCompound(STRUCTURE_TAG));
    }

    /**
     * Write data from the tile entity into a compound tag for saving to disk.
     */
    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.put(INVENTORY_TAG, inventory.serializeNBT());
        tag.put(STRUCTURE_TAG, structureSerialize(structure));
        return tag;
    }

    private CompoundNBT structureSerialize(Collection<BlockPos> structure) {
        CompoundNBT tag = new CompoundNBT();
        int i = 0;
        for(BlockPos p : structure)
        {
            byte data[] = {(byte)p.getX(), (byte)p.getY(), (byte)p.getZ()};
            tag.putByteArray(""+i, data);
            // I'm not sure how vanilla handles BlockPos serialization, so I'm doing it myself
            i++;
        }
        tag.putInt("structureSize", i);
        return tag;
    }

    private Collection<BlockPos> structureDeserialize(CompoundNBT tag) {
        Collection<BlockPos> structure = new HashSet<>();
        int size = tag.getInt("structureSize");
        byte data[];
        BlockPos pos;
        for(int i = 0; i < size; i++)
        {
            data = tag.getByteArray(""+i);
            pos = new BlockPos(data[0],data[1],data[2]);
            structure.add(pos);
        }
        return structure;
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
            // LOGGER.info(world.getGameTime());
        }

    }

    /**
     * Invalidates our tile entity
     */
    @Override
    public void remove() {
        super.remove();
        // We need to invalidate our capability references so that any cached references (by other mods) don't
        // continue to reference our capabilities and try to use them and/or prevent them from being garbage collected
        inventoryCapabilityExternal.invalidate();
    }

    //DEBUG
    public void craft(int slot) {
        // This method represents a hard coded solution, but minecraft uses json to store its recipies, including smelt times,
        // ingredients, and outputs. You should probably use that system instead.
        /*
         * whenever an ingredient is added to the vat, check if there are enough ingredients to begin a reaction.
         *  if there aren't, do nothing.
         *  if there are, use the rate equation for the specific ingredient to determine how long it will take.
         *   this time value should be in ticks.
         *  after this amount of time has passed, pick an available nucleation site and place a crystal there.
         *   each tick, reduce the time remaining for this reaction by one.
         *   when time remaining is less than one, place the crystal and stop counting down.
         */
        if(!inventory.getStackInSlot(slot).isEmpty())
        {
            BlockPos crystallizingPos;

            for (BlockPos p : structure)
            {
                if (getWorld().getBlockState(p).getBlock().equals(ModBlocks.NUCLEATION_BLOCK.get()))
                {
                    // check for any eligible neighbors, which include water or NotFluid
                    Map<Direction, Block> neighbors = Util.getNeighbors(getWorld(), p);
                    for(Direction d : neighbors.keySet())
                    {
                        if(neighbors.get(d).equals(Blocks.WATER) || neighbors.get(d).equals(ModBlocks.NOT_FLUID.get()))
                        {
                            crystallizingPos = p.offset(d);

                            getWorld().setBlockState(crystallizingPos, product(slot));
                            inventory.extractItem(slot, 1, false);
                            return;
                        }
                    }
                }
            }
            LOGGER.info("No valid crystallizing position was found.");
            return;
        }
        LOGGER.info("Insufficient crafting materials for recipe: IRON_CRYSTAL_BLOCK");

    }

    private BlockState product(int slot) {
        switch (slot)
        {
            case IRON_ORE_SLOT:
                return ModBlocks.IRON_CRYSTAL_BLOCK.get().getDefaultState();
            default:
                return ModBlocks.NOT_FLUID.get().getDefaultState();
        }
    }
}

