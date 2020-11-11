package Inlining;

import java.util.ArrayList;
import java.util.List;

import Absyn.Absyn;
import Absyn.CallExp;
import Absyn.Dec;
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
import Graph.Node;
import Symbol.Symbol;


import Cloner.AbsynCloner;

public class Pruner extends AbsynCloner {

    FunctionCallGraph callGraph;
    public int pruneCount = 0;

    public Pruner(Absyn absyn) {
        CallGraphVisitor callGraphVisitor = new CallGraphVisitor();
        absyn.accept(callGraphVisitor);
        this.callGraph = callGraphVisitor.functionCallGraph;
    }

    @Override
    public void visit(LetExp exp) {
        DecList clonedDecList = null, first = null, temp = null;
        if(exp.decs != null) {
            for(DecList decList = exp.decs; decList != null; decList = decList.tail) {
                if(decList.head instanceof FunctionDec){
                    Node node = callGraph.getNode((FunctionDec)decList.head);
                    if(node == null)  {
                        pruneCount++;
                        continue;
                    }
                } 
                decList.head.accept(this);
                Dec clonedDec = this.visitedDec;
                if(first == null) {
                    first = clonedDecList = new DecList(clonedDec, null);
                } else {
                    temp = clonedDecList;
                    clonedDecList = new DecList(clonedDec, null);
                    temp.tail = clonedDecList;
                }
            }
        }
        Exp clonedBody = null;
        if(exp.body != null) {
            exp.body.accept(this);
            clonedBody = this.visitedExp;
        }
        this.visitedExp = new LetExp(exp.pos, first, clonedBody);
    }
}