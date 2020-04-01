package RegAlloc;

import java.util.Hashtable;

import FlowGraph.FlowGraph;
import Graph.Node;
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

    public BitArrayInterferenceGraphImpl(FlowGraph flowGraph) {
        liveMap = new Hashtable<Node, TempList>();
        tempMap = new Hashtable<Integer, Temp>();
        for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
            var node = nodes.head;
            for (var tl = flowGraph.def(node); tl != null; tl = tl.tail){
                tempMap.put(tl.head.hashCode(), tl.head);
            }
        }
        int capacity = getCapacity(flowGraph);
        // initialise
        for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
            liveInMap.put(nodes.head, new BitArraySet(capacity));
            liveOutMap.put(nodes.head, new BitArraySet(capacity));
        }
        do {
            boolean changed = false;
            for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
                var node = nodes.head;
                BitArraySet liveInPrev = liveInMap.get(node);
                BitArraySet liveOutPrev = liveOutMap.get(node);
                var t = new BitArraySet(flowGraph.use(node), capacity);
                //System.out.println(t);
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
        //populate live map with live out variables
        for(Node n : liveOutMap.keySet()){
            var bitMap = liveOutMap.get(n);
            for(int i = 0; i < capacity; i++) {
                if(bitMap.getBit(i)) {
                    TempList tempList = liveMap.get(n);
                    if(tempList != null) {
                        tempList = new TempList(tempMap.get(i), tempList);
                        liveMap.put(n, tempList);
                    } else {
                        tempList = new TempList(tempMap.get(i), null);
                        liveMap.put(n, tempList);
                    }
                }
            }
        }
        //from the liveMap, construct the interference graph
        for(Node n : liveMap.keySet()){
            TempList tempList = liveMap.get(n);
            for(; tempList != null; tempList = tempList.tail) {
                for(var defs = flowGraph.def(n); defs != null; defs = defs.tail) {
                    if(!flowGraph.isMove(n)) {
                        System.out.println("adding " + defs.head + " to " + tempList.head);
                        Node from = this.newNode();
                        nodeTempMap.put(from, defs.head);
                        tempNodeMap.put(defs.head, from);
                        Node to = this.newNode();
                        nodeTempMap.put(to, tempList.head);
                        tempNodeMap.put(tempList.head, to);
                        this.addEdge(from, to);
                    } else {
                        if(defs.head != tempList.head) {
                            System.out.println("adding " + defs.head + " to " + tempList.head);
                            Node from = this.newNode();
                            nodeTempMap.put(from, defs.head);
                            tempNodeMap.put(defs.head, from);
                            Node to = this.newNode();
                            nodeTempMap.put(to, tempList.head);
                            tempNodeMap.put(tempList.head, to);
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Temp gtemp(Node node) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MoveList moves() {
        // TODO Auto-generated method stub
        return null;
    }
}