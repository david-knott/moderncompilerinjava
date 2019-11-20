package Translate;

import Symbol.Symbol;
import Util.BoolList;

public class Level {
    //stack frame associate with this level
    Frame.Frame frame;
    //formal function arguments for level
    public AccessList formals;
    public Level(Level parent, Symbol name, BoolList fmls){

    }

    //associate a frame with this level
    public Level(Frame.Frame f){

    }
    //allocate new local in the current frame
    //probaby called by accessing the symbol table
    //when a new variable is declared
    public Access allocLocal(boolean escape){
        return null;
    }

}
