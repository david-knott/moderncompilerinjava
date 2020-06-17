package Translate;

import Intel.IntelFrame;
import Semant.Semant;
import Temp.Label;
import Temp.Temp;
import Tree.BINOP;
import Tree.CALL;
import Tree.CJUMP;
import Tree.CONST;
import Tree.ESEQ;
import Tree.EXP;
import Tree.ExpList;
import Tree.JUMP;
import Tree.LABEL;
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
public class Translator {

    private FragList fragList;
    private boolean arrayBoundsCheck;
    private boolean nullRecordCheck;
    private boolean dumpGraphs;
    private boolean dumpAbsyn;
    private boolean dumpTree;
    private boolean dumpCanonicalTree;

    public Translator() {
        this.arrayBoundsCheck = false;
        this.nullRecordCheck = false;
        this.dumpGraphs = false;
        this.dumpAbsyn = false;
        this.dumpTree = false;
        this.dumpCanonicalTree = false;
    }

    public Translator(boolean arrayBoundsCheck, boolean nullRecordCheck) {
        this.arrayBoundsCheck = arrayBoundsCheck;
        this.nullRecordCheck = nullRecordCheck;
        this.dumpGraphs = false;
        this.dumpAbsyn = false;
        this.dumpTree = false;
        this.dumpCanonicalTree = false;

    }

    /**
     * Returns a linked list of fragments. This includes data fragments for strings,
     * and ProcFrag for functions. There is also a top level frag for the main tiger
     * implicit function call.
     * 
     * @return
     */
    public FragList getResult() {
        return this.fragList;
    }

    /**
     * Add a IR fragment as a side affect of generating the IR.
     * @param procFrag
     */
    public void addFrag(Frag procFrag){
        this.fragList = FragList.append(this.fragList, procFrag);
    }

    /**
     * This function saves a translated rocFrag to an internal
     * linked list. This linked list is used after translate
     * has completed and it passed to the later stages.
     * @param level the current static function level
     * @param body  the body of the function we are translating
     */
    public void procEntryExit(Level level, Exp body) {
        if (body == null)
            return;
        addFrag(new ProcFrag(level.frame.procEntryExit1(body.unNx()), level.frame));
    }

    /**
     * Returns an integer expression 
     * @param value
     * @return
     */
    public Exp integer(int value) {
        return new Ex(new CONST(value));
    }

    /**
     * Returns a string expression and generates
     * an assembly data fragment and stores in a
     * linked list of fragments.
     * @param literal
     * @param level
     * @return
     */
    public Exp string(String literal, Level level) {
        Label label = new Label();
        var stringFragment = level.frame.string(label, literal);
        addFrag(new DataFrag(stringFragment));
        return new Ex(new NAME(label));
    }

    /**
     * Returns a noop expression.
     * @return a noop expression.
     */
    public Exp Noop() {
        return new Ex(new CONST(0));
    }

