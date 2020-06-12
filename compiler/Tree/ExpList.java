package Tree;

public class ExpList {

    public static ExpList append(ExpList me, Exp t) {
        if (me == null) {
            return new ExpList(t);
        }
        if (me.tail == null) {
            return new ExpList(me.head, new ExpList(t));
        }
        return new ExpList(me.head, ExpList.append(me.tail, t));
    }

    public Exp head;
    public ExpList tail;

    public ExpList(Exp h) {
        head = h;
        tail = null;
    }

    public ExpList(Exp h, ExpList t) {
        head = h;
        tail = t;
    }

    public ExpList append(Tree.Exp exp) {
        if (exp == null)
            throw new IllegalArgumentException("Exp cannot be null");
        var last = this;
        while (last.tail != null) {
            last = last.tail;
        }
        last.tail = new ExpList(exp, null);
        return last.tail;
    }
}
