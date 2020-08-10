package Tree;

abstract public class Exp extends IR {
	abstract public ExpList kids();

	abstract public Exp build(ExpList kids);
}
