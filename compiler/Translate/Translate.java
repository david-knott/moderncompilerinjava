package Translate;

import Tree.BINOP;
import Tree.CONST;
import Tree.MEM;
import Tree.TEMP;

/**
 * Translates to IR and stores fragments for use
 * after translation phase is complete.
 */
public class Translate {

    private Frag frags;

    /**
     * Returns a linked list of fragments. This includes
     * data fragments for strings, and ProcFrag for functions.
     * There is also a top level frag for the main tiger implicit
     * function call.
     * @return
     */
    public Frag getResult() {
        return frags;
    }

    /**
     * This function has the side effect of remembering a ProcFrag
     */
    public void procEntryExit(Level level, Exp body) {
        var procFrag = new ProcFrag(body.unNx(), null);
        if (frags == null) {
            frags = procFrag;
        } else {
           procFrag.next = frags;
           frags = procFrag;
        }
        // var statement1 = level.frame.procEntryExit1(body.unNx());
    }

    /**
     * Returns a IR expression of a simple variable. This comprises of an offset
     * from the defining frames frame pointer. If this variable is declared at same
     * level this will be the associated framees frame pointer. If the variable is
     * defined in at a lower level, it wil be relative to that frame pointer, passed
     * using the chain of static links
     * 
     * @param access the variable and the level where it was defined
     * @param level  the level this variable is being accessed from
     * @return a translated expression
     */
    public Exp simpleVar(Access access, Level level) {
        var exp = staticLinkOffset(access, level);
        return new Ex(access.acc.exp(exp));
    }

    /**
     * Return the array element at index i. This can be found by getting the mem at
     * variable at offet k, this is a pointer to the array memory location on the
     * heap. The element will be at the memory location + i * word size
     * 
     * @param translatedArrayVar
     * @param transIndexExp
     * @param access
     * @param level
     * @return
     */
    public Exp subscriptVar(ExpTy transIndexExp, ExpTy translatedArrayVar, Level level) {
        var baseExp = translatedArrayVar.exp.unEx();
        var indexExp = transIndexExp.exp.unEx();
        // subscript value is at the base array address
        // plus the array index multiplied by the word size
        var x = new BINOP(BINOP.PLUS, baseExp, new BINOP(BINOP.MUL, indexExp, new CONST(level.frame.wordSize())));
        return new Ex(x);
    }

    public Exp fieldVar(Exp exp, int fieldIndex, Level level) {
        return new Ex(new BINOP(BINOP.PLUS, exp.unEx(), new CONST(fieldIndex * level.frame.wordSize())));
    }

    public Exp binaryOperator(int i, ExpTy transExpLeft, ExpTy transExpRight) {
        return new Ex(new BINOP(i, transExpLeft.exp.unEx(), transExpRight.exp.unEx()));
    }

	public Exp relativeOperator(int i, ExpTy transExpLeft, ExpTy transExpRight) {
		return new RelCx(transExpLeft.exp.unEx(), transExpRight.exp.unEx(), i);
	}

	public Exp equalsOperator(int i, ExpTy transExpLeft, ExpTy transExpRight) {
		return null;
	}

    public Exp integer(int value) {
        return new Ex(new CONST(value));
    }

    public Exp string(String literal) {
        //create a data fragment ?
        return new Ex(new CONST(0));
    }

    public Exp Noop() {
        return new Ex(new Tree.CONST(0));
    }

    public Exp functionBody(Level level, ExpTy firstFunction) {
        return Noop();
    }

    public Exp transDec(Level level, Access translateAccess) {
        return Noop();
    }

    public Exp nil() {
        return Noop();
    }

    public Exp call(Level level) {
        return Noop();
    }

    public Exp seq(Level level, ExpTy expTy) {
        return Noop();
    }

    public Exp array(Level level, ExpTy transInitExp) {
        return Noop();
    }

    public Exp record(Level level, ExpTyList expTyList) {
        return Noop();
    }

    public Exp forE(Level level, ExpTy lowTy, ExpTy hiTy, ExpTy transBody) {
        return Noop();
    }

    public Exp whileL(Level level, ExpTy testExp, ExpTy transBody) {
        return Noop();
    }

    public Exp ifE(Level level, ExpTy testExp, ExpTy thenExp, ExpTy elseExp) {
        return Noop();
    }

    public Exp breakE(Level level) {
        return Noop();
    }

    public Exp fieldEList(Level level, ExpTy transExp) {
        return Noop();
    }

	public Exp assign(Level level, ExpTy transVar, ExpTy transExp) {
        return Noop();
    }

	public Exp ifE(Level level, ExpTy testExp, ExpTy thenExp) {
		return null;
	}
    private Tree.Exp staticLinkOffset(Access access, Level level) {
        // starting from the level where variable is used
        // we decent until we reach the level that
        // the variable is defined in. We build up
        // an expression as follows
        // item1 = MEM ( BINOP (k1, FP)) - 1 is level of usage
        // item2 = MEM ( BINOP (k2, item1))
        // item3 = MEM ( BINOP (k3, item2))
        // itemn-1 = MEM ( BINOP (kn-1, itemn-2))
        // itemn = MEM ( BINOP (kn, itemn-1)) - n is level of definition
        // Calculate position of static link relative to framepointer
        final int staticLinkOffset = 0;
        Tree.Exp exp = new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), new TEMP(level.frame.FP())));
        // Variable is defined in a stack frame beneath the current one
        var slinkLevel = level;
        while (slinkLevel != access.home) {
            exp = new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), exp));
            slinkLevel = slinkLevel.parent;
        }
        return exp;
    }
}