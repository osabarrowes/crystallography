package crystallography.tileentity;

import crystallography.init.ModBlocks;
import crystallography.init.ModItems;
import crystallography.init.ModTileEntityTypes;
import crystallography.libs.Util;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
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

public class TestControllerBlockTileEntity extends TileEntity{

    private static final Logger LOGGER = LogManager.getLogger();

    private Collection<BlockPos> structure;

    // private HashMap<Item, Integer> data = new HashMap<>();

    private VatData vatData = new VatData();

    private ItemStackHandler handler;

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

    public void addItem(Item item, int count)
    {
        vatData.add(item, count);
    }

    //DEBUG
    public void reportContents()
    {
        vatData.reportContents();
    }

    private ItemStackHandler getHandler()
    {
        if (handler == null)
            handler = new ItemStackHandler(2); // default constructor sets size to 1
        return handler;
    }

    @Override
    public void read(CompoundNBT tag) {
        CompoundNBT invTag = tag.getCompound("inv");
        getHandler().deserializeNBT(invTag);
        // TODO vatData

        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        CompoundNBT compound = getHandler().serializeNBT();
        tag.put("inv", compound);
        // TODO vatData

        return super.write(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) getHandler());
        }
        return super.getCapability(cap, side);
    }

    //DEBUG
    public void craft() {
        vatData.craft();
    }

    private class VatData implements INBTSerializable<CompoundNBT>
    {
        protected NonNullList<ItemStack> stacks;

        protected HashMap<Item, Integer> items = new HashMap<>();

        private int maxSize;

        public VatData()
        {
            this(2);
        }

        public VatData(int maxSize)
        {
            this.maxSize = maxSize;
        }

        public void setSize(int size)
        {
            stacks = NonNullList.withSize(size, ItemStack.EMPTY);
        }

        public void add(Item item, int count)
        {
            if(items.containsKey(item))
                items.put(item, items.get(item) + count);
            else
                items.put(item, count);

            LOGGER.info("I received data. Item: " + item + ", count: " + count);
        }

        @Override
        public CompoundNBT serializeNBT() {
            // TODO implement
            //copied from ItemStackHandler
            ListNBT nbtTagList = new ListNBT();
            for (int i = 0; i < stacks.size(); i++)
            {
                if (!stacks.get(i).isEmpty())
                {
                    CompoundNBT itemTag = new CompoundNBT();
                    itemTag.putInt("Slot", i);
                    stacks.get(i).write(itemTag);
                    nbtTagList.add(itemTag);
                }
            }
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("Items", nbtTagList);
            nbt.putInt("Size", stacks.size());
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            // TODO implement
            //copied from ItemStackHandler
            setSize(nbt.contains("Size", Constants.NBT.TAG_INT) ? nbt.getInt("Size") : stacks.size());
            ListNBT tagList = nbt.getList("Items", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < tagList.size(); i++)
            {
                CompoundNBT itemTags = tagList.getCompound(i);
                int slot = itemTags.getInt("Slot");

                if (slot >= 0 && slot < stacks.size())
                {
                    stacks.set(slot, ItemStack.read(itemTags));
                }
            }
            onLoad();
        }

        // protected void onLoad() {} // I'm not sure why this exists

        public void reportContents() {
            for(Item item : items.keySet())
                LOGGER.info("I contain Item: " + item + ", count: " + items.get(item));
        }

        public void craft() {
            // if I have at least one iron ore and at least one example item, place an iron crystal block on one of the fluid (or NotFluid) neighbors of any nucleation block
            if(items.get(Items.IRON_ORE) != null && items.get(Items.IRON_ORE) > 0 && items.get(ModItems.EXAMPLE_ITEM) != null && items.get(ModItems.EXAMPLE_ITEM) > 0)
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

                                getWorld().setBlockState(crystallizingPos, ModBlocks.IRON_CRYSTAL_BLOCK.get().getDefaultState());
                                items.put(Items.IRON_ORE, items.get(Items.IRON_ORE) - 1);
                                items.put(ModItems.EXAMPLE_ITEM, items.get(ModItems.EXAMPLE_ITEM) - 1);
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
    }
}
