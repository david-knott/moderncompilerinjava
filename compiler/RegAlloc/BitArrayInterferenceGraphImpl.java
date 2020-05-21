package RegAlloc;

import java.util.Hashtable;

import FlowGraph.FlowGraph;
import Graph.Node;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempList;

public class BitArrayInterferenceGraphImpl extends InterferenceGraph {

    /**
     * Returns a count of all definitions
     * 
     * @param flowGraph
     * @return
     */
    public static int getCapacity(FlowGraph flowGraph) {
        var capacity = 0;
        for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
            var node = nodes.head;
            for (var tl = flowGraph.def(node); tl != null; tl = tl.tail)
                capacity = Math.max(capacity, tl.head.hashCode());
        }
        System.out.println("capacity " + capacity);
        return capacity;
    }

    private Hashtable<Node, Temp> nodeTempMap = new Hashtable<Node, Temp>();
    private Hashtable<Temp, Node> tempNodeMap = new Hashtable<Temp, Node>();
    private Hashtable<Node, TempList> liveMap;
    private Hashtable<Integer, Temp> tempMap;
    private Hashtable<Node, BitArraySet> liveInMap = new Hashtable<Node, BitArraySet>();
    private Hashtable<Node, BitArraySet> liveOutMap = new Hashtable<Node, BitArraySet>();

    private Temp getTemp(Integer i) {
        if (tempMap.containsKey(i)) {
            return tempMap.get(i);
        }
        return null;
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

    public BitArrayInterferenceGraphImpl(FlowGraph flowGraph) {
        liveMap = new Hashtable<Node, TempList>();
        tempMap = new Hashtable<Integer, Temp>();
        int capacity = 0;
        for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
            var node = nodes.head;
            for (var tl = flowGraph.def(node); tl != null; tl = tl.tail) {
                tempMap.put(tl.head.hashCode(), tl.head);
                capacity = Math.max(capacity, tl.head.hashCode());
            }
        }
        // initialise maps with empty bit sets
        for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
            liveInMap.put(nodes.head, new BitArraySet(capacity));
            liveOutMap.put(nodes.head, new BitArraySet(capacity));
        }
        // calculate live ranges using liveness equations
        do {
            boolean changed = false;
            for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
                var node = nodes.head;
                BitArraySet liveInPrev = liveInMap.get(node);
                BitArraySet liveOutPrev = liveOutMap.get(node);
                BitArraySet liveIn = new BitArraySet(flowGraph.use(node), capacity)
                        .union(liveOutPrev.difference(new BitArraySet(flowGraph.def(node), capacity)));
                BitArraySet liveOut = new BitArraySet(capacity);
                for (var succ = node.succ(); succ != null; succ = succ.tail) {
                    BitArraySet liveInSucc = liveInMap.get(succ.head);
                    liveOut = liveOut.union(liveInSucc);
                }
                // save liveIn and liveOut in hash map for node
                liveInMap.put(node, liveIn);
                liveOutMap.put(node, liveOut);
                changed = changed || (!liveIn.equals(liveInPrev) || !liveOut.equals(liveOutPrev));
            }
            if (!changed) {
                break;
            }
        } while (true);
        // add live ranges as tempLists to liveOutmap
        for (Node n : liveOutMap.keySet()) {
            var bitMap = liveOutMap.get(n);
            for (int i = 0; i < capacity; i++) {
                if (bitMap.getBit(i)) {
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
                            this.addEdge(from, to);
                        }
                    }
                }
            } else {
                // for each def temp and liveout temp create edge
                for (; defs != null; defs = defs.tail) {
                    for (; tempList != null; tempList = tempList.tail) {
                        if(tempList.head != defs.head) {
                            Node to = this.getOrCreate(tempList.head);
                            Node from = this.getOrCreate(defs.head);
                            this.addEdge(from, to);
                        }
                    }
                }
            }
        }
        System.out.println("done");
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
        return null;
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