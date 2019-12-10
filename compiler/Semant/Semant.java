package Semant;

import Absyn.FieldList;
import Absyn.FieldVar;
import Absyn.FunctionDec;
import Absyn.SimpleVar;
import Absyn.SubscriptVar;
import Absyn.TypeDec;
import ErrorMsg.ArgumentMismatchError;
import ErrorMsg.BreakNestingError;
import ErrorMsg.FieldNotDefinedError;
import ErrorMsg.FunctionNotDefinedError;
import ErrorMsg.TypeMismatchError;
import ErrorMsg.TypeNotIntError;
import ErrorMsg.TypeNotVoidError;
import ErrorMsg.UndefinedTypeError;
import ErrorMsg.UndefinedVariableError;
import ErrorMsg.VariableAssignError;
import Symbol.Symbol;
import Temp.Label;
import Translate.Access;
import Translate.Exp;
import Translate.ExpTy;
import Translate.Level;
import Translate.Translate;
import Types.ARRAY;
import Types.NAME;
import Types.RECORD;
import Util.BoolList;

public class Semant {
    private final Env env;
    private final boolean breakScope;
    private Level level;
    private final Translate translate;
    public static final Types.Type INT = new Types.INT();
    public static final Types.Type STRING = new Types.STRING();
    public static final Types.Type VOID = new Types.VOID();
    public static final Types.Type NIL = new Types.NIL();

    public Semant(final ErrorMsg.ErrorMsg err, final Level lvl) {
        this(new Env(err, lvl), false, lvl);
    }

