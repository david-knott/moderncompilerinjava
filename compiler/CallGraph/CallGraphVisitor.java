package CallGraph;

import Absyn.CallExp;
import Absyn.DefaultVisitor;
import Absyn.FunctionDec;

/**
 * Creates a call graph of functions, 
 * eg function a calls b call c.
 */
public class CallGraphVisitor extends DefaultVisitor {

    public static FunctionCallGraph computeClosure(FunctionCallGraph functionCallGraph) {
        return null;
    }

    FunctionDec visitedFunction;
    public FunctionCallGraph functionCallGraph;
    
    @Override
    public void visit(FunctionDec exp) {
        this.visitedFunction = exp;
        super.visit(exp);
    }

    @Override
    public void visit(CallExp exp) {
        FunctionDec src = this.visitedFunction;
        addEdge(src, (FunctionDec)exp.def);
        super.visit(exp);
    }

    private void addEdge(FunctionDec src, FunctionDec args) {
        System.out.println("edge:" + src + " " + args);
    }
}
