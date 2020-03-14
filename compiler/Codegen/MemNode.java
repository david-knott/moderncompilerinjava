package Codegen;

import Tree.Exp;
import Tree.ExpList;
import Tree.MEM;
import Tree.Stm;

class MemNode extends TreeNode {

    private TreeNodeValueFunction<MEM> treeNodeValueFunction;

    public MemNode(String named) {
        super(named);
    }

    public MemNode(TreeNodeValueFunction<MEM> treeNodeValueFunction, String named) {
        super(named);
        this.treeNodeValueFunction = treeNodeValueFunction;
    }

    @Override
    public boolean isMatch(Exp exp) {
        if (exp instanceof MEM) {
            if (this.treeNodeValueFunction.compareValue((MEM) exp)) {
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
        MEM tmp = (MEM)exp;
        return new ExpList(tmp.exp, null);
    }
}