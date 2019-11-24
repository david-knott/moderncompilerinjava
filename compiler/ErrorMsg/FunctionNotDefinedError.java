package ErrorMsg;

import Symbol.Symbol;

public class FunctionNotDefinedError extends CompilerError{

    private final Symbol field;

    public FunctionNotDefinedError(final int p, final Symbol fld) {
        super(p);
        field = fld;
    }

    public Symbol getName() {
        return field;
    }
}