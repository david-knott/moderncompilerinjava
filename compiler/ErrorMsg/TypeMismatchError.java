package ErrorMsg;

import Types.Type;

public class TypeMismatchError extends CompilerError{
    private final Type type1;
    private final Type type2;

    public TypeMismatchError(final int p, final Type t1) {
        super(p);
        type1 = t1;
        type2 = null;
    }

    public TypeMismatchError(final int p, final Type t1, final Type t2) {
        super(p);
        type1 = t1;
        type2 = t2;
    }

    public Type getType1(){
        return type1;
    }

    public Type getType2(){
        return type2;
    }
}