package Bind;

import Absyn.Absyn;
import Types.Type;

class SymbolTableElement {
    final Type type;
    final Absyn exp;

    public SymbolTableElement(Type type) {
        this.type = type;
        this.exp = null;
    }

    public SymbolTableElement(Type type, Absyn exp) {
        this.type = type;
        this.exp = exp;
    }
}