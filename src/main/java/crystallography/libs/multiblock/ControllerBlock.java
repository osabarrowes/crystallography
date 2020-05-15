package crystallography.libs.multiblock;

import java.util.Set;

/**
 * Controller blocks exist as components of a multiblock and serve as the overseer or master. Logic which applies to the
 * entire multiblock structure should be handled by the ControllerBlock.
 *
 * Exactly one controllerBlock is allowed per multiblock structure.
 */
public abstract class ControllerBlock extends MultiBlockComponent {

    // A list containing all the multiblocks in this structure.
    // CLEANME We do this because recursion. If a block is in the set already, then it has already been visited. No additional visitation is required.
    // TODO ensure only valid IMultiBlockComponents are contained in this list.
    private Set<MultiBlockComponent> structure;

    public ControllerBlock(Properties properties) {
        super(properties);
    }

    /*
     * Exactly one controllerBlock is allowed per multiblock structure.
     *
      */
    public boolean isValid()
    {
        for(MultiBlockComponent comp : structure)
        {
            if(comp instanceof ControllerBlock)
                return false;
        }
        structure.add(this);
        return imValid(this, structure);
    }
    // ControllerBlock will start the algorithm initially, but any MultiBlockComponent should be able to do so.

}
