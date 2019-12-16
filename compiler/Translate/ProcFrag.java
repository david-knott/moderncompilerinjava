package Translate;

/**
 * Function fragment which contains
 * the frame for the procedure, with information
 * about locals and variables. The body contains
 * the IL code, which is returned from the procEntryExit1
 */
public class ProcFrag  extends Frag{
    private Tree.Stm body;
    private Frame.Frame frame;
    
    public ProcFrag(Tree.Stm bdt, Frame.Frame frm){
        body = bdt;
        frame = frm;
    }
}