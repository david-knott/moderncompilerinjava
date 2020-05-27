package RegAlloc;

import java.util.BitSet;
import java.util.Hashtable;

import FlowGraph.FlowGraph;
import Graph.Node;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempList;

class IGBackwardControlEdges extends InterferenceGraph {
    private Hashtable<Node, BitSet> liveInMap = new Hashtable<Node, BitSet>();
    private Hashtable<Node, BitSet> liveOutMap = new Hashtable<Node, BitSet>();
    private Hashtable<Node, TempList> liveMap;
    private Hashtable<Integer, Temp> tempMap;
    private Hashtable<Temp, Node> tempNodeMap = new Hashtable<Temp, Node>();
    private Hashtable<Node, Temp> nodeTempMap = new Hashtable<Node, Temp>();
    private MoveList moveList = null;
    private int iterationCount = 0;

    private Temp getTemp(Integer i) {
        if (tempMap.containsKey(i)) {
            return tempMap.get(i);
        }
        return null;
    }

    private BitSet fromTempList(TempList tempList) {
        BitSet bs = new BitSet();
        for (; tempList != null; tempList = tempList.tail) {
            bs.set(tempList.head.hashCode());
        }
        return bs;
    }

    private int compare(BitSet lhs, BitSet rhs) {
        if (lhs.equals(rhs))
            return 0;
        BitSet xor = (BitSet) lhs.clone();
        xor.xor(rhs);
        int firstDifferent = xor.length() - 1;
        if (firstDifferent == -1)
            return 0;
        return rhs.get(firstDifferent) ? 1 : -1;
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

    private void computeLiveness(FlowGraph flowGraph) {
        for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
            var node = nodes.head;
            for (var tl = flowGraph.def(node); tl != null; tl = tl.tail) {
                tempMap.put(tl.head.hashCode(), tl.head);
            }
        }
        // initialise maps with empty bit sets
        for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
            liveInMap.put(nodes.head, new BitSet());
            liveOutMap.put(nodes.head, new BitSet());
        }
        // calculate live ranges using liveness equations, except in reverse this time.
        boolean changed = false;
        do {
            /*
             * changed <- false loop ( ) if old and new sets not equal changed <- true if
             * changed = false break
             * 
             */
            changed = false;
            iterationCount++;
            for (NodeList nodes = flowGraph.nodes().reverse(); nodes != null; nodes = nodes.tail) {
                var node = nodes.head;
                BitSet liveInPrev = (BitSet) liveInMap.get(node).clone();
                BitSet liveOutPrev = (BitSet) liveOutMap.get(node).clone();
                // reverse, calculate liveout first
                BitSet liveOut = new BitSet();
                for (var succ = node.succ(); succ != null; succ = succ.tail) {
                    BitSet liveInPred = (BitSet) (liveInMap.get(succ.head).clone());
                    liveOut.or(liveInPred);
                }
                liveOutMap.put(node, liveOut);
                // calculate live in.
                BitSet liveIn = (BitSet) this.fromTempList(flowGraph.use(node)).clone();
                BitSet def = (BitSet) this.fromTempList(flowGraph.def(node)).clone();
                // BitSet dif = (BitSet)liveOutPrev.clone();
                BitSet dif = (BitSet) liveOutPrev.clone();
                dif.andNot(def);
                liveIn.or(dif);
                // record the liveIn and liveOut for this node
                liveInMap.put(node, liveIn);
                var c1 = compare(liveIn, liveInPrev);
                var c2 = compare(liveOut, liveOutPrev);
                changed = changed || ((c1 != 0) || (c2 != 0));
            }
            System.out.println("--- Iteration " + iterationCount + " ----");
            for (NodeList nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
                System.out.println(nodes.head + " " + liveInMap.get(nodes.head) + " " + liveOutMap.get(nodes.head));
            }
            System.out.println("---------------");
            if (!changed)
                break;

        } while (true);
    }

    private void computeLiveRanges() {
        /// add live ranges as tempLists to liveOutmap
        for (Node n : liveOutMap.keySet()) {
            var bitMap = liveOutMap.get(n);
            for (int i = 0; i < bitMap.size(); i++) {
                if (bitMap.get(i)) {
                    TempList tempList = liveMap.get(n);
                    Temp temp = getTemp(i);
                    if (temp != null) {
                        if (tempList != null) {
                            tempList = new TempList(temp, tempList);
                            liveMap.put(n, tempList);
                        } else {
                            tempList = new TempList(temp, null);
                            liveMap.put(n, tempList);
                        }
                    }
                }
            }
        }
    }

    private void buildGraph(FlowGraph flowGraph) {
        for (Node n : liveMap.keySet()) {
            TempList tempList = liveMap.get(n);
            var defs = flowGraph.def(n);
            var uses = flowGraph.use(n);
            if (flowGraph.isMove(n)) {
                // for each use temp that is not equals to liveout temp create edge
                for (; uses != null; uses = uses.tail) { // dont need this loop, as only 1 use per moe ?
                    for (; tempList != null; tempList = tempList.tail) {
                        // we can assume moves only have 1 src and 1 dest
                        if (uses.head != tempList.head && defs.head != tempList.head) {
                            Node from = this.getOrCreate(defs.head);
                            Node to = this.getOrCreate(tempList.head);
                            this.moveList = new MoveList(from, to, this.moveList);
                            this.addEdge(from, to);
                        }
                    }
                }
            } else {
                // for each def temp and liveout temp create edge
                for (; defs != null; defs = defs.tail) {
                    for (; tempList != null; tempList = tempList.tail) {
                        if (tempList.head != defs.head) {
                            Node to = this.getOrCreate(tempList.head);
                            Node from = this.getOrCreate(defs.head);
                            this.addEdge(from, to);
                        }
                    }
                }
            }
        }
    }

    /**
     * Constructor for a InterferenceGraphv2.
     * 
     * @param flowGraph the flowgraph
     */
    public IGBackwardControlEdges(FlowGraph flowGraph) {
        liveMap = new Hashtable<Node, TempList>();
        tempMap = new Hashtable<Integer, Temp>();
        this.computeLiveness(flowGraph);
        this.computeLiveRanges();
        this.buildGraph(flowGraph);

    }

    public int getIterationCount() {
        return this.iterationCount;
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