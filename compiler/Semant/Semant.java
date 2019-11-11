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
     * Translates an abstract syntax record type into a tiger type
     * 
     * @param t
     * @return
     */
    Types.Type transTy(Absyn.RecordTy t) {
        // go through each the fields in this record type
        Types.RECORD head = null;
        Types.RECORD prev = null;
        for (Absyn.FieldList l = t.fields; l != null; l = l.tail) {
            var cached = (Types.Type) env.tenv.get(l.typ);
            if (cached == null) {
                error(t.pos, "Undefined type " + t);
                continue;
            }
            // create record for current item
            Types.RECORD current = new Types.RECORD(l.name, cached, null);
            if (head == null)
                head = current;
            if (prev != null)
                prev.tail = current;
            prev = current;
        }
        return head;
    }

    /**
     * Translates an abstract syntax array type into a tiger type
     * 
     * @param t
     * @return
     */
    Types.Type transTy(Absyn.ArrayTy t) {
        System.out.println("translate array type " + t);
        // lookup type of array ( intArray = array of int )
        Types.Type cached = (Types.Type) env.tenv.get(t.typ);
        if (cached == null) {
            error(t.pos, "Undefined type " + t);
            return null;
        }
        return new Types.ARRAY(cached);
    }

    /**
     * Translates a type t into a tiger type
     * 
     * @param t
     * @return
     */
    Types.Type transTy(Absyn.NameTy t) {
        // lookup the type by its symbol name in the type cache
        Types.Type cached = (Types.Type) env.tenv.get(t.name);
        // type for symbol t.name has not been seen, lets add it to our cache
        if (cached == null) {
            error(t.pos, "Undefined type " + t);
            return null;
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
        if (e instanceof Absyn.FieldVar) {
            return transVar((Absyn.FieldVar) e);
        }
        if (e instanceof Absyn.SubscriptVar) {
            return transVar((Absyn.SubscriptVar) e);
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

    ExpTy transVar(Absyn.FieldVar e) {
        throw new Error("Not Implemented " + e.getClass().getName());
    }

    ExpTy transVar(Absyn.SubscriptVar e) {
        throw new Error("Not Implemented " + e.getClass().getName());
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
        // get the expression type
        Types.Type type = initExpTy.ty;
        // if the expression type is not null
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
        case Absyn.OpExp.MINUS:
        case Absyn.OpExp.MUL:
        case Absyn.OpExp.DIV:
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
     * Translate a record expression. example rectype {name=\"Nobody\", age=12345} A
     * record expression contains a symbol for the type and one or more name value
     * pairs that are a symbol and a initialising expression. Type validation checks
     * 1) Check the type is valid 'rectype' 2) Check the fields are the correct type
     * Notes & Questions 1) Do we need to consider nested record types ? 2) How do
     * we handle array properties 3) Is the order of the field exp list important ?
     * 4) Are all fields mandatory ? YES
     * 
     * @param recordExp
     * @return
     */
    ExpTy transExp(Absyn.RecordExp recordExp) {
        var expressionTypeSymbol = recordExp.typ;
        var tigerType = (Types.RECORD) env.tenv.get(expressionTypeSymbol);
        if (tigerType == null) {
            error(recordExp.pos, "Unknown type " + expressionTypeSymbol);
        }
        // Loop through fieldExpLists rec{field1=value, field2=value2.....}
        var temp = tigerType;
        for (var fel = recordExp.fields; fel != null; fel = fel.tail) {
            var fieldExpTy = transExp(fel, temp);
            // advance to next tiger type
            temp = tigerType.tail;
        }
        return new ExpTy(null, tigerType);
    }

    /**
     * Translates a fieldExpList to its tiger type A fieldExpList is of form
     * property1=expression1 Typechecker should check that the type of property1 is
     * the same as the type of expression1
     * 
     * @param fel
     * @return
     */
    ExpTy transExp(Absyn.FieldExpList fel, Types.RECORD recordType) {
        if (recordType == null) {
            error(fel.pos, "Unknown symbol: " + fel.name);
            return new ExpTy(null, null);
        }
        // get type of field for this record type
        var fieldType = recordType.fieldType;
        var fieldName = recordType.fieldName;
        if (fel.name != fieldName) {
            error(fel.pos, "Unexpected symbol: " + fel.name + ", expecting " + fieldName);
        }
        // get initialising expression
        var initExp = fel.init;
        // get transExp of initialising expression
        var transExp = transExp(initExp);
        // compare type of field with type of expression
        if (fieldType != transExp.ty) {
            error(fel.pos, "Type mismatch: " + fieldType + " != " + transExp.ty);
        }
        // return exp and type
        return new ExpTy(null, fieldType);
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