package Translate;

import Frame.Frame;

/**
 * Function assembly fragment which also contains the frame for the procedure.
 * The frame layout contains information about locals and variables. The body
 * contains the IL code, which is returned from the procEntryExit1
 */
public class ProcFrag extends Frag {

    /**
     * Intermediate representation function body
     */
    public final Tree.Stm body;
    
    /**
     * Activation record for this function
     */
    public final Frame frame;

    /**
     * Initialises a new instance of a ProcFrag
     * 
     * @param bdt the function body
     * @param frm the activation record for this function
     */
    public ProcFrag(Tree.Stm bdt, Frame frm) {
        body = bdt;
        frame = frm;
    }

    @Override
    public Frame getFrame() {
        return this.frame;
    }

    @Override
    public void accept(FragmentVisitor fragmentVisitor) {
        fragmentVisitor.visit(this);
    }
}
