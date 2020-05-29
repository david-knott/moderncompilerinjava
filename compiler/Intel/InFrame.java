package Intel;

import Frame.Access;
import Tree.BINOP;
import Tree.CONST;
import Tree.Exp;
import Tree.MEM;

class InFrame extends Access {
    int offset;

    InFrame(int os) {
        offset = os;
    }

    /**
     * Returns the memory access for this variable relative
     * to the frame pointer. If this access is being accessed
     * from a nested function, we use a frame pointer calculated
     * from the static links.
     */
    @Override
    public Exp exp(Exp framePtr) {
       return new MEM(new BINOP(BINOP.PLUS, framePtr, new CONST(offset)));
    }
}