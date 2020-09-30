package Bind;

import java.util.Hashtable;
import java.util.Stack;

import Absyn.Absyn;
import Absyn.ArrayExp;
import Absyn.ArrayTy;
import Absyn.BreakExp;
import Absyn.CallExp;
import Absyn.DefaultVisitor;
import Absyn.Exp;
import Absyn.ExpList;
import Absyn.FieldList;
import Absyn.ForExp;
import Absyn.FunctionDec;
import Absyn.IntExp;
import Absyn.LetExp;
import Absyn.NameTy;
import Absyn.NilExp;
import Absyn.RecordExp;
import Absyn.RecordTy;
import Absyn.SeqExp;
import Absyn.SimpleVar;
import Absyn.StringExp;
import Absyn.Ty;
import Absyn.Typable;
import Absyn.TypeDec;
import Absyn.Var;
import Absyn.VarDec;
import Absyn.WhileExp;
import ErrorMsg.ErrorMsg;
import Symbol.Symbol;
import Tree.CONST;
import Types.ARRAY;
import Types.Constants;
import Types.FUNCTION;
import Types.NAME;
import Types.RECORD;
import Types.Type;
import Util.Assert;

/**
 * The Binder class traverses the abstract syntax tree and binds variable,
 * function and type uses to their definitions. This is done using the symbol
 * tables. These are used in the type checking and translation phases.
 * 
 * A Question, what is the purpose of the type member variable ? What does it do
 * ? 1) During traversal, it allows types calculated at a child node be
 * propagated back to parent nodes.
 */
public class Binder extends DefaultVisitor {

    private final SymbolTable typeSymbolTable;
    private final SymbolTable varSymbolTable;
    private final SymbolTable functionSymbolTable;
    private Type visitedType = null;
    private final Stack<Absyn> loops = new Stack<Absyn>();
    private final ErrorMsg errorMsg;

    private void setType(Ty exp, Type type) {
        exp.setType(type);
    }

    private void setType(Var exp, Type type) {
        exp.setType(type);
    }

    private void setType(Exp exp, Type type) {
        exp.setType(type);
    }

    public Binder(ErrorMsg errorMsg) {
        this.errorMsg = errorMsg;
        // base system types
        Hashtable<Symbol, SymbolTableElement> tinit = new Hashtable<Symbol, SymbolTableElement>();
        // hack so that type usages for ints and string does not show a zero.
        tinit.put(Symbol.symbol("int"), new SymbolTableElement(Constants.INT, null));
        tinit.put(Symbol.symbol("string"), new SymbolTableElement(Constants.STRING, null));
        this.typeSymbolTable = new SymbolTable(tinit);
        // base functions
        Hashtable<Symbol, SymbolTableElement> finit = new Hashtable<Symbol, SymbolTableElement>();
        finit.put(Symbol.symbol("print"), new SymbolTableElement(
                new FUNCTION(new RECORD(Symbol.symbol("s"), Constants.STRING, null), Constants.VOID)));
        finit.put(Symbol.symbol("print_int"), new SymbolTableElement(
                new FUNCTION(new RECORD(Symbol.symbol("s"), Constants.INT, null), Constants.VOID)));
        this.functionSymbolTable = new SymbolTable(finit);
        // var table
        this.varSymbolTable = new SymbolTable();
    }

    /**
     * Visit the letexp expression. This creates new function, variable and type
     * scopes.
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
            //type of let is type of body, if any.
        }
        // bind stuff to the let exp now
        this.functionSymbolTable.endScope();
        this.varSymbolTable.endScope();
        this.typeSymbolTable.endScope();
    }

    /**
     * Visit an integer expression. Sets type INT
     */
    @Override
    public void visit(IntExp exp) {
        this.visitedType = Constants.INT;
        this.setType(exp, this.visitedType);
    }

    /**
     * Visit an string expression. Sets type STRING
     */
    @Override
    public void visit(StringExp exp) {
        this.visitedType = Constants.STRING;
        this.setType(exp, this.visitedType);
    }

    /**
     * Visits a nil expression. Sets its type to Nil
     */
    @Override
    public void visit(NilExp exp) {
        this.visitedType = Constants.NIL;
        this.setType(exp, this.visitedType);
    }

