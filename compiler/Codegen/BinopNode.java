package Codegen;

import Tree.BINOP;
import Tree.Exp;
import Tree.ExpList;
import Tree.Stm;

class BinopNode extends TreeNode {

    private TreeNodeValueFunction<BINOP> treeNodeValueFunction;

    public BinopNode(String named, TreeNodeValueFunction<BINOP> treeNodeValueFunction) {
        super(named);
        this.treeNodeValueFunction = treeNodeValueFunction;
    }

    public BinopNode(TreeNodeValueFunction<BINOP> treeNodeValueFunction) {
        this.treeNodeValueFunction = treeNodeValueFunction;
    }

    @Override
    public boolean isMatch(Exp exp) {
        if (exp instanceof BINOP) {
            if (this.treeNodeValueFunction.compareValue((BINOP) exp)) {
                return true;
            }
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
        BINOP tmp = (BINOP)exp;
        return new ExpList(tmp.left, new ExpList(tmp.right, null));
    }
}