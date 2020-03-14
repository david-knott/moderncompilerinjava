package Codegen;

import Tree.Exp;
import Tree.ExpList;
import Tree.Stm;
import Tree.TEMP;

class TempNode extends TreeNode {

    public TempNode(String named) {
        super(named);
    }

    @Override
    public boolean isMatch(Exp exp) {
        if (exp instanceof TEMP) {
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
        return null;
    }
}