    /**
     * Returns a nil expression.
     * @return a nill expression.
     */
    public Exp nil() {
        return new Ex(new CONST(0));
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
    private Tree.Exp staticLinkOffset(Access access, Level level) {
        //variable is defined at same level as use,
        //just return the fp as framePointer
        Tree.Exp exp = new TEMP(level.frame.FP());
        if(level == access.home){
            return exp;
        } else {
            //get current frames static link ( in rbp - 8),
            //lookup value, which is a pointer to the previous
            //frames static link, etc
            var slinkLevel = level;
            int staticLinkOffset = -8;
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
     * @return the {@link Translate.Exp translated expression}
     */
    public Exp varExp(ExpTy varEp){
        return varEp.exp;
    }

    /**
     * Allocates an array in the heap. Returns a pointer to the
     * base address. The base address contains the size of the array
     * stored as a word. The elements are stored contigously after the
     * size
     */
    public Exp array(Level level, ExpTy transSizeExp, ExpTy transInitExp) {
        if(!this.arrayBoundsCheck) {
            Temp arrayPointer = Temp.create();
            ExpList args = new ExpList(
                transSizeExp.exp.unEx(), 
                new ExpList(
                    transInitExp.exp.unEx(), 
                    null
                )
            );
            return new Ex(
                new ESEQ(
                    new MOVE(
                        new TEMP(arrayPointer), 
                        level.frame.externalCall("initArray", args) /* return a call tree */
                    ), 
                    new TEMP(arrayPointer)
                )
            );
        } else {
            ExpList args = new ExpList(
                new BINOP(
                    BINOP.PLUS, 
                    transSizeExp.exp.unEx(), 
                    new CONST(1)
                ), 
                new ExpList(
                    transInitExp.exp.unEx(), 
                    null
                )
            );
            Temp arrayPointer = Temp.create();
            return new Ex(
                new ESEQ(
                    new MOVE(
                        new TEMP(arrayPointer), 
                        level.frame.externalCall("initArray", args) /* return a call tree */
                    ), 
                    new TEMP(arrayPointer)
                )
            );
        }
    }

    /**
     * Return the array element at index i. 
     * This can be found by getting the mem at variable at offet k, 
     * this is a pointer to the array memory location on the
     * heap. The element will be at the memory location + i * word size
     * First word contains the size of the array
     * Elements are at word * 2, word * 3, word * 4
     * @param translatedArrayVar
     * @param transIndexExp
     * @param level
     * @return
     */
    public Exp subscriptVar(ExpTy transIndexExp, ExpTy translatedArrayVar, Level level) {
        var baseExp = translatedArrayVar.exp.unEx();
        var indexExp = transIndexExp.exp.unEx();
        var gotoSegFault = new Label();
        var gotoAnd = new Label();
        var gotoSubscript = new Label();
        if(!this.arrayBoundsCheck) {
            return new Ex(new MEM(
                new BINOP(
                    BINOP.PLUS, 
                    baseExp, 
                    new BINOP(
                        BINOP.MUL, 
                        indexExp,
                        new CONST(level.frame.wordSize() * 2)
                    )
                )                
            ));
        } else {
            var check = new ESEQ(
                new SEQ(
                    new CJUMP(
                        CJUMP.GE, 
                        indexExp, 
                        new MEM(
                            baseExp
                        ),
                        gotoSegFault,
                        gotoAnd
                    ),
                    new SEQ(
                        new LABEL(gotoAnd),
                        new SEQ(
                            new CJUMP(
                                CJUMP.LT, 
                                indexExp, 
                                new CONST(0),
                                gotoSegFault,
                                gotoSubscript
                            ),
                            new SEQ(
                                new LABEL(gotoSegFault),
                                new SEQ(
                                    new MOVE(
                                        new MEM(new CONST(0)),
                                        new CONST(0) /* TODO: assembly is a bit weird */
                                    ),
                                    new LABEL(gotoSubscript) /* not needed as a seg fauly will happen */
                                )
                            )
                        )
                    )
                ),
                new MEM(
                    new BINOP(
                        BINOP.PLUS, 
                        baseExp, 
                        new BINOP(
                            BINOP.MUL, 
                            new BINOP(
                                BINOP.PLUS,
                                indexExp, 
                                new CONST(1)
                            ),
                            new CONST(level.frame.wordSize())
                        )
                    )                
                )
            );
            return new Ex(check);
        }
    }

    /**
     * Initialises a new record in the heap.
     * @param level
     * @param expTyList
     * @return
     */
    public Exp record(Level level, ExpTyList expTyList) {
        Temp recordPointer = IntelFrame.rax;//new Temp();
        //Temp recordPointer = new Temp();
        Stm stm = fieldList(recordPointer, expTyList, level);
        int total = 0;
        for (var s = expTyList; s != null; s = s.tail) total++;
        int size = level.frame.wordSize() * total;
        return new Ex(new ESEQ(
            new SEQ(
                new MOVE(
                    new TEMP(recordPointer),
                    level.frame.externalCall(
                        "initRecord", 
                        new ExpList(
                            new CONST(size), 
                            null
                        )
                    )
                ), 
                stm
            ),
            new TEMP(recordPointer)));
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

    /**
     * Translates a field variable. Using the base reference and the field offset
     * this returns the value at the computed memory location. 
     * This method checks if the base reference is nil before dereferencing the field.
     * If the value at the base reference is null we move 0 to the 
     * 0 memory reference to cause a segmentation fault
     * @param exp base reference of the record
     * @param fieldIndex the field index, based on the order of the fields
     * @param level the level that the record is being called
     * @return
     */
    public Exp fieldVar(Exp exp, int fieldIndex, Level level) {
        var gotoSegFault = new Label();
        var gotoSubscript = new Label();
        if(this.nullRecordCheck) {
            return new Ex(
                new ESEQ(
                    new SEQ(
                        new CJUMP(
                            CJUMP.EQ, 
                            new MEM(
                                exp.unEx()
                            ), 
                            new CONST(0), 
                            gotoSegFault, 
                            gotoSubscript
                        ),
                        new SEQ(
                            new LABEL(gotoSegFault),
                            new SEQ(
                                new MOVE( /* triggers a seg fault */
                                    new MEM(new CONST(0)),
                                    new CONST(0)
                                ),
                                new LABEL(gotoSubscript)
                            )
                        )
                    ),
                    new MEM(
                        new BINOP(
                            BINOP.PLUS, 
                            exp.unEx(), 
                            new CONST(
                                fieldIndex * level.frame.wordSize()
                            )
                        )           
                    )
                )
            );
        } else {
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
    }

    public Exp equalsOperator(int i, ExpTy transExpLeft, ExpTy transExpRight) {
        return new RelCx(transExpLeft.exp.unEx(), transExpRight.exp.unEx(), i);
    }

    public Exp binaryOperator(int i, ExpTy transExpLeft, ExpTy transExpRight) {
        return new Ex(new BINOP(i, transExpLeft.exp.unEx(), transExpRight.exp.unEx()));
    }

    public Exp relativeOperator(int i, ExpTy transExpLeft, ExpTy transExpRight) {
        if(transExpLeft == null)
            throw new Error("transExpLeft is null");
        if(transExpRight == null)
            throw new Error("transExpRight is null");
         if(transExpLeft.exp == null)
            throw new Error("transExpLeft.exp is null");
        if(transExpRight.exp == null)
            throw new Error("transExpRight.exp is null");
            
        return new RelCx(transExpLeft.exp.unEx(), transExpRight.exp.unEx(), i);
    }

    /**
     * Returns a translated function body. If the function returns
     * a value, an additional move is appended to move the expression
     * result into the RV register.
     * @param level the static nesting level of the function
     * @param firstFunction the function body expression and return type
     * @return an expression
     */
    public Exp functionBody(Level level, ExpTy firstFunction) {
        if(!firstFunction.ty.coerceTo(Semant.VOID)) {
            return new Nx(new MOVE(new TEMP(level.frame.RV()), firstFunction.exp.unEx()));
        } else {
            return firstFunction.exp;
        }
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
    public Exp call(boolean useStaticLink, Level callerLevel, Level calleeLevel, Label functionLabel, ExpTyList expTyList, Type result) {
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
        //ExpList expList = new ExpList(staticLink, null);
        ExpList expList = null;
        if(useStaticLink) {
            expList = ExpList.append(expList, staticLink);
        }
        while(expTyList != null){
            expList = ExpList.append(expList, expTyList.expTy.exp.unEx());
            expTyList = expTyList.tail;
        }
        //TODO: Why are these the same
        if(result.coerceTo(Semant.VOID)){
            return new Ex(new CALL(new NAME(functionLabel), expList));
          //  return new Nx(new EXP(new CALL(new NAME(functionLabel), expList)));
        } else {
            return new Ex(new CALL(new NAME(functionLabel), expList));
          //  return new Nx(new MOVE(new TEMP(callerLevel.frame.RV()), new CALL(new NAME(functionLabel), expList)));
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
            return new Ex(expSeq(expTyList));
        } else {
            return new Nx(buildSeq(expTyList));
        }
    }

    /**
     * Returns a Tree Stm from a Translated Expression List.
     * If there is only one Translated Expression in the list
     * that expression is returned as a statement. If there is
     * more than one item, a Tree Seqence is returned. This function
     * calls itself recursivly.
     * @param expTyList
     * @return
     */
    private Tree.Stm buildSeq(ExpTyList expTyList) {
        //shouldn't happen
        if(expTyList.expTy == null) {
            return null;
        }
        if(expTyList.tail != null) {
            return new SEQ(expTyList.expTy.exp.unNx(), buildSeq(expTyList.tail));
        }
        return expTyList.expTy.exp.unNx();
    }

    /**
     * Returns a new ESEQ ( Expression Sequence), where the statements
     * are evaluated first and the the expression is returned. If there is
     * only one item in the list, that item is returned as an expression. 
     * If there is more than one iten in the list, we create an ESEQ with the
     * first n - 1 items as statements and the nth item as a expression
     * @param level
     * @param expTyList
     * @return an tree expression
     */
    private Tree.Exp expSeq(ExpTyList expTyList) {
        //invariant check, shouldn't happen
        if(expTyList.expTy == null && expTyList.tail == null){
            return null;
        }
        //only one item in list, so just return it as expression
        var firstEx = expTyList.expTy.exp;
        if(expTyList.tail == null){
            return firstEx.unEx();
        }
        //only two items in list so, return first and last
        if(expTyList.tail.tail == null) {
            expTyList = expTyList.tail;
            return new ESEQ(firstEx.unNx(), expTyList.expTy.exp.unEx());
        }
        //more than 2
        //build list with n - 1 items
        ExpTyList allExceptLast = new ExpTyList(expTyList.expTy);
        expTyList = expTyList.tail;
        for(; expTyList != null; expTyList = expTyList.tail) {
            //the last item is next, update reference to expTypList
            //and exit the loop
            if(expTyList.tail == null) {
                break;
            }
            allExceptLast = ExpTyList.append(allExceptLast, expTyList.expTy);
        }
        //using the sequence and the last item build eseq
        ExpTy last = expTyList.expTy;
        Stm statement = this.buildSeq(allExceptLast);
        return new ESEQ(statement, last.exp.unEx());
   }

    

    /**
     * Returns IR for a while loop
     * @param level the static nesting level this while loop is at
     * @param loopEnd the label used to break out of the lop
     * @param testExp the expression that is tested at the start of each loop
     * @param transBody the translated body expression
     * @return
     */
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
                transVar.exp.unEx(), 
                transExp.exp.unEx()
            )
        );
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

    private Exp body(ExpTy expTy){
        if(expTy == null)
            return Noop();
        return expTy.exp;
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
     * Translates a variable declation. This is generally
     * a move from an source expression to a destination
     * which may be a temporary or a memory location depending
     * on whether the variable escapes.
     * @param level
     * @param translateAccess
     * @param exp
     * @return a translated expression.
     */
	public Exp transDec(Level level, Access translateAccess, Exp exp) {
        return new Nx(
            new MOVE(
                simpleVar(translateAccess, level).unEx(), 
                exp.unEx()
            )
        );
	}

    /**
     * Removes static link from formal list, used by Semant
     * @param formals
     * @return an access linked list
     */
	public AccessList stripStaticLink(AccessList formals) {
		return formals != null ? formals.tail : null;
	}
}