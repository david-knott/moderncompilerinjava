package Translate;

import Temp.Label;
import Temp.Temp;
import Tree.LABEL;
import Tree.Stm;

abstract public class Exp {
    abstract Tree.Exp unEx();

    abstract Tree.Stm unNx();

    abstract Tree.Stm unCx(Label t, Label f);
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
        throw new RuntimeException("Not implemented");
    }

}

class Nx extends Exp {

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

abstract class Cx extends Exp {

    @Override
    Tree.Exp unEx() {
        Label t = new Label();
        Label f = new Label();
        Temp r = new Temp();
        return new Tree.ESEQ(
                new Tree.SEQ(new Tree.MOVE(new Tree.TEMP(r), new Tree.CONST(1)), new Tree.SEQ(unCx(t, f),
                        new Tree.SEQ(new Tree.LABEL(f),
                                new Tree.SEQ(new Tree.MOVE(new Tree.TEMP(r), new Tree.CONST(0)), new Tree.LABEL(t))))),
                new Tree.TEMP(r));
    }

    abstract Tree.Stm unCtx(Label t, Label f);

    @Override
    Tree.Stm unNx() {
        throw new RuntimeException("Not implemented");
    }
}