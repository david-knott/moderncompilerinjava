package Tree;

abstract public class Stm {
	abstract public ExpList kids();

	abstract public Stm build(ExpList kids);

	abstract void accept(TreeVisitor treeVisitor);
}
