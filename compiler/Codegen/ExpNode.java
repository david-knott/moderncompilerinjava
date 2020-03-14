package Codegen;

import Tree.Exp;
import Tree.ExpList;
import Tree.Stm;

class ExpNode extends TreeNode {

    public ExpNode(String named) {
        super(named);
    }

    @Override
    public boolean isMatch(Exp exp) {
        return true;
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
        return new ExpList(exp, null);
    }
}