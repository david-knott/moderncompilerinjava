package Bind;

import java.util.Hashtable;
import java.util.Stack;

import Absyn.Absyn;
import Absyn.ArrayExp;
import Absyn.ArrayTy;
import Absyn.BreakExp;
import Absyn.CallExp;
import Absyn.DefaultVisitor;
import Absyn.FieldList;
import Absyn.ForExp;
import Absyn.FunctionDec;
import Absyn.IntExp;
import Absyn.LetExp;
import Absyn.NameTy;
import Absyn.RecordExp;
import Absyn.RecordTy;
import Absyn.SimpleVar;
import Absyn.StringExp;
import Absyn.Typable;
import Absyn.TypeDec;
import Absyn.VarDec;
import Absyn.WhileExp;
import ErrorMsg.ErrorMsg;
import Symbol.Symbol;
import Types.ARRAY;
import Types.Constants;
import Types.FUNCTION;
import Types.NAME;
import Types.RECORD;
import Types.Type;
import Util.Assert;


/**
 * The Binder class traverses the abstract syntax tree and binds variable,
 * function and type declarations to their respective symbol tables.
 * These are used in the type checking and translation phases.
 */
public class Binder extends DefaultVisitor {

    SymbolTable typeSymbolTable;
    SymbolTable varSymbolTable;
    SymbolTable functionSymbolTable;
    Type type = null;
    private Stack<Absyn> loops = new Stack<Absyn>();
    private final ErrorMsg errorMsg;

    public Binder(ErrorMsg errorMsg) {
        this.errorMsg = errorMsg;
        // base system types
        Hashtable<Symbol, SymbolTableElement> tinit = new Hashtable<Symbol, SymbolTableElement>();
        // hack so that type usages for ints and string does not show a zero.
        tinit.put(Symbol.symbol("int"), new SymbolTableElement(Constants.INT, new IntExp(0, 0)));
        tinit.put(Symbol.symbol("string"), new SymbolTableElement(Constants.STRING, new StringExp(0, "")));
        this.typeSymbolTable = new SymbolTable(tinit);
        // base functions
        Hashtable<Symbol, SymbolTableElement> finit = new Hashtable<Symbol, SymbolTableElement>();
        finit.put(Symbol.symbol("print"),
                new SymbolTableElement(
                    new FUNCTION(new RECORD(Symbol.symbol("s"), Constants.STRING, null), Constants.VOID))
        );
        finit.put(Symbol.symbol("print_int"),
                new SymbolTableElement(
                    new FUNCTION(new RECORD(Symbol.symbol("s"), Constants.INT, null), Constants.VOID))
        );
        this.functionSymbolTable = new SymbolTable(finit);
        // var table
        this.varSymbolTable = new SymbolTable();
    }

    /**
     * Visit the letexp expression. This creates new
     * function, variable and type scopes.
     */
    @Override
    public void visit(LetExp exp) {
        this.typeSymbolTable.beginScope();
        this.varSymbolTable.beginScope();
        this.functionSymbolTable.beginScope();
        if (exp.decs != null) {
            exp.decs.accept(this);
        }
        if (exp.body != null) {
            exp.body.accept(this);
        }
        //bind stuff to the let exp now
        this.functionSymbolTable.endScope();
        this.varSymbolTable.endScope();
        this.typeSymbolTable.endScope();
    }

    /**
     * Visit an integer expression. Sets type INT
     */
    @Override
    public void visit(IntExp exp) {
        this.type = Constants.INT;
    }

    /**
     * Visit an string expression. Sets type STRING
     */
    @Override
    public void visit(StringExp exp) {
        this.type = Constants.STRING;
    }

    /**
     * Visit a simple var expression and bind it to its declaration.
     */
    @Override
    public void visit(SimpleVar exp) {
        if(this.varSymbolTable.contains(exp.name)) {
            SymbolTableElement def = this.varSymbolTable.lookup(exp.name);
            this.type = def.type;
            exp.setDef(def.exp);
        } else {
            this.errorMsg.error(exp.pos, "undeclared variable:" + exp.name);
        }
    }

