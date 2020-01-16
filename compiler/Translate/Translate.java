package Translate;

import Semant.Semant;
import Temp.Label;
import Temp.Temp;
import Tree.BINOP;
import Tree.CALL;
import Tree.CONST;
import Tree.ESEQ;
import Tree.ExpList;
import Tree.JUMP;
import Tree.MEM;
import Tree.MOVE;
import Tree.NAME;
import Tree.SEQ;
import Tree.Stm;
import Tree.TEMP;

/**
 * Translates to IR and stores fragments for use after translation phase is
 * complete.
 */
public class Translate {

    private Frag frags;

    /**
     * Returns a linked list of fragments. This includes data fragments for strings,
     * and ProcFrag for functions. There is also a top level frag for the main tiger
     * implicit function call.
     * 
     * @return
     */
    public Frag getResult() {
        return frags;
    }

    public void addFrag(Frag procFrag){
        if (frags == null) {
            frags = procFrag;
        } else {
            procFrag.next = frags;
            frags = procFrag;
        }
    }

    /**
     * This function has the side effect of remembering a ProcFrag
     */
    public void procEntryExit(Level level, Exp body) {
        // body could be null if there
        // is a type checking errpr
        if (body == null)
            return;
        addFrag(new ProcFrag(body.unNx(), null));
        new Tree.Print(System.out).prStm(body.unNx());
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
        return new Ex(
                new BINOP(BINOP.PLUS, baseExp, new BINOP(BINOP.MUL, indexExp, new CONST(level.frame.wordSize()))));
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
        return new RelCx(transExpLeft.exp.unEx(), transExpRight.exp.unEx(), i);
    }

    public Exp integer(int value) {
        return new Ex(new CONST(value));
    }

    //TODO: String IR
    public Exp string(String literal, Level level) {
        Label label = new Label();
        var stringFragment = level.frame.string(label, literal);
        addFrag(new DataFrag(label, stringFragment));
        return new Ex(new NAME(label));
    }

    public Exp Noop() {
        return new Ex(new Tree.CONST(0));
    }

    public Exp functionBody(Level level, ExpTy firstFunction) {
        return firstFunction.exp;
    }

    //TODO: nil IR
    public Exp nil() {
        throw new Error();
    }

    public Exp call(Level callerLevel, Level calleeLevel, Label functionLabel, ExpTyList expTyList) {
        if (callerLevel == null)
            throw new IllegalArgumentException("Caller level cannot be null");
        if (calleeLevel == null)
            throw new IllegalArgumentException("Callee level cannot be null");
        if (functionLabel == null)
            throw new IllegalArgumentException("FunctionLabel cannot be null");
        //if difference is negative, callee level is less than calller level
        //which means the function being called has statically nested outside
        //the the callee
        //if difference is positive, the callee is statically nesed inside the
        //caller
        var difference = calleeLevel.depthDifference(callerLevel);
        if(difference < 0){
            //follow difference + 1 static links from callee
        } else if (difference > 0){
            //use the callers level ??
        } else {
            //recursive function call
            if(calleeLevel == callerLevel) {

            } else {
                //call to function at same level
            }
        }
        //add current frames frame pointer as parameter to call
        var exp = new MEM(
            new BINOP(
                BINOP.PLUS, 
                staticLinkOffset(
                    callerLevel, 
                    calleeLevel
                ),
                new CONST(0)
            )
        );
        ExpList expList = new ExpList(exp, null);
        while(expTyList != null){
            expList.append(expTyList.expTy.exp.unEx());
            expTyList = expTyList.tail;
        }
        return new Ex(new CALL(new NAME(functionLabel), expList));
    }

    /**
     * Translates a sequence of expressions into IR. If a sequence can be used for
     * syntactic grouping or for a list of expressions with the last item as the
     * returned value Note that the parameter exTyList is in reverse
     */
    public Exp seq(Level level, ExpTyList expTyList) {
        // list is reversed
        if (expTyList.tail == null) {
            return expTyList.expTy.exp;
        } else {
            //TODO: Check if loop through the list in reverse
            Stm seq = null;
            for (var e = expTyList; e != null; e = e.tail) {
                if (seq == null) {
                    seq = e.expTy.exp.unNx();
                } else {
                    seq = new SEQ(seq, e.expTy.exp.unNx());
                }
                if (e.tail == null) {
                    if (e.expTy.ty.coerceTo(Semant.VOID)) {
                        return new Nx(new SEQ(seq, e.expTy.exp.unNx()));
                    } else {
                        return new Ex(new ESEQ(seq, e.expTy.exp.unEx()));
                    }
                }
            }
            return new Nx(seq);
        }
    }

    public Exp array(Level level, ExpTy transSizeExp, ExpTy transInitExp) {
        ExpList args = new ExpList(transSizeExp.exp.unEx(), new ExpList(transInitExp.exp.unEx(), null));
        Temp arrayPointer = new Temp();
        return new Ex(
            new ESEQ(
                new MOVE(
                    new TEMP(arrayPointer), 
                    new CALL(
                        new NAME(
                            new Label("initArray")
                        ),
                        args 
                    )
                ), 
                new TEMP(arrayPointer)
            )
        );
    }

