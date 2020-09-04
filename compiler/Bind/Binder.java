package Bind;

import Absyn.DefaultVisitor;
import Absyn.ArrayExp;
import Absyn.ArrayTy;
import Absyn.AssignExp;
import Absyn.BreakExp;
import Absyn.CallExp;
import Absyn.DecList;
import Absyn.ExpList;
import Absyn.FieldExpList;
import Absyn.FieldList;
import Absyn.FieldVar;
import Absyn.ForExp;
import Absyn.FunctionDec;
import Absyn.IfExp;
import Absyn.IntExp;
import Absyn.LetExp;
import Absyn.NameTy;
import Absyn.NilExp;
import Absyn.OpExp;
import Absyn.RecordExp;
import Absyn.RecordTy;
import Absyn.SeqExp;
import Absyn.SimpleVar;
import Absyn.StringExp;
import Absyn.SubscriptVar;
import Absyn.TypeDec;
import Absyn.Var;
import Absyn.VarDec;
import Absyn.VarExp;
import Absyn.WhileExp;

/**
 * The Binder class traverses the abstract syntax tree
 * and remembers variable, function and type declarations.
 * 
 * These are used in the type checking and semantic analysis phase.
 */
public class Binder extends DefaultVisitor {


    @Override
    public void visit(ArrayExp exp) {
        exp.size.accept(this);
        exp.init.accept(this);
    }

    @Override
    public void visit(ArrayTy exp) {
    }

    @Override
    public void visit(AssignExp exp) {
        exp.exp.accept(this);
        exp.var.accept(this);
    }

    @Override
    public void visit(BreakExp exp) {
    }

    @Override
    public void visit(CallExp exp) {
        exp.args.accept(this);
    }

    @Override
    public void visit(DecList exp) {
        for(;exp != null; exp = exp.tail) {
            exp.head.accept(this);
        }
    }

    @Override
    public void visit(ExpList exp) {
        for(;exp != null; exp = exp.tail) {
            exp.head.accept(this);
        }
    }

    @Override
    public void visit(FieldExpList exp) {
        for(;exp != null; exp = exp.tail) {
            exp.init.accept(this);
        }
    }

    @Override
    public void visit(FieldList exp) {
    }

    @Override
    public void visit(FieldVar exp) {
    }

    @Override
    public void visit(ForExp exp) {
        exp.body.accept(this);
        exp.hi.accept(this);
        exp.var.accept(this);
    }

    @Override
    public void visit(FunctionDec exp) {
        exp.body.accept(this);
        exp.params.accept(this);
    }

    @Override
    public void visit(IfExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(IntExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(LetExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(NameTy exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(NilExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(OpExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(RecordExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(RecordTy exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(SeqExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(SimpleVar exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(StringExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(SubscriptVar exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(TypeDec exp) {
        //type declared here.

    }

    @Override
    public void visit(Var exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(VarDec exp) {
        //variable is being declared here.

    }

    @Override
    public void visit(VarExp exp) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(WhileExp exp) {
        // TODO Auto-generated method stub

    }
}
