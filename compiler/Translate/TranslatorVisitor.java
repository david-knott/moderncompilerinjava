package Translate;

import java.util.Hashtable;

import Absyn.ArrayExp;
import Absyn.AssignExp;
import Absyn.BreakExp;
import Absyn.CallExp;
import Absyn.DecList;
import Absyn.DefaultVisitor;
import Absyn.FieldExpList;
import Absyn.FieldList;
import Absyn.FieldVar;
import Absyn.ForExp;
import Absyn.FunctionDec;
import Absyn.IfExp;
import Absyn.IntExp;
import Absyn.LetExp;
import Absyn.NilExp;
import Absyn.OpExp;
import Absyn.RecordExp;
import Absyn.SeqExp;
import Absyn.SimpleVar;
import Absyn.StringExp;
import Absyn.SubscriptVar;
import Absyn.VarDec;
import Absyn.VarExp;
import Absyn.WhileExp;
import Intel.IntelFrame;
import Temp.Label;
import Temp.Temp;
import Tree.BINOP;
import Tree.CALL;
import Tree.CONST;
import Tree.ESEQ;
import Tree.EXP;
import Tree.ExpList;
import Tree.JUMP;
import Tree.MEM;
import Tree.MOVE;
import Tree.NAME;
import Tree.Stm;
import Tree.TEMP;
import Types.Constants;
import Util.BoolList;

class TranslatorVisitor extends DefaultVisitor {

    private Hashtable<VarDec, Access> functionAccesses = new Hashtable<VarDec, Access>();
    private Hashtable<Absyn.Absyn, Label> functionLabels = new Hashtable<Absyn.Absyn, Label>();
    private Hashtable<FunctionDec, Level> functionLevels = new Hashtable<FunctionDec, Level>();

    private Exp visitedExp;
    private Level currentLevel;
    private FragList fragList;

    public TranslatorVisitor() {
    }

    public FragList getFragList() {
        return this.fragList;
    }

    /**
     * Returns a MEM object which represents a static link to a variable in a frame
     * higher up the stack. If the variable is defined in the current stack frame,
     * the static link will just refer to the current activation records frame
     * pointer.
     * 
     * @param access the @see Translate.Access, which contains the @see Frame.Access
     *               and @see Translate.Level.
     * @param level  @see Translate.Level where we are accessing the variable from.
     * @return a @see Tree.Exp containing MEM expressions.
     */
    private Tree.Exp staticLinkOffset(Access access, Level level) {
        // variable is defined at same level as use,
        // just return the fp as framePointer
        // get current frames static link ( in rbp - 8),
        // lookup value, which is a pointer to the previous
        // frames static link, etc
        Tree.Exp exp = new TEMP(level.frame.FP());
        var slinkLevel = level;
        int staticLinkOffset = -8;
        while (slinkLevel != access.home) {
            exp = new MEM(new BINOP(BINOP.PLUS, new CONST(staticLinkOffset), exp));
            slinkLevel = slinkLevel.parent;
        }
        return exp;
    }

    @Override
    public void visit(ArrayExp exp) {
        exp.size.accept(this);
        Exp sizeExp = this.visitedExp;
        exp.init.accept(this);
        Exp initExp = this.visitedExp;
        Temp arrayPointer = Temp.create();
        ExpList args = new ExpList(sizeExp.unEx(), new ExpList(initExp.unEx(), null));
        this.visitedExp = new Ex(
                new ESEQ(new MOVE(new TEMP(arrayPointer), currentLevel.frame.externalCall("initArray", args)),
                        new TEMP(arrayPointer)));
    }

    @Override
    public void visit(AssignExp exp) {
        exp.var.accept(this);
        ;
        Exp varExp = this.visitedExp;
        exp.exp.accept(this);
        Exp expExp = this.visitedExp;
        this.visitedExp = new Nx(new MOVE(varExp.unEx(), expExp.unEx()));
    }

    @Override
    public void visit(BreakExp exp) {
        Absyn.Absyn loop = exp.loop;
        Label loopEnd = null;
        this.visitedExp = new Nx(new JUMP(loopEnd));
    }

