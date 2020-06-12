package crystallography.block;

import crystallography.libs.multiblock.ControllerBlock;
import crystallography.libs.multiblock.MultiBlockComponent;
import crystallography.tileentity.NotFluidTileEntity;
import crystallography.tileentity.NucleationBlockTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Placeholder block for the universal solvent until I can figure out how to use fluids correctly
 */
public class NotFluid extends MultiBlockComponent {
    public NotFluid(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValid(World worldIn, BlockPos pos, Set<BlockPos> structure) {

        // FIXME also appears in nucleation block. dry your code
        for (BlockPos p : structure)
        {
            if (worldIn.getBlockState(p).getBlock()instanceof ControllerBlock) {
                TileEntity myTE = worldIn.getTileEntity(pos);
                if(myTE instanceof NotFluidTileEntity)
                {
                    ((NotFluidTileEntity) myTE).setControllerPos(p);
                    break;
                }
            }

        }

        return true;
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new NotFluidTileEntity();
    }


    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if(!state.get(VALID))
            return;
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof NotFluidTileEntity) {
            ((NotFluidTileEntity)tileentity).onEntityCollision(entityIn);
        }
    }
}
