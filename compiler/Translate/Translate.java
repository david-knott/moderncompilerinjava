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
        // TODO: Handle static links, where the variable is stored
        // in a frame beneath this one. We need the level the current
        // function is being used in and the level where the variable is
        // declared.

        // starting from the level of usage
        // we decent until we reach the level that
        // the accesss is defined at.
        // exp = MEM(+ ( CONST kn, MEM( +(CONST kn-1 ) )
        var slinkLevel = level;
        while (slinkLevel != access.home) {
            // produces a chain of MEM and BINOP nodes
            // to fetch stat links for all frames
            // need the static link offset.
            // get the current frames pointer and find the position
            // of the static link ?
            var framePtr = new TEMP(level.frame.FP());
            // TODO: i am assuming the first item is the static link
            if (level.formals != null) {

                var staticLinkExp = level.formals.head.acc.exp(framePtr);
            }
            slinkLevel = slinkLevel.parent;
        } 

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
