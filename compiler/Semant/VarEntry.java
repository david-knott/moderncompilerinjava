package Semant;


class VarEntry extends Entry {

    Translate.Access access;
    Types.Type ty;
    boolean readOnly;

    VarEntry(Types.Type t, Translate.Access acc) {
        ty = t;
        access = acc;

    }
}
