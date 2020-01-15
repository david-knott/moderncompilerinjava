package Tree;

public class ExpList {
    public Exp head;
    public ExpList tail;

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
