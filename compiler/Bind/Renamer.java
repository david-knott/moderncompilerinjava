package Bind;

import java.util.Hashtable;

import Absyn.Absyn;
import Absyn.CallExp;
import Absyn.DefaultVisitor;
import Absyn.FieldList;
import Absyn.FunctionDec;
import Absyn.RecordExp;
import Absyn.SimpleVar;
import Absyn.TypeDec;
import Absyn.VarDec;
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
        for(FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            String uniqueFunctionName = functionDec.name.toString() + "_" + (this.id++);
            Symbol newSymbol = Symbol.symbol(uniqueFunctionName);
            newNames.put(functionDec, newSymbol);
            functionDec.name = newSymbol;
        }
        for(FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            if(functionDec.params != null) {
                for(FieldList fl = functionDec.params; fl != null; fl = fl.tail) {
                    fl.typ = newNames.get(fl.def);
                    String uniqueParamName = fl.name + "_" + (this.id++);
                    Symbol newPSymbol = Symbol.symbol(uniqueParamName);
                    newNames.put(fl, newPSymbol);
                    fl.name = newPSymbol;
                }
                if(functionDec.result != null) {
                    functionDec.result.name  = newNames.get(functionDec.result.def);
                }
                if(functionDec.body != null) {
                    functionDec.body.accept(this);
                }
            }
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