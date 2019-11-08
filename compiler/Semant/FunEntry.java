package Semant;

import Translate.ExpTy;
import java_cup.runtime.Symbol;
import Translate.Exp;
import Symbol.Table;

class FunEntry extends Entry {
    Types.RECORD formals;
    Types.Type result;

    public FunEntry(Types.RECORD f, Types.Type r) {
        formals = f;
        result = r;
    }
}

