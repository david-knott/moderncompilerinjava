package Bind;

import Absyn.DefaultVisitor;
import Absyn.FieldList;
import Absyn.FieldVar;
import Absyn.FunctionDec;
import Absyn.RecordExp;
import Absyn.SimpleVar;
import Absyn.TypeDec;
import Absyn.VarDec;
import Absyn.VarExp;

import java.util.Hashtable;

import Absyn.CallExp;
import Absyn.Absyn;
import Symbol.Symbol;

/**
 * Ensures that all variables, types and functions have unique names
 * within a AST. This is done on the original AST. 
 * Note the field names are unaffected.
 */
public class Renamer extends DefaultVisitor {

    private int id = 0;
    private Hashtable<Absyn, Symbol> newNames = new Hashtable<Absyn, Symbol>();

    @Override
    public void visit(TypeDec exp) {
        String uniqueTypeName = exp.name.toString() + "_" + (this.id++);
        Symbol newSymbol = Symbol.symbol(uniqueTypeName);
        newNames.put(exp, newSymbol);
        exp.name = newSymbol;
        if(exp.next != null) {
            exp.next.accept(this);
        }
    }

    @Override
    public void visit(VarDec exp) {
        exp.typ.name = newNames.get(exp.typ.def);
        String uniqueVarName = exp.name + "_" + (this.id++);
        Symbol newSymbol = Symbol.symbol(uniqueVarName);
        newNames.put(exp, newSymbol);
        exp.name = newSymbol;
        exp.init.accept(this);
    }

    @Override
    public void visit(FunctionDec exp) {
        if(exp.params != null) {
            for(FieldList fl = exp.params; fl != null; fl = fl.tail) {
                fl.typ = newNames.get(fl.def);
                String uniqueParamName = fl.name + "_" + (this.id++);
                Symbol newPSymbol = Symbol.symbol(uniqueParamName);
                newNames.put(fl, newPSymbol);
                fl.name = newPSymbol;
            }
        }
        String uniqueFunctionName = exp.name.toString() + "_" + (this.id++);
        Symbol newSymbol = Symbol.symbol(uniqueFunctionName);
        newNames.put(exp, newSymbol);
        exp.name = newSymbol;
        if(exp.result != null) {
            exp.result.name  = newNames.get(exp.result.def);
        }
        if(exp.body != null) {
            exp.body.accept(this);
        }
        if(exp.next != null) {
            exp.next.accept(this);
        }
    }

    @Override
    public void visit(CallExp exp) {
        exp.func = newNames.get(exp.def);
        if(exp.args != null) {
            exp.args.accept(this);
        }
    }

    @Override
    public void visit(RecordExp exp) {
        exp.typ = newNames.get(exp.def);
        exp.fields.accept(this);
    }
    
    @Override
    public void visit(SimpleVar exp) {
        exp.name = newNames.get(exp.def);
    }
}