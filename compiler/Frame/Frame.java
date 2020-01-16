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
     * For each incoming register parameter, move it to the place from which it is seen from within the function. 
     * This could be a fresh temporary. One good way to handle this is for newFrame to create a sequence of 
     * Tree.MOVE statements as it creates all the formal parameter "accesses." newFrame can put this into the 
     * frame data structure, and procEntryExit1 can just concatenate it onto the procedure body.
     * Also concatenated to the body are statements for saving and restoring of callee-save registers 
     * (including the return-address register). If your register allocator does not implement spilling, 
     * all the callee-save (and return-address) registers should be written to the frame at the beginning 
     * of the procedure body and fetched back afterward. 
     * Therefore, procEntryExit1 should call allocLocal for each register to be saved, 
     * and generate Tree.MOVE instructions to save and restore the registers. 
     * With luck, saving and restoring the callee-save registers will give the register allocator 
     * enough headroom to work with, so that some nontrivial programs can be compiled. 
     * Of course, some programs just cannot be compiled without spilling.
     * 
     * If your register allocator implements spilling, then the callee-save registers should not 
     * always be written to the frame. 
     * Instead, if the register allocator needs the space, it may choose to spill only some 
     * of the callee-save registers. But "precolored" temporaries are never spilled; 
     * so procEntryExit1 should make up new temporaries for each callee-save (and return-address) 
     * register. On entry, it should move all these registers to their new temporary locations, 
     * and on exit, it should move them back. Of course, these moves (for nonspilled registers) 
     * will be eliminated by register coalescing, so they cost nothing.
     */
    abstract public Tree.Stm procEntryExit1(Tree.Stm body);

    /**
     * Calls an external function outside of Tiger
     */
    abstract public Tree.Exp externalCall(String func, Tree.ExpList args);

    abstract public Tree.Exp string(Label l, String literal);
}
