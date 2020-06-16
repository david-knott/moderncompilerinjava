package Translate;

import Types.Type;

public class ExpTy {

    public static ExpTy ERROR = new ExpTy();

    public Exp exp;
    public Type ty;

    private ExpTy() {
        this.exp = null;
        this.ty = null;
    }

    public ExpTy(Exp e, Type t) {
        exp = e;
        ty = t;
    }
}

