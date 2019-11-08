package Semant;

import Translate.ExpTy;
import java_cup.runtime.Symbol;
import Translate.Exp;
import Symbol.Table;

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

    /**
     * Translates an abstract syntax record type into a semantic type
     * TBC - check if this should lookup symbol table for field list types
     * @param t
     * @return
     */
    Types.Type transTy(Absyn.RecordTy t) {
        System.out.println("translate record type " + t);
        Types.RECORD r = null;
        for(Absyn.FieldList l = t.fields; l != null; l = l.tail){
            r = new Types.RECORD(l.name, new Types.NAME(l.typ), r);
        }
        return r;
    }

    /**
     * Translates an abstract syntax array type into a semantic type
     * TBC - check if this should lookup symbol table for element type 
     * @param t
     * @return
     */
    Types.Type transTy(Absyn.ArrayTy t) {
        System.out.println("translate array type " + t);
        return new Types.ARRAY(new Types.NAME(t.typ));
    }

    /**
     * Translates a type t into its equivalent semantic type
     * @param t
     * @return
     */
    Types.Type transTy(Absyn.NameTy t) {
        if(t.name.toString().equals("int")) {
            return INT;
        }
         if(t.name.toString().equals("string"))
            return STRING;
        Types.Type cached = (Types.Type)env.tenv.get(t.name);
        if(cached == null) {
            cached = new Types.NAME(t.name);
            env.tenv.put(t.name, cached);
        }
        return cached;
    }

    /**
     * Dispatcher function for types
     */
    Types.Type transTy(Absyn.Ty t) {
        if(t instanceof Absyn.NameTy)
            return transTy((Absyn.NameTy)t);
        if(t instanceof Absyn.RecordTy)
            return transTy((Absyn.RecordTy)t);
        if(t instanceof Absyn.ArrayTy)
            return transTy((Absyn.ArrayTy)t);
        throw new Error("Not Implemented " + t.getClass().getName());
    }

    ExpTy transVar(Absyn.Var e) {
        if (e instanceof Absyn.SimpleVar) {
            return transVar((Absyn.SimpleVar) e);
        }
        throw new Error("Not Implemented " + e.getClass().getName());
    }

    ExpTy transVar(Absyn.SimpleVar e) {
        System.out.println("Found simple var " + e);
        Entry x = (Entry) (env.venv.get(e.name));
        if (x instanceof VarEntry) {
            VarEntry ent = (VarEntry) x;
            return new ExpTy(null, ent.ty);
        } else {
            error(e.pos, "Undefined variable: " + e.name);
            return new ExpTy(null, INT);
        }
    }

    Exp transDec(Absyn.TypeDec e) {
        System.out.println("translate type declaration");
        env.venv.put(e.name, transTy(e.ty));
        return null;
    }

    Exp transDec(Absyn.VarDec e) {
        System.out.println("translate variable declaration " + e.name);
        // var varname:vartype = expression 
        ExpTy initExpTy = transExp(e.init);
        Types.Type type = initExpTy.ty;
        if (e.typ != null) {
            Types.Type otherType = transTy(e.typ);
            if(otherType != type) {
                error(e.pos, "Types do not match, type of (" + e + " translates to " + otherType + " is not of declared type " + type + ")");
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
        System.out.println("checking let expression");
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
        System.out.println("checking string expression");
        return new ExpTy(null, STRING);
    }

    ExpTy transExp(Absyn.IntExp intExp) {
        System.out.println("checking int expression");
        return new ExpTy(null, INT);
    }

    ExpTy transExp(Absyn.CallExp callExp) {
        System.out.println("checking call expression");
        return new ExpTy(null, null);
    }

    ExpTy transExp(Absyn.ArrayExp arrayExp) {
        System.out.println("checking array expression");
        return new ExpTy(null, null);
    }

    ExpTy transExp(Absyn.SeqExp seqExp) {
        System.out.println("checking sequence expression");
        return new ExpTy(null, null);
    }

    public ExpTy transExp(Absyn.Exp e) {
        if (e instanceof Absyn.VarExp)
            return transExp((Absyn.VarExp) e);
        else if (e instanceof Absyn.IntExp)
            return transExp((Absyn.IntExp) e);
        else if (e instanceof Absyn.StringExp)
            return transExp((Absyn.StringExp) e);
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