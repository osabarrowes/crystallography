package crystallography.tileentity;

import crystallography.common.crafting.RecipeVatReaction;
import crystallography.init.ModBlocks;
import crystallography.init.ModItems;
import crystallography.init.ModRecipeTypes;
import crystallography.init.ModTileEntityTypes;
import crystallography.common.crafting.Solutes;
import crystallography.libs.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
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
import java.util.*;

public class TestControllerBlockTileEntity extends TileEntity implements ITickableTileEntity {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String INVENTORY_TAG = "vat_data";
    private static final String STRUCTURE_TAG = "structure";
    private static final int IRON_ORE_SLOT = 0;
    private static final int IRON_CATALYST_SLOT = 1;
    private Set<BlockPos> structure;

    // Store the capability lazy optionals as fields to keep the amount of objects we use to a minimum
    private final LazyOptional<ItemStackHandler> inventoryCapabilityExternal = LazyOptional.of(() -> this.inventory);

    public final ItemStackHandler inventory = new ItemStackHandler( Solutes.countAllSolutes()) {
        @Override
        public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
            return Solutes.isAcceptedOre(stack.getItem()) || Solutes.isAcceptedCatalyst(stack.getItem()) || Solutes.isAcceptedMisc(stack.getItem());
            // FIXME look at cadiboo's example mod for a better way to handle this
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
            // FIXME use tags instead of hard coding
            if (getStackInSlot(slot).getItem().equals(Items.IRON_ORE)) {
                return Util.fluidCount(structure, world);
            }
            if (getStackInSlot(slot).getItem().equals(ModItems.EXAMPLE_ITEM)) {
                return Util.fluidCount(structure, world) * 8; // FIXME ItemStacks which have a max stack size of 64 aren't able to exceed that, even though it should according to this
                // Might have to make additional slots based on the size of the vat in order to accommodate this, probably do that using setSize?
            }
            return 64;

        }
    };

    public TestControllerBlockTileEntity() {
        super(ModTileEntityTypes.TEST_CONTROLLER_BLOCK_TILE_ENTITY.get());

    }

    public void setStructure(Set<BlockPos> structure)
    {
        this.structure = structure;
    }

    public Set<BlockPos> getStructure()
    {
        if(structure == null)
            return new HashSet<>(); // why are you calling me without having a structure?
        return structure;
    }

    /**
     * Attempts to place the passed stack into the vat. Returns stack of leftover items.
     */
    public ItemStack addItem(ItemStack stack)
    {
        // TODO use tags instead of hard coding
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
    }

    //DEBUG
    public void reportContents()
    {
        LOGGER.info("ItemStackHandler Contents");
        for(int i = 0; i < inventory.getSlots(); i++)
            LOGGER.info("Item=" + inventory.getStackInSlot(i).getItem() + ", count=" + inventory.getStackInSlot(i).getCount());
        LOGGER.info("Structure size:" + structure.size());
    }

    // TODO implement this helper method copied from example mod, use tags, see line 65
//    /**
//     * @return If the stack is not empty and has a smelting recipe associated with it
//     */
//    private boolean isInput(final ItemStack stack) {
//        if (stack.isEmpty())
//            return false;
//        return getRecipe(stack).isPresent();
//    }

    // TODO implement this helper method copied from example mod, use tags, see line 65
//    /**
//     * @return If the stack's item is equal to the result of smelting our input
//     */
//    private boolean isOutput(final ItemStack stack) {
//        final Optional<ItemStack> result = getResult(inventory.getStackInSlot(INPUT_SLOT));
//        return result.isPresent() && ItemStack.areItemsEqual(result.get(), stack);
//    }

    // TODO implement this helper method copied from example mod, use tags, see link 65
//    /**
//     * @return The smelting recipe for the input stack
//     */
//    private Optional<FurnaceRecipe> getRecipe(final ItemStack input) {
//        // Due to vanilla's code we need to pass an IInventory into RecipeManager#getRecipe so we make one here.
//        return getRecipe(new Inventory(input));
//    }


    /**
     * @return The reaction recipe for the inventory
     */
    private Optional<RecipeVatReaction> getRecipe(final IInventory inventory) {
        return world.getRecipeManager().getRecipe(ModRecipeTypes.RECIPE_VAT_REACTION_TYPE, inventory, world);
    }

    /**
     * @return The result of reacting the input stack
     */
    private Optional<ItemStack> getResult(final ItemStack input) {
        // Due to vanilla's code we need to pass an IInventory into RecipeManager#getRecipe and
        // AbstractCookingRecipe#getCraftingResult() so we make one here.
        final Inventory dummyInventory = new Inventory(input);
        return getRecipe(dummyInventory).map(recipe -> recipe.getCraftingResult(dummyInventory));
    }

    /**
     * Mimics the code in AbstractFurnaceTileEntity#getCookTime()
     *
     * @return The custom smelt time or 200 if there is no recipe for the input
     */
    private short getSmeltTime(final ItemStack input) {
        // TODO implement
        return 0;
//        return getRecipe(input)
//                .map(AbstractCookingRecipe::getCookTime)
//                .orElse(200)
//                .shortValue();
    }

    @Override
    public void onLoad() {
        // TODO implement
        super.onLoad();
        // We set this in onLoad instead of the constructor so that TileEntities
        // constructed from NBT (saved tile entities) have this set to the proper value
        if (world != null && !world.isRemote);
            // lastEnergy = energy.getEnergyStored();
    }

    // FIXME mappings
    @Override
    public void func_230337_a_(BlockState blockstate, CompoundNBT tag) {
        super.func_230337_a_(blockstate, tag);
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

    /**
     * Helper method for serializing a structure to NBT
     * @param structure the set of blockpos which represents the block in this multiblock structure
     */
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

    /**
     * Helper method for deserializing a structure from NBT
     */
    private Set<BlockPos> structureDeserialize(CompoundNBT tag) {
        Set<BlockPos> structure = new HashSet<>();
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

    /**
     * Called once per tick (20 hz)
     */
    @Override
    public void tick() {
        if(!world.isRemote) {
            // LOGGER.info(world.getGameTime());
            // each tick, calculate the remaining time for all currently progressing reactions using the rate equation
            //  this would be handled with a call to getSmeltTime, passing the reaction, or some sort of internal call on Reaction objects, or a combination of both
            // use the remaining time to determine the finish time, by adding it to world time
            // increment all reactions by one
            // if any reactions now have finish time equal to world time, nucleate them and remove them from the list of reactions
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
        inventoryCapabilityExternal.invalidate(); // I believe that super already does this.
    }

    //DEBUG
    public void craft(int slot) {
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

