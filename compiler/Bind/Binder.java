package Bind;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Hashtable;

import Absyn.ArrayTy;
import Absyn.DefaultVisitor;
import Absyn.FieldList;
import Absyn.ForExp;
import Absyn.FunctionDec;
import Absyn.LetExp;
import Absyn.NameTy;
import Absyn.RecordTy;
import Absyn.TypeDec;
import Absyn.VarDec;
import Absyn.WhileExp;
import Parse.sym;
import Symbol.Symbol;
import Types.ARRAY;
import Types.INT;
import Types.NAME;
import Types.RECORD;
import Types.STRING;
import Types.Type;
import Util.Assert;

class Scope {

    private INT INT = new INT();
    private STRING STRING = new STRING();
    public Hashtable<Symbol, Type> table = new Hashtable<Symbol, Type>();
    public final Scope parent;

    public Scope() {
        parent = null;
        this.table.put(Symbol.symbol("int"), INT);
        this.table.put(Symbol.symbol("string"), STRING);
    }

    Scope(Scope parent) {
        this.parent = parent;
    }

    public void put(Symbol symbol, Type o) {
        Assert.assertNotNull(symbol);
        Assert.assertNotNull(o);
        this.table.put(symbol, o);
    }

    public Type lookup(Symbol symbol) {
        Assert.assertNotNull(symbol);
        Scope currentScope = this;
        do {
            if (currentScope.table.containsKey(symbol)) {
                return currentScope.table.get(symbol);
            }
            currentScope = currentScope.parent;
        } while (currentScope != null);
        throw new Error("Cannot find symbol " + symbol);
    }

    /**
     * Create a new scope for the given namespace
     */
    public void beginScope() {
        System.out.println("beginscope");

    }

    /**
     * Destroy the most recently used scope namespace.
     */
    public void endScope() {
        System.out.println("endscope");

    }

    public void debug(OutputStream outputStream) {
        try(PrintStream printStream = new PrintStream(outputStream)) {
            printStream.println("## scope debug information");

        }
    }
}

/**
 * The Binder class traverses the abstract syntax tree and remembers variable,
 * function and type declarations.
 * 
 * These are used in the type checking and semantic analysis phase.
 */
public class Binder extends DefaultVisitor {

    Scope typeScope = new Scope();
    Type type = null;

    @Override
    public void visit(LetExp exp) {
        this.typeScope.beginScope();
        if (exp.decs != null) {
            exp.decs.accept(this);
        }
        if (exp.body != null) {
            exp.body.accept(this);
        }
        this.typeScope.endScope();
    }

    @Override
    public void visit(FunctionDec exp) {
        /*
         * this.typeScope.beginScope(); if(exp.params != null) {
         * exp.params.accept(this); } exp.body.accept(this); this.typeScope.endScope();
         */
    }

    /**
     * Visits a user defined type declaration and binds the symbol to its type
     * representation. This allows further lookups using the exp.name which will
     * resolve the the user defined type.
     */
    @Override
    public void visit(TypeDec exp) {
        this.type = new NAME(exp.name);
        this.typeScope.put(exp.name, this.type);
        // visit the type definition.
        exp.ty.accept(this);
        // bind symbol to empty name type 
        System.out.println("bind:type:" + exp.name + " => " + this.type);
    }

    /**
     * Visits a record type within a type declaration. This method is probably not
     * needed.
     */
    @Override
    public void visit(RecordTy exp) {
        RECORD last = null;
        FieldList expList = exp.fields;
        do
        {
            last = new RECORD(expList.name, this.typeScope.lookup(expList.typ), last);
            expList = expList.tail;
        }while(expList != null);
        ((NAME)this.type).bind(last);
    }

    /**
     * Visits an array type within a type declaration. Contructs an ARRAY type and
     * stores it in the symbol table.
     */
    @Override
    public void visit(ArrayTy exp) {
        ((NAME)this.type).bind(new ARRAY(this.typeScope.lookup(exp.typ)));
    }

    /**
     * Visit a explicity type in a var declaration, eg var a:int = 1, where int is
     * the NameTy or visit the return type defined in a function declaration eg
     * function a():int, where int is the NameTy, or type t = int, where int is NameTy
     */
    @Override
    public void visit(NameTy exp) {
        ((NAME)this.type).bind(this.typeScope.lookup(exp.name));
    }
}