    Semant(final Env e, boolean bs, Level lev) {
        env = e;
        breakScope = bs;
        level = lev;
        translate = new Translate();
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
    Types.Type transTy(final Absyn.RecordTy t) {
        // go through each the fields in this record type
        Types.RECORD head = null;
        Types.RECORD prev = null;
        for (Absyn.FieldList l = t.fields; l != null; l = l.tail) {
            final var cached = fetchTypeAndReport(l.typ, t.pos);
            // create record for current item
            final Types.RECORD current = new Types.RECORD(l.name, cached, null);
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
    Types.Type transTy(final Absyn.ArrayTy t) {
        final var cached = fetchTypeAndReport(t.typ, t.pos);
        return new Types.ARRAY(cached);
    }

    /**
     * Translates a type t into a native type
     * 
     * @param t
     * @return return the looked up nametype
     */
    Types.Type transTy(final Absyn.NameTy t) {
        final var cached = fetchTypeAndReport(t.name, t.pos);
        return cached;
    }

    /**
     * Dispatcher function for types
     */
    Types.Type transTy(final Absyn.Ty t) {
        if (t instanceof Absyn.NameTy)
            return transTy((Absyn.NameTy) t);
        if (t instanceof Absyn.RecordTy)
            return transTy((Absyn.RecordTy) t);
        if (t instanceof Absyn.ArrayTy)
            return transTy((Absyn.ArrayTy) t);
        throw new Error("Not Implemented " + t.getClass().getName());
    }

    /**
     * Translate a variable into a expression
     */
    ExpTy transVar(final Absyn.SimpleVar e) {
        final var x = env.venv.get(e.name);
        if (x instanceof VarEntry) {
            final VarEntry ent = (VarEntry) x;
            return new ExpTy(translate.simpleVar(ent.access, level), ent.ty);
        } else {
            env.errorMsg.add(new UndefinedVariableError(e.pos, e.name));
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
    ExpTy transVar(final Absyn.FieldVar e) {
        var varType = transVar(e.var).ty.actual();
        // TODO: This calls simpleVar eventually,
        // TODO: DOes it use its translated exp ?
        var varExp = transVar(e.var);

        if (!(varType.actual() instanceof RECORD)) {
            env.errorMsg.add(new TypeMismatchError(e.pos, varType.actual()));
        }
        for (var r = (RECORD) varType.actual(); r != null; r = r.tail) {
            if (r.fieldName == e.field) {
                return new ExpTy(null, r.fieldType.actual());
            }
        }
        env.errorMsg.add(new FieldNotDefinedError(e.pos, e.field));
        return new ExpTy(null, INT);
    }

    /**
     * Translate an array subscript item into an intermediate expression and a tiger
     * type eg anarray[int_variable], anarray is an array of type t, with element of
     * type e Subscript index should evaluate to int
     * 
     * @param e
     * @return
     */
    ExpTy transVar(final Absyn.SubscriptVar e) {
        final var indexExp = e.index;
        final var transIndexExp = transExp(indexExp);
        // translate the index expression and check its an INT
        if (transIndexExp.ty.actual() != INT) {
            env.errorMsg.add(new TypeMismatchError(e.pos, transIndexExp.ty.actual()));
        }
        // translate the variable and check its an instance of an ARRAY
        final var translatedArrayVar = transVar(e.var);
        final Types.Type elementType = translatedArrayVar.ty.actual();
        if (!(elementType.actual() instanceof ARRAY)) {
            env.errorMsg.add(new TypeMismatchError(e.pos, elementType.actual()));
        }
        return new ExpTy(null, ((ARRAY) elementType).element);
    }

    /**
     * Dispatcher function for var types
     * 
     * @param e
     * @return
     */
    ExpTy transVar(final Absyn.Var e) {
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

    /**
     * Translate a function declaration into an intermediate expresion:w
     * 
     * @param
     * @return
     */
    Exp transDec(final Absyn.FunctionDec e) {
        FunctionDec current = e;
        // if we have already processed this function while
        // to handling recursive functions
        // add function entry to environment tables so it
        // is available for lookup inside the function body
        // this is to facilitate recursive function calls
        do {
            // get the functions return type
            var functionReturnType = current.result != null ? transTy(current.result).actual() : Semant.VOID;
            // add a new nesting level into the function entry
            // add allocations for the parameters to be passed
            // to this function
            // creates a new level and a new frame and allocates space for the formal parameters
            // for each formal parameter, we need to get its frame access
            var functionEntry = new FunEntry(new Level(level, e.name, buildBoolList(current.params)), new Label(),
                    transTypeFields(current.params), functionReturnType);
            // add function entry into the value environment
            env.venv.put(current.name, functionEntry);
            current = current.next;
        } while (current != null);
        current = e;
        do {
            //I think we begin scope here because we
            //are processing the function body in this 
            //loop
            env.venv.beginScope();
            // get the new level for this function
            var newLevel = ((FunEntry) env.venv.get(current.name)).level;
            var vent = (FunEntry) env.venv.get(current.name);
            var translateAccess = newLevel.formals;
            for (var p = current.params; p != null; p = p.tail) {
                //TODO: Check if this is right, Passing in the translate access to the var entry
                var varEntry = new VarEntry(env.tenv.get(p.typ).actual(), translateAccess.head);
                env.venv.put(p.name, varEntry);
                translateAccess = translateAccess.tail;
            }
            final var transBody = new Semant(env, breakScope, newLevel).transExp(current.body);
            if (!transBody.ty.coerceTo(vent.result)) {
                env.errorMsg.add(new TypeMismatchError(e.pos, transBody.ty.actual(), vent.result));
            }
            env.venv.endScope();
            current = current.next;
        } while (current != null);
        return null;
    }

    /**
     * Recursive support for declarations
     * 
     * @param e
     * @return
     */
    Exp transDec(final Absyn.TypeDec e) {
        // we assume that contiguous declarations may be recursive
        // process headers first to capture a reference to type name
        TypeDec next = e;
        do {
            final var namedType = new Types.NAME(next.name);
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
            final var mappedType = transTy(next.ty);
            // get the named type from the env
            final var namedType = (NAME) env.tenv.get(next.name);
            namedType.bind(mappedType);
            next = e.next;
        } while (next != null);
        return null;
    }

    /**
     * Translates a variable declaration into an intermediate expression and tiger
     * type
     * 
     * @param e
     * @return
     */
    Exp transDec(final Absyn.VarDec e) {
        // var varname:vartype = expression
        final ExpTy initExpTy = transExp(e.init);
        // get the expression type
        final Types.Type type = initExpTy.ty.actual();
        final Types.Type otherType = e.typ != null ? transTy(e.typ).actual() : initExpTy.ty.actual();
        // if the expression type is not null
        if (e.typ != null) {
            if (otherType != type) {
                env.errorMsg.add(new TypeMismatchError(e.pos, otherType.actual(), initExpTy.ty.actual()));
            }
        }
        // check that any variable that is assigned to nil is a record type
        if (initExpTy.ty.actual() == NIL && !(otherType.actual() instanceof RECORD)) {
            env.errorMsg.add(new TypeMismatchError(e.pos, otherType.actual(), initExpTy.ty.actual()));
        }
        // allocate space for this variable
        var translateAccess = level.allocLocal(e.escape);
        // add variable value mapping
        var varEntry = new VarEntry(type, translateAccess);
        env.venv.put(e.name, varEntry);
        return null;
    }

    /**
     * Dispatcher for declaration types. Note that the symbol tables are populated
     * in these methods
     */
    Exp transDec(final Absyn.Dec e) {
        if (e instanceof Absyn.VarDec)
            return transDec((Absyn.VarDec) e);
        else if (e instanceof Absyn.TypeDec)
            return transDec((Absyn.TypeDec) e);
        else if (e instanceof Absyn.FunctionDec)
            return transDec((Absyn.FunctionDec) e);
        else
            throw new Error("Not Implemented " + e.getClass().getName());
    }

    /**
     * Translate a var expressions into ir code
     * 
     * @param e
     * @return
     */
    ExpTy transExp(final Absyn.VarExp e) {
        final var transVar = transVar(e.var);
        return new ExpTy(null, transVar.ty);
    }

    /**
     * Returns a let expression
     * 
     * @param e
     * @return
     */
    ExpTy transExp(final Absyn.LetExp e) {
        env.venv.beginScope();
        env.tenv.beginScope();
        //TODO: need to ensure that we dont include duplicate
        //functions, where are there are functions that
        //are contigous
        for (Absyn.DecList p = e.decs; p != null; p = p.tail)
            transDec(p.head);
        final ExpTy et = transExp(e.body);
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
    ExpTy transExp(final Absyn.OpExp e) {

        var transExpLeft = transExp(e.left);
        var transExpRight = transExp(e.right);
        switch (e.oper) {
        case Absyn.OpExp.PLUS:
        case Absyn.OpExp.MINUS:
        case Absyn.OpExp.MUL:
        case Absyn.OpExp.DIV:
        case Absyn.OpExp.LE:
        case Absyn.OpExp.GE:
        case Absyn.OpExp.LT:
        case Absyn.OpExp.GT:
            if (!transExpLeft.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpLeft.ty));
            }
            if (!transExpRight.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpRight.ty));
            }

        case Absyn.OpExp.EQ:
            //the order here is important, expRigth.coerceTo(expLeft) is not the same as the reverse

            if (!transExpRight.ty.coerceTo(transExpLeft.ty)) {
                env.errorMsg.add(new TypeMismatchError(e.left.pos, transExpLeft.ty, transExpRight.ty));
            }

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
    ExpTy transExp(final Absyn.NilExp nilExp) {
        return new ExpTy(null, NIL);
    }

    /**
     * Returns an string expression
     * 
     * @param intExp
     * @return
     */
    ExpTy transExp(final Absyn.StringExp stringExp) {
        return new ExpTy(null, STRING);
    }

    /**
     * Returns an integer expression
     * 
     * @param intExp
     * @return
     */
    ExpTy transExp(final Absyn.IntExp intExp) {
        return new ExpTy(null, INT);
    }

    /**
     * Returns a translated call expression. Checks that the order of arguments
     * matches the formals
     * 
     * @param callExp
     * @return
     */
    ExpTy transExp(final Absyn.CallExp callExp) {
        final Entry x = (Entry) (env.venv.get(callExp.func));
        if (x instanceof FunEntry) {
            // evaluate each expression in arg
            // list and check if it is correct type
            final var ent = (FunEntry) x;
            var argExpList = callExp.args;
            for (RECORD fmlType = ((FunEntry) x).formals; fmlType != null; fmlType = fmlType.tail) {
                if (argExpList == null) {
                    // error(callExp.pos, "Supplied argument list is too short");
                    env.errorMsg.add(new ArgumentMismatchError(callExp.pos, null, null));
                    break;
                }
                final var transArg = transExp(argExpList.head);
                if (transArg.ty.actual() != fmlType.fieldType.actual()) {
                    // error(callExp.pos, "Incorrect type in function ");
                    env.errorMsg.add(
                            new ArgumentMismatchError(callExp.pos, fmlType.fieldType.actual(), transArg.ty.actual()));
                }
                argExpList = argExpList.tail;
            }
            if (argExpList != null) {
                env.errorMsg.add(new ArgumentMismatchError(callExp.pos, null, null));
                // error(callExp.pos, "Supplied argument list is too long");
            }
            return new ExpTy(null, ent.result);
        } else {
            env.errorMsg.add(new FunctionNotDefinedError(callExp.pos, callExp.func));
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
    ExpTy transExp(final Absyn.SeqExp seqExp) {
        Types.Type returnType;
        if (seqExp.list == null) {
            returnType = VOID;
        } else {
            Absyn.ExpList expList = seqExp.list;
            if (expList.head == null && expList.tail == null) {
                returnType = VOID;
            }
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
    ExpTy transExp(final Absyn.ArrayExp arrayExp) {
        final var typeSymbol = arrayExp.typ;
        final var sizeExp = arrayExp.size;
        final var initExp = arrayExp.init;
        // check that the type of size is an int
        final var tSizeTy = transExp(sizeExp).ty;
        if (tSizeTy != INT) {
            env.errorMsg.add(new TypeMismatchError(arrayExp.pos, tSizeTy, Semant.INT));
        }
        // Get type of expression, it should be an array and not null
        final var tt = (Types.Type) env.tenv.get(typeSymbol);
        if (tt == null || !(tt.actual() instanceof Types.ARRAY)) {
            env.errorMsg.add(new TypeMismatchError(arrayExp.pos, tt));
        } else {
            // check that initialising expression is the
            // same type as the array element type
            if (transExp(initExp).ty != ((Types.ARRAY) tt.actual()).element) {
                // error(arrayExp.pos, "Type mismatch: array init expression is not of type " +
                // tt);
                env.errorMsg.add(new TypeMismatchError(arrayExp.pos, tt));
            }
            return new ExpTy(null, tt.actual());
        }
        return new ExpTy(null, VOID);
    }

    /**
     * Translate a record expression. example rectype {name=\"Nobody\", age=12345} A
     * record expression contains a symbol for the type and one or more name value
     * pairs that are a symbol and a initialising expression. Type validation checks
     * 1) Check the type is valid 'rectype' 2) Check the fields are the correct type
     * Notes & Questions 1) Do we need to consider nested record types ?
     * 
     * @param recordExp
     * @return
     */
    ExpTy transExp(final Absyn.RecordExp recordExp) {
        final var expressionTypeSymbol = recordExp.typ;
        final var tigerType = (Types.Type) env.tenv.get(expressionTypeSymbol);
        if (tigerType == null) {
            env.errorMsg.add(new UndefinedTypeError(recordExp.pos, tigerType));
        }
        // Loop through fieldExpLists rec{field1=value, field2=value2.....}
        if (!(tigerType.actual() instanceof RECORD)) {
            env.errorMsg.add(new TypeMismatchError(recordExp.pos, tigerType));
        } else {

            var temp = (RECORD) tigerType.actual();
            for (var fel = recordExp.fields; fel != null; fel = fel.tail) {
                // TODO: translate the field expression, and do what with it ?
                final var fieldExpTy = transExp(fel, temp);
                // advance to next tiger type
                temp = temp.tail;
            }
        }
        return new ExpTy(null, tigerType);
    }

    /**
     * Translates an assignment expession into a native type and intermediate code
     * 
     * @param assignExp
     * @return
     */
    ExpTy transExp(final Absyn.AssignExp assignExp) {
        var transVar = transVar(assignExp.var); // left value
        var transExp = transExp(assignExp.exp); // right value
        //check to see if we are assigning to a readonly variable
        if (assignExp.var instanceof SimpleVar) {
            var simpleVar = (SimpleVar) assignExp.var;
            var varEntry = (VarEntry) env.venv.get(simpleVar.name);
            if (varEntry.readOnly) {
                env.errorMsg.add(new VariableAssignError(assignExp.pos, simpleVar.name));
            }
        } else if(assignExp.var instanceof SubscriptVar ){
            var subscriptVar = (SubscriptVar)assignExp.var;
            

        } else if(assignExp.var instanceof FieldVar ){

            var fieldVar = (FieldVar)assignExp.var;

        } else {
            throw new RuntimeException("Unhandled type " + assignExp.var);
        }

        if (transVar.ty.actual() != transExp.ty.actual()) {
            env.errorMsg.add(new TypeMismatchError(assignExp.pos, transVar.ty.actual(), transExp.ty.actual()));
        }
        return new ExpTy(null, Semant.VOID);
    }

    /**
     * Returns a translated for loop. When evaluating the body of the loop, a new
     * instance of Semant is created with its break scope variable set to true. This
     * indicates that break statements are legal inside this expression
     * 
     * @param forExp
     * @return
     */
    ExpTy transExp(final Absyn.ForExp forExp) {
        env.tenv.beginScope();
        env.venv.beginScope();
        // add the loop start value to value table
        // the loop index definately doesn't escape
        var translateAccess = level.allocLocal(false);
        var startVarEntry = new VarEntry(INT, translateAccess);
        env.venv.put(forExp.var.name, startVarEntry);
        var lowTy = transExp(forExp.var.init);
        if (lowTy.ty.actual() != Semant.INT) {
            env.errorMsg.add(new TypeNotIntError(forExp.var.pos, lowTy.ty.actual()));
        }
        VarEntry varEntry = (VarEntry) env.venv.get(forExp.var.name);
        varEntry.readOnly = true;
        var hiTy = transExp(forExp.hi);
        if (hiTy.ty.actual() != Semant.INT) {
            env.errorMsg.add(new TypeNotIntError(forExp.hi.pos, hiTy.ty.actual()));
        }
        var transBody = new Semant(env, true, level).transExp(forExp.body);
        if (transBody.ty.actual() != Semant.VOID) {
            env.errorMsg.add(new TypeNotVoidError(forExp.body.pos, transBody.ty.actual()));
        }
        env.venv.endScope();
        env.tenv.endScope();
        return new ExpTy(null, Semant.VOID);
    }

    /**
     * Returns a translated while loop. When evaluating the body of the loop, a new
     * instance of Semant is created with its break scope variable set to true. This
     * indicates that break statements are legal inside this expression
     * 
     * @param whileExp
     * @return
     */
    ExpTy transExp(final Absyn.WhileExp whileExp) {
        env.tenv.beginScope();
        env.venv.beginScope();
        var testExp = transExp(whileExp.test);
        if (testExp.ty.actual() != INT) {
            env.errorMsg.add(new TypeMismatchError(whileExp.test.pos, testExp.ty.actual(), Semant.INT));
        }
        var transBody = new Semant(env, true, level).transExp(whileExp.body);
        if (transBody.ty.actual() != Semant.VOID) {
            env.errorMsg.add(new TypeMismatchError(whileExp.pos, transBody.ty.actual(), Semant.VOID));
        }
        env.venv.endScope();
        env.tenv.endScope();
        return new ExpTy(null, Semant.VOID);

    }

    ExpTy transExp(final Absyn.IfExp ifExp) {
        var testExp = transExp(ifExp.test);
        if (testExp.ty.actual() != INT) {
            env.errorMsg.add(new TypeMismatchError(ifExp.test.pos, testExp.ty.actual(), Semant.INT));
        }
        var thenExp = transExp(ifExp.thenclause);
        var elseExp = ifExp.elseclause != null ? transExp(ifExp.elseclause) : null;
        if (elseExp != null && !elseExp.ty.coerceTo(thenExp.ty)) {
            env.errorMsg.add(new TypeMismatchError(ifExp.thenclause.pos, thenExp.ty.actual(), elseExp.ty.actual()));
            return new ExpTy(null, Semant.VOID);
        } else if (elseExp != null && elseExp.ty.coerceTo(thenExp.ty)) {
            return new ExpTy(null, elseExp.ty.actual());
        } else {
            return new ExpTy(null, Semant.VOID);
        }
    }

    /**
     * Returns a translated break expression. This expression must be nested within
     * a while or for loop
     * 
     * @param breakExp
     * @return
     */
    ExpTy transExp(final Absyn.BreakExp breakExp) {
        if (!breakScope) {
            env.errorMsg.add(new BreakNestingError(breakExp.pos));
        }
        return new ExpTy(null, Semant.VOID);
    }

    /**
     * Translates a fieldExpList to its tiger type A fieldExpList is of form
     * property1=expression1 Typechecker should check that the type of property1 is
     * the same as the type of expression1 TODO: Improve field list checking.
     * 
     * @param fel
     * @return
     */
    ExpTy transExp(final Absyn.FieldExpList fel, final Types.RECORD recordType) {
        if (recordType == null) {
            // error(fel.pos, "Unknown symbol: " + fel.name);
            env.errorMsg.add(new UndefinedVariableError(fel.pos, fel.name));
            return new ExpTy(null, null);
        }
        // get type of field for this record type
        final var fieldType = recordType.fieldType;
        final var fieldName = recordType.fieldName;
        if (fel.name != fieldName) {
            env.errorMsg.add(new UndefinedVariableError(fel.pos, fel.name));
        }
        // get initialising expression
        final var initExp = fel.init;
        // get transExp of initialising expression
        final var transExp = transExp(initExp);
        // compare type of field with type of expression
        if (fieldType.actual() != transExp.ty.actual()) {
            // error(fel.pos, "Type mismatch: " + fieldType + " != " + transExp.ty);
            env.errorMsg.add(new TypeMismatchError(fel.pos, fieldType, transExp.ty));
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
    public ExpTy transExp(final Absyn.Exp e) {
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
        else if (e instanceof Absyn.AssignExp)
            return transExp((Absyn.AssignExp) e);
        else if (e instanceof Absyn.WhileExp)
            return transExp((Absyn.WhileExp) e);
        else if (e instanceof Absyn.ForExp)
            return transExp((Absyn.ForExp) e);
        else if (e instanceof Absyn.IfExp)
            return transExp((Absyn.IfExp) e);
        else if (e instanceof Absyn.BreakExp)
            return transExp((Absyn.BreakExp) e);
        else if (e instanceof Absyn.NilExp)
            return transExp((Absyn.NilExp) e);
        else
            throw new Error("Cannot handle " + e.getClass().getName());
    }

    private BoolList buildBoolList(final FieldList fields) {
        BoolList head = null;
        BoolList prev = null;
        for (Absyn.FieldList l = fields; l != null; l = l.tail) {
            final BoolList current = new BoolList(l.escape, null);
            if (head == null)
                head = current;
            else
                prev.tail = current; // insert the current item at the end of the previous
            prev = current;
        }
        return head;
    }

    private RECORD transTypeFields(final FieldList fields) {
        Types.RECORD head = null;
        Types.RECORD prev = null;
        for (Absyn.FieldList l = fields; l != null; l = l.tail) {
            final var cached = fetchTypeAndReport(l.typ, l.pos);
            final Types.RECORD current = new Types.RECORD(l.name, cached, null);
            if (head == null)
                head = current;
            else
                prev.tail = current; // insert the current item at the end of the previous
            prev = current;
        }
        return head;
    }

    private Types.Type fetchTypeAndReport(final Symbol sym, final int pos) {
        final Types.Type cached = (Types.Type) env.tenv.get(sym);
        if (cached == null) {
            env.errorMsg.add(new UndefinedVariableError(pos, sym));
            return null;
        }
        return cached;
    }
}