    @Override
    public void visit(CallExp exp) {

        Level usageLevel = this.currentLevel;
        Level definedLevel = this.functionLevels.get(exp.def);
        // if the function being called has no body its a primitive
        // and doesn't need a static link.
        FunctionDec defined = (FunctionDec) exp.def;
        boolean useStaticLink = defined.body == null;
        ExpList expList = null;
        if (useStaticLink) {
            Tree.Exp staticLink = null;
            if (definedLevel == usageLevel) { // recusive or same level, pass calleers static link ( not frame pointer )
                staticLink = new MEM(new BINOP(BINOP.MINUS, new TEMP(definedLevel.frame.FP()),
                        new CONST(definedLevel.frame.wordSize())));
            } else {
                // if calling a function in a higher level, we pass the callers frame pointer
                // if calling a function in a lower level, we pass the callers static link
                if (definedLevel.parent == usageLevel) {
                    staticLink = new TEMP(usageLevel.frame.FP());
                } else {
                    Level l = usageLevel;
                    // get callers static link ( frame pointer address of parent frame)
                    staticLink = new MEM(new BINOP(BINOP.MINUS, new TEMP(l.frame.FP()), new CONST(l.frame.wordSize())));
                    // if callee and caller have a common parent
                    // if callee is not parent of caller,
                    while (l.parent != definedLevel.parent) {
                        staticLink = new MEM(new BINOP(BINOP.MINUS, staticLink, new CONST(l.frame.wordSize())));
                        l = l.parent;
                    }
                }
            }
            expList = ExpList.append(expList, staticLink);
        }

        for (Absyn.ExpList argList = exp.args; argList != null; argList = argList.tail) {
            argList.head.accept(this);
            Exp translatedArg = this.visitedExp;
            expList = ExpList.append(expList, translatedArg.unEx());
        }
        Label functionLabel = this.functionLabels.get(exp.def);
        if (exp.getType().coerceTo(Constants.VOID)) {
            this.visitedExp = new Nx(new EXP(new CALL(new NAME(functionLabel), expList)));
        } else {
            this.visitedExp = new Ex(new CALL(new NAME(functionLabel), expList));
        }
    }

