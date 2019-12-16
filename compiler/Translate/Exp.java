package Translate;

import Temp.Label;
import Temp.Temp;
import Tree.Stm;

abstract public class Exp {
    abstract Tree.Exp unEx();

    abstract Tree.Stm unNx();

    abstract Tree.Stm unCx(Label t, Label f);
}

class Ex extends Exp {
    private final Tree.Exp exp;

    Ex(Tree.Exp e) {
        exp = e;
    }

    @Override
    Tree.Exp unEx() {
        return exp;
    }

    @Override
    Stm unNx() {
        return new Tree.EXP(exp);
    }

    @Override
    Stm unCx(Label t, Label f) {
        // labels t and f are used
        // the expresion evaluates to
        // 0, then go to label t
        // if expression evaultest to
        // 1 the go to label f
        return new Tree.CJUMP(Tree.CJUMP.EQ, this.unEx(), new Tree.CONST(0) , t, f);
    }
}

class Nx extends Exp {
    private final Tree.Stm stm;

    Nx(Tree.Stm s) {
        stm = s;
    }

    @Override
    Tree.Exp unEx() {
        throw new RuntimeException("Not implemented, this should never occur");
    }

    @Override
    Stm unNx() {
        return stm;
    }

    @Override
    Stm unCx(Label t, Label f) {
        throw new RuntimeException("Not implemented, this should never occur");
    }
}

abstract class Cx extends Exp {

    @Override
    Tree.Exp unEx() {
        Label t = new Label();
        Label f = new Label();
        Temp r = new Temp();
        //move const 1 into temp r
        //call conditional statement with labels
        //add label false
        //move const 0 into register r
        //add label true
        //create emit temp with register r
        return new Tree.ESEQ(
                new Tree.SEQ(new Tree.MOVE(new Tree.TEMP(r), new Tree.CONST(1)), 
                new Tree.SEQ(unCx(t, f),
                        new Tree.SEQ(new Tree.LABEL(f),
                                new Tree.SEQ(new Tree.MOVE(new Tree.TEMP(r), new Tree.CONST(0)), 
                                new Tree.LABEL(t))))),
                new Tree.TEMP(r));
    }

    @Override
    Tree.Stm unNx() {
        Label a = new Label();
        return new Tree.SEQ(unCx(a, a), new Tree.LABEL(a));
    }
}

class RelCx extends Cx {

    private Tree.Exp right;
    private Tree.Exp left;
    private int operator;

    RelCx(Tree.Exp r, Tree.Exp l, int op) {
        right = r;
        left = l;
        operator = op;
    }

    @Override
    Stm unCx(Label t, Label f) {
        return new Tree.CJUMP(operator, left, right, t, f);
    }
}

class IfThenElseExp extends Exp {

    Exp cond, a, b;
    Label t = new Label();
    Label f = new Label();
    Label join = new Label();

    IfThenElseExp(Exp cc, Exp aa, Exp bb) {
        cond = cc;
        a = aa;
        b = bb;
    }

    @Override
    Tree.Exp unEx() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    Stm unNx() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    Stm unCx(Label t, Label f) {
        throw new RuntimeException("Not implemented");
    }
}