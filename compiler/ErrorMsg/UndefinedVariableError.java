package ErrorMsg;

import Symbol.Symbol;

public class UndefinedVariableError extends CompilerError {

    private final Symbol symbol;
    public UndefinedVariableError(final int p, final Symbol sym) {
        super(p);
        symbol = sym;
    }

    public Symbol getSymbol(){
        return symbol;
    }
    
}