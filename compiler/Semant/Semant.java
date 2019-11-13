package Semant;

import Translate.ExpTy;
import Types.ARRAY;
import Types.NAME;
import Types.RECORD;

import java.util.ArrayList;

import Absyn.TypeDec;
import Translate.Exp;

public class Semant {
    private final Env env;
    public static final Types.Type INT = new Types.INT();
    public static final Types.Type STRING = new Types.STRING();
    public static final Types.Type VOID = new Types.VOID();
    public static final Types.Type NIL = new Types.NIL();

    private void error(int pos, String message) {
        env.errorMsg.error(pos, message);
    }

    private Exp checkString(ExpTy et, int pos) {
        if (!(et.ty.actual() instanceof Types.STRING)) {
            error(pos, "String required");
        }
        return et.exp;
    }

    private Exp checkInt(ExpTy et, int pos) {
        if (!(et.ty.actual() instanceof Types.INT)) {
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
     * Returns the env, this is used for testing.
     * 
     * @return
     */
    public Env getEnv() {
        return this.env;
    }

    /**
     * Translates an abstract syntax record type into a tiger record type
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
                error(t.pos, "Undefined type " + l.typ);
                continue;
            }
            // create record for current item
            Types.RECORD current = new Types.RECORD(l.name, cached, null);
            if (head == null)
                head = current;
            // set the previous items tail to the current item
            // if we dont do this, the list will be reversed when
            // used by the recordexp transExp
            if (prev != null)
                prev.tail = current;
            prev = current;
        }
        return head;
    }

    /**
     * Translates an abstract syntax array type into a tiger type
     * @param t
     * @return
     */
    Types.Type transTy(Absyn.ArrayTy t) {
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

    /**
     * Dispatcher function for var types
     * @param e
     * @return
     */
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
        Entry x = (Entry) (env.venv.get(e.name));
        if (x instanceof VarEntry) {
            VarEntry ent = (VarEntry) x;
            return new ExpTy(null, ent.ty);
        } else {
            error(e.pos, "Undefined variable: " + e.name);
            return new ExpTy(null, INT);
        }
    }

    /**
     * Translates an abstract field variable into a intermediate expression and a tiger type
     * for example { name = "david knott",....}, field is name and david is variable.
     * Type checker should ensure that the type of var is the same as the type of the field.
     * @param e
     * @return
     */
    ExpTy transVar(Absyn.FieldVar e) {
    
        throw new Error("Not Implemented " + e.getClass().getName());
    }

    /**
     * Translate an array subscript item into an intermediate expression and a tiger type
     * eg anarray[int_variable], anarray is an array of type t, with element of type e
     * Subscript index should evaluate to int
     * @param e
     * @return
     */
    ExpTy transVar(Absyn.SubscriptVar e) {
        var indexExp = e.index;
        if(transExp(indexExp).ty.actual() != INT){
            error(e.pos, "Subscript expression is not of type int: ");
        }
        var tranlatedArrayVar = transVar(e.var);
        //tranlatedArrayVar.ty is type of ARRAY
        

        //Entry entry = (Entry)(env.venv.get(e.));

        //var variableExp = transVar(e.var);
        //error(e.pos, "Variable type is not an array");
        
        //returntype is
        throw new Error("Not Implemented " + e.getClass().getName());
    }

    Exp transDec(Absyn.FunctionDec e) {
        throw new Error("Function declarations not implemented.");
    }

    /**
     * Translates a type declaration into an intermediate expression and tiger type.
     * for example type atype = {a: int, b: string, c: othertype} Declarations can
     * be non recursive mutually recursive recursive Recursive type declarations
     * should be contiguous, we can use the tail field to find them.
     * 
     * @param e
     * @return
     */
    Exp transDecOld(Absyn.TypeDec e) {
        // we translate the abstract syntax type into a tiger type eg RecordTy is
        // translated to RECORD
        var mappedType = transTy(e.ty);
        // add type mapping from type name to the native tiger type, so atype maps to
        // RECORD instance
        env.tenv.put(e.name, mappedType);
        return null;
    }

    /**
     * Recursive support for declarations 
     * @param e
     * @return
     */
    Exp transDec(Absyn.TypeDec e) {
        //process headers first
        TypeDec next = e;
        do {
            var namedType = new Types.NAME(next.name);
            // stick it into the type env so that its available for
            // look ups by the transTy function
            env.tenv.put(next.name, namedType);
            next = e.next;
        } while(next != null);
        //process bodies of type declarations
        next = e;
        do {
            // set the name types actual type to the
            //type returned by the the transTy function
            var mappedType = transTy(next.ty);
            //get the named type from the env
            var namedType = (NAME)env.tenv.get(next.name);
            namedType.bind(mappedType);
            next = e.next;
        } while(next != null);

        /*
        //created a named type for the type definition
        var namedType = new Types.NAME(e.name);
        //stick it into the type env so that its available for 
        //look ups by the transTy function
        env.tenv.put(e.name, namedType);
        var mappedType = transTy(e.ty);
        //set the name types actual type to the
        //type returned by the the transTy function
        namedType.bind(mappedType);
        */
        return null;
    }


    /**
     * Translates a variable declaration into an intermediate expression and tiger
     * type
     * 
     * @param e
     * @return
     */
    Exp transDec(Absyn.VarDec e) {
        // var varname:vartype = expression
        ExpTy initExpTy = transExp(e.init);
        // get the expression type
        Types.Type type = initExpTy.ty.actual();
        // if the expression type is not null
        if (e.typ != null) {
            Types.Type otherType = transTy(e.typ).actual();
            if (otherType != type) {
                error(e.pos, "Types do not match, type of (" + e + " translates to " + otherType
                        + " is not of declared type " + type + ")");
            }
        }
        // add variable value mapping
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
        var transVar = transVar(e.var);
        return new ExpTy(null, transVar.ty);
    }

    /**
     * Returns a let expression
     * 
     * @param e
     * @return
     */
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

    /**
     * Returns an operator expression
     * 
     * @param e
     * @return
     */
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

    /**
     * Returns a nil expression
     * 
     * @param intExp
     * @return
     */
    ExpTy transExp(Absyn.NilExp nilExp) {
        return new ExpTy(null, NIL);
    }

    /**
     * Returns an string expression
     * 
     * @param intExp
     * @return
     */
    ExpTy transExp(Absyn.StringExp stringExp) {
        return new ExpTy(null, STRING);
    }

    /**
     * Returns an integer expression
     * 
     * @param intExp
     * @return
     */
    ExpTy transExp(Absyn.IntExp intExp) {
        return new ExpTy(null, INT);
    }

    /**
     * Returns a translated call expression
     * 
     * @param callExp
     * @return
     */
    ExpTy transExp(Absyn.CallExp callExp) {
        return new ExpTy(null, null);
    }

    /**
     * Returns a translated expression for a sequence If sequence is empty, return
     * type is void
     * 
     * @param seqExp
     * @return
     */
    ExpTy transExp(Absyn.SeqExp seqExp) {
        Types.Type returnType;
        if (seqExp.list == null) {
            returnType = VOID;
        } else {
            Absyn.ExpList expList = seqExp.list;
            // skip to end of the sequence
            while (expList.tail != null)
                expList = expList.tail;
            returnType = transExp(expList.head).ty;
        }
        return new ExpTy(null, returnType);
    }

    /**
     * Translate an array expresion, eg arrtype1 [10] of 0, where arrtype is the
     * type of the array, 10 is the size expression and 0 is the item initialiser
     * expression. The type of size must be an int The type of initialiser must be
     * the same as the array type ( arrtyp1 )
     * 
     * @param arrayExp
     * @return
     */
    ExpTy transExp(Absyn.ArrayExp arrayExp) {
        var typeSymbol = arrayExp.typ;
        var sizeExp = arrayExp.size;
        var initExp = arrayExp.init;
        // check that the type of size is an int
        var tSizeTy = transExp(sizeExp).ty;
        if (tSizeTy != INT) {
            error(arrayExp.pos, "Type mismatch: array size expression is not an int " + tSizeTy);
        }
        // Get type of expression, it should be an array and not null
        var tt = (Types.Type) env.tenv.get(typeSymbol);
        if (tt == null || !(tt.actual() instanceof Types.ARRAY)) {
            error(arrayExp.pos, "Type not found:" + typeSymbol);
        } else {
            // check that initialising expression is the same type as the array element type
            if (transExp(initExp).ty != ((Types.ARRAY)tt.actual()).element) {
                error(arrayExp.pos, "Type mismatch: array init expression is not of type " + tt);
            }
        }
        return new ExpTy(null, tt);
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
        var tigerType = (Types.Type) env.tenv.get(expressionTypeSymbol);
        if (tigerType == null) {
            error(recordExp.pos, "Unknown type " + expressionTypeSymbol);
        }
        // Loop through fieldExpLists rec{field1=value, field2=value2.....}
        var temp = (RECORD)tigerType.actual();
        for (var fel = recordExp.fields; fel != null; fel = fel.tail) {
            // translate the field expression, and do what with it ?
            var fieldExpTy = transExp(fel, temp);
            // advance to next tiger type
            temp = temp.tail;
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

    /**
     * Main dispatch function for expressions
     * @param e
     * @return
     */
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