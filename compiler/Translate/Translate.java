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
import Types.Type;

/**
 * Translates Abstract Syntax to IR and stores fragments for use 
 * in next phase of compileation.
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

    /**
     * Add a IR fragment as a side affect of generating the IR.
     * @param procFrag
     */
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
      * @param level the current static function level
      * @param body the body of the function we are translating
      */
    public void procEntryExit(Level level, Exp body) {
        if (body == null)
            return;
        if (body instanceof Ex) {
            var exp = new Nx(new MOVE(new TEMP(level.frame.RV()), body.unEx()));
            var statement = level.frame.procEntryExit1(exp.unNx());
            addFrag(new ProcFrag(statement, null));
        } else {
            var statement = level.frame.procEntryExit1(body.unNx());
            addFrag(new ProcFrag(statement, null));
        }
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
        var exp =  staticLinkOffset(access, level);
        return new Ex(access.acc.exp(exp));
    }

    /**
     * Fetches value from location, either temporary or from frame
     */
    public Exp varExp(ExpTy varEp){
        return varEp.exp;
    }

    /**
     * Return the array element at index i. This can be found by getting the mem at
     * variable at offet k, this is a pointer to the array memory location on the
     * heap. The element will be at the memory location + i * word size
     * @param translatedArrayVar
     * @param transIndexExp
     * @param level
     * @return
     */
    public Exp subscriptVar(ExpTy transIndexExp, ExpTy translatedArrayVar, Level level) {
        var baseExp = translatedArrayVar.exp.unEx();
        var indexExp = transIndexExp.exp.unEx();
        return new Ex(
            new MEM(
                new BINOP(
                    BINOP.PLUS, 
                    baseExp, 
                    new BINOP(
                        BINOP.MUL, 
                        indexExp, 
                        new CONST(level.frame.wordSize())
                    )

                )                
            )
        );
    }

    /**
     * Translates a field variable. Using the base reference and the field offset
     * this returns the memory location of the field. If its an int, this will be
     * the contents of the memory location, if its anything else, its the memory 
     * location
     * @param exp base reference of the record
     * @param fieldIndex the field index, based on the order of the fields
     * @param level the level that the record is being called
     * @return
     */
    public Exp fieldVar(Exp exp, int fieldIndex, Level level) {
        return new Ex(
            new MEM(
                new BINOP(
                    BINOP.PLUS, 
                    exp.unEx(), 
                    new CONST(
                        fieldIndex * level.frame.wordSize()
                    )
        
                )           
            )
        );
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
        return new Ex(new Tree.CONST(-1));
    }

    public Exp functionBody(Level level, ExpTy firstFunction) {
        return firstFunction.exp;
    }

    public Exp nil() {
        return new Ex(new CONST(0));
    }

    /**
     * Generates IR for a call function. Function actual parameters
     * are passed in as part of calling sequence which happens later.
     * @param callerLevel the function calling the callee
     * @param calleeLevel the function being called
     * @param functionLabel the function label for the callee function
     * @param expTyList the agument list of the callee function
     * @param result the return type of the callee function
     * @return
     */
    public Exp call(Level callerLevel, Level calleeLevel, Label functionLabel, ExpTyList expTyList, Type result) {
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
        Tree.Exp staticLink = null;
    //   callerLevel.frame
        int staticLinkOffset = 0;
        if(difference < 0){
            staticLink = new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), new TEMP(callerLevel.frame.FP())));
            while(difference < 0){
                staticLink = new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), staticLink));
                difference++;
            }
        } else if (difference > 0){
            staticLink = new TEMP(callerLevel.frame.FP());
        } else {
            //recursive call, use current frames static link
            staticLink = new MEM(
            new BINOP(
                BINOP.PLUS, 
                new CONST(staticLinkOffset), 
                new TEMP(callerLevel.frame.FP())
            )
        );
        
        }
        //add current frames frame pointer as parameter to call
        ExpList expList = new ExpList(staticLink, null);
        while(expTyList != null){
            expList.append(expTyList.expTy.exp.unEx());
            expTyList = expTyList.tail;
        }
        if(result.coerceTo(Semant.VOID)){
            return new Ex(new CALL(new NAME(functionLabel), expList));
        } else {
            return new Ex(new CALL(new NAME(functionLabel), expList));
        }
    }

    /**
     * Translates a sequence of expressions into IR. If a sequence can be used for
     * syntactic grouping or for a list of expressions with the last item as the
     * returned value 
     */
    public Exp seq(Level level, ExpTyList expTyList) {
        if (expTyList.expTy != null && expTyList.tail == null) {
            return expTyList.expTy.exp;
        }
        if(expTyList.last().expTy.ty != Semant.VOID){
            return new Ex(expSeq(level, expTyList));
        } else {
            return new Nx(stmSeq(level, expTyList));
        }
    }

    private Stm stmSeq(Level level, ExpTyList expTyList) {
        if(expTyList.expTy == null && expTyList.tail == null){
            return null;
        }
        var firstStm = expTyList.expTy.exp.unNx();
        if(expTyList.tail == null){
            return firstStm;
        }
        SEQ seq = new SEQ(firstStm, null);
        expTyList = expTyList.tail;
        while(expTyList != null){
            if(expTyList.tail == null){
                seq.right = expTyList.expTy.exp.unNx();
            }else{
                SEQ seq1 = new SEQ(expTyList.expTy.exp.unNx(), null);
                seq.right = seq1;
                seq = seq1;
            }
            expTyList = expTyList.tail;
        }
        return seq;
    }

    private Tree.Exp expSeq(Level level, ExpTyList expTyList) {
        if(expTyList.expTy == null && expTyList.tail == null){
            return null;
        }
        var firstEx = expTyList.expTy.exp;
        if(expTyList.tail == null){
            return firstEx.unEx();
        }
        if(expTyList.tail.tail == null){
            return new ESEQ(firstEx.unNx(), expTyList.tail.expTy.exp.unEx());
        }
        ESEQ eseq = null;
        //still a bug here if only 2 items in list
        SEQ seq = new SEQ(firstEx.unNx(), null);
        expTyList = expTyList.tail;
        while(expTyList != null){
            if(expTyList.tail == null){
                eseq = new ESEQ(seq, expTyList.expTy.exp.unEx());
            }else{
                if(expTyList.tail.tail == null){
                    seq.right = expTyList.expTy.exp.unNx();
                } else {
                    SEQ seq1 = new SEQ(expTyList.expTy.exp.unNx(), null);
                    seq.right = seq1;
                    seq = seq1;
                }
           }
            expTyList = expTyList.tail;
        }
        return eseq;
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

    private Stm fieldList(Temp recordPointer,ExpTyList expTyList, Level level){
        if(expTyList == null || expTyList.expTy == null){
            return Noop().unNx();
        }
        int total = 0;
        Stm first = new MOVE(
                        new MEM(
                            new BINOP(
                                BINOP.PLUS, 
                                new TEMP(recordPointer), 
                                new CONST(level.frame.wordSize() * total)
                            )
                        ),
            expTyList.expTy.exp.unEx()
        );
        if(expTyList.tail == null){
            return first; 
        }
        SEQ seq = new SEQ(first, null);
        expTyList = expTyList.tail;
        var prev = seq;
        while (expTyList != null) {
            total++;
            Stm fieldExp = 
                new MOVE(
                    new MEM(
                        new BINOP(
                            BINOP.PLUS, 
                            new TEMP(recordPointer), 
                            new CONST(level.frame.wordSize() * total)
                        )
                    ),
                    expTyList.expTy.exp.unEx()
                );
            if (expTyList.tail == null) {
                prev.right = fieldExp;
            } else {
                SEQ next = new SEQ(fieldExp, null);
                prev.right = next;
                prev = next;
            }
            expTyList = expTyList.tail;
        }
        return seq;
    }

    public Exp record(Level level, ExpTyList expTyList) {
        Temp recordPointer = new Temp();
        Stm stm = fieldList(recordPointer, expTyList, level);
        int total = 0;
        for (var s = expTyList; s != null; s = s.tail) total++;
        int size = level.frame.wordSize() * total;
        return new Ex(new ESEQ(
                new SEQ(new MOVE(new TEMP(recordPointer),
                        new CALL(new NAME(new Label("malloc")), new ExpList(new CONST(size), null))), stm),
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
        return new Nx(
            new MOVE(
       //         new MEM(
                    transVar.exp.unEx()
         //       )
         , 
                transExp.exp.unEx()
            )
        );
    }

    private Exp body(ExpTy expTy){
        if(expTy == null)
            return Noop();
        return expTy.exp;
    }

    private Stm declarations(ExpTyList decList){
        //null dec list or head is expTy
        if(decList == null || decList.expTy == null){
            return Noop().unNx();
        }
        Stm first = decList.expTy.exp.unNx();
        if(decList.tail == null){
            return first;
        }
        SEQ seq = new SEQ(first, null);
        decList = decList.tail;
        var prev = seq;
        while (decList != null) {
            if (decList.tail == null) {
                prev.right = decList.expTy.exp.unNx();
            } else {
                SEQ next = new SEQ(decList.expTy.exp.unNx(), null);
                prev.right = next;
                prev = next;
            }
            decList = decList.tail;
        }
        return seq;
    }

    /**
     * Translate a let expression into IR. We use noops
     * where a strcut
     * @param decList translated list of declarations
     * @param body translated body to function
     * @return
     */
    public Exp letE(ExpTyList decList, ExpTy body) {
        Stm decStatments = declarations(decList);
        Exp bodyExp = body(body);
        if(body.ty == Semant.VOID) {
            return new Nx(
                new SEQ(
                    decStatments,
                    bodyExp.unNx() 
                )
            );
        } else {
            return new Ex(
                new ESEQ(
                    decStatments,
                    bodyExp.unEx()
                )
            );
        }
    }

    /**
     * Returns a MEM object which represents a static link to
     * a variable in a frame higher up the stack. If the variable
     * is defined in the current stack frame, the static link 
     * will just refer to the current activation records frame pointer.
     * @param access
     * @param level
     * @return a Tree.Exp containing MEM expressions.
     */
    public Tree.Exp staticLinkOffset(Access access, Level level) {
        int staticLinkOffset = 0;
        Tree.Exp exp = new MEM(
            new BINOP(
                BINOP.PLUS, 
                new CONST(staticLinkOffset), 
                new TEMP(level.frame.FP())
            )
        );
        
        var slinkLevel = level;
        while (slinkLevel != access.home) {
            exp = new MEM(
                new BINOP(
                    BINOP.PLUS, 
                    new CONST(staticLinkOffset), 
                    exp
                )
            );
            slinkLevel = slinkLevel.parent;
        }
        return exp;
    }

	public Exp transDec(Level level, Access translateAccess, Exp exp) {
        return new Nx(
            new MOVE(
                simpleVar(translateAccess, level).unEx(), 
                exp.unEx()
            )
        );
	}
}