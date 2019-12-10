package Translate;

import Temp.Label;
import Tree.BINOP;
import Tree.CONST;
import Tree.MEM;
import Tree.TEMP;

/**
 * Creates intermediate code, using frame information
 * level and the ?
 */
public class Translate {

    private Frag frags;

    public Frag getResult() {
        return frags;
    }

    /**
     * This function has the side effect of remembering
     * a ProcFrag
     */
    public void procEntryExit(Level level, Exp body) {

    }

    public Exp simpleVar(Access access, Level level) {
        
        /*
        var frameAcess = access.acc;
        int offset = 0;
        if(frameAcess instanceof InFrame){
        }
        new MEM(new BINOP(BINOP.PLUS, new TEMP(level.frame.FP()), new CONST(k)));
        */
        return null;
    }

    public Exp string(Label label, String literal){
        return null;
    }

    /**
     * Translates formals at a level
     * @param level
     */
    void formals(Level level){

    }
}