    public Exp record(Level level, ExpTyList expTyList) {
        Temp recordPointer = new Temp();
        SEQ initSubTreeSeq = null;
        int total = 0;
        // TODO: BUG HERE WHEN THERE IS ONLY ONE FIELD
        for (var s = expTyList; s != null; s = s.tail) {
            if (s.tail == null) {
                initSubTreeSeq.right = new MOVE(
                        new MEM(new BINOP(0, new TEMP(recordPointer), new CONST(level.frame.wordSize() * total))),
                        s.expTy.exp.unEx());
            } else {
                initSubTreeSeq = new SEQ(new MOVE(
                        new MEM(new BINOP(0, new TEMP(recordPointer), new CONST(level.frame.wordSize() * total))),
                        s.expTy.exp.unEx()), initSubTreeSeq);
            }
            total++;
        }
        int size = level.frame.wordSize() * total;
        return new Ex(new ESEQ(
                new SEQ(new MOVE(new TEMP(recordPointer),
                        new CALL(new NAME(new Label("malloc")), new ExpList(new CONST(size), null))), initSubTreeSeq),
                new TEMP(recordPointer)));
    }

    public Exp forE(Level level, Label loopEnd, ExpTy lowTy, ExpTy hiTy, ExpTy transBody) {
        return new Ex(new Tree.CONST(5));
    }

    public Exp whileL(Level level, Label loopEnd, ExpTy testExp, ExpTy transBody) {
        var whileStart = new Label();
        var loopStart = new Label();
        return new Nx(new SEQ(new Tree.LABEL(whileStart),
                new SEQ(testExp.exp.unCx(loopStart, loopEnd), new SEQ(new Tree.JUMP(loopStart), new Tree.SEQ(
                        new Tree.LABEL(loopStart),
                        new SEQ(transBody.exp.unNx(), new SEQ(new Tree.JUMP(whileStart), new Tree.LABEL(loopEnd))))))));
    }

    /**
     * Jumps to enclosing while loop end label
     **/
    public Exp breakE(Level level, Label loopEnd) {
        // if break is illegally nested loopend will be null
        if (loopEnd == null)
            return Noop();
        return new Nx(new JUMP(loopEnd));
    }

    public Exp ifE(Level level, ExpTy testExp, ExpTy thenExp, ExpTy elseExp) {
        return new IfThenElseExp(testExp.exp, thenExp.exp, elseExp.exp);
    }

    public Exp ifE(Level level, ExpTy testExp, ExpTy thenExp) {
        return new IfThenElseExp(testExp.exp, thenExp.exp, null);
    }

    /**
     * Assign the value in transExp into the location of transvar
     */
    public Exp assign(Level level, ExpTy transVar, ExpTy transExp) {
        return new Nx(new MOVE(new MEM(transVar.exp.unEx()), transExp.exp.unEx()));
    }

    /**
     * 
     */
    //TODO: Still a bug in this method
    public Exp letE(ExpTyList expTyList) {
        ExpTyList current = expTyList.reverse();
        //only one item in list
        if(current.tail == null){
            return new Ex(current.expTy.exp.unEx());
        }
        //split the list in first n -1 items and last n item
        var allExceptLast = expTyList.exceptLast();
        var last = expTyList.last();
        if(allExceptLast.tail == null){
            return new Ex(
                new ESEQ(
                    allExceptLast.expTy.exp.unNx(),
                    last.expTy.exp.unEx() 
                )
            );
        } else {
            var seq = new SEQ(allExceptLast.expTy.exp.unNx(), null);
            allExceptLast = allExceptLast.tail;
            while(allExceptLast != null){
                if(allExceptLast.tail == null) {
                    seq.right = allExceptLast.expTy.exp.unNx();
                } else {
                    seq = new SEQ(allExceptLast.expTy.exp.unNx(), seq);
                }
                allExceptLast = allExceptLast.tail;
            }
            return new Ex(
                new ESEQ(
                    seq,
                    last.expTy.exp.unEx() 
                )
            );
        }
    }

    
    private Tree.Exp staticLinkOffset(Level target, Level source) {
        final int staticLinkOffset = 0;
        Tree.Exp exp = new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), new TEMP(target.frame.FP())));
        var slinkLevel = source;
        while (slinkLevel != target) {
            exp = new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), exp));
            slinkLevel = slinkLevel.parent;
        }
        return exp;
    }

    private Tree.Exp staticLinkOffset(Access access, Level level) {
        final int staticLinkOffset = 0;
        Tree.Exp exp = new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), new TEMP(level.frame.FP())));
        var slinkLevel = level;
        while (slinkLevel != access.home) {
            exp = new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), exp));
            slinkLevel = slinkLevel.parent;
        }
        return exp;
    }

}
