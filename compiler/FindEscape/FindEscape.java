package FindEscape;

import Absyn.ArrayExp;
import Absyn.AssignExp;
import Absyn.BreakExp;
import Absyn.CallExp;
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

abstract class Escape {
    int depth;

    abstract void setEscape();
}

class FormalEscape extends Escape {
    Absyn.FieldList fl;

    FormalEscape(int d, Absyn.FieldList f) {
        depth = d;
        fl = f;
        fl.escape = true;// false;
    }

    void setEscape() {
        fl.escape = true;
    }
}

class VarEscape extends Escape {
    Absyn.VarDec vd;

    VarEscape(int d, Absyn.VarDec v) {
        depth = d;
        vd = v;
        vd.escape = true; //false;
    }

    void setEscape() {
        vd.escape = true;
    }
}

public class FindEscape {

    public final Symbol.GenericTable<Escape> escEnv = new Symbol.GenericTable<Escape>();

    public FindEscape(Absyn.Exp e) {
        traverseExp(0, e);
    }

    private void traverseExp(int depth, LetExp letExp) {
        this.escEnv.beginScope();
        for (var dec = letExp.decs; dec != null; dec = dec.tail) {
            traverseDec(depth + 1, dec.head);
        }
        traverseExp(depth + 1, letExp.body);
        this.escEnv.endScope();
    }

    private void traverseExp(int depth, VarExp varExp) {
        traverseVar(depth, varExp.var);
    }

    private void traverseExp(int depth, IntExp intExp) {
    }

    private void traverseExp(int depth, StringExp stringExp) {
    }

    private void traverseExp(int depth, CallExp callExp) {
        for (var exp = callExp.args; exp != null; exp = exp.tail) {
            traverseExp(depth, exp.head);
        }
    }

    private void traverseExp(int depth, OpExp opExp) {
        traverseExp(depth, opExp.left);
        traverseExp(depth, opExp.right);
    }

    private void traverseExp(int depth, SeqExp seqExp) {
        for (var exp = seqExp.list; exp != null; exp = exp.tail) {
            traverseExp(depth, exp.head);
        }
    }

    private void traverseExp(int depth, ArrayExp arrayExp) {
        traverseExp(depth, arrayExp.init);
        traverseExp(depth, arrayExp.size);
    }

    private void traverseExp(int depth, RecordExp recordExp) {
        for (var field = recordExp.fields; field != null; field = field.tail) {
            traverseExp(depth, field.init);
        }
    }

    private void traverseExp(int depth, AssignExp assignExp) {
        traverseExp(depth, assignExp.exp);
        traverseVar(depth, assignExp.var);
    }

    private void traverseExp(int depth, WhileExp exp) {
        traverseExp(depth, exp.body);
        traverseExp(depth, exp.test);
    }

    private void traverseExp(int depth, ForExp forExp) {
        traverseExp(depth, forExp.body);
        traverseExp(depth, forExp.hi);
        traverseDec(depth, forExp.var);
    }

    private void traverseExp(int depth, IfExp ifExp) {
        traverseExp(depth, ifExp.elseclause);
        traverseExp(depth, ifExp.test);
        traverseExp(depth, ifExp.thenclause);
    }

    private void traverseExp(int depth, BreakExp breakExp) {
    }

    private void traverseExp(int depth, NilExp nilExp) {
    }

    private void traverseExp(int depth, Absyn.Exp e) {
        if(e == null)
            return;
        if (e instanceof Absyn.VarExp)
            traverseExp(depth, (Absyn.VarExp) e);
        else if (e instanceof Absyn.IntExp)
            traverseExp(depth, (Absyn.IntExp) e);
        else if (e instanceof Absyn.StringExp)
            traverseExp(depth, (Absyn.StringExp) e);
        else if (e instanceof Absyn.CallExp)
            traverseExp(depth, (Absyn.CallExp) e);
        else if (e instanceof Absyn.LetExp)
            traverseExp(depth, (Absyn.LetExp) e);
        else if (e instanceof Absyn.OpExp)
            traverseExp(depth, (Absyn.OpExp) e);
        else if (e instanceof Absyn.SeqExp)
            traverseExp(depth, (Absyn.SeqExp) e);
        else if (e instanceof Absyn.ArrayExp)
            traverseExp(depth, (Absyn.ArrayExp) e);
        else if (e instanceof Absyn.RecordExp)
            traverseExp(depth, (Absyn.RecordExp) e);
        else if (e instanceof Absyn.AssignExp)
            traverseExp(depth, (Absyn.AssignExp) e);
        else if (e instanceof Absyn.WhileExp)
            traverseExp(depth, (Absyn.WhileExp) e);
        else if (e instanceof Absyn.ForExp)
            traverseExp(depth, (Absyn.ForExp) e);
        else if (e instanceof Absyn.IfExp)
            traverseExp(depth, (Absyn.IfExp) e);
        else if (e instanceof Absyn.BreakExp)
            traverseExp(depth, (Absyn.BreakExp) e);
        else if (e instanceof Absyn.NilExp)
            traverseExp(depth, (Absyn.NilExp) e);
        else
            throw new Error("Cannot handle " + e);
    }

    private void traverseDec(int depth, Absyn.FunctionDec e) {
        this.escEnv.beginScope();
        for (var fl = e.params; fl != null; fl = fl.tail) {
            var formalEscape = new FormalEscape(depth, fl);
            this.escEnv.put(fl.name, formalEscape);
        }
        traverseExp(depth, e.body);
        this.escEnv.endScope();
    }

    private void traverseDec(int depth, Absyn.VarDec e) {
        traverseExp(depth, e.init);
        this.escEnv.put(e.name, new VarEscape(depth, e));
    }

    private void traverseDec(int depth, Absyn.TypeDec e) {
    }



    private void traverseDec(int depth, Absyn.Dec e) {
        if (e instanceof Absyn.VarDec)
            traverseDec(depth, (Absyn.VarDec) e);
        else if (e instanceof Absyn.TypeDec)
            traverseDec(depth, (Absyn.TypeDec) e);
        else if (e instanceof Absyn.FunctionDec)
            traverseDec(depth, (Absyn.FunctionDec) e);
        else
            throw new Error("Not Implemented " + e.getClass().getName());
    }

    private void traverseVar(int depth, SimpleVar simpleVar){
        var sv = this.escEnv.get(simpleVar.name);
        //we check for undefined variable in the semant phase
        if(sv != null && sv.depth < depth){
            sv.setEscape();
        }
    }

    private void traverseVar(int depth, FieldVar fieldVar){
        traverseVar(depth, fieldVar.var);
    }

    private void traverseVar(int depth, SubscriptVar subscriptVar){
        traverseVar(depth, subscriptVar.var);
    }

    private void traverseVar(int depth, Absyn.Var v) {
        if (v instanceof Absyn.SimpleVar) {
            traverseVar(depth, (Absyn.SimpleVar) v);
        } else if (v instanceof Absyn.FieldVar) {
            traverseVar(depth, (Absyn.FieldVar) v);
        } else if (v instanceof Absyn.SubscriptVar) {
            traverseVar(depth, (Absyn.SubscriptVar) v);
        } else
        throw new Error("Not Implemented " + v.getClass().getName());
    }
}