    @Override
    public void visit(DecList exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    /*
     * Commmented out because there is a naming collision between the Translate and
     * Absyn packages.
     * 
     * @Override public void visit(Absyn.ExpList exp) { // TODO Auto-generated
     * method stub super.visit(exp); }
     */

    @Override
    public void visit(FieldExpList exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    @Override
    public void visit(FieldList exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    @Override
    public void visit(FieldVar exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    @Override
    public void visit(ForExp exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    @Override
    public void visit(FunctionDec exp) {
        //create new level and frame to store locals
        for(FunctionDec current = exp; current != null; current = current.next) {
            if(current.body == null) {
                // No level for primitives.
                Label label = Label.create(current.name);
                this.functionLabels.put(exp, label);
            } else if(current.name.toString().equals("tigermain")) {
                Label label = Label.create("tigermain");
                this.currentLevel = new Level(new IntelFrame(label, getBoolList(current.params)));
                this.functionLevels.put(current, this.currentLevel);
                this.functionLabels.put(exp, label);
            } else {
                // notice that we creat the new level
                // using the function formal arguments
                // these are supplied so the level & frame
                // can create Frame.Access ( Temp or Mem )
                // for the function.
                Label label = Label.create();
                this.currentLevel = new Level(
                    this.currentLevel, 
                    label, 
                    getBoolList(
                        current.params
                    ),
                    true /* static link */
                );
                this.functionLabels.put(exp, label);
                this.functionLevels.put(current, this.currentLevel);
            }
        }
        // visit the declations again 
        for(FunctionDec current = exp; current != null; current = current.next) {
            if(exp.body != null) {
                // save current level.
                Level parent = this.currentLevel;
                // find level created for function and enter it.
                this.currentLevel = this.functionLevels.get(current);
                // get the translate access list ( frame.access & level )
                for(AccessList formals = this.currentLevel.formals; formals != null; formals = formals.tail) {

//                    this.functionAccesses.put(exp.)
                }
                // visit body of function using new level, breakScope
                exp.body.accept(this);
                // get translated fragment.
                Exp translatedBody = this.visitedExp;
                // creates a new fragment for the function.
                this.procEntryExit(this.currentLevel, translatedBody);

                this.currentLevel = parent;
            }

        }
        // TODO: Correct ?
        this.visitedExp = new Ex(new CONST(0));
    }

    /**
     * This function saves a translated rocFrag to an internal
     * linked list. This linked list is used after translate
     * has completed and it passed to the later stages.
     * @param level the current static function level
     * @param body  the body of the function we are translating
     */
    private void procEntryExit(Level level, Exp body) {
        if (body == null)
            return;
        Stm procStat =  level.frame.procEntryExit1(body.unNx());
        if(this.fragList == null) {
            this.fragList = new FragList(new ProcFrag(procStat, level.frame));
        } else {
            FragList end = this.fragList;
            while(end.tail != null) {
                end = end.tail;
            }
            end.tail = new FragList(new ProcFrag(procStat, level.frame));
        }
    }

    /**
     * Converts a field list into a BoolList. Each
     * boolean in this list represents a variable, where
     * a true indicates the variable should escape and false
     * where it should not.
     * @param fields @see Absyn.FieldList which can be null.
     * @return a single linked list where each node represents either a true or false value or null.
     */
    private BoolList getBoolList(final DecList decList) {
        BoolList boolList = null; //
        if (decList != null) {
            VarDec varDec = (VarDec)decList.head;
            boolList = new BoolList(varDec.escape, null);
            var fieldTail = decList.tail;
            while (fieldTail != null) {
                VarDec vd2 = (VarDec)fieldTail.head;
                boolList.append(vd2.escape);
                fieldTail = fieldTail.tail;
            }
        }
        return boolList;
    }

    @Override
    public void visit(IfExp exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    @Override
    public void visit(IntExp exp) {
        this.visitedExp = new Ex(new CONST(exp.value));
    }

    @Override
    public void visit(LetExp exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    @Override
    public void visit(NilExp exp) {
        this.visitedExp = new Ex(new CONST(0));
    }

    @Override
    public void visit(OpExp exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    @Override
    public void visit(RecordExp exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    @Override
    public void visit(SeqExp exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    @Override
    public void visit(SimpleVar exp) {
        // we need to find where this var was declared first.
        // when var is declared, we allocate space on the frame.
        Access access = functionAccesses.get((VarDec)exp.def);
        Tree.Exp stateLinkExp =  staticLinkOffset(access, this.currentLevel);
        this.visitedExp = new Ex(access.acc.exp(stateLinkExp));
    }

    @Override
    public void visit(StringExp exp) {
        Label label = Label.create();
        var stringFragment = currentLevel.frame.string(label, exp.value);
        //addFrag(new DataFrag(stringFragment));
        this.visitedExp = new Ex(new NAME(label));
    }

    @Override
    public void visit(SubscriptVar exp) {
        exp.index.accept(this);
        Exp indexExp = this.visitedExp;
        exp.var.accept(this);
        Exp baseExp = this.visitedExp;
        this.visitedExp =  new Ex(new MEM(
            new BINOP(
                BINOP.PLUS, 
                baseExp.unEx(), 
                new BINOP(
                    BINOP.MUL, 
                    indexExp.unEx(),
                    new CONST(currentLevel.frame.wordSize())
                )
            )                
        ));
    }

    
    @Override
    public void visit(VarDec exp) {
        exp.init.accept(this);
        Exp initExp = this.visitedExp;
        // create a new access for this variable.
        Access translateAccess = this.currentLevel.allocLocal(exp.escape);
        // store in hash table for future usage.
        functionAccesses.put(exp, translateAccess);
        // create tree expression along with static link calculation ( is this needed ?? )
        Tree.Exp decExp = translateAccess.acc.exp(staticLinkOffset(translateAccess, this.currentLevel));
        this.visitedExp = new Nx(
            new MOVE(
                decExp,
                initExp.unEx()
            )
        );
    }

    @Override
    public void visit(VarExp exp) {
        // visit the variable expression.
        exp.var.accept(this);
    }

    @Override
    public void visit(WhileExp exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }


}