    /**
     * Visit a simple var expression and bind it to its declaration. Sets the simple
     * var type to its definition type.
     */
    @Override
    public void visit(SimpleVar exp) {
        if (this.varSymbolTable.contains(exp.name)) {
            SymbolTableElement def = this.varSymbolTable.lookup(exp.name);
            this.visitedType = def.type;
            this.setType(exp, this.visitedType);
            exp.setType(def.type);
            exp.setDef(def.exp);
        } else {
            this.errorMsg.error(exp.pos, "undeclared variable:" + exp.name);
            this.visitedType = null;
        }
    }

    /**
     * Visit a call expression and bind it to the function declaration.
     */
    @Override
    public void visit(CallExp exp) {
        if (this.functionSymbolTable.contains(exp.func)) {
            SymbolTableElement def = this.functionSymbolTable.lookup(exp.func);
            exp.setDef(def.exp);
            this.setType(exp, def.type);
            this.visitedType = def.type;
            super.visit(exp);
        } else {
            this.errorMsg.error(exp.pos, "undeclared function:" + exp.func);
            this.visitedType = null;
        }
    }

    /**
     * Visit a variable declaration and add it to the symbol table.
     */
    @Override
    public void visit(VarDec exp) {
        // if the variable type was specified.
        if (exp.typ != null) {
            exp.typ.accept(this);
        }
        // visit the initializer for the var dec.
        exp.init.accept(this);
        Type initType = this.visitedType;
        // check the variable is present in variable table.
        if (!this.varSymbolTable.contains(exp.name, false)) {
            this.varSymbolTable.put(exp.name, new SymbolTableElement(initType, exp));
        } else {
            this.errorMsg.error(exp.pos, "redefinition:" + exp.name);
        }
        this.visitedType = Constants.VOID;
    }

    /**
     * Visit an array expression. This is where an array is used in a rvalue expression.
     */
    @Override
    public void visit(ArrayExp exp) {
        if (this.typeSymbolTable.contains(exp.typ)) {
            SymbolTableElement def = this.typeSymbolTable.lookup(exp.typ);
            exp.setDef(def.exp);
            this.setType(exp, def.type);
            this.visitedType = def.type;
            super.visit(exp);
        } else {
            this.errorMsg.error(exp.pos, "undefined type:" + exp.typ);
            this.visitedType = null;
        }
    }

    /**
     * Visit a record expression. This is where a record is used in a rvalue expression.
     */
    @Override
    public void visit(RecordExp exp) {
        if (this.typeSymbolTable.contains(exp.typ)) {
            SymbolTableElement def = this.typeSymbolTable.lookup(exp.typ);
            exp.setDef(def.exp);
            this.setType(exp, def.type);
            this.visitedType = def.type;
            super.visit(exp);
        } else {
            this.errorMsg.error(exp.pos, "undefined type:" + exp.typ);
            this.visitedType = null;
        }
    }

    /**
     * Visit break expression and assign it to member lastBreak.
     */
    @Override
    public void visit(BreakExp exp) {
        if (this.loops.empty()) {
            this.errorMsg.error(exp.pos, "`break' outside any loop:" + exp.pos);
        } else {
            exp.loop = this.loops.peek();
        }
        this.visitedType = Constants.VOID;
    }

    /**
     * Visit while loop and capture and breaks within its body.
     */
    @Override
    public void visit(WhileExp exp) {
        this.loops.push(exp);
        exp.test.accept(this);
        // visit the body which could contain a break.
        exp.body.accept(this);
        this.loops.pop();
        this.visitedType = Constants.VOID;
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
        this.visitedType = Constants.VOID;
    }

