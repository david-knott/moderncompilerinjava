package FindEscape;

abstract class Escape {
    int depth;

    abstract void setEscape();
}

class FormalEscape extends Escape {
    Absyn.FieldList fl;

    FormalEscape(int d, Absyn.FieldList f) {
        depth = d;
        fl = f;
        fl.escape = false;
    }

    void setEscape() {
        fl.escape = true;
    }
}

class VarEscape extends Escape {
    Absyn.VarDec vd;

    VarEscape(int d, Absyn.VarDec v) {
        depth = d;
        vd = v;
        vd.escape = false;
    }

    void setEscape() {
        vd.escape = true;
    }
}

public class FindEscape {

    Symbol.GenericTable<Escape> escEnv = new Symbol.GenericTable<Escape>();

    void traverseVar(int depth, Absyn.Var v) {

    }

    void traverseExp(int depth, Absyn.Exp e) {

    }

    void traverseDec(int depth, Absyn.Dec d) {

    }

    public FindEscape(Absyn.Exp e) {
        traverseExp(0, e);
    }
}
