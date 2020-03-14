package Codegen;

import Tree.Exp;
import Tree.ExpList;
import Tree.MOVE;
import Tree.Stm;

class MoveNode extends TreeNode {

    public MoveNode(String named) {
        super(named);
    }

    @Override
    public boolean isMatch(Exp exp) {
        return false;
    }

    @Override
    public boolean isMatch(Stm stm) {
        if (stm instanceof MOVE) {
            return true;
        }
        return false;
    }

    @Override
    public Tree.ExpList kids(Stm stm){
        MOVE move = (MOVE)stm;
        return new Tree.ExpList(move.src, new Tree.ExpList(move.dst, null));
    }

    @Override
    public ExpList kids(Exp exp) {
        throw new Error();
    }
}