package crystallography.init;

import crystallography.Crystallography;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author xenonni
 */

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =  DeferredRegister.create(ForgeRegistries.FLUIDS, Crystallography.MOD_ID);

    // FIXME implement fluid for 1.16
//    public static final RegistryObject<Fluid> UNIVERSAL_SOLVENT = FLUIDS.register("universal_solvent", () -> new CrystallographyFluid(
//            "universal_solvent",
//            new ResourceLocation(Crystallography.MOD_ID, "block/fluid/universal_solvent_still"),
//            new ResourceLocation(Crystallography.MOD_ID, "block/fluid/universal_solvent_flowing"),
//            null, // I hope that this is handled by the class itself, which overrides createAttributes
//            true
//    ) );
}
