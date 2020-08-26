package Tree;

public abstract class IR {

    abstract public void accept(TreeVisitor treeVisitor);

    public abstract int getOperator();

    public abstract int getArity();

    public abstract IR getNthChild(int index);
}