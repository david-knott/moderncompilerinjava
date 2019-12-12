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
     * Returns a IR expression of a simple variable. This comprises of an offset
     * from the defining frames frame pointer. If this variable is declared at same
     * level this will be the associated framees frame pointer. If the variable is
     * defined in at a lower level, it wil be relative to that frame pointer, passed
     * using the chain of static links
     * 
     * @param access the variable and the level where it was defined
     * @param level  the level this variable is being accessed from
     * @return a translated expression
     */
    public Exp simpleVar(Access access, Level level) {
        var exp = staticLinkOffset(access, level);
        return new Ex(access.acc.exp(exp));
    }

    /**
     * Return the array element at index i. This can be found by
     * getting the mem at variable at offet k, this is a pointer
     * to the array memory location on the heap. 
     * The element will be at the memory location + i * word size
     * @param access
     * @param level
     * @return
     */
    public Exp subscriptVar(Access access, Level level) {
        var exp = staticLinkOffset(access, level);
        int i = 10;
        //tke memory value of exp and add k * i
        var x = new BINOP(BINOP.PLUS, new MEM(exp), new CONST( level.frame.wordSize() * i));
        return new Ex(x);
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

    private Tree.Exp staticLinkOffset(Access access, Level level) {
        // starting from the level where variable is used
        // we decent until we reach the level that
        // the variable is defined in. We build up
        // an expression as follows
        // item1 = MEM ( BINOP (k1, FP)) - 1 is level of usage
        // item2 = MEM ( BINOP (k2, item1))
        // item3 = MEM ( BINOP (k3, item2))
        // itemn-1 = MEM ( BINOP (kn-1, itemn-2))
        // itemn = MEM ( BINOP (kn, itemn-1)) - n is level of definition
        // Calculate position of static link relative to framepointer
        final int staticLinkOffset = 8;
        Tree.Exp exp = new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), new TEMP(level.frame.FP())));
        // Variable is defined in a stack frame beneath the current one
        var slinkLevel = level;
        while (slinkLevel != access.home) {
            exp = new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), exp));
            slinkLevel = slinkLevel.parent;
        }
        return exp;
    }
}
