package Codegen;

import Tree.Exp;
import Tree.ExpList;
import Tree.MEM;
import Tree.Stm;

class MemNode extends TreeNode {

    public MemNode(String named) {
        super(named);
    }

    @Override
    public boolean isMatch(Exp exp) {
        if (exp instanceof MEM) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isMatch(Stm stm) {
        return false;
    }

    @Override
    public ExpList kids(Stm stm) {
        throw new Error();
    }

    @Override
    public ExpList kids(Exp exp) {
        MEM tmp = (MEM)exp;
        return new ExpList(tmp.exp, null);
    }
}