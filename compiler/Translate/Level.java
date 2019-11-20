package Translate;

import Symbol.Symbol;
import Util.BoolList;

public class Level {
    Frame.Frame frame;
    public AccessList formals;
    public Level(Level parent, Symbol name, BoolList fmls){

    }
    public Level(Frame.Frame f){

    }
    public Access allocLocal(boolean escape){
        return null;
    }

}
