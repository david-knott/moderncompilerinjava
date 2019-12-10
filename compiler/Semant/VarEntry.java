package Semant;

class VarEntry extends Entry {

    //TODO: Implement
    Translate.Access access;
    Types.Type ty;
    boolean readOnly;

    VarEntry(Types.Type t) {
        ty = t;

    }
}
