package Types;

/**
 * This class represents the type of a function.
 */
public class FUNCTION extends Type {
	public final RECORD formals;
	public final Type result;

	public FUNCTION(RECORD formals, Type result) {
		this.formals = formals;
		this.result = result;
    }
    
    public boolean coerceTo(Type t) {
		return (t.actual() instanceof FUNCTION);
	}
}