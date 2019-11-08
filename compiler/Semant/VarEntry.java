package Semant;

import Translate.ExpTy;
import java_cup.runtime.Symbol;
import Translate.Exp;
import Symbol.Table;

class VarEntry extends Entry {
    Types.Type ty;

    VarEntry(Types.Type t) {
        ty = t;
    }
}

