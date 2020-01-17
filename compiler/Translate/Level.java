package Translate;

import Symbol.Symbol;
import Temp.Label;
import Util.BoolList;

/**
 * A new level is created for each function definition we encounter during
 * the type check phase. 
 */
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

    /**
     * Returns true if level is an ancestor.
     * @param level
     * @return true is level is ancestor otherwise false
     */
    public boolean ancestor(Level level){
        var start = level;
        //start with this, follow parent link
        //check if level is equal to parent
        while(start != null){
            if(start == this)
                return true;
            start = start.parent;
        }
        return false;
    }

    /**
     * Returns true if level is a descendant.
     * @param level
     * @return true is level is descendant otherwise false
     */
    public boolean descendant(Level level){
        var start = this;
        while(start != null){
            if(start == level)
                return true;
            start = start.parent;
        }
        return false;
    }

    /**
     * Returns difference in depth. If
     * level is an ancestor of of this, a negative
     * value is returned.
     * If level is a descendant of this, a positive
     * value is return,
     * If both levels are equal, a zero is returned
     * @param level
     * @return depth difference between levels 
     */
    public int depthDifference(Level level){
        Level start = null;
        Level end = null;
        int sign = 1;
        if(ancestor(level)){
            end = this;
            start = level;
            sign = -1;
        } else if(descendant(level)){
            start = this;
            end = level;
            sign = 1;
        } else {
            return 0;
        }
        int depth = 0;
        while(start != null){
            if(start == end) {
                break;
            }
            depth++;
            start = start.parent;
        }
        return sign * depth;
    }
}
