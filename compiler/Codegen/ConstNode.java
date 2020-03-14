package Codegen;

import Tree.CONST;
import Tree.Exp;
import Tree.ExpList;
import Tree.Stm;

class ConstNode extends TreeNode {

    public ConstNode(String named) {
        super(named);
    }

    @Override
    public boolean isMatch(Exp exp) {
        if (exp instanceof CONST) {
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