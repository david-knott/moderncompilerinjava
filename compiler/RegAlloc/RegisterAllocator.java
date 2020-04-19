package RegAlloc;

import java.util.Hashtable;

import Assem.InstrList;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Frame.Frame;
import Graph.Node;
import Temp.Temp;
import Temp.TempMap;
import Util.LinkedListSet;
import Util.LinkedListStack;

/**
 * Register allocator class.
 */
public class RegisterAllocator implements TempMap {

    public InstrList instrs;
    public Frame frame;
    private FlowGraph flowGraph;
    private InterferenceGraph interferenceGraph;

    public RegisterAllocator(Frame frame, InstrList instructions) {
        if (frame == null)
            throw new Error("f cannot be null");
        if (instructions == null)
            throw new Error("il cannot be null");
        this.frame = frame;
        this.instrs = instructions;
        this.flowGraph = new AssemFlowGraph(this.instrs);
        this.interferenceGraph = new BitArrayInterferenceGraphImpl(this.flowGraph);
    }
    
    LinkedListSet<Temp>  getPrecolouredTemps() {
        return null; //this.frame.precoloured();
    }

    LinkedListSet<Temp> getInitialTemps() {
        return null;//this.frame.registers();
    }

    LinkedListSet<Node> getSimplifyWorklist() {
        return null;
    }

    LinkedListSet<Node> getColours() {
        return null;
    }

    LinkedListSet<Node> getColouredNodes() {
        return null;
    }

    int nodeCount() {
        int count = 0;
        for(var nodes = this.interferenceGraph.nodes(); nodes != null; nodes = nodes.tail) {
            count++;
        }
        return count;
    }

    public void allocate() {
        // generate a worklist set and degrees for nodes
        Hashtable<Node, Integer> degrees = new Hashtable<Node, Integer>();
        LinkedListStack<Node> simpleStack = new LinkedListStack<Node>();
        for (var nodes = this.interferenceGraph.nodes(); nodes != null; nodes = nodes.tail) {
            degrees.put(nodes.head, nodes.head.degree());
        }
        var simplifyWorklist = this.getSimplifyWorklist();
        do
        {
            var node = simplifyWorklist.first();
            simpleStack.push(node);
            for(var adj = node.adj(); adj != null; adj = adj.tail) {
                var degree = degrees.get(adj.head) - 1;
                degrees.put(adj.head, degree);
            }

        }while(!simplifyWorklist.isEmpty());
        //assign colours to the gragh
        while(!simpleStack.isEmpty()) {
            var node = simpleStack.pop();
            //var okColours = this.getColours();
            for(var adj = node.adj(); adj != null; adj = adj.tail) {
                Temp temp = null;
                if(this.getColouredNodes().contains(adj.head) || this.getPrecolouredTemps().contains(temp)) {
                    //okColours.remove(temp);
                }
            }
        }
    }

    @Override
    public String tempMap(Temp t) {
        return this.frame.tempMap(t);
    }
}