package Semant;

import Absyn.FieldList;
import Absyn.FunctionDec;
import Absyn.TypeDec;
import Util.Assert;
import Core.CompilerEventType;
import Core.Component;
import ErrorMsg.ArgumentMismatchError;
import ErrorMsg.ErrorMsg;
import ErrorMsg.FunctionNotDefinedError;
import ErrorMsg.TypeMismatchError;
import ErrorMsg.UndefinedTypeError;
import ErrorMsg.UndefinedVariableError;
import Symbol.Symbol;
import Temp.Label;
import Translate.Exp;
import Translate.ExpTy;
import Translate.ExpTyList;
import Translate.FragList;
import Translate.Level;
import Translate.Translator;
import Types.ARRAY;
import Types.NAME;
import Types.RECORD;
import Util.BoolList;

public class Semant extends Component{

    public static CompilerEventType SEMANT_START = new CompilerEventType("Start");
    private final Env env;
    private final Label breakScopeLabel;
    private final Translator translator;
    private Level level;
    public final static int PLUS = 0, MINUS = 1, MUL = 2, DIV = 3, AND = 4, OR = 5, LSHIFT = 6, RSHIFT = 7, ARSHIFT = 8;
    public final static int EQ = 0, NE = 1, LT = 2, GT = 3, LE = 4, GE = 5, ULT = 6, ULE = 7, UGT = 8, UGE = 9;
    public static final Types.Type INT = new Types.INT();
    public static final Types.Type STRING = new Types.STRING();
    public static final Types.Type VOID = new Types.VOID();
    public static final Types.Type NIL = new Types.NIL();
    public SemantValidator semantValidator;
    
    public Semant(ErrorMsg err, final Level lvl, Translator trans) {
        this(new Env(err, lvl), null, lvl, trans);
    }

    Semant(final Env e, Label bsl, Level lev, Translator trans) {
        env = e;
        breakScopeLabel = bsl;
        level = lev;
        translator = trans;
        this.semantValidator = new SemantValidator(e);
    }

    /**
     * Returns true if there are semantic validation errors.
     * @return true or false
     */
    public boolean hasErrors() {
        return this.env.errorMsg.anyErrors;
    }

    /**
     * Main method to translate and type check ast.
     * We also must translate the top level implicit function
     * 
     * @param absyn
     * @return
     */
    public FragList getTreeFragments(Absyn.Exp absyn) {
        this.trigger(SEMANT_START, absyn);
        var trans = this.transExp(absyn);
        translator.procEntryExit(level, trans.exp);
        return translator.getResult();
    }

    /**
     * Returns a record type.
     * @param fields
     * @return
     */
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

