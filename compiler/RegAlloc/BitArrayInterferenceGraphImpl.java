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
                capacity++;
        }
        return capacity;
    }

    class BitArraySet {

        private static final int ALL_ONES = 0xFFFFFFFF;
        private static final int WORD_SIZE = 32;

        private int bits[] = null;

        public BitArraySet(int size) {
            // bits = new int[capacity];
            bits = new int[size / WORD_SIZE + (size % WORD_SIZE == 0 ? 0 : 1)];
        }

        public BitArraySet(TempList tempList, int capacity) {
            this(capacity);
            for (var tp = tempList; tp != null; tp = tp.tail) {
                var temp = tp.head;
                var pos = temp.hashCode();
                setBit(pos, true);
            }
        }

        public boolean getBit(int pos) {
            return (bits[pos / WORD_SIZE] & (1 << (pos % WORD_SIZE))) != 0;
        }

        public void setBit(int pos, boolean b) {
            int word = bits[pos / WORD_SIZE];
            int posBit = 1 << (pos % WORD_SIZE);
            if (b) {
                word |= posBit;
            } else {
                word &= (ALL_ONES - posBit);
            }
            bits[pos / WORD_SIZE] = word;
        }

        public BitArraySet add(TempList tempList) {
            return this;
        }

        public BitArraySet union(BitArraySet bitArraySet) {
            return null;
        }

        public BitArraySet difference(BitArraySet bitArraySet) {
            return null;
        }

        public BitArraySet intersection(BitArraySet bitArraySet) {
            return null;
        }

        public boolean equals(BitArraySet other) {
            return false;
        }

        public BitArraySet clone() {
            return null;
        }
    }

    private Hashtable<Node, TempList> liveMap;
    private Hashtable<Node, BitArraySet> liveInMap = new Hashtable<Node, BitArraySet>();
    private Hashtable<Node, BitArraySet> liveOutMap = new Hashtable<Node, BitArraySet>();

    public BitArrayInterferenceGraphImpl(FlowGraph flowGraph) {
        liveMap = new Hashtable<Node, TempList>();
        boolean changed = false;
        int capacity = getCapacity(flowGraph);
        // initialise
        for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
            liveInMap.put(nodes.head, new BitArraySet(capacity));
            liveOutMap.put(nodes.head, new BitArraySet(capacity));
        }
        do {
            changed = false;
            for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
                var node = nodes.head;
                BitArraySet liveInPrev = liveInMap.get(node);
                BitArraySet liveOutPrev = liveOutMap.get(node);
                BitArraySet liveIn = new BitArraySet(flowGraph.use(node), capacity)
                        .union(liveOutPrev.difference(new BitArraySet(flowGraph.def(node), capacity)));
                BitArraySet liveOut = new BitArraySet(capacity);
                for (var succ = node.succ(); succ != null; succ = succ.tail) {
                    BitArraySet liveInSucc = null;
                    liveOut = liveOut.union(liveInSucc);
                }
                // save liveIn and liveOut in hash map for node
                liveInMap.put(node, liveIn);
                liveOutMap.put(node, liveOut);
                changed |= (!liveIn.equals(liveInPrev) || !liveOut.equals(liveOutPrev));
            }
            if (!changed) {
                break;
            }
        } while (true);
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