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
    public AccessList formals;

    public Level(Level prnt, Symbol name, BoolList fmls) {
        AccessList accessList = null;
        AccessList current = null;
        AccessList previous = null;
        for(var fml = fmls; fml != null; fml = fml.tail){
        }
        //add new formal parameter for static link to parent frame
        frame = prnt.frame.newFrame(new Label(), new BoolList(true, fmls));
    }

    // associate a frame with this level
    public Level(Frame.Frame f) {
        frame = f;
    }

    public Access allocLocal(boolean escape) {
        return new Access(this, frame.allocLocal(escape));
    }
}
