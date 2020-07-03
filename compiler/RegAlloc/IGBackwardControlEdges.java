package RegAlloc;

import java.util.BitSet;
import java.util.Hashtable;

import Assem.Instr;
import FlowGraph.FlowGraph;
import Graph.Node;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempList;

public class IGBackwardControlEdges extends InterferenceGraph {
    private Hashtable<Temp, Node> tempNodeMap = new Hashtable<Temp, Node>();
    private Hashtable<Node, Temp> nodeTempMap = new Hashtable<Node, Temp>();
    private MoveList moveList = null;
    private FlowGraph flowGraph;
    private Liveness liveness;

    private Hashtable<Node, Integer> useCount = new Hashtable<Node, Integer>();
    private Hashtable<Node, Integer> defCount = new Hashtable<Node, Integer>();

    private void updateUseAndDefCounts() {
        for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
            Node node = nodes.head;
            for (TempList defs = flowGraph.def(node); defs != null; defs = defs.tail) {
                Node defNode = this.tnode(defs.head);
                if (defNode != null) {
                    useCount.put(defNode, defCount.getOrDefault(node, 1) + 1);
                }
            }
            for (TempList uses = flowGraph.use(node); uses != null; uses = uses.tail) {
                Node useNode = this.tnode(uses.head);
                if (useNode != null) {
                    useCount.put(useNode, defCount.getOrDefault(node, 1) + 1);
                }
            }
        }
    }

    private Node getOrCreate(Temp temp) {
        if (tempNodeMap.containsKey(temp)) {
            return tempNodeMap.get(temp);
        }
        var newNode = this.newNode();
        nodeTempMap.put(newNode, temp);
        tempNodeMap.put(temp, newNode);
        return newNode;
    }

    public void addEdge(Node from, Node to) {
        if(from == to) return;
        super.addEdge(from, to);
    }


    private void buildGraph(FlowGraph flowGraph) {
        int edges = 0;
        for(NodeList nodeList = this.flowGraph.nodes(); nodeList != null; nodeList = nodeList.tail) {
            Node n = nodeList.head;
            var defs = flowGraph.def(n);
            var uses = flowGraph.use(n);
            //compute the interference edges
            if (flowGraph.isMove(n)) {
                boolean interferes = false;
                // for each use temp that is not equals to liveout temp create edge
                //for (; uses != null; uses = uses.tail) { // dont need this loop, as only 1 use per moe ?
                    for (TempList tempList = this.liveness.liveMap(n); tempList != null; tempList = tempList.tail) {
                        // we can assume moves only have 1 src and 1 dest
                        if (uses.head != tempList.head && defs.head != tempList.head) {
                            Node from = this.getOrCreate(defs.head);
                            Node to = this.getOrCreate(tempList.head);
                            this.addEdge(from, to);
                            interferes = true;
                            edges++;
                        }
                    }
                    if(!interferes) {
                    //    this.moveList = new MoveList(this.tnode(flowGraph.use(n).head), this.tnode(flowGraph.def(n).head), this.moveList);
                    }
                //}
            } else {
                // for each def temp and liveout temp create edge
                for (; defs != null; defs = defs.tail) {
                    for (TempList tempList = this.liveness.liveMap(n); tempList != null; tempList = tempList.tail) {
                        Node to = this.getOrCreate(tempList.head);
                        Node from = this.getOrCreate(defs.head);
                        this.addEdge(from, to);
                            edges++;
                    }
                }
            }
        }
        System.out.println("IG built nodes:" + edges);
        this.flowGraph.show(System.out);
        this.show(System.out);
    }

    /**
     * Constructor for a InterferenceGraphv2.
     * 
     * @param flowGraph the flowgraph
     */
    public IGBackwardControlEdges(FlowGraph flowGraph, Liveness liveness) {
        this.flowGraph = flowGraph;
        this.liveness = liveness;
        this.buildGraph(flowGraph);
        this.updateUseAndDefCounts();
    }

    @Override
    public Node tnode(Temp temp) {
        return this.tempNodeMap.get(temp);
    }

    @Override
    public Temp gtemp(Node node) {
        return this.nodeTempMap.get(node);
    }

    @Override
    public MoveList moves() {
        return this.moveList;
    }

    @Override
    public int spillCost(Node node) {
        double f = ((float)this.defCount.getOrDefault(node, 0) + this.useCount.getOrDefault(node, 0))/node.degree();
        return (int)(100 * f);
    }

    @Override
    public void show(java.io.PrintStream out) {
        for (NodeList p = nodes(); p != null; p = p.tail) {
            Node n = p.head;
            out.print(n.toString());
            out.print(": ");
            out.print(this.gtemp(n));
            out.print(" interferes with:");
            for (NodeList q = n.succ(); q != null; q = q.tail) {
                out.print(this.gtemp(q.head));
                out.print(" ");
            }
            out.println();
        }
    }

    @Override
    public void bind(Node node, Temp temp) {
        // TODO Auto-generated method stub

    }
}