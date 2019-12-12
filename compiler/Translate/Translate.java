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
     * @param access the variable and the level where it was defined 
     * @param level the level this variable is being accessed from
     * @return a translated expression
     */
    public Exp simpleVar(Access access, Level level) {

        // starting from the level where variable is used
        // we decent until we reach the level that
        // the variable is defined in. We build up
        // an expression as follows
        // item1 = MEM ( BINOP (k1, FP)) - 1 is level of usage
        // item2 = MEM ( BINOP (k2, item1))
        // item3 = MEM ( BINOP (k3, item2))
        // itemn-1 = MEM ( BINOP (kn-1, itemn-2))
        // itemn = MEM ( BINOP (kn, itemn-1)) - n is level of definition
        
        //Calculate position of static link relative to framepointer
        //TODO: Is it correct to assume static is always at 8
        final int staticLinkOffset = 8;
        Tree.Exp exp =new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), new TEMP(level.frame.FP())));
        //Variable is defined in a stack frame beneath the current one
        var slinkLevel = level;
        while (slinkLevel != access.home) {
            exp = new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), exp));
            slinkLevel = slinkLevel.parent;
        } 
        return new Ex(access.acc.exp(exp));
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
