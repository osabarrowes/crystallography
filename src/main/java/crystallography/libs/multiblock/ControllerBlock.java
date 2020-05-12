package crystallography.libs.multiblock;

import java.util.Set;

/**
 * Controller blocks exist as components of a multiblock and serve as the overseer or master. Logic which applies to the
 * entire multiblock structure should be handled by the ControllerBlock.
 *
 * Only one controllerBlock is allowed per multiblock structure.
 */
public abstract class ControllerBlock extends MultiBlockComponent {

    // A list containing all the multiblocks in this structure.
    // CLEANME We do this because recursion. If a block is in the set already, then it has already been visited. No additional visitation is required.
    // TODO ensure only valid IMultiBlockComponents are contained in this list.
    private Set<MultiBlockComponent> structure;

    public ControllerBlock(Properties properties) {
        super(properties);
    }

    // Only one controllerBlock is allowed per multiblock structure.
    public boolean isValid(Set<MultiBlockComponent> notMyStructure)
    {
        for(MultiBlockComponent comp : notMyStructure)
        {
            if(comp instanceof ControllerBlock)
                return false;
        }
        structure.add(this);
        return isValid();
    }
    // ControllerBlock will start the algorithm

}
