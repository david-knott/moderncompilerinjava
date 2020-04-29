package Tree;

abstract public class Stm {
	abstract public ExpList kids();

	abstract public Stm build(ExpList kids);

	abstract public void accept(TreeVisitor treeVisitor);
}