    /**
     * Visit a call expression and bind it to the function declaration.
     */
    @Override
    public void visit(CallExp exp) {
        if(this.functionSymbolTable.contains(exp.func)) {
            SymbolTableElement def = this.functionSymbolTable.lookup(exp.func);
            exp.setDef(def.exp);
            this.type = def.type;
            super.visit(exp);
        } else {
            this.errorMsg.error(exp.pos, "undeclared function:" + exp.func);
        }
    }

    /**
     * Visit a variable declaration and add it to the symbol table.
     */
    @Override
    public void visit(VarDec exp) {
        if(exp.typ != null) {
            if(this.typeSymbolTable.contains(exp.typ.name)) {
                SymbolTableElement def = this.typeSymbolTable.lookup(exp.typ.name);
                exp.typ.setDef(def.exp);
            } else {
                this.errorMsg.error(exp.pos, "undefined type:" + exp.typ.pos);
            }
        }
        exp.init.accept(this);
        Type initType = this.type;
        // variable definition
        if(!this.varSymbolTable.contains(exp.name, false)) {
            this.varSymbolTable.put(exp.name, new SymbolTableElement(initType, exp));
        } else {
            this.errorMsg.error(exp.pos, "redefinition:" + exp.name);
        }
    }

    @Override
    public void visit(ArrayExp exp) {
        if(this.typeSymbolTable.contains(exp.typ)) {
            SymbolTableElement def = this.typeSymbolTable.lookup(exp.typ);
            exp.setDef(def.exp);
            this.type = def.type;
            super.visit(exp);
        } else {
            this.errorMsg.error(exp.pos, "undefined type:" + exp.typ);
        }
        super.visit(exp);
    }

    @Override
    public void visit(RecordExp exp) {
        if(this.typeSymbolTable.contains(exp.typ)) {
            SymbolTableElement def = this.typeSymbolTable.lookup(exp.typ);
            exp.setDef(def.exp);
            this.type = def.type;
            super.visit(exp);
        } else {
            this.errorMsg.error(exp.pos, "undefined type:" + exp.typ);
        }
        super.visit(exp);
    }

    /**
     * Visit break expression and assign it to member lastBreak. 
     */
    @Override
    public void visit(BreakExp exp) {
        if(this.loops.empty()) {
            this.errorMsg.error(exp.pos, "`break' outside any loop:" + exp.pos);
        } else {
            exp.loop = this.loops.peek();
        }
    }


    /**
     * Visit while loop and capture and breaks within its body.
     */
    @Override
    public void visit(WhileExp exp) {
        this.loops.push(exp);
        exp.test.accept(this);
        //visit the body which could contain a break.
        exp.body.accept(this);
        this.loops.pop();
    }

    /**
     * Visit for loop and capture and breaks within its body.
     */
    @Override
    public void visit(ForExp exp) {
        this.loops.push(exp);
        exp.var.accept(this);
        exp.hi.accept(this);
        exp.body.accept(this);
        this.loops.pop();
    }

    /**
     * Visit a function declaration. Visit the function header first, this includes the function name
     * its formal arguments and return type. These are added to the function symbol table. A second pass
     * then examines each contigour function body and adds the formals to variable environment.
     */
    @Override
    public void visit(FunctionDec exp) {
        // first pass for function headers.
        for(FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            Type returnType = null;
            if(functionDec.result != null) {
                if(this.typeSymbolTable.contains(functionDec.result.name)) {
                    SymbolTableElement def = this.typeSymbolTable.lookup(functionDec.result.name);
                    functionDec.result.setDef(def.exp);
                    returnType = def.type;
                } else {
                    this.errorMsg.error(functionDec.result.pos, "undefined type:" + functionDec.result.name);
                }
            } else {
                returnType = Constants.VOID;
            }
            RECORD paramType = null;
            if(functionDec.params != null) {
                // call accept, which sets this.type
                functionDec.params.accept(this);
                paramType = (RECORD)this.type;
            }
            // function definition
            if(!this.functionSymbolTable.contains(functionDec.name, false)) {
                this.functionSymbolTable.put(
                    functionDec.name, 
                    new SymbolTableElement(
                        new FUNCTION(
                            paramType, 
                            returnType
                        ),
                        functionDec
                    )
                );
            } else {
                this.errorMsg.error(exp.pos, "redefinition:" + functionDec.name);
            }
        }
        // second pass for function body.
        for(FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            this.varSymbolTable.beginScope();
            for (var param = functionDec.params; param != null; param = param.tail) {
                SymbolTableElement paramType = this.typeSymbolTable.lookup(param.typ);
                param.setDef(paramType.exp);
                // formal variable definition
                if(!this.varSymbolTable.contains(param.name, false)) {
                    this.varSymbolTable.put(param.name, new SymbolTableElement(paramType.type, param));
                } else {
                    this.errorMsg.error(param.pos, "redefinition:" + param.name);
                }
            }
            functionDec.body.accept(this);
            this.varSymbolTable.endScope();
        }
    }

    /**
     * Visits a user defined type declaration and binds the symbol to its type
     * representation. This allows further lookups using the exp.name which will
     * resolve the the user defined type.
     */
    @Override
    public void visit(TypeDec exp) {
        // visit the header first and create a name 
        for(TypeDec typeDec = exp; typeDec != null; typeDec = typeDec.next) {
            this.type = new NAME(typeDec.name);
            // install type definition
            if(!this.typeSymbolTable.contains(typeDec.name)) {
                this.typeSymbolTable.put(typeDec.name, new SymbolTableElement(this.type, typeDec));
            } else {
                this.errorMsg.error(typeDec.pos, "redefinition:" + typeDec.name);
            }
        }
        for(TypeDec typeDec = exp; typeDec != null; typeDec = typeDec.next) {
            SymbolTableElement def = this.typeSymbolTable.lookup(typeDec.name);
            this.type = def.type;
            typeDec.ty.accept(this);
        }
    }

    /**
     * Visits a record type within a type declaration. It expects
     * that named is set.
     */
    @Override
    public void visit(RecordTy exp) {
        Assert.assertNotNull(this.type);
        NAME named = (NAME)this.type;
        if(exp.fields != null) {
            exp.fields.accept(this);
            named.bind(this.type);
        }
    }

    /**
     * Visits a fieldlist, used for both function arguments and
     * record definitions. Sets the this.type field to the record.
     */
    @Override
    public void visit(FieldList exp) {
        RECORD last = null, first = null, temp = null;
        FieldList expList = exp;
        do
        {
            temp = last;
            // type usage
            if(this.typeSymbolTable.contains(expList.typ)) {
                SymbolTableElement def = this.typeSymbolTable.lookup(expList.typ);
                expList.setDef(def.exp);
                last = new RECORD(expList.name, def.type, null);
                if(first == null) {
                    first = last;
                } else {
                    temp.tail = last; 
                }
            } else {
                this.errorMsg.error(expList.pos, "undefined type:" + expList.typ);
            }
            expList = expList.tail;
        }while(expList != null);
        this.type = first;
    }

    /**
     * Visits an array type within a type declaration. Contructs an ARRAY type and
     * stores it in the symbol table.
     */
    @Override
    public void visit(ArrayTy exp) {
        // type usage
        if(this.typeSymbolTable.contains(exp.typ)) {
            SymbolTableElement def = this.typeSymbolTable.lookup(exp.typ);
            exp.setDef(def.exp);
            ((NAME)this.type).bind(new ARRAY(def.type));
        } else {
            this.errorMsg.error(exp.pos, "undefined type:" + exp.typ);
        }
    }

    /**
     * Visit a explicit type in a var declaration, eg var a:int = 1, where int is
     * the NameTy or visit the return type defined in a function declaration eg
     * function a():int, where int is the NameTy, or type t = int, where int is NameTy
     */
    @Override
    public void visit(NameTy exp) {
        // type usuge
        if(this.typeSymbolTable.contains(exp.name)) {
            SymbolTableElement def = this.typeSymbolTable.lookup(exp.name);
            exp.setDef(def.exp);
            ((NAME)this.type).bind(def.type);
        } else {
            this.errorMsg.error(exp.pos, "undefined type:" + exp.name);
        }
    }
}