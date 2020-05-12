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
    // A block can't be added to a set twice!
    // CLEANME We do this because recursion.
    // TODO ensure only valid IMultiBlockComponents are contained in this list.
    private Set<MultiBlockComponent> structure;

    private boolean isController;

    public ControllerBlock(Properties properties) {
        super(properties);
        isController = true;
    }

    // Only one controllerBlock is allowed per multiblock structure.
    public boolean isValid(Set<MultiBlockComponent> notMyStructure)
    {
        // MultiBlockComponent b = new MultiBlockComponent();

        for(MultiBlockComponent comp : notMyStructure)
        {
            if(comp instanceof ControllerBlock)
                return false;
        }
        structure.add(this);
        return isControllerValid();
    }

    // ControllerBlock will start the algorithm
    // ControllerBlock will end the algorithm, and perform isValid() at that time

    /**
     * Contains the algorithm to validate this controller block.
     */
    public abstract boolean isControllerValid();
}
