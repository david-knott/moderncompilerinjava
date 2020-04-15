package RegAlloc;

import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Graph.Node;
import Temp.Temp;
import Temp.TempMap;

/**
 * Double linked list implementation of a sequence
 */
class DoubleLinkedList<T> {

    private Item HEADER = new Item();
    private Item TRAILER = new Item();

    class Item {
        T t;
        Item next;
        Item prev;

        private Item() {
        }

        public Item(T t, Item prev, Item next) {
            this.prev = prev;
            this.next = next;
            this.t = t;
        }
    }

    public DoubleLinkedList() {
        HEADER.next = TRAILER;
        TRAILER.prev = HEADER;
    }

    void addToStart(T t) {
        Item i = new Item(t, HEADER, HEADER.next);
        HEADER.next = i;
    }

    void addToEnd(T t) {
        Item i = new Item(t, TRAILER, TRAILER.prev);
        TRAILER.prev = i;
    }

    T removeFromStart() {
        return null;
    }

    T removeFromEnd() {
        return null;
    }
}

/**
 * Single linked list implementation of a stack
 * @param <T>
 */
class SimpleStack<T> {
    
    private Item head;

    class Item {
        T t;
        Item tail;

        public Item(T t, Item tail) {
            this.tail = tail;
            this.t = t;
        }
    }

    T pop() {
        Item last = this.head;
        this.head = this.head.tail;
        return last.t;
    }

    void push(T t) {
        if(head == null) {
            head = new Item(t, null);
        } else {
            head = new Item(t, head);
        }
    }

}
/**
 * Represents a basic implementation of the register allocation
 * algorithm that does not support spilling or coalescing
 */
class RegAllocImpl {

    //precoloured - all machine registers pre assigned a colour
    //initial = temporary registers, not precoloured and not yet processed
    // list of low degree non move related nodes
    private DoubleLinkedList<Node> simplifyWorklist;
    //freezeWorklist = low degree move related nodes
    private DoubleLinkedList<Node> freezeWorklist;
    //spillWorklist - high degree modes
    private DoubleLinkedList<Node> spillWorklist;
    //spilledNodes - nodes marked for spilling during this round, initially empty
    private DoubleLinkedList<Node> spilledNodes;
    //coalescedNodes - registers that have been coalesed, when u <- v is coalesced
    private DoubleLinkedList<Node> coalescedNodes;
    //v is added to this set and u put back on some work list ( or vice versa )
    //colouredNodes - nodes succesfully coloured
    private DoubleLinkedList<Node> colouredNodes;
    //selectStack - stack containing temporaries removed from graph.
    private SimpleStack<Temp> selectStack;

    //contains the degrees of all nodes within our interference graph.
    private int[] degrees;

    public Assem.InstrList instrs;
    public Frame.Frame frame;
    private FlowGraph flowGraph;
    private InterferenceGraph interferenceGraph;

    public RegAllocImpl() {
        super();
        this.simplifyWorklist = new DoubleLinkedList<Node>();
        this.freezeWorklist = new DoubleLinkedList<Node>();
        this.spillWorklist = new DoubleLinkedList<Node>();
        this.spilledNodes =  new DoubleLinkedList<Node>();
        this.coalescedNodes =  new DoubleLinkedList<Node>();
        this.colouredNodes =  new DoubleLinkedList<Node>();
        this.selectStack = new SimpleStack<Temp>();
    }

    public void main() {
        this.liveness();
        this.build();
        this.makeWorklist();
        do {
            if (this.canSimplify())
                this.simpify();
            else if (this.canCoalesce())
                this.coalesce();
            else if (this.canFreeze())
                this.freeze();
            else if (this.canSpill())
                this.selectSpills();

        } while (!done());
        this.assignColours();
        if (this.hasSpilledNodes()) {
            this.rewriteProgram();
            this.main();
        }
    }

    public boolean hasSpilledNodes() {
        return true;
    }

    public boolean done() {
        return true;
    }

    /**
     * Builds the control flow graph
     */
    public void liveness() {
        this.flowGraph = new AssemFlowGraph(this.instrs);
        this.flowGraph.show(System.out);
    }

    /**
     * Builds the interference graph using liveness
     * analysis. We use 2 data structures to store information
     * about the interference graph, a node adjacency list tells
     * us what nodes are adjacent to a particular node. We also use
     * a bit matrix to tell if node x and node y are adjacent.
     * A degree array is populated with the degree of all nodes within
     * the graph.
     */
    public void build() {
        this.interferenceGraph = new BitArrayInterferenceGraphImpl(this.flowGraph);
        this.interferenceGraph.show(System.out);
        for(var n = this.interferenceGraph.nodes(); n != null; n = n.tail){

        }
    }

    /**
     * Creates a worklist using the interference graph
     */
    public void makeWorklist() {

    }

    public boolean canSimplify() {
        return true;
    }

    /**
     * Simply to graph by removing nodes that are less that degree
     * and then looking for further oppurtunities to simplify.
     */
    public void simpify() {

    }

    public boolean canCoalesce() {
        return false;
    }

    public void coalesce() {

    }

    public boolean canFreeze() {
        return false;
    }

    public void freeze() {

    }

    public boolean canSpill() {
        return false;
    }

    public void selectSpills() {

    }

    public void assignColours() {

    }

    public void rewriteProgram() {

    }

    /**
     * Reverses instruction list
     */
    private Assem.InstrList reverse(Assem.InstrList instrs) {
        if (instrs == null)
            return null;
        Assem.InstrList rev = new Assem.InstrList(instrs.head, null);
        instrs = instrs.tail;
        while (instrs != null) {
            rev = new Assem.InstrList(instrs.head, rev);
            instrs = instrs.tail;
        }
        return rev;
    }

}

public class RegAlloc implements TempMap {

    public Assem.InstrList instrs;
    public Frame.Frame frame;

    public RegAlloc(Frame.Frame f, Assem.InstrList il) {
        if (f == null)
            throw new Error("f cannot be null");
        if (il == null)
            throw new Error("il cannot be null");
        this.frame = f;
        this.instrs = il;
    }

    private InterferenceGraph buildInterferenceGraph() {
        var flowGraph = new AssemFlowGraph(this.instrs);
        flowGraph.show(System.out);
        var interferenceGraph = new BitArrayInterferenceGraphImpl(flowGraph);
        interferenceGraph.show(System.out);
        return interferenceGraph;
    }

    @Override
    public String tempMap(Temp t) {
        return new Colour(buildInterferenceGraph(), this.frame, this.frame.registers()).tempMap(t);
    }

}