    /**
     * Returns the type of symbol or null if that symbols is
     * not present in environment.
     * @param sym
     * @param pos
     * @return the type of symbol or null.
     */
    private Types.Type getType(final Symbol sym, final int pos) {
        this.semantValidator.checkType(sym, pos);
        final Types.Type cached = (Types.Type) env.tenv.get(sym);
        return cached;
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
     * Translates an abstract syntax array type into a native type eg array of int.
     * This retreives the element type from a cache.
     * 
     * @param t
     * @return
     */
    Types.Type transTy(final Absyn.ArrayTy t) {
        final var cached = getType(t.typ, t.pos);
        return new Types.ARRAY(cached);
    }

    /**
     * Translates a type t into a native type. This retrieves
     * the name type from a cache.
     * 
     * @param t
     * @return return the looked up nametype
     */
    Types.Type transTy(final Absyn.NameTy t) {
        Assert.assertNotNull(t);
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
        if(!this.semantValidator.checkVariableDefined(e.name, e.pos))
            return ExpTy.ERROR;
        VarEntry ent = (VarEntry)env.venv.get(e.name);
        var translateExp = translator.simpleVar(ent.access, level);
        return new ExpTy(translateExp, ent.ty);
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
        var varExp = transVar(e.var);
        if(!this.semantValidator.isRecord(varExp, e.var.pos))
            return ExpTy.ERROR;
        var varType = varExp.ty.actual();
        int i = 0;
        // TODO: Refactor node add
        for (RECORD record = (RECORD) varType; record != null; record = record.tail) {
            if (record.fieldName == e.field) {
                Exp translateExp = translator.fieldVar(varExp.exp, i, level);
                return new ExpTy(translateExp, record.fieldType.actual());
            }
            i++;
        }
        this.semantValidator.undefinedField(e.field, e.pos);
        return ExpTy.ERROR;
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
        var indexExp = e.index;
        var transIndexExp = transExp(indexExp);
        // translate the index expression and check its an INT
        this.semantValidator.isInt(transIndexExp, indexExp.pos);
        // translate the variable and check its an instance of an ARRAY
        var translatedArrayVar = transVar(e.var);
        Types.Type elementType = translatedArrayVar.ty.actual();
        this.semantValidator.isArray(translatedArrayVar);
        var translateExp = translator.subscriptVar(transIndexExp, translatedArrayVar, level);
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
     * COnverts a field list into a BoolList. Each
     * boolean in this list represents a variable, where
     * a true indicates the variable should escape and false
     * where it should not.
     * @param fields
     * @return
     */
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
        if(env.venv.get(e.name) != null) {
            return this.translator.Noop(); 
        }
        for(FunctionDec current = e; current != null; current = current.next) {
            // get the functions return type
            var functionReturnType = current.result != null ? transTy(current.result) : Semant.VOID;
            // add a new nesting level into the function entry
            // add allocations for the parameters to be passed
            // to this function
            // creates a new level and a new frame and allocates
            // space for the formal parameters
            // for each formal parameter, we need to get its frame access
            Label functionLabel = Label.create();
            var functionEntry = new FunEntry(
                new Level(
                    level, 
                    functionLabel, 
                    getBoolList(
                        current.params
                    )
                ),
                functionLabel,
                getRecordType(
                    current.params
                ), 
                functionReturnType
            );
            // add function entry into the value environment
            env.venv.put(current.name, functionEntry);
        } 
        for(FunctionDec current = e; current != null; current = current.next) {
            // I think we begin scope here because we
            // are processing the function body in this loop
            env.venv.beginScope();
            // get the new level created in the parent scope for this function
            var newLevel = ((FunEntry) env.venv.get(current.name)).level;
            var vent = (FunEntry) env.venv.get(current.name);
            // iterate formals adding access to the created var entries
            var translateAccess = translator.stripStaticLink(newLevel.formals);
            for (var p = current.params; p != null; p = p.tail) {
                var varEntry = new VarEntry(
                    env.tenv.get(p.typ).actual(), 
                    translateAccess.head
                );
                env.venv.put(p.name, varEntry);
                translateAccess = translateAccess.tail;
            }
            var transBody = new Semant(env, breakScopeLabel, newLevel, translator).transExp(current.body);
            this.semantValidator.sameType(transBody, vent.result, current.pos);
            env.venv.endScope();
            Exp translatedBody = this.translator.functionDec(newLevel, transBody);
            this.translator.procEntryExit(newLevel, translatedBody);
        }
        return translator.Noop();
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
        return translator.Noop();
    }

    /**
     * Translates a variable declaration into an intermediate expression
     * 
     * @param e
     * @return
     */
    Exp transDec(final Absyn.VarDec e) {
        ExpTy initExpTy = transExp(e.init);
        Types.Type type = initExpTy.ty.actual();
        if(e.typ != null) {
            Types.Type other = transTy(e.typ);
            this.semantValidator.sameType(initExpTy, other, e.pos);
            this.semantValidator.nilAssignedToRecord(initExpTy, other, e.pos);
        }
        var translateAccess = level.allocLocal(e.escape);
        var varEntry = new VarEntry(type, translateAccess);
        env.venv.put(e.name, varEntry);
        return translator.transDec(level, translateAccess, initExpTy.exp);
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
        return new ExpTy(translator.varExp(transVar), transVar.ty);
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
        ExpTyList irDecList = null;
        var decList = e.decs;
        while(decList != null){
            var transDec = transDec(decList.head);
            var et = new ExpTy(transDec, Semant.VOID);
            irDecList = ExpTyList.append(irDecList, et);
            decList = decList.tail;
        }
        ExpTy irBody = transExp(e.body);
        var irLet = translator.letE(irDecList, irBody);
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
            this.semantValidator.isInt(transExpLeft, e.left.pos);
            this.semantValidator.isInt(transExpRight, e.right.pos);
            return new ExpTy(translator.binaryOperator(PLUS, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.MINUS:
            this.semantValidator.isInt(transExpLeft, e.left.pos);
            this.semantValidator.isInt(transExpRight, e.right.pos);
            return new ExpTy(translator.binaryOperator(MINUS, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.MUL:
            this.semantValidator.isInt(transExpLeft, e.left.pos);
            this.semantValidator.isInt(transExpRight, e.right.pos);
            return new ExpTy(translator.binaryOperator(MUL, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.DIV:
            this.semantValidator.isInt(transExpLeft, e.left.pos);
            this.semantValidator.isInt(transExpRight, e.right.pos);
            return new ExpTy(translator.binaryOperator(DIV, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.LE:
            this.semantValidator.isInt(transExpLeft, e.left.pos);
            this.semantValidator.isInt(transExpRight, e.right.pos);
            return new ExpTy(translator.relativeOperator(LE, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.GE:
            this.semantValidator.isInt(transExpLeft, e.left.pos);
            this.semantValidator.isInt(transExpRight, e.right.pos);
            return new ExpTy(translator.relativeOperator(GE, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.LT:
            this.semantValidator.isInt(transExpLeft, e.left.pos);
            this.semantValidator.isInt(transExpRight, e.right.pos);
            return new ExpTy(translator.relativeOperator(LT, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.GT:
            this.semantValidator.isInt(transExpLeft, e.left.pos);
            this.semantValidator.isInt(transExpRight, e.right.pos);
            return new ExpTy(translator.relativeOperator(GT, transExpLeft, transExpRight), INT);

        case Absyn.OpExp.EQ:
            // the order here is important,
            // expRigth.coerceTo(expLeft) is not the same as
            // the reverse
            this.semantValidator.sameType(transExpRight, transExpLeft, e.left.pos);
            return new ExpTy(translator.equalsOperator(EQ, transExpLeft, transExpRight, this.level), INT);
        case Absyn.OpExp.NE:
            // the order here is important,
            // expRigth.coerceTo(expLeft) is not the same as
            // the reverse
            this.semantValidator.sameType(transExpRight, transExpLeft, e.left.pos);
            return new ExpTy(translator.equalsOperator(NE, transExpLeft, transExpRight, this.level), INT);
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
        return new ExpTy(translator.nil(), NIL);
    }

    /**
     * Returns an string expression
     * 
     * @param stringExp
     * @return
     */
    ExpTy transExp(final Absyn.StringExp stringExp) {
        return new ExpTy(translator.string(stringExp.value, level), STRING);
    }

    /**
     * Returns an integer expression
     * 
     * @param intExp
     * @return
     */
    ExpTy transExp(final Absyn.IntExp intExp) {
        return new ExpTy(translator.integer(intExp.value), INT);
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
            ExpTyList expTyList = null;
            for (RECORD fmlType = funEntry.formals; fmlType != null; fmlType = fmlType.tail) {
                if (argExpList == null) {
                    env.errorMsg.add(new ArgumentMismatchError(callExp.pos, null, null));
                    break;
                }
                var transArg = transExp(argExpList.head);
                expTyList = ExpTyList.append(expTyList, transArg);
                if (transArg.ty.actual() != fmlType.fieldType.actual()) {
                    env.errorMsg.add(
                            new ArgumentMismatchError(callExp.pos, fmlType.fieldType.actual(), transArg.ty.actual()));
                }
                argExpList = argExpList.tail;
            }
            if (argExpList != null) {
                env.errorMsg.add(new ArgumentMismatchError(callExp.pos, null, null));
            }
            return new ExpTy(translator.call(!funEntry.level.isTopLevel(), level, funEntry.level, funEntry.label, expTyList, funEntry.result), funEntry.result);
        } else {
            env.errorMsg.add(new FunctionNotDefinedError(callExp.pos, callExp.func));
            return new ExpTy(translator.Noop(), INT);
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
            return new ExpTy(translator.Noop(), VOID);
        }
        if (expList.head == null) {
            return new ExpTy(translator.Noop(), VOID);
        } 
        ExpTyList etList = new ExpTyList(transExp(expList.head), null);
        expList = expList.tail;
        while(expList != null) {
            etList = ExpTyList.append(etList, transExp(expList.head));
            expList = expList.tail;
        }
        
        return new ExpTy(translator.seq(level, etList), etList.last().expTy.ty);
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
            return new ExpTy(translator.array(level, transSizeExp, transInitExp), tt.actual());
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
        this.semantValidator.isRecord(tigerType, recordExp.pos);
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
                expTyList = ExpTyList.append(expTyList, transFieldListExp(recordFieldsTail, recordType));
                recordFieldsTail = recordFieldsTail.tail;
                recordType = recordType.tail;
            }
        }
        // a list of all the fieldExpTys here
        return new ExpTy(translator.record(level, expTyList), tigerType);
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
        this.semantValidator.sameType(transVar, transExp, assignExp.pos);
        this.semantValidator.isReadonly(assignExp.var, assignExp.pos);
        return new ExpTy(translator.assign(level, transVar, transExp), Semant.VOID);
    }

    /**
     * Returns a translated for loop. When evaluating the body of the loop, a new
     * instance of Semant is created with its break scope variable set to true. This
     * indicates that break statements are legal inside this expression
     * 
     * We assume the index variable does not escape
     * 
     * Note that the Loop Indexer is not initialised via the VarDec abstact syntax.
     * We explicity create it here and set it to read only inside the loop body.
     * 
     * IR Code The for loop can be translated into the following sequence
     * 
     * @param forExp
     * @return
     */
    ExpTy transExp(final Absyn.ForExp forExp) {
        env.tenv.beginScope();
        env.venv.beginScope();
        var access = level.allocLocal(false);
        var varEntry = new VarEntry(Semant.INT, access);
        env.venv.put(forExp.var.name, varEntry);
        ExpTy explo = transExp(forExp.var.init);
        this.semantValidator.isInt(explo, forExp.var.init.pos);
        ExpTy exphi = transExp(forExp.hi);
        this.semantValidator.isInt(exphi, forExp.hi.pos);
        var loopExit = Label.create();
        varEntry.readonly = true;
        ExpTy expbody = new Semant(this.env, loopExit, this.level, this.translator).transExp(forExp.body);
        varEntry.readonly = false;
        this.semantValidator.isVoid(expbody);
        env.venv.endScope();
        env.tenv.endScope();
        return new ExpTy(this.translator.forL(level, loopExit,access, explo, exphi, expbody), Semant.VOID);
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
        this.semantValidator.isInt(testExp, whileExp.test.pos);
        var loopExit = new Label();
        var expboody = new Semant(this.env, loopExit, this.level, this.translator).transExp(whileExp.body);
        this.semantValidator.isVoid(expboody);
        env.venv.endScope();
        env.tenv.endScope();
        return new ExpTy(translator.whileL(this.level, loopExit, testExp, expboody), Semant.VOID);

    }

    /**
     * Returns a translated break expression. This expression must be nested within
     * a while or for loop
     * 
     * @param breakExp
     * @return
     */
    ExpTy transExp(final Absyn.BreakExp breakExp) {
        this.semantValidator.illegalBreak(this.breakScopeLabel, breakExp.pos);
        return new ExpTy(translator.breakE(level, this.breakScopeLabel), Semant.VOID);
    }

    /**
     * Returns a translated if expression.
     * 
     * @param ifExp
     * @return
     */
    ExpTy transExp(final Absyn.IfExp ifExp) {
        var testExp = transExp(ifExp.test);
        this.semantValidator.isInt(testExp, ifExp.pos);
        var thenExp = transExp(ifExp.thenclause);
        var elseExp = ifExp.elseclause != null ? transExp(ifExp.elseclause) : null;
        if (elseExp != null && !elseExp.ty.coerceTo(thenExp.ty)) {
            env.errorMsg.add(new TypeMismatchError(ifExp.thenclause.pos, thenExp.ty.actual(), elseExp.ty.actual()));
            return new ExpTy(null, Semant.VOID);
        } else if (elseExp != null && elseExp.ty.coerceTo(thenExp.ty)) {
            return new ExpTy(translator.ifE(level, testExp, thenExp, elseExp), elseExp.ty.actual());
        } else {
            return new ExpTy(translator.ifE(level, testExp, thenExp), Semant.VOID);
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
        Assert.assertNotNull(recordType);
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

    
}
