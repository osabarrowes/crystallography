package crystallography.init;

import crystallography.Crystallography;
import crystallography.libs.tileentity.NucleationBlockTileEntity;
import crystallography.libs.tileentity.TestControllerBlockTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.objectweb.asm.commons.JSRInlinerAdapter;

/**
 * Holds a list of all our {@link TileEntityType}s.
 * Suppliers that create TileEntityTypes are added to the DeferredRegister.
 * The DeferredRegister is then added to our mod event bus in our constructor.
 * When the TileEntityType Registry Event is fired by Forge and it is time for the mod to
 * register its TileEntityTypes, our TileEntityTypes are created and registered by the DeferredRegister.
 * The TileEntityType Registry Event will always be called after the Block and Item registries are filled.
 * Note: This supports registry overrides.
 *
 * Based on code from Cadiboo's example mod.
 *
 * @author xenonni
 */
public class ModTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Crystallography.MOD_ID);

    // We don't have a datafixer for our TileEntities, so we pass null into build. (Cadiboo)
    // Whatever that means...
    public static final RegistryObject<TileEntityType<TestControllerBlockTileEntity>> TEST_CONTROLLER_BLOCK_TILE_ENTITY = TILE_ENTITY_TYPES.register("test_controller_block_tile_entity", () ->
            TileEntityType.Builder.create(TestControllerBlockTileEntity::new, ModBlocks.TEST_CONTROLLER_BLOCK.get()).build(null)
    );

    public static final RegistryObject<TileEntityType<NucleationBlockTileEntity>> NUCLEATION_BLOCK_TILE_ENTITY = TILE_ENTITY_TYPES.register("nucleation_block_tile_entity", () ->
            TileEntityType.Builder.create(NucleationBlockTileEntity::new, ModBlocks.NUCLEATION_BLOCK.get()).build(null)
    );
}
