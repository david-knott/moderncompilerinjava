package Translate;

import java.util.ArrayList;
import java.util.List;

import Symbol.Symbol;
import Temp.Label;
import Util.BoolList;

public class Level {
    // stack frame associate with this levels()
    Frame.Frame frame;
    Level parent;
    // formal function arguments for level
    //this includes a static link to the 
    //previous stack frame
    public AccessList formals;

    public Level(Level prnt, Symbol name, BoolList fmls) {
        AccessList accessList = null;
        AccessList current = null;
        AccessList previous = null;
        for(var fml = fmls; fml != null; fml = fml.tail){
        }
        frame = prnt.frame.newFrame(new Label(), fmls);
    }

    // associate a frame with this level
    public Level(Frame.Frame f) {
        frame = f;
    }

    public Access allocLocal(boolean escape) {
        return new Access(this, frame.allocLocal(escape));
    }
}