    /**
     * Visit a function declaration. Visit the function header first, this includes
     * the function name its formal arguments and return type. These are added to
     * the function symbol table. A second pass then examines each contigious
     * function body and adds the formals to variable environment.
     */
    @Override
    public void visit(FunctionDec exp) {
        // first pass for function headers.
        for (FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            Type returnType = null;
            if (functionDec.result != null) {
                // lookup the type of the return value
                if (this.typeSymbolTable.contains(functionDec.result.name)) {
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
            if (functionDec.params != null) {
                // call accept method on paraneter, which sets this.visitedType
                functionDec.params.accept(this);
                paramType = (RECORD) this.visitedType;
            }
            // function definition
            if (!this.functionSymbolTable.contains(functionDec.name, false)) {
                FUNCTION functionType = new FUNCTION(paramType, returnType);
                functionDec.setType(functionType);
                this.functionSymbolTable.put(functionDec.name, new SymbolTableElement(functionType, functionDec));
            } else {
                this.errorMsg.error(exp.pos, "redefinition:" + functionDec.name);
            }
        }
        // second pass for function body.
        for (FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            this.varSymbolTable.beginScope();
            for (var param = functionDec.params; param != null; param = param.tail) {
                SymbolTableElement paramType = this.typeSymbolTable.lookup(param.typ.name);
                param.setDef(paramType.exp);
                // formal variable definition
                if (!this.varSymbolTable.contains(param.name, false)) {
                    this.varSymbolTable.put(param.name, new SymbolTableElement(paramType.type, param));
                } else {
                    this.errorMsg.error(param.pos, "redefinition:" + param.name);
                }
            }
            functionDec.body.accept(this);
            this.varSymbolTable.endScope();
        }
        this.visitedType = Constants.VOID;
    }

    /**
     * Visits a user defined type declaration and binds the symbol to its type
     * representation. This allows further lookups using the exp.name which will
     * resolve the the user defined type.
     * 
     * type a = int // TypeDec(NameTy(SymbolTy)) tpye intArray = array of int //
     * TypeDec(ArrayTy(SymbolTy)) type rec = {first: typ1, secont: ty2} //
     * TypeDec(RecordTy(FieldList)))
     */
    @Override
    public void visit(TypeDec exp) {
        // for a block of type declarations, visit the lvalue
        for (TypeDec typeDec = exp; typeDec != null; typeDec = typeDec.next) {
            // create a new name type for the lvalue.
            Type nameType = new NAME(typeDec.name);
            // install new type definitio. in symbol table.
            if (!this.typeSymbolTable.contains(typeDec.name)) {
                this.typeSymbolTable.put(typeDec.name, new SymbolTableElement(nameType, typeDec));
            } else {
                this.errorMsg.error(typeDec.pos, "redefinition:" + typeDec.name);
            }
        }
        // visit again and process the rvalue, which could be namety, arrayty or recordty.
        for (TypeDec typeDec = exp; typeDec != null; typeDec = typeDec.next) {
            if (this.typeSymbolTable.contains(typeDec.name)) {
                SymbolTableElement symbolTableElement = this.typeSymbolTable.lookup(typeDec.name);
                NAME nameType = (NAME) symbolTableElement.type;
                // visit lvalue to get the type..
                typeDec.ty.accept(this);
                // bind the ty value.
                nameType.bind(this.visitedType);
            }
        }
        this.visitedType = Constants.VOID;
    }

    /**
     * Visit a explicit type in a var declaration, eg var a:int = 1, where int is
     * the NameTy or visit the return type defined in a function declaration eg
     * function a():int, where int is the NameTy, or type t = int, where int is
     * NameTy. It can also be used in a FieldList {a: int, b: b}
     */
    @Override
    public void visit(NameTy exp) {
        // lookup the rvalue type in the symbol table.
        if (this.typeSymbolTable.contains(exp.name)) {
            SymbolTableElement def = this.typeSymbolTable.lookup(exp.name);
            // set the type for binding.
            this.visitedType = def.type;
            this.setType(exp, this.visitedType);
        } else {
            this.errorMsg.error(exp.pos, "undefined type:" + exp.name);
            this.visitedType = null;
        }
    }

    /**
     * Visits an array type within a type declaration. Contructs an ARRAY type and
     * stores it in the symbol table.
     */
    @Override
    public void visit(ArrayTy exp) {
        // type usage
        if (this.typeSymbolTable.contains(exp.typ)) {
            // lookup type of each array element.
            SymbolTableElement def = this.typeSymbolTable.lookup(exp.typ);
            // set the type for binding
            this.visitedType = new ARRAY(def.type);
            this.setType(exp, this.visitedType);
        } else {
            this.errorMsg.error(exp.pos, "undefined type:" + exp.typ);
            this.visitedType = null;
        }
    }

    @Override
    public void visit(RecordTy exp) {
        if(exp.fields != null) {
            // the field accept call sets visited type
            exp.fields.accept(this);
            this.setType(exp, this.visitedType);
        }
    }

    /**
     * Visits a fieldlist, used for both function arguments and record definitions.
     */
    @Override
    public void visit(FieldList exp) {
        // build record type 
        RECORD last = null, first = null, temp = null;
        FieldList expList = exp;
        do {
            temp = last;
            expList.typ.accept(this);
            // this.visitedType could be null if the type does not exist.
            if(this.visitedType != null) {
                this.setType(expList.typ, this.visitedType);
                last = new RECORD(expList.name, this.visitedType, null);
                if (first == null) {
                    first = last;
                } else {
                    temp.tail = last;
                }
            }
            expList = expList.tail;
        } while (expList != null);
        this.visitedType = first;
    }
}