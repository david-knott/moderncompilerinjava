package Types;

public class INT extends Type {
	private static String NAME = "int";

	public INT() {
	}

	public boolean coerceTo(Type t) {
		return (t.actual() instanceof INT);
	}

	public String toString() {
		return NAME;
	}
}
