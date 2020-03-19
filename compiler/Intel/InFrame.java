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

    @Override
    public Exp exp(Exp framePtr) {
        return new MEM(new BINOP(BINOP.PLUS, framePtr, new CONST(offset)));
    }
}