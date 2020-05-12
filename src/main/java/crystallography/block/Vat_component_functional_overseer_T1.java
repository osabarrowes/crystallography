package crystallography.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * When right clicked by the player, this block performs the multiblock validation algorithm to
 * determine whether or not it is part of a well formed Vat multiblock structure. It reports this
 * in the chat and has no other purpose.
 *
 * @author xenonni
 */
public class Vat_component_functional_overseer_T1 extends Block{

    private static final Logger LOGGER = LogManager.getLogger();

    public Vat_component_functional_overseer_T1(final Block.Properties properties) {
        super(properties);
    }

    /**
     * This method is called when the player right clicks on the block.
     *
     * Code provided by forgedocs
     * https://mcforge.readthedocs.io/en/latest/blocks/interaction/#onblockactivated
     * @return true on successful activation
     */
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        if (!worldIn.isRemote)
        {
            LOGGER.info("My name is " + this.getRegistryName());
            LOGGER.info("I also go by " +  worldIn.getBlockState(pos).getBlock().getRegistryName());
            LOGGER.info("The block above me is: " + worldIn.getBlockState(pos.add(0,1,0)).getBlock().getRegistryName());
        }
        return ActionResultType.SUCCESS;
    }

    /*
     * Called when the block is left clicked
     *
    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player)
    {
        // if(!worldIn.isRemote)
        System.out.println("hello? anyone?");
        LOGGER.info("Block at " + pos.toString() + " was activated");
        // return true;
    }
    */
}
