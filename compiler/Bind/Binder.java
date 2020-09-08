package Bind;

import java.util.Hashtable;

import Absyn.ArrayTy;
import Absyn.DefaultVisitor;
import Absyn.FieldList;
import Absyn.FunctionDec;
import Absyn.IntExp;
import Absyn.LetExp;
import Absyn.NameTy;
import Absyn.OpExp;
import Absyn.RecordTy;
import Absyn.StringExp;
import Absyn.TypeDec;
import Absyn.VarDec;
import Symbol.Symbol;
import Types.ARRAY;
import Types.Constants;
import Types.FUNCTION;
import Types.NAME;
import Types.RECORD;
import Types.Type;

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

    public Binder() {
        // base system types
        Hashtable<Symbol, Type> tinit = new Hashtable<Symbol, Type>();
        tinit.put(Symbol.symbol("int"), Constants.INT);
        tinit.put(Symbol.symbol("string"), Constants.STRING);
        this.typeSymbolTable = new SymbolTable(tinit);
        // base functions
        Hashtable<Symbol, Type> finit = new Hashtable<Symbol, Type>();
        finit.put(Symbol.symbol("print"),
                new FUNCTION(new RECORD(Symbol.symbol("s"), Constants.STRING, null), Constants.VOID));
        this.functionSymbolTable = new SymbolTable(finit);
        // var tableo
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
        this.functionSymbolTable.endScope();
        this.varSymbolTable.endScope();
        this.typeSymbolTable.endScope();
    }

    @Override
    public void visit(IntExp exp) {
        this.type = Constants.INT;
    }

    @Override
    public void visit(StringExp exp) {
        this.type = Constants.STRING;
    }

    @Override
    public void visit(OpExp exp) {
        this.type = Constants.INT;
    }

    /**
     * Visit a variable declaration and add it to the symbol table.
     */
    @Override
    public void visit(VarDec exp) {
        exp.init.accept(this);
        Type initType = this.type;
        this.varSymbolTable.put(exp.name, initType);
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
            Type returnType = functionDec.result != null ? this.typeSymbolTable.lookup(functionDec.result.name) : Constants.VOID;
            RECORD paramType = null;
            if(exp.params != null) {
                exp.params.accept(this);
                paramType = (RECORD)this.type;
            }
            this.functionSymbolTable.put(functionDec.name, new FUNCTION(paramType, returnType));
        }
        // second pass for function body.
        for(FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            this.varSymbolTable.beginScope();
            for (var param = functionDec.params; param != null; param = param.tail) {
                Type paramType = this.typeSymbolTable.lookup(param.typ);
                this.varSymbolTable.put(param.name, paramType);
                exp.body.accept(this);
            }
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
        for(TypeDec typeDec = exp; typeDec != null; typeDec = typeDec.next) {
            this.type = new NAME(typeDec.name);
            this.typeSymbolTable.put(typeDec.name, this.type);
        }
        for(TypeDec typeDec = exp; typeDec != null; typeDec = typeDec.next) {
            typeDec.ty.accept(this);
        }
    }

    /**
     * Visits a record type within a type declaration. It expects
     * that named is set.
     */
    @Override
    public void visit(RecordTy exp) {
        NAME named = (NAME)this.type;
        exp.fields.accept(this);
        named.bind(this.type);
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
            last = new RECORD(expList.name, this.typeSymbolTable.lookup(expList.typ), null);
            if(first == null) {
                first = last;
            } else {
               temp.tail = last; 
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
        ((NAME)this.type).bind(new ARRAY(this.typeSymbolTable.lookup(exp.typ)));
    }

    /**
     * Visit a explicit type in a var declaration, eg var a:int = 1, where int is
     * the NameTy or visit the return type defined in a function declaration eg
     * function a():int, where int is the NameTy, or type t = int, where int is NameTy
     */
    @Override
    public void visit(NameTy exp) {
        ((NAME)this.type).bind(this.typeSymbolTable.lookup(exp.name));
    }
}