package Translate;

public class ExpTyList {
    public ExpTy expTy;
    public ExpTyList tail;

    public ExpTyList(ExpTy et, ExpTyList t) {
        if (et == null)
            throw new IllegalArgumentException("ExpTy et cannot be null");
        expTy = et;
        tail = t;
    }

    public ExpTyList(ExpTy et) {
        if (et == null)
            throw new IllegalArgumentException("ExpTy et cannot be null");
        expTy = et;
        tail = null;
    }

    public ExpTyList reverse() {
        var head = this;
        ExpTyList reversed = null;
        while (head != null) {
            reversed = new ExpTyList(head.expTy, reversed);
            head = head.tail;
        }
        return reversed;
    }

    public ExpTyList append(ExpTy et) {
        if (et == null)
            throw new IllegalArgumentException("ExpTy et cannot be null");
        var last = this;
        while (last.tail != null) {
            last = last.tail;
        }
        last.tail = new ExpTyList(et, null);
        return last.tail;
    }
}