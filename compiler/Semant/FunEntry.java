package Semant;

import Temp.Label;
import Translate.Level;

class FunEntry extends Entry {
    Translate.Level level;
    Temp.Label label;
    Types.RECORD formals;
    Types.Type result;

    public FunEntry(Translate.Level lev, Temp.Label lab, Types.RECORD f, Types.Type r) {
        formals = f;
        result = r;
        level = lev;
        label = lab;
    }

}

