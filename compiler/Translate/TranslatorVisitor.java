package Translate;

import java.util.Hashtable;

import Absyn.Absyn;
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
import Tree.CONST;
import Tree.ESEQ;
import Tree.ExpList;
import Tree.JUMP;
import Tree.MEM;
import Tree.MOVE;
import Tree.NAME;
import Tree.TEMP;

class TranslatorVisitor extends DefaultVisitor {

    private Exp visitedExp;
    private Level currentLevel;

    public TranslatorVisitor() {
        Label label = Label.create("tigermain");
        currentLevel = new Level(new IntelFrame(label, null));
    }

    /**
     * Returns a MEM object which represents a static link to
     * a variable in a frame higher up the stack. If the variable
     * is defined in the current stack frame, the static link 
     * will just refer to the current activation records frame pointer.
     * @param access the @see Translate.Access, which contains the @see Frame.Access and @see Translate.Level.
     * @param level @see Translate.Level where we are accessing the variable from.
     * @return a @see Tree.Exp containing MEM expressions.
     */
    private Tree.Exp staticLinkOffset(Access access, Level level) {
        //variable is defined at same level as use,
        //just return the fp as framePointer
        //get current frames static link ( in rbp - 8),
        //lookup value, which is a pointer to the previous
        //frames static link, etc
        Tree.Exp exp = new TEMP(level.frame.FP());
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

    @Override
    public void visit(ArrayExp exp) {
        exp.size.accept(this);
        Exp sizeExp = this.visitedExp;
        exp.init.accept(this);
        Exp initExp = this.visitedExp;
        Temp arrayPointer = Temp.create();
        ExpList args = new ExpList(
            sizeExp.unEx(), 
            new ExpList(
                initExp.unEx(), 
                null
            )
        );
        this.visitedExp = new Ex(
            new ESEQ(
                new MOVE(
                    new TEMP(arrayPointer), 
                    currentLevel.frame.externalCall("initArray", args)
                ), 
                new TEMP(arrayPointer)
            )
        );
    }

    @Override
    public void visit(AssignExp exp) {
        exp.var.accept(this);;
        Exp varExp = this.visitedExp;
        exp.exp.accept(this);
        Exp expExp = this.visitedExp;
        this.visitedExp = new Nx(
            new MOVE(
                varExp.unEx(), 
                expExp.unEx()
            )
        );
    }

    @Override
    public void visit(BreakExp exp) {
        Absyn loop = exp.loop;
        Label loopEnd = null;
        this.visitedExp = new Nx(new JUMP(loopEnd));
    }

    @Override
    public void visit(CallExp exp) {
    }

    @Override
    public void visit(DecList exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    /*
    Commmented out because there is a naming collision between the
    Translate and Absyn packages.
    @Override
    public void visit(Absyn.ExpList exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }
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
        // TODO Auto-generated method stub
        super.visit(exp);
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
        Access access = variableAccess.get((VarDec)exp.def);
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

    private Hashtable<VarDec, Access> variableAccess = new Hashtable<VarDec, Access>();
    
    @Override
    public void visit(VarDec exp) {
        exp.init.accept(this);
        Exp initExp = this.visitedExp;
        // create a new access for this variable.
        Access translateAccess = this.currentLevel.allocLocal(exp.escape);
        // store in hash table for future usage.
        variableAccess.put(exp, translateAccess);
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