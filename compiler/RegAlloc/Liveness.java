package RegAlloc;

import java.util.BitSet;
import java.util.Hashtable;

import Assem.Instr;
import FlowGraph.FlowGraph;
import Graph.Node;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempList;

class Liveness {
    private Hashtable<Node, BitSet> liveInMap = new Hashtable<Node, BitSet>();
    private Hashtable<Node, BitSet> liveOutMap = new Hashtable<Node, BitSet>();
    private Hashtable<Node, TempList> liveMap;
    private Hashtable<Integer, Temp> tempMap;
    private FlowGraph flowGraph;

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
            changed = false;
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
                    TempList tempList = this.liveMap.get(n);
                    Temp temp = getTemp(i);
                    if (temp != null) {
                        tempList = TempList.append(tempList, temp);
                        this.liveMap.put(n, tempList);
                    }
                }
            }
        }
    }

    public TempList liveMap(Node node) {
        return this.liveMap.get(node);
    }

    public TempList liveMap(Instr instr) {
        return this.liveMap.get(flowGraph.node(instr));
    }

    public Liveness(FlowGraph flowGraph) {
        this.liveMap = new Hashtable<Node, TempList>();
        this.tempMap = new Hashtable<Integer, Temp>();
        this.computeLiveness(flowGraph);
        this.computeLiveRanges();
        this.flowGraph = flowGraph;
    }

    public TempWorkList liveOut(Node node) {
        TempWorkList nodeWorkList = null;
        for (TempList tempList = this.liveMap.get(node); tempList != null; tempList = tempList.tail) {
            nodeWorkList = TempWorkList.append(nodeWorkList, tempList.head);
        }
        return nodeWorkList;
    }
}