package ErrorMsg;

import Types.Type;

public class UndefinedTypeError extends CompilerError {

    private final Type symbol;
    public UndefinedTypeError(final int p, final Type sym) {
        super(p);
        symbol = sym;
    }

    public Type getType(){
        return symbol;
    }
    
}