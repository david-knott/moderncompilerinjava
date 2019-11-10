package Semant;

import Translate.ExpTy;
import java_cup.runtime.Symbol;
import Translate.Exp;
import Symbol.Table;

public class Semant {
    private final Env env;
    public static final Types.Type INT = new Types.INT();
    public static final Types.Type STRING = new Types.STRING();

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
     * Translates an abstract syntax record type into a semantic type TBC - check if
     * this should lookup symbol table for field list types
     * 
     * @param t
     * @return
     */
    Types.Type transTy(Absyn.RecordTy t) {
        System.out.println("translate record type " + t);
        // go through each the fields in this record type
        Types.RECORD r = null;
        for (Absyn.FieldList l = t.fields; l != null; l = l.tail) {
            System.out.println("record type field " + l);
            var cached = env.tenv.get(l.typ);
            if (cached == null) {
                cached = new Types.NAME(l.typ);
                System.out.println("created new name type " + cached + " to tenv (" + l + ")");
                env.tenv.put(l.typ, cached);

            } else {
                System.out.println("found " + l.typ + " => " + cached + " tenv");
            }

            r = new Types.RECORD(l.name, new Types.NAME(l.typ), r);
        }
        return r;
    }

    /**
     * Translates an abstract syntax array type into a semantic type TBC - check if
     * this should lookup symbol table for element type
     * 
     * @param t
     * @return
     */
    Types.Type transTy(Absyn.ArrayTy t) {
        System.out.println("translate array type " + t);
        // lookup type of array ( intArray = array of int )
        Types.Type cached = (Types.Type) env.tenv.get(t.typ);
        if (cached == null) {
            cached = new Types.NAME(t.typ);
            System.out.println("created new name type " + cached + " to tenv (" + t + ")");
            env.tenv.put(t.typ, cached);
        } else {
            System.out.println("found " + t + "=>" + cached + " tenv");
        }
        return new Types.ARRAY(cached);
    }

    /**
     * Translates a type t into its equivalent semantic type
     * 
     * @param t
     * @return
     */
    Types.Type transTy(Absyn.NameTy t) {
        // lookup the type by its symbol name in the type cache
        Types.Type cached = (Types.Type) env.tenv.get(t.name);
        System.out.println("transTy(" + t + ")");
        // type for symbol t.name has not been seen, lets add it to our cache
        if (cached == null) {
            cached = new Types.NAME(t.name);
            System.out.println("created new name type " + cached + " to tenv (" + t + ")");
            env.tenv.put(t.name, cached);
        } else {
            System.out.println("found " + t + "=>" + cached + " tenv");
        }
        return cached;
    }

    /**
     * Dispatcher function for types
     */
    Types.Type transTy(Absyn.Ty t) {
        if (t instanceof Absyn.NameTy)
            return transTy((Absyn.NameTy) t);
        if (t instanceof Absyn.RecordTy)
            return transTy((Absyn.RecordTy) t);
        if (t instanceof Absyn.ArrayTy)
            return transTy((Absyn.ArrayTy) t);
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

    Exp transDec(Absyn.FunctionDec e) {
        System.out.println("translate function declaration");
        // if typedec has next its got recuring type, with are recursive
        if (e.next != null) {
            throw new Error("Recursive types not implemented.");
        }
        return null;
    }

    Exp transDec(Absyn.TypeDec e) {
        System.out.println("tranDec: translate type declaration");
        // if typedec has next its got recuring type, with are recursive
        if (e.next != null) {
            throw new Error("Recursive types not implemented.");
        }
        // type dec attributes are a symbol name and its type ty
        // we translate ty into its type
        // we add a mapping for type name to its type
        var mappedType = transTy(e.ty);
        System.out.println("transDec: adding type mapping for " + e + " => " + mappedType);
        // add mapping from type name to native tiger type
        env.tenv.put(e.name, mappedType);
        return null;
    }

    Exp transDec(Absyn.VarDec e) {
        // var varname:vartype = expression
        System.out.println("transDec: translate variable declaration " + e.name);
        ExpTy initExpTy = transExp(e.init);
        //get the expression type
        Types.Type type = initExpTy.ty;
        //if the expression type is not null
        System.out.println(">>>> expression type " + type + " " + e.init);
        if (e.typ != null) {
            Types.Type otherType = transTy(e.typ);
            if (otherType != type) {
                error(e.pos, "Types do not match, type of (" + e + " translates to " + otherType
                        + " is not of declared type " + type + ")");
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
        else if (e instanceof Absyn.FunctionDec)
            return transDec((Absyn.FunctionDec) e);
        else
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
        System.out.println("checking array expression " + arrayExp);
        return new ExpTy(null, null);
    }

    ExpTy transExp(Absyn.SeqExp seqExp) {
        System.out.println("checking sequence expression");
        return new ExpTy(null, null);
    }

    /**
     * Translate a record expression.
     * example rectype {name=\"Nobody\", age=\"Nobody\"}
     * A record expression contains a symbol for the type
     * and one or more name value pairs that are a symbol and
     * a initialising expression.
     * Type validation checks
     * 1) Check the type is valid 'rectype'
     * 2) Check the fields are the correct type
     * Notes & Questions
     * Do we need to consider nested record types ?
     * How do we handle array properties
     * Is the order of the field exp list important ?
     * Are are fields mandatory ?
     * @param recordExp
     * @return
     */
    ExpTy transExp(Absyn.RecordExp recordExp) {
        var expressionTypeSymbol = recordExp.typ;
        var cached = (Types.Type)env.tenv.get(expressionTypeSymbol);
        if(cached == null){
            error(recordExp.pos, "Unknown type " + expressionTypeSymbol);
        }
        System.out.println("checking record expression:" + expressionTypeSymbol + " => " + cached);
        //Loop through fieldExpLists rec{field1=value, field2=value2}
        for(var fel = recordExp.fields; fel != null; fel = fel.tail){
            var symbolName = fel.name;
            //lookup type of symbolname (eg field1 ) for this record type
            var initExp = fel.init;
            //check if the type of symbolname is same as initExp
            var tigerTranslatedType = transExp(initExp);
        }
        return new ExpTy(null, cached);
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
        else if (e instanceof Absyn.RecordExp)
            return transExp((Absyn.RecordExp) e);
        else
            throw new Error("Cannot handle " + e.getClass().getName());
    }
}