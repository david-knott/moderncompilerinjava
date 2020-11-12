package Translate;

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
import Absyn.VarExp;
import Absyn.WhileExp;
import Temp.Label;
import Temp.Temp;
import Tree.CONST;
import Tree.ESEQ;
import Tree.ExpList;
import Tree.JUMP;
import Tree.MOVE;
import Tree.TEMP;

class TranslatorVisitor extends DefaultVisitor {

    private Exp visitedExp;
    private Level currentLevel;

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
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    @Override
    public void visit(StringExp exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    @Override
    public void visit(SubscriptVar exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    @Override
    public void visit(VarExp exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }

    @Override
    public void visit(WhileExp exp) {
        // TODO Auto-generated method stub
        super.visit(exp);
    }
}