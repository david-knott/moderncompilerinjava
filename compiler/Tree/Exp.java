package Tree;

abstract public class Exp {
	abstract public ExpList kids();

	abstract public Exp build(ExpList kids);

	abstract public void accept(TreeVisitor treeVisitor);
}
