package Semant;

class VarEntry extends Entry {

    //Translate.Access access;
    Types.Type ty;
    boolean readOnly;

    VarEntry(Types.Type t) {
        ty = t;

    }
}
