package Translate;

import Symbol.Symbol;
import Temp.Label;
import Util.BoolList;

public class Level {
    // stack frame associate with this levels()
    Frame.Frame frame;
    Level parent;
    public AccessList formals;

    public Level(Level prnt, Symbol name, BoolList fmls) {
        System.out.println("Level(Level,Symbol,BoolList):" + this.toString());
        //TODO: Ensure formal ordering is correct
        parent = prnt;
        AccessList current = null;
        AccessList previous = null;
        // add new formal parameter for static link to parent frame
        frame = prnt.frame.newFrame(new Label(), new BoolList(true, fmls));
        //get the formals from the stack and add their access to translate acccess
        Frame.AccessList frameFormals = frame.formals;
        for (var fml = fmls; fml != null; fml = fml.tail) {
            if(formals == null){
                formals = new AccessList(new Access(prnt, frameFormals.head));
                current = formals;
            } else {
                previous = current;
                current = new AccessList(new Access(prnt, frameFormals.head));
                previous.tail = current;
            }
            frameFormals = frameFormals.tail;
        }
    }

    // associate a frame with this level
    public Level(Frame.Frame f) {
        System.out.println("Level(Frame):" + this.toString());
        frame = f;
    }

    public Access allocLocal(boolean escape) {
        return new Access(this, frame.allocLocal(escape));
    }
}
