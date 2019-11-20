package Semant;

class FunEntry extends Entry {
    //Translate.Level level;
    //Temp.Label label;
    Types.RECORD formals;
    Types.Type result;

    public FunEntry(Types.RECORD f, Types.Type r) {
        formals = f;
        result = r;
    }
}

