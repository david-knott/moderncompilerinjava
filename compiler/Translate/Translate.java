package Translate;

import Temp.Label;
import Tree.BINOP;
import Tree.CONST;
import Tree.MEM;
import Tree.TEMP;

/**
 * Creates intermediate code, using frame information level and the ?
 */
public class Translate {

    private Frag frags;

    public Frag getResult() {
        return frags;
    }

    /**
     * This function has the side effect of remembering a ProcFrag
     */
    public void procEntryExit(Level level, Exp body) {

    }

    /**
     * Returns a IR of a simple variable. This comprises of an offset from the
     * framepointer + the frame pointers. This is wrapped in a a EX class
     * 
     * @param access
     * @param level
     * @return
     */
    public Exp simpleVar(Access access, Level level) {
        var treeExp = access.acc.exp(new TEMP(level.frame.FP()));
        return new Ex(treeExp);
    }

    public Exp subscriptVar(Access access, Level level) {
        throw new Error("Not implemeneted");
    }

    public Exp fieldVar(Access access, Level level) {
        throw new Error("Not implemeneted");
    }

    public Exp string(Label label, String literal) {
        return null;
    }

    /**
     * Translates formals at a level ???
     * 
     * @param level
     */
    void formals(Level level) {

    }
}
