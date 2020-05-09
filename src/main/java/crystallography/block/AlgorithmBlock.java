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
public class AlgorithmBlock extends Block{

    private static final Logger LOGGER = LogManager.getLogger();

    public AlgorithmBlock(final Block.Properties properties) {
        super(properties);
    }

    /*
     * Copied from Cadiboo's example mod
     * Called when a player right clicks our block.
     *
     * @deprecated Call via {@link BlockState#onBlockActivated(World, PlayerEntity, Hand, BlockRayTraceResult)} whenever possible.
     * Implementing/overriding is fine.
     *
     * onBlockActivated was removed from Block in Forge 31.1.0
     * you MUST use BlockState.onBlockActivated instead
     *
     * public ActionResultType onBlockActivated(final BlockState state, final World worldIn, final BlockPos pos, final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit)
     */

    /**
     * I think this method is called when the block is activated, specifically in this case by right clicking
     *
     * Code provided by forgedocs
     * https://mcforge.readthedocs.io/en/latest/blocks/interaction/#onblockactivated
     * @return true on successful activation
     */
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        LOGGER.info("Block at " + pos.toString() + " was activated");
        return true;
    }
}
