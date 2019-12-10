package Translate;

import Temp.Label;
import Tree.Stm;

abstract public class Exp {
    abstract Tree.Exp unEx();

    abstract Tree.Stm unNx();

    abstract Tree.Stm unCx(Temp.Label t, Temp.Label f);
}


class IfThenElseExp extends Exp {
    Exp cond, a, b;
    Label t = new Label();
    Label f = new Label();
    Label join = new Label();

    IfThenElseExp(Exp cc, Exp aa, Exp bb)
    {
        cond = cc;
        a = aa;
        b = bb;
    }

    @Override
    Tree.Exp unEx() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    Stm unNx() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    Stm unCx(Label t, Label f) {
        // TODO Auto-generated method stub
        return null;
    }
}

class Ex extends Exp {

    @Override
    Tree.Exp unEx() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    Stm unNx() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    Stm unCx(Label t, Label f) {
        // TODO Auto-generated method stub
        return null;
    }

}

class Nx extends Exp {

    @Override
    Tree.Exp unEx() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    Stm unNx() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    Stm unCx(Label t, Label f) {
        // TODO Auto-generated method stub
        return null;
    }
    
}