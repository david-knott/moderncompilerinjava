package Bind;

import java.util.Hashtable;

import Absyn.Absyn;
import Absyn.CallExp;
import Absyn.DefaultVisitor;
import Absyn.FieldList;
import Absyn.FunctionDec;
import Absyn.NameTy;
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
        // exp.typ can be null eg var a := 0
        if(exp.typ != null) {
            exp.typ.name = newNames.get(exp.typ.def);
        }
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
                // update the ty name, create new symbol for formal args, except for int & strings.
                for(FieldList fl = functionDec.params; fl != null; fl = fl.tail) {
                    // set the renamed type symbol.
                    if(fl.def != null) {
                        fl.typ.name =  newNames.get(fl.def);
                    }
                    // create new param names for formal arguments.
                    String uniqueParamName = fl.name + "_" + (this.id++);
                    Symbol newPSymbol = Symbol.symbol(uniqueParamName);
                    newNames.put(fl, newPSymbol);
                    fl.name = newPSymbol;
                }
            }
            // rename the return type of the function, except for int & strings.
            if (functionDec.result != null) {
                functionDec.result.accept(this);
            }
            // process body.
            if (functionDec.body != null) {
                functionDec.body.accept(this);
            }
        }
    }

    /**
     * Sets the name field in the NameTy to its renamed
     * equivalent, except when then the NameTy references 
     * a string or int type.
     */
    @Override
    public void visit(NameTy exp) {
        if(exp.name == Symbol.symbol("int") || exp.name == Symbol.symbol("string")) {
            return;
        }
        Symbol newSymbol = newNames.get(exp.def);
        exp.name = newSymbol;
    }

    /**
     * Sets the function name in the CallExp to its
     * renamed equivalent, except when the function is
     * a runtime function.
     */
    @Override
    public void visit(CallExp exp) {
        // if runtime function exp.def will be null
        if(exp.def != null) {
            exp.func = newNames.get(exp.def);
        }
        if(exp.args != null) {
            exp.args.accept(this);
        }
    }

    /**
     * Sets the record type to its renamed equivalent.
     */
    @Override
    public void visit(RecordExp exp) {
        exp.typ = newNames.get(exp.def);
        exp.fields.accept(this);
    }
    
    /**
     * Sets a simple variable reference to its renamed
     * equivalent.
     */
    @Override
    public void visit(SimpleVar exp) {
        exp.name = newNames.get(exp.def);
    }
}