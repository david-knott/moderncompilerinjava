package Bind;

import java.util.Hashtable;

import Absyn.DefaultVisitor;
import Absyn.ForExp;
import Absyn.FunctionDec;
import Absyn.LetExp;
import Absyn.TypeDec;
import Absyn.VarDec;
import Absyn.WhileExp;
import Symbol.Symbol;


class Scope {

    public Hashtable<Symbol, Object> table = new Hashtable<Symbol, Object>();
    public final Scope parent;

    public Scope() {
        parent = null;
    }

    Scope(Scope parent) {
        this.parent = parent;
    }

    public void put(Symbol symbol, Object o) {

    }

    public Object lookup(Symbol symbol) {
        return null;
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
}

/**
 * The Binder class traverses the abstract syntax tree
 * and remembers variable, function and type declarations.
 * 
 * These are used in the type checking and semantic analysis phase.
 */
public class Binder extends DefaultVisitor {

    Scope scope = new Scope();

    @Override
    public void visit(LetExp exp) {
        this.scope.beginScope();
        if(exp.decs != null) {
            exp.decs.accept(this);
        }
        if(exp.body != null) {
            exp.body.accept(this);
        }
        this.scope.endScope();
    }

    @Override
    public void visit(FunctionDec exp) {
        this.scope.beginScope();
        if(exp.params != null) {
            exp.params.accept(this);
        }
        exp.body.accept(this);
        this.scope.endScope();
    }

    /**
     * Visits a user defined type declaration and binds the symbol to its 
     * type representation. This allows further looks using
     * the exp.name which will resolve the the user defined type.
     */
    @Override
    public void visit(TypeDec exp) {
        System.out.println("bind:type:" + exp.name +  " => " + exp.ty);
        // we need to construct the type type
        this.scope.put(exp.name, exp);
    }

    @Override
    public void visit(VarDec exp) {
        System.out.println("bind:var:" + exp.name +  " => " + exp.typ);
        this.scope.put(exp.name, exp);
    }
}