package Types;

public class VOID extends Type {

	public boolean coerceTo(Type t) {
		return (t.actual() instanceof VOID);
	}
}