package Translate;

import java.text.Format;

import Symbol.Symbol;
import Temp.Label;
import Util.BoolList;

/**
 * A new level is created for each function definition we encounter during
 * the type check phase. The level represents the static nesting level of the
 * function within the source code. Function nesting is represented as a tree
 * with the main entry method as the root of this tree. Each function will
 * have a level instance.
 * The frame member is the activate record layout for the associated function
 * The parent member is the parent level which contains this level.
 * The formals member is the formal argument list for this function. It is
 * used to indicate whether the arguments should be placed in memory or registers
 */
public class Level {
   
    Frame.Frame frame;
    Level parent;
    public AccessList formals;

    // associate a frame with this level
    public Level(Frame.Frame f) {
        frame = f;
    }

    /**
     * Creates a new static nesting level, using the supplied level as it parent.
     * The symbol name relates to the function that is linked to this level.
     * @param prnt
     * @param name
     * @param fmls
     */
    public Level(Level prnt, Symbol name, BoolList fmls) {
        parent = prnt;
        frame = prnt.frame.newFrame(new Label(name), new BoolList(true, fmls));
        Frame.AccessList frameFormals = frame.formals;
        for(; frameFormals != null; frameFormals = frameFormals.tail) {
            if(formals == null){
                formals = new AccessList(new Access(this, frameFormals.head));
            } else {
                formals = formals.append(new Access(this, frameFormals.head));
            }
        }
    }


    /**
     * Creates a local access within the frame for this level
     * @param escape should the variable escape or not
     * @return the new access
     */
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
