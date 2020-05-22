package Tree;

/**
 * EmptyStm class, used for testing.
 */
public class EmptyStm extends Stm {

	private String title;

	public EmptyStm(String title) {
		this.title = title;
	}

	public EmptyStm() {
		this.title = "Default";
	}

	@Override
	public ExpList kids() {
		return null;
	}

	@Override
	public Stm build(ExpList kids) {
		return this;
	}

	@Override
	public void accept(TreeVisitor treeVisitor) {

	}

	public String toString() {
		return this.title;
	}

}