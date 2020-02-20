package Semant;

import Absyn.AssignExp;
import Absyn.BreakExp;
import Absyn.DecList;
import Absyn.ExpList;
import Absyn.FieldList;
import Absyn.FieldVar;
import Absyn.ForExp;
import Absyn.FunctionDec;
import Absyn.IfExp;
import Absyn.IntExp;
import Absyn.LetExp;
import Absyn.NameTy;
import Absyn.OpExp;
import Absyn.SeqExp;
import Absyn.SimpleVar;
import Absyn.SubscriptVar;
import Absyn.TypeDec;
import Absyn.VarDec;
import Absyn.VarExp;
import Absyn.WhileExp;
import ErrorMsg.ArgumentMismatchError;
import ErrorMsg.BreakNestingError;
import ErrorMsg.FieldNotDefinedError;
import ErrorMsg.FunctionNotDefinedError;
import ErrorMsg.TypeMismatchError;
import ErrorMsg.TypeNotIntError;
import ErrorMsg.UndefinedTypeError;
import ErrorMsg.UndefinedVariableError;
import ErrorMsg.VariableAssignError;
import Symbol.Symbol;
import Temp.Label;
import Translate.Exp;
import Translate.ExpTy;
import Translate.ExpTyList;
import Translate.Frag;
import Translate.Level;
import Translate.Translate;
import Types.ARRAY;
import Types.NAME;
import Types.RECORD;
import Util.BoolList;

public class Semant {
    private final Env env;
    private final Label breakScopeLabel;
    private final Translate translate;
    private Level level;
    public static final Types.Type INT = new Types.INT();
    public static final Types.Type STRING = new Types.STRING();
    public static final Types.Type VOID = new Types.VOID();
    public static final Types.Type NIL = new Types.NIL();

    public Semant(final ErrorMsg.ErrorMsg err, final Level lvl, Translate trans) {
        this(new Env(err, lvl), null, lvl, trans);
    }

    Semant(final Env e, Label bsl, Level lev, Translate trans) {
        env = e;
        breakScopeLabel = bsl;
        level = lev;
        translate = trans;
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
     * Main method to translate and type check ast
     * 
     * @param absyn
     * @return
     */
    public Frag transProg(Absyn.Exp absyn) {
        var trans = this.transExp(absyn);
        translate.procEntryExit(level, trans.exp);
        return translate.getResult();
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
        var fieldType = getType(t.fields.typ, t.pos);
        var record = new Types.RECORD(t.fields.name, fieldType, null);
        var item = t.fields.tail;
        while (item != null) {
            fieldType = getType(item.typ, item.pos);
            record.append(item.name, fieldType);
            item = item.tail;
        }
        return record;
    }

    /**
     * Translates an abstract syntax array type into a native type eg array of int
     * 
     * @param t
     * @return
     */
    Types.Type transTy(final Absyn.ArrayTy t) {
        final var cached = getType(t.typ, t.pos);
        return new Types.ARRAY(cached);
    }

    /**
     * Translates a type t into a native type
     * 
     * @param t
     * @return return the looked up nametype
     */
    Types.Type transTy(final Absyn.NameTy t) {
        final var cached = getType(t.name, t.pos);
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
     * Translate a variable into a IR
     */
    ExpTy transVar(final Absyn.SimpleVar e) {
        final var x = env.venv.get(e.name);
        if (x instanceof VarEntry) {
            final VarEntry ent = (VarEntry) x;
            var translateExp = translate.simpleVar(ent.access, level);
            return new ExpTy(translateExp, ent.ty);
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
        final var varExp = transVar(e.var);
        final var varType = varExp.ty.actual();
        if (!(varType.actual() instanceof RECORD)) {
            env.errorMsg.add(new TypeMismatchError(e.pos, varType.actual()));
        }
        // iterate through each record field till we find a match
        int i = 0;
        // TODO: Refactor node add
        for (var r = (RECORD) varType.actual(); r != null; r = r.tail) {
            if (r.fieldName == e.field) {
                var translateExp = translate.fieldVar(varExp.exp, i, level);
                return new ExpTy(translateExp, r.fieldType.actual());
            }
            i++;
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
            //TODO: tidy this up
            return new ExpTy(translate.Noop(), VOID);
        }
        // translate the variable and check its an instance of an ARRAY
        final var translatedArrayVar = transVar(e.var);
        final Types.Type elementType = translatedArrayVar.ty.actual();
        if (!(elementType.actual() instanceof ARRAY)) {
            env.errorMsg.add(new TypeMismatchError(e.pos, elementType.actual()));
            //TODO: tidy this up
            return new ExpTy(translate.Noop(), VOID);
        }
        // type checking is complete, translate to IL
        var translateExp = translate.subscriptVar(transIndexExp, translatedArrayVar, level);
        return new ExpTy(translateExp, ((ARRAY) elementType).element);
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
        // if we have already processed this function while
        // to handling recursive functions
        // add function entry to environment tables so it
        // is available for lookup inside the function body
        // this is to facilitate recursive function calls
        FunctionDec current = e;
        do {
            // get the functions return type
            var functionReturnType = current.result != null ? transTy(current.result).actual() : Semant.VOID;
            // add a new nesting level into the function entry
            // add allocations for the parameters to be passed
            // to this function
            // creates a new level and a new frame and allocates
            // space for the formal parameters
            // for each formal parameter, we need to get its frame access
            var functionEntry = new FunEntry(new Level(level, e.name, getBoolList(current.params)), new Label(),
                    getRecordType(current.params), functionReturnType);
            // add function entry into the value environment
            env.venv.put(current.name, functionEntry);
            current = current.next;
        } while (current != null);
        current = e;
        ExpTy firstFunction = null;
        do {
            // I think we begin scope here because we
            // are processing the function body in this loop
            env.venv.beginScope();
            // get the new level for this function
            var newLevel = ((FunEntry) env.venv.get(current.name)).level;
            var vent = (FunEntry) env.venv.get(current.name);
            // iterate formals adding access to the created var entries
            var translateAccess = translate.formals(newLevel.formals);
            for (var p = current.params; p != null; p = p.tail) {
                var varEntry = new VarEntry(env.tenv.get(p.typ).actual(), translateAccess.head);
                env.venv.put(p.name, varEntry);
                translateAccess = translateAccess.tail;
            }
            var transBody = new Semant(env, breakScopeLabel, newLevel, translate).transExp(current.body);
            if (firstFunction == null) {
                firstFunction = transBody;
            }
            if (!transBody.ty.coerceTo(vent.result)) {
                env.errorMsg.add(new TypeMismatchError(e.pos, transBody.ty.actual(), vent.result));
            }
            env.venv.endScope();
            current = current.next;
        } while (current != null);
        // add the fragment to the list
        var body = translate.functionBody(level, firstFunction);
        translate.procEntryExit(level, body);
        return translate.Noop();
    }

    /**
     * Recursive support for declarations No intermediate code is generated here
     * 
     * @param e
     * @return
     */
    Exp transDec(final Absyn.TypeDec e) {
        // we assume that contiguous declarations may be recursive
        // process headers first to capture a reference to type name
        TypeDec next = e;
        do {
            var namedType = new Types.NAME(next.name);
            // stick it into the type env with a null type binding
            // we can use the name type for other declarations that
            // depend on it, and set its binding type later.
            env.tenv.put(next.name, namedType);
            next = next.next;
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
            next = next.next;
        } while (next != null);
        //check that all namedTypes have a binding
        for(var o = e; o != null; o = o.next) {
            var namedType = (NAME) env.tenv.get(o.name);
            if(namedType.isLoop()){
                throw new Error("Loop");
            }
        }
        return translate.Noop();
    }

    /**
     * Translates a variable declaration into an intermediate expression
     * 
     * @param e
     * @return
     */
    Exp transDec(final Absyn.VarDec e) {
        ExpTy initExpTy = transExp(e.init);
        //TODO: Tidy this up, this happens when the
        //init expression cannot be translated
        if(initExpTy.exp == null){
            return translate.Noop();
        }
        Types.Type type = initExpTy.ty.actual();
        Types.Type otherType = e.typ != null ? transTy(e.typ).actual() : initExpTy.ty.actual();
        // if the expression type is not null
        if (e.typ != null) {
            if(!type.coerceTo(otherType)){
                env.errorMsg.add(new TypeMismatchError(e.pos, otherType.actual(), initExpTy.ty.actual()));
            }
        }
        // check that any variable that is assigned to nil is a record type
        if (initExpTy.ty.actual() == NIL && !(otherType.actual() instanceof RECORD)) {
            env.errorMsg.add(new TypeMismatchError(e.pos, otherType.actual(), initExpTy.ty.actual()));
        }
        // allocate space for this variable
        var translateAccess = level.allocLocal(e.escape);
        var varEntry = new VarEntry(type, translateAccess);
        env.venv.put(e.name, varEntry);
        return translate.transDec(level, translateAccess, initExpTy.exp);
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
     * Translate a var expressions into ir code and type
     * 
     * @param e
     * @return
     */
    ExpTy transExp(final Absyn.VarExp e) {
        final var transVar = transVar(e.var);
        return new ExpTy(translate.varExp(transVar), transVar.ty);
    }

    /**
     * Returns a let expression
     * 
     * IR ?
     * 
     * @param e
     * @return
     */
    ExpTy transExp(final Absyn.LetExp e) {
        env.venv.beginScope();
        env.tenv.beginScope();
        ExpTyList irDecList = null;
        var decList = e.decs;
        while(decList != null){
            var transDec = transDec(decList.head);
            var et = new ExpTy(transDec, Semant.VOID);
            if(irDecList == null){
                irDecList = new ExpTyList(et);
            } else {
                irDecList.append(et);
            }
            decList = decList.tail;
        }
        ExpTy irBody = transExp(e.body);
        var irLet = translate.letE(irDecList, irBody);
        env.tenv.endScope();
        env.venv.endScope();
        return new ExpTy(irLet, irBody.ty);
    }

    /**
     * Returns an operator expression
     * 
     * @param e
     * @return
     */
    ExpTy transExp(final Absyn.OpExp e) {
        // TODO: implement this correctly
        var transExpLeft = transExp(e.left);
        var transExpRight = transExp(e.right);
        switch (e.oper) {
        case Absyn.OpExp.PLUS:
            if (!transExpLeft.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpLeft.ty));
            }
            if (!transExpRight.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpRight.ty));
            }
            return new ExpTy(translate.binaryOperator(0, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.MINUS:
            if (!transExpLeft.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpLeft.ty));
            }
            if (!transExpRight.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpRight.ty));
            }
            return new ExpTy(translate.binaryOperator(1, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.MUL:
            if (!transExpLeft.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpLeft.ty));
            }
            if (!transExpRight.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpRight.ty));
            }
            return new ExpTy(translate.binaryOperator(2, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.DIV:
            if (!transExpLeft.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpLeft.ty));
            }
            if (!transExpRight.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpRight.ty));
            }
            return new ExpTy(translate.binaryOperator(3, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.LE:
            if (!transExpLeft.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpLeft.ty));
            }
            if (!transExpRight.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpRight.ty));
            }
            return new ExpTy(translate.relativeOperator(4, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.GE:
            if (!transExpLeft.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpLeft.ty));
            }
            if (!transExpRight.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpRight.ty));
            }
            return new ExpTy(translate.relativeOperator(5, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.LT:
            if (!transExpLeft.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpLeft.ty));
            }
            if (!transExpRight.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpRight.ty));
            }
            return new ExpTy(translate.relativeOperator(6, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.GT:
            if (!transExpLeft.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpLeft.ty));
            }
            if (!transExpRight.ty.coerceTo(Semant.INT)) {
                env.errorMsg.add(new TypeNotIntError(e.left.pos, transExpRight.ty));
            }
            return new ExpTy(translate.relativeOperator(7, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.EQ:
            // the order here is important,
            // expRigth.coerceTo(expLeft) is not the same as
            // the reverse
            if (!transExpRight.ty.coerceTo(transExpLeft.ty)) {
                env.errorMsg.add(new TypeMismatchError(e.left.pos, transExpLeft.ty, transExpRight.ty));
            }
            return new ExpTy(translate.equalsOperator(8, transExpLeft, transExpRight), INT);
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
        return new ExpTy(translate.nil(), NIL);
    }

    /**
     * Returns an string expression
     * 
     * @param stringExp
     * @return
     */
    ExpTy transExp(final Absyn.StringExp stringExp) {
        return new ExpTy(translate.string(stringExp.value, level), STRING);
    }

    /**
     * Returns an integer expression
     * 
     * @param intExp
     * @return
     */
    ExpTy transExp(final Absyn.IntExp intExp) {
        return new ExpTy(translate.integer(intExp.value), INT);
    }

    /**
     * Returns a translated call expression. Checks that the order of arguments
     * matches the formals
     * 
     * @param callExp
     * @return
     */
    ExpTy transExp(final Absyn.CallExp callExp) {
        final Entry entry = (Entry) (env.venv.get(callExp.func));
        if (entry instanceof FunEntry) {
            var funEntry = (FunEntry) entry;
            var argExpList = callExp.args;
            // Type check first
            // TODO: Refactor node add
            for (RECORD fmlType = funEntry.formals; fmlType != null; fmlType = fmlType.tail) {
                if (argExpList == null) {
                    env.errorMsg.add(new ArgumentMismatchError(callExp.pos, null, null));
                    break;
                }
                var transArg = transExp(argExpList.head);
                if (transArg.ty.actual() != fmlType.fieldType.actual()) {
                    env.errorMsg.add(
                            new ArgumentMismatchError(callExp.pos, fmlType.fieldType.actual(), transArg.ty.actual()));
                }
                argExpList = argExpList.tail;
            }
            if (argExpList != null) {
                env.errorMsg.add(new ArgumentMismatchError(callExp.pos, null, null));
            }
            // translation generation
            // loop through the actual arguments
            // passed to the function reference
            argExpList = callExp.args;
            ExpTyList expTyList = null;
            if (argExpList != null) {
                // translate the argument and add to list
                expTyList = new ExpTyList(transExp(argExpList.head));
                argExpList = argExpList.tail;
                while (argExpList != null) {
                    expTyList.append(transExp(argExpList.head));
                    argExpList = argExpList.tail;
                }
            }
            
            return new ExpTy(translate.call(level, funEntry.level, funEntry.label, expTyList, funEntry.result), funEntry.result);
        } else {
            env.errorMsg.add(new FunctionNotDefinedError(callExp.pos, callExp.func));
            return new ExpTy(translate.Noop(), INT);
        }
    }

    /**
     * Translate an expression list into IR using rules as per specification
     * 
     * @param expList
     * @return
     */
    ExpTy transExp(Absyn.ExpList expList) {
        if (expList == null) {
            return new ExpTy(translate.Noop(), VOID);
        }
        if (expList.head == null) {
            return new ExpTy(translate.Noop(), VOID);
        } 
        ExpTyList etList = new ExpTyList(transExp(expList.head), null);
        expList = expList.tail;
        while(expList != null) {
            etList.append(transExp(expList.head));
            expList = expList.tail;
        }
        
        return new ExpTy(translate.seq(level, etList), etList.last().expTy.ty);
    }

    /**
     * Returns a translated expression for a sequence If sequence is empty, 
     * return type is void
     * @param seqExp
     * @return
     */
    ExpTy transExp(final Absyn.SeqExp seqExp) {
        return transExp(seqExp.list);
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
        var typeSymbol = arrayExp.typ;
        var sizeExp = arrayExp.size;
        var initExp = arrayExp.init;
        // check that the type of size is an int
        var transSizeExp = transExp(sizeExp);
        if (transSizeExp.ty != INT) {
            env.errorMsg.add(new TypeMismatchError(arrayExp.pos, transSizeExp.ty, Semant.INT));
        }
        // Get type of expression, it should be an array and not null
        final var tt = (Types.Type) env.tenv.get(typeSymbol);
        if (tt == null || !(tt.actual() instanceof Types.ARRAY)) {
            env.errorMsg.add(new TypeMismatchError(arrayExp.pos, tt));
            return new ExpTy(null, VOID);
        } else {
            // check that initialising expression is the
            // same type as the array element type
            var transInitExp = transExp(initExp);
            if (transInitExp.ty != ((Types.ARRAY) tt.actual()).element) {
                env.errorMsg.add(new TypeMismatchError(arrayExp.pos, tt));
            }
            return new ExpTy(translate.array(level, transSizeExp, transInitExp), tt.actual());
        }
    }

    /**
     * Generates the Type and IR for a record initialisation. The initialiser for
     * each field type contains an expression
     * 
     * For IR, we must create a record in heap memory using external function calls
     * to alloc memory. We use the field offsets to calculate field memory locations
     * and set those to the initialisation values.
     * 
     * @param recordExp the record to be initialised
     * @return
     */
    ExpTy transExp(final Absyn.RecordExp recordExp) {
        var expressionTypeSymbol = recordExp.typ;
        var tigerType = (Types.Type) env.tenv.get(expressionTypeSymbol);
        if (tigerType == null) {
            env.errorMsg.add(new UndefinedTypeError(recordExp.pos, tigerType));
        }
        ExpTyList expTyList = null;
        // ExpTyList current = null;
        if (!(tigerType.actual() instanceof RECORD)) {
            env.errorMsg.add(new TypeMismatchError(recordExp.pos, tigerType));
        } else {
            var recordType = (RECORD) tigerType.actual();
            expTyList = new ExpTyList(transFieldListExp(recordExp.fields, recordType), null);
            var recordFieldsTail = recordExp.fields.tail;
            recordType = recordType.tail;
            while (recordFieldsTail != null) {
                expTyList.append(transFieldListExp(recordFieldsTail, recordType));
                recordFieldsTail = recordFieldsTail.tail;
                recordType = recordType.tail;
            }
        }
        // a list of all the fieldExpTys here
        return new ExpTy(translate.record(level, expTyList), tigerType);
    }

    /**
     * Translates an assignment expession into a type and intermediate code An
     * assignment expression occurs when a value or expression is assigned to a
     * defined variable
     * 
     * For IR, this would be a evaluating the right side and moving this value into
     * the memory / register of the left side
     * 
     * @param assignExp
     * @return
     */
    ExpTy transExp(final Absyn.AssignExp assignExp) {
        var transVar = transVar(assignExp.var); // lvalue
        var transExp = transExp(assignExp.exp); // rvalue
        if(transExp.exp == null) {
            //TODO: handle l value and r value
          //  env.errorMsg.add(new UndefinedVariableError(assignExp.var.pos, assignExp.exp));
            return new ExpTy(translate.Noop(), Semant.VOID);
        }
        if(transVar.exp == null) {
            //TODO: handle l value and r value
          //  env.errorMsg.add(new UndefinedVariableError(assignExp.var.pos, assignExp.exp));
            return new ExpTy(translate.Noop(), Semant.VOID);
        }
        // check to see if we are trying to assign to a readonly variable
        if (assignExp.var instanceof SimpleVar) {
            var simpleVar = (SimpleVar) assignExp.var;
            var varEntry = (VarEntry) env.venv.get(simpleVar.name);
            if (varEntry == null) {
                env.errorMsg.add(new UndefinedVariableError(assignExp.var.pos, simpleVar.name));
                return new ExpTy(translate.Noop(), Semant.VOID);
                
            } else {
                if (varEntry.readOnly) {
                    env.errorMsg.add(new VariableAssignError(assignExp.pos, simpleVar.name));
                }
            }
            //
        } else if (assignExp.var instanceof SubscriptVar) {
            // var subscriptVar = (SubscriptVar) assignExp.var;
            //

        } else if (assignExp.var instanceof FieldVar) {
            // var fieldVar = (FieldVar) assignExp.var;

        } else {
            throw new RuntimeException("Unhandled type " + assignExp.var);
        }

        if(!transVar.ty.coerceTo(transExp.ty)){
            env.errorMsg.add(new TypeMismatchError(assignExp.pos, transVar.ty.actual(), transExp.ty.actual()));
        }
        return new ExpTy(translate.assign(level, transVar, transExp), Semant.VOID);
    }

    /**
     * Returns a translated for loop. When evaluating the body of the loop, a new
     * instance of Semant is created with its break scope variable set to true. This
     * indicates that break statements are legal inside this expression
     * 
     * We assume the index variable does not escape
     * 
     * IR Code The for loop can be translated into the following sequence
     * 
     * @param forExp
     * @return
     */
    ExpTy transExp(final Absyn.ForExp forExp) {
        return transExp(rewriteForExp(forExp));
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
        // end of loop label
        var loopEnd = new Label();
        // pass in the end loop label so that breaks can jump to it
        var transBody = new Semant(env, loopEnd, level, translate).transExp(whileExp.body);
        //if (transBody.ty.actual() != Semant.VOID) {
        if (transBody.ty.coerceTo(Semant.VOID)) {
            env.errorMsg.add(new TypeMismatchError(whileExp.pos, transBody.ty.actual(), Semant.VOID));
        }
        env.venv.endScope();
        env.tenv.endScope();
        return new ExpTy(translate.whileL(level, loopEnd, testExp, transBody), Semant.VOID);

    }

    /**
     * Returns a translated break expression. This expression must be nested within
     * a while or for loop
     * 
     * @param breakExp
     * @return
     */
    ExpTy transExp(final Absyn.BreakExp breakExp) {
        if (breakScopeLabel == null) {
            env.errorMsg.add(new BreakNestingError(breakExp.pos));
        }
        return new ExpTy(translate.breakE(level, breakScopeLabel), Semant.VOID);
    }

    /**
     * Returns a translated if expression.
     * 
     * @param ifExp
     * @return
     */
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
            return new ExpTy(translate.ifE(level, testExp, thenExp, elseExp), elseExp.ty.actual());
        } else {
            return new ExpTy(translate.ifE(level, testExp, thenExp), Semant.VOID);
        }
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

    /**
     * Translates a field exp list into its type and returns IR
     * 
     * @param fel
     * @return
     */
    private ExpTy transFieldListExp(final Absyn.FieldExpList fel, final Types.RECORD recordType) {
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
        if(!transExp.ty.coerceTo(fieldType)){
            env.errorMsg.add(new TypeMismatchError(fel.pos, fieldType, transExp.ty));
        }
        return transExp;
    }

    private BoolList getBoolList(final FieldList fields) {
        BoolList boolList = null; //
        if (fields != null) {
            boolList = new BoolList(fields != null ? fields.escape : null, null);
            var fieldTail = fields.tail;
            while (fieldTail != null) {
                boolList.append(fieldTail.escape);
                fieldTail = fieldTail.tail;
            }
        }
        return boolList;
    }

    private RECORD getRecordType(final FieldList fields) {
        RECORD recordType = null;
        if (fields != null) {
            var fieldType = getType(fields.typ, fields.pos);
            recordType = new RECORD(fields.name, fieldType, null);
            var fieldTail = fields.tail;
            while (fieldTail != null) {
                fieldType = getType(fieldTail.typ, fieldTail.pos);
                recordType.append(fieldTail.name, fieldType);
                fieldTail = fieldTail.tail;
            }
        }
        return recordType;
    }

    private Types.Type getType(final Symbol sym, final int pos) {
        final Types.Type cached = (Types.Type) env.tenv.get(sym);
        if (cached == null) {
            env.errorMsg.add(new UndefinedVariableError(pos, sym));
            return null;
        }
        return cached;
    }

    private LetExp rewriteForExp(ForExp forExp){
        return new LetExp(
            forExp.pos,
            new DecList(
                forExp.var,
                new DecList(
                    new VarDec(
                        forExp.pos,
                        Symbol.symbol("limit"),
                        new NameTy(
                            forExp.pos,
                            Symbol.symbol("int")
                        ),
                        forExp.hi
                    ),
                    null
                )
            ), 
            new SeqExp(
                forExp.pos,
                new ExpList(
                    new IfExp(
                        forExp.pos, 
                        new OpExp(
                            forExp.pos, 
                            new VarExp(
                                forExp.pos, 
                                new SimpleVar(
                                    forExp.pos, 
                                    Symbol.symbol("i")
                                )
                            ), 
                            OpExp.LE, 
                            new VarExp(
                                0, 
                                new SimpleVar(
                                    0, 
                                    Symbol.symbol("limit")
                                )
                            )
                        ), 
                        new WhileExp(
                            forExp.pos, 
                            new IntExp(forExp.pos, 1),
                            new SeqExp(
                               forExp.body.pos,
                               new ExpList(
                                    forExp.body,
                                    new ExpList(
                                        new IfExp(
                                            forExp.pos,
                                            new OpExp(
                                                forExp.pos,
                                                new VarExp(
                                                    0, 
                                                    new SimpleVar(
                                                       0, 
                                                       Symbol.symbol("i")
                                                    )
                                                ), 
                                                OpExp.EQ,
                                                new VarExp(
                                                    0, 
                                                    new SimpleVar(
                                                       0, 
                                                       Symbol.symbol("limit")
                                                    )
                                                )
                                            ),
                                            new BreakExp(0) /* true so break */,
                                            new AssignExp(/* false so increment */
                                                0,
                                                new SimpleVar(
                                                    0, 
                                                    Symbol.symbol("i")
                                                ),
                                                new OpExp(
                                                    0, 
                                                    new VarExp(
                                                        0, 
                                                        new SimpleVar(
                                                            0, 
                                                            Symbol.symbol("i")
                                                        )
                                                    ),
                                                    OpExp.PLUS, 
                                                    new IntExp(
                                                        0, 
                                                        1
                                                    )
                                                )
                                           )
                                        ),
                                        null
                                    )
                               )
                            )
                        )
                    ), 
                    null
                )
            )
        );
    }
}
