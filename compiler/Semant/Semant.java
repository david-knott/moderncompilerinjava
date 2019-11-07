package Semant;

import Translate.ExpTy;
import java_cup.runtime.Symbol;
import Translate.Exp;
import Symbol.Table;

class Env {
    Table venv;
    Table tenv;
    ErrorMsg.ErrorMsg errorMsg;

    Env(ErrorMsg.ErrorMsg err) {
        errorMsg = err;
        venv = new Table();
        tenv = new Table();
    }
}

abstract class Entry {

}

class VarEntry extends Entry {
    Types.Type ty;

    VarEntry(Types.Type t) {
        ty = t;
    }
}

class FunEntry extends Entry {
    Types.RECORD formals;
    Types.Type result;

    public FunEntry(Types.RECORD f, Types.Type r) {
        formals = f;
        result = r;
    }
}

public class Semant {
    private final Env env;
    private final Types.Type INT = new Types.INT();
    private final Types.Type STRING = new Types.STRING();

    private void error(int pos, String message) {
        env.errorMsg.error(pos, message);
    }

    private Exp checkInt(ExpTy et, int pos) {
        if (!(et.ty instanceof Types.INT)) {
            error(pos, "Integer required");
        }
        return et.exp;
    }

    public Semant(ErrorMsg.ErrorMsg err) {
        this(new Env(err));
    }

    Semant(Env e) {
        env = e;
    }


    Types.Type transTy(Absyn.RecordTy t){
        //give a type t, transate it into
        return null;
    }


    Types.Type transTy(Absyn.ArrayTy t){
        //give a type t, transate it into
        return null;
    }

    Types.Type transTy(Absyn.NameTy t){
        //give a type t, transate it into
        return null;
    }





    Types.Type transTy(Absyn.Ty t){
        //give a type t, transate it into
        return null;
    }

    ExpTy transVar(Absyn.Var e) {
        if (e instanceof Absyn.SimpleVar) {
            return transVar((Absyn.SimpleVar) e);
        }
        throw new Error("Not Implemented " + e.getClass().getName());
    }

    ExpTy transVar(Absyn.SimpleVar e) {
        Entry x = (Entry) (env.venv.get(e.name));
        if (x instanceof VarEntry) {
            VarEntry ent = (VarEntry) x;
            return new ExpTy(null, ent.ty);
        } else {
            error(e.pos, "Undefined variable");
            return new ExpTy(null, INT);
        }
    }

    Exp transDec(Absyn.TypeDec e){
       // Symbol e.name
       // Ty ty
       // TypeDec next

        env.venv.put(e.name, transTy(e.ty));
       return null;
    }

    Exp transDec(Absyn.VarDec e) {
        // a variable declaration,
        // Symbol s= e.name
        // add symbol name and exp type into venv
        // if e.typ is defined and doesn't equal the type of exp, its an error
        ExpTy initExpTy = transExp(e.init);
        Types.Type type = initExpTy.ty;
        if (e.typ != null) {
            if(transTy(e.typ) != type){
                error(e.pos, "Undefined variable");
            }
        }
        env.venv.put(e.name, new VarEntry(type));
        return null;
    }

    Exp transDec(Absyn.Dec e) {
        if (e instanceof Absyn.VarDec)
            return transDec((Absyn.VarDec) e);
        else if (e instanceof Absyn.TypeDec)
            return transDec((Absyn.TypeDec) e);
        throw new Error("Not Implemented " + e.getClass().getName());
    }

    ExpTy transExp(Absyn.VarExp e) {
        return new ExpTy(null, null);
    }

    ExpTy transExp(Absyn.LetExp e) {
        env.venv.beginScope();
        env.tenv.beginScope();
        for (Absyn.DecList p = e.decs; p != null; p = p.tail)
            transDec(p.head);
        ExpTy et = transExp(e.body);
        env.tenv.endScope();
        env.venv.endScope();
        return new ExpTy(null, et.ty);
    }

    ExpTy transExp(Absyn.OpExp e) {
        switch (e.oper) {
        case Absyn.OpExp.PLUS:
            checkInt(transExp(e.left), e.left.pos);
            checkInt(transExp(e.right), e.right.pos);
            return new ExpTy(null, INT);
        }
        throw new Error("OpExp - Unknown operator " + e.oper);
    }

    ExpTy transExp(Absyn.StringExp stringExp) {
        return new ExpTy(null, STRING);
    }

    ExpTy transExp(Absyn.IntExp intExp) {
        return new ExpTy(null, INT);
    }

    ExpTy transExp(Absyn.CallExp callExp) {
        return new ExpTy(null, null);
    }

    ExpTy transExp(Absyn.ArrayExp arrayExp) {
        return new ExpTy(null, null);
    }

    ExpTy transExp(Absyn.SeqExp seqExp) {
        return new ExpTy(null, null);
    }

    public ExpTy transExp(Absyn.Exp e) {
        if (e instanceof Absyn.VarExp)
            return transExp((Absyn.VarExp) e);
        else if (e instanceof Absyn.IntExp)
            return transExp((Absyn.IntExp) e);
        else if (e instanceof Absyn.CallExp)
            return transExp((Absyn.CallExp) e);
        else if (e instanceof Absyn.LetExp)
            return transExp((Absyn.LetExp) e);
        else if (e instanceof Absyn.OpExp)
            return transExp((Absyn.OpExp) e);
        else if (e instanceof Absyn.SeqExp)
            return transExp((Absyn.SeqExp) e);
        else if (e instanceof Absyn.ArrayExp)
            return transExp((Absyn.ArrayExp) e);
        else
            throw new Error("Cannot handle " + e.getClass().getName());
    }
}