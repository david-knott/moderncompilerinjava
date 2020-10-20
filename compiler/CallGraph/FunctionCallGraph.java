package CallGraph;

import java.util.Hashtable;

import Absyn.FunctionDec;
import Graph.Graph;
import Graph.Node;

public class FunctionCallGraph extends Graph {

    Hashtable<FunctionDec, Node> functionDecs = new Hashtable<FunctionDec, Node>();

	public boolean inCycle(FunctionDec exp) {
		return super.inCycle(this.functionDecs.get(exp));
    }
    
    private Node getNode(FunctionDec src) {
        Node srcNode = null;
        if(this.functionDecs.containsKey(src)) {
            srcNode = this.functionDecs.get(src);
        } else {
            srcNode = this.newNode();
            this.functionDecs.put(src, srcNode);
        }
        return srcNode;
    }

    public void addEdge(FunctionDec src, FunctionDec args) {
        Node srcNode = this.getNode(src);
        Node destNode = this.getNode(args);
        this.addEdge(srcNode, destNode);
    }

}