package Semant;

import Absyn.FieldList;
import Absyn.FunctionDec;
import Absyn.Ty;
import Absyn.TypeDec;
import Symbol.Symbol;
import Translate.Exp;
import Translate.ExpTy;
import Types.ARRAY;
import Types.NAME;
import Types.RECORD;
import Types.Type;

public class Semant {
    private final Env env;
    public static final Types.Type INT = new Types.INT();
    public static final Types.Type STRING = new Types.STRING();
    public static final Types.Type VOID = new Types.VOID();
    public static final Types.Type NIL = new Types.NIL();

    private void error(int pos, String message) {
        env.errorMsg.error(pos, message);
    }

    private Types.Type fetchTypeAndReport(final Symbol sym, final int pos) {
        Types.Type cached = (Types.Type) env.tenv.get(sym);
        if (cached == null) {
            error(pos, "No type found for symbol:" + sym);
            return null;
        }
        return cached;
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
     * Translates an abstract syntax record type into a tiger record type this
     * represents translating a field list {name: string, age: int} into its
     * corresponding native type.
     * 
     * @param t
     * @return a native type
     */
    Types.Type transTy(Absyn.RecordTy t) {
        // go through each the fields in this record type
        Types.RECORD head = null;
        Types.RECORD prev = null;
        for (Absyn.FieldList l = t.fields; l != null; l = l.tail) {
            var cached = fetchTypeAndReport(l.typ, t.pos);
            // create record for current item
            Types.RECORD current = new Types.RECORD(l.name, cached, null);
            // set the previous items tail to the current item
            // if we dont do this, the list will be reversed when
            // used by the recordexp transExp
            if (head == null)
                head = current;
            else
                prev.tail = current; // insert the current item at the end of the previous
            prev = current;
        }
        return head;
    }

    /**
     * Translates an abstract syntax array type into a native type eg array of int
     * 
     * @param t
     * @return
     */
    Types.Type transTy(Absyn.ArrayTy t) {
        var cached = fetchTypeAndReport(t.typ, t.pos);
        return new Types.ARRAY(cached);
    }

    /**
     * Translates a type t into a native type
     * 
     * @param t
     * @return return the looked up nametype
     */
    Types.Type transTy(Absyn.NameTy t) {
        var cached = fetchTypeAndReport(t.name, t.pos);
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
     * 
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
     * Translates an abstract field variable into a intermediate expression and a
     * tiger type for example { name = "david knott",....}, field is name and david
     * is variable. Type checker should ensure that the type of var is the same as
     * the type of the field.
     * 
     * @param e
     * @return
     */
    ExpTy transVar(Absyn.FieldVar e) {

        throw new Error("Not Implemented " + e.getClass().getName());
    }

    /**
     * Translate an array subscript item into an intermediate expression and a tiger
     * type eg anarray[int_variable], anarray is an array of type t, with element of
     * type e Subscript index should evaluate to int
     * 
     * @param e
     * @return
     */
    ExpTy transVar(Absyn.SubscriptVar e) {
        var indexExp = e.index;
        // translate the index expression and check its an INT
        if (transExp(indexExp).ty.actual() != INT) {
            error(e.pos, "Subscript expression is not of type int: ");
        }
        // translate the variable and check its an instance of an ARRAY
        var translatedArrayVar = transVar(e.var);
        Types.Type elementType = translatedArrayVar.ty.actual();
        if (!(elementType instanceof ARRAY)) {
            error(e.pos, "Type of variable is not an array");
        }
        return new ExpTy(null, elementType);
    }

    RECORD transTypeFields(FieldList fields) {
        Types.RECORD head = null;
        Types.RECORD prev = null;
        for (Absyn.FieldList l = fields; l != null; l = l.tail) {
            var cached = fetchTypeAndReport(l.typ, l.pos);
            Types.RECORD current = new Types.RECORD(l.name, cached, null);
            if (head == null)
                head = current;
            else
                prev.tail = current; // insert the current item at the end of the previous
            prev = current;
        }
        return head;
    }

    /**
     * 
     * Translate a function declaration into an intermediate expresion:w
     * 
     * @param
     * @return
     */
    Exp transDec(Absyn.FunctionDec e) {
        // process the function headers first
        // e.body - function body , type of this is return type of function ;
        // e.name - name of function
        // e.next - related recursive function
        // e.params - formals for the function
        // e.result - type of result, may be null
        // for a function, what should we type check ?
        // need to record the types of the formals
        // need to check that the return type matches the
        // body return type
        FunctionDec current = e;
        do {

            // add function first
            env.venv.put(current.name,
                    new FunEntry(transTypeFields(current.params), current.result != null ? transTy(current.result).actual() : null));
            current = current.next;
        } while (current != null);
        current = e;
        do {

            env.venv.beginScope();
            for (var p = current.params; p != null; p = p.tail) {
                // add formals as vars within function scope
                env.venv.put(p.name, new VarEntry((Types.Type) env.tenv.get(p.typ)));
            }
            var transBody = transExp(current.body);
            if (current.result != null && transBody.ty.actual() != transTy(current.result)) {
                error(current.pos, "Return type does not match body type");
            }
            env.venv.endScope();
            current = current.next;
        } while (current != null);




        /*
         * next = e; do { var translatedBody = transExp(next.body);
         * System.out.println(translatedBody);
         * 
         * next = e.next; } while (next != null);
         */

        return null;
    }

    /**
     * Recursive support for declarations
     * 
     * @param e
     * @return
     */
    Exp transDec(Absyn.TypeDec e) {
        // we assume that contiguous declarations may be recursive
        // process headers first to capture a reference to type name
        TypeDec next = e;
        do {
            var namedType = new Types.NAME(next.name);
            // stick it into the type env with a null type binding
            // we can use the name type for other declarations that
            // depend on it, and set its binding type later.
            env.tenv.put(next.name, namedType);
            next = e.next;
        } while (next != null);
        // process type declarations expressions, where the type name above
        // may be used recursively
        next = e;
        do {
            // set the name types actual type to the
            // type returned by the the transTy function
            var mappedType = transTy(next.ty);
            // get the named type from the env
            var namedType = (NAME) env.tenv.get(next.name);
            namedType.bind(mappedType);
            next = e.next;
        } while (next != null);
        /*
         * //created a named type for the type definition var namedType = new
         * Types.NAME(e.name); //stick it into the type env so that its available for
         * //look ups by the transTy function env.tenv.put(e.name, namedType); var
         * mappedType = transTy(e.ty); //set the name types actual type to the //type
         * returned by the the transTy function namedType.bind(mappedType);
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
        Entry x = (Entry) (env.venv.get(callExp.func));
        if (x instanceof FunEntry) {
            //evaluate each expression in arg list and check if
            //correct type
            var ent = (FunEntry) x;
            var argExpList = callExp.args;
            for(RECORD argType = ((FunEntry)x).formals; argType != null; argType = argType.tail){
                if(argExpList == null){
                    error(callExp.pos, "Supplied argument list is too short");
                    break;
                }
                var transArg = transExp(argExpList.head);
                if (transArg.ty.actual() != argType.fieldType.actual()) {
                    error(callExp.pos, "Incorrect type in function ");
                }
                argExpList = argExpList.tail;
            }
            if(argExpList != null){
                error(callExp.pos, "Supplied argument list is too long");
            }
            return new ExpTy(null, ent.result);
        } else {
            error(callExp.pos, "Undefined function: " + callExp.func);
            return new ExpTy(null, INT);
        }
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
            if (transExp(initExp).ty != ((Types.ARRAY) tt.actual()).element) {
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
        var temp = (RECORD) tigerType.actual();
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
     * 
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