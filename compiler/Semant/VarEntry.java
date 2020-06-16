package Semant;

class VarEntry extends Entry {

    Translate.Access access;
    Types.Type ty;

    VarEntry(Types.Type t, Translate.Access acc) {
        ty = t;
        access = acc;
    }
}
