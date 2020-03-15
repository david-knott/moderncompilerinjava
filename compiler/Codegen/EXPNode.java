package Codegen;

import Tree.EXP;
import Tree.Exp;
import Tree.ExpList;
import Tree.Stm;

class EXPNode extends TreeNode {

    public EXPNode(String named) {
        super(named);
    }

    @Override
    public boolean isMatch(Exp exp) {
        return false;
    }

    @Override
    public boolean isMatch(Stm stm) {
        return stm instanceof EXP;
    }

    @Override
    public ExpList kids(Stm stm) {
        EXP x = (EXP)stm;
        return new ExpList(x.exp, null);
    }

    @Override
    public ExpList kids(Exp exp) {
        return null;
    }
}