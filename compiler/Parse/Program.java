package Parse;

import Absyn.DecList;
import Absyn.FunctionDec;
import Symbol.Symbol;

public class Program {

    public Absyn.Exp absyn;

    public Program(Absyn.Exp absyn) {
        this.absyn = absyn;
    }

    public Program(DecList decList) {
        throw new Error("Program(DecList) not implemented.");
    }

    /**
     * Returns the program as a sequence of declarations, wrapped in a function
     * call, which links into the runtime main.
     * @return
     */
    public DecList getDecList() {
        return new DecList(
            new FunctionDec(0, Symbol.symbol("_main"), null, null, this.absyn, null), 
            null
        );
    }
}