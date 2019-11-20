package Semant;

class VarEntry extends Entry {

    //Translate.Access access;
    Types.Type ty;

    VarEntry(Types.Type t) {
        ty = t;
    }
}
