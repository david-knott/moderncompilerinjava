package ErrorMsg;

import Symbol.Symbol;

public class VariableAssignError extends CompilerError {

    private final Symbol symbol;
    public VariableAssignError(final int p, final Symbol sym) {
        super(p);
        symbol = sym;
    }

    public Symbol getSymbol(){
        return symbol;
    }
}