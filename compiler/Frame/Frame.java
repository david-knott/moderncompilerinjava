package Frame;

import Temp.Label;
import Temp.Temp;

public abstract class Frame {
    public Label name;
    public AccessList formals;

    /**
     * Frame pointer register
     * @return a temporary register
     */
    abstract public Temp FP();

    /**
     * Return the value register. This is the
     * register where a function puts its return
     * value
     * @return a temporary register
     */
    abstract public Temp RV();

    /**
     * Word size of the machine in bytes.
     * @return wordsize in bytes
     */
    abstract public int wordSize();

    /**
     * Creates a new frame for a function with label name
     * and formal list formals 
     * @param name the name of the function
     * @param formals the formal argument list
     * @return a new Frame
     */
    abstract public Frame newFrame(Label name, Util.BoolList formals);

    /**
     * Allocate a new local variable in a register or the
     * current frame
     * @param escape
     * @return the variable access
     */
    abstract public Access allocLocal(boolean escape);

    /**
     * Handles view shift in functions. This function
     * is used in generating the code for functions
     */
    abstract public Tree.Stm procEntryExit1(Tree.Stm body);

    /**
     * Calls an external function outside of Tiger
     */
    abstract public Tree.Exp externalCall(String func, Tree.ExpList args);

    abstract public Tree.Exp string(Label l, String literal);
}