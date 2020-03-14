package Codegen;

import Tree.CALL;
import Tree.Exp;
import Tree.ExpList;
import Tree.Stm;

class CallNode extends TreeNode {

    public CallNode(String named) {
        super(named);
    }

    @Override
    public boolean isMatch(Exp exp) {
        if (exp instanceof CALL) {
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
        CALL call = (CALL)exp;
        return new ExpList(call.func, call.args);
    }
}