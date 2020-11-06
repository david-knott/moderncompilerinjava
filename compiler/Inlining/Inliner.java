package Inlining;

import java.util.ArrayList;
import java.util.List;

import Absyn.Absyn;
import Absyn.CallExp;
import Absyn.DecList;
import Absyn.Exp;
import Absyn.ExpList;
import Absyn.FieldList;
import Absyn.FunctionDec;
import Absyn.LetExp;
import Absyn.SeqExp;
import Absyn.SimpleVar;
import Absyn.VarDec;
import Absyn.VarExp;
import CallGraph.CallGraphVisitor;
import CallGraph.FunctionCallGraph;
import Cloner.AbsynCloner;
import Symbol.Symbol;

/**
 * Identifies functions that are only called once and
 * inlines them into the call site.
 */
public class Inliner extends AbsynCloner {
    
    List<FunctionDec> ignore = new ArrayList<FunctionDec>();
    FunctionCallGraph callGraph;

    public Inliner(Absyn absyn) {
        CallGraphVisitor callGraphVisitor = new CallGraphVisitor();
        absyn.accept(callGraphVisitor);
        this.callGraph = callGraphVisitor.functionCallGraph;
    }

    @Override
    public void visit(FunctionDec exp) {
        // if function is in cycle or if exp.body is null 
        // this is a primitive which we ignore.
        for(FunctionDec functionDec = exp; functionDec != null; functionDec = functionDec.next) {
            if(functionDec.body == null || this.callGraph.inCycle(functionDec)) {
                ignore.add(functionDec);
            }
        }
        super.visit(exp);
    }

    @Override
    public void visit(CallExp exp) {
        FunctionDec functionDec = (FunctionDec)exp.def;
        if(!ignore.contains(functionDec)) {
            ExpList argList = exp.args;
            DecList decList = null, first = null, temp = null;
            for(FieldList fieldList = functionDec.params; fieldList != null; fieldList = fieldList.tail){
                VarDec varDec = new VarDec(0, fieldList.name, fieldList.typ, argList.head/* argument */);
                if(first == null) {
                    first = decList = new DecList(varDec, null);
                } else {
                    temp = decList;
                    decList = new DecList(varDec, null);
                    temp.tail = decList;
                }
                argList = argList.tail;
            }
            //TODO: if the return type is void, we dont need a res.
            VarDec varDec = new VarDec(0, Symbol.symbol("res"), functionDec.result,  functionDec.body/* exp */);
            DecList end = first;
            for(;end.tail != null; end = end.tail);
            end.tail = new DecList(varDec, null);
            Exp letExp = new LetExp(0, first, new SeqExp(0, new ExpList(new VarExp(0, new SimpleVar(0, Symbol.symbol("res"))), null)));
            this.visitedExp = letExp;
        } else {
            super.visit(exp);
        }
    }
}
