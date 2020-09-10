package Absyn;

import Symbol.Symbol;
import Types.Type;

public class CallExp extends Exp implements Typable {
    public Symbol func;
    public ExpList args;
    public Absyn def;
    private Type type;

    public CallExp(int p, Symbol f, ExpList a) {
        pos = p;
        func = f;
        args = a;
    }

    @Override
    public void accept(AbsynVisitor visitor) {
        visitor.visit(this);
    }

    public void setDef(Absyn exp) {
        this.def = exp;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
