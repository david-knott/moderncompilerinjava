package Tree;

public class CALL extends Exp {
    public Exp func;
    public ExpList args;

    public CALL(Exp f, ExpList a) {
        func = f;
        args = a;
    }

    public ExpList kids() {
        return new ExpList(func, args);
    }

    public Exp build(ExpList kids) {
        return new CALL(kids.head, kids.tail);
    }

    @Override
    public void accept(TreeVisitor treeVisitor) {
        treeVisitor.visit(this);
    }

    @Override
    public int getOperator() {
        return TreeKind.CALL;
    }

    @Override
    public int getArity() {
        return this.args != null ? this.args.size() : 0;
    }

    @Override
    public IR getNthChild(int index) {
        return this.args.head;
    }

}
