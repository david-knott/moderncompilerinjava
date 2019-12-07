package Translate;

import Intel.IntelFrame;
import Symbol.Symbol;
import Temp.Label;
import Util.BoolList;

public class Level {
    // stack frame associate with this level
    Frame.Frame frame;
    Level parent;
    // formal function arguments for level
    public AccessList formals;

    public Level(Level prnt, Symbol name, BoolList fmls) {
        frame = prnt.frame.newFrame(new Label(), fmls);
    }

    // associate a frame with this level
    public Level(Frame.Frame f) {
        frame = f;
    }

    // allocate new local variable in the current frame
    // probaby called by accessing the symbol table
    // when a new variable is declared
    public Access allocLocal(boolean escape) {
        frame.allocLocal(escape);
        return null;
    }

}
