package Translate;

public class ExpTyList {
    public ExpTy expTy;
    public ExpTyList tail;

    public ExpTyList(ExpTy et, ExpTyList t) {
        expTy = et;
        tail = t;
    }
    
    public ExpTyList reverse(){
        return this;
    }
}