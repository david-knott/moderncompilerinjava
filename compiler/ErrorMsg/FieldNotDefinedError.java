package ErrorMsg;

import Symbol.Symbol;

public class FieldNotDefinedError extends CompilerError{

    private final Symbol field;

    public FieldNotDefinedError(final int p, final Symbol fld) {
        super(p);
        field = fld;
    }

    public Symbol getField() {
        return field;
    }
}