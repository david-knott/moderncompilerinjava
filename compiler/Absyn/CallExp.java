package Absyn;

import Symbol.Symbol;

public class CallExp extends Exp implements Typable {
    public Symbol func;
    public ExpList args;
    public Absyn def;

    public CallExp(int p, Symbol f, ExpList a) {
        pos = p;
        func = f;
        args = a;
    }

    @Override
    public void accept(AbsynVisitor visitor) {
        visitor.visit(this);
    }

    public void def(Absyn exp) {
        this.def = exp;
    }
}
