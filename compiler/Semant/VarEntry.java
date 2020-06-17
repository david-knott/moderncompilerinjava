package Semant;

class VarEntry extends Entry {

    Translate.Access access;
    Types.Type ty;
    boolean readonly = false;

    VarEntry(Types.Type t, Translate.Access acc) {
        ty = t;
        access = acc;
    }

    VarEntry(Types.Type t, Translate.Access acc, boolean ro) {
        ty = t;
        access = acc;
        readonly = ro;
    }
}
