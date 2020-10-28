package Parse;

import Absyn.DecList;
import Absyn.FunctionDec;
import Symbol.Symbol;

public class Program {
    public Absyn.Exp absyn;

    /**
     * Returns the program as a sequence of declarations.
     * @return
     */
    public DecList getDecList() {
        return new DecList(
            new FunctionDec(0, Symbol.symbol("_main"), null, null, this.absyn, null), 
            null
        );
    }

    public Program(Absyn.Exp absyn) {
        this.absyn = absyn;
    }
}