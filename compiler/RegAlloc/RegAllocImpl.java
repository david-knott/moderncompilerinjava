package RegAlloc;

import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Graph.Node;
import Temp.Temp;
import Util.LinkedListStack;
import Util.DoubleLinkedList;
/**
 * Represents a basic implementation of the register allocation
 * algorithm that does not support spilling or coalescing
 */
class RegAllocImpl {

    //precoloured - all machine registers pre assigned a colour
    private DoubleLinkedList<Temp> precoloured;
    //initial = temporary registers, not precoloured and not yet processed
    private DoubleLinkedList<Temp> initial;
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
    private LinkedListStack<Temp> selectStack;
    //8 registers
    private int K = 8;
    //contains the degrees of all nodes within our interference graph.
    private int[] degrees;
    //all the instructions
    public Assem.InstrList instrs;
    public Frame.Frame frame;
    private FlowGraph flowGraph;
    private InterferenceGraph interferenceGraph;

    public RegAllocImpl() {
        super();
        this.precoloured = new DoubleLinkedList<Temp>();
        this.initial = new DoubleLinkedList<Temp>();
        this.simplifyWorklist = new DoubleLinkedList<Node>();
        this.freezeWorklist = new DoubleLinkedList<Node>();
        this.spillWorklist = new DoubleLinkedList<Node>();
        this.spilledNodes =  new DoubleLinkedList<Node>();
        this.coalescedNodes =  new DoubleLinkedList<Node>();
        this.colouredNodes =  new DoubleLinkedList<Node>();
        this.selectStack = new LinkedListStack<Temp>();
        this.degrees = new int[]{};
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

    private int degree(Temp temp) {
        return 0;
    }

    /**
     * Creates a worklist using the interference graph
     */
    public void makeWorklist() {
        /*
        for(var i = initial.first(); i != initial.TRAILER; i = i.next) {
            Temp n = i.t;
            var prev = i.prev;
            var next = i.next;
            prev.next= i.next;
            next.prev = i.prev;
            if(this.degree(n) >= K) {
                throw new Error("spill - not implemeneted");
            } else if(this.moveRelated(n)) {
                throw new Error("move related - not implemeneted");
            } else {
                this.simplifyWorklist.addToEnd(this.interferenceGraph.tnode(n));
            }
        }*/
    }

    private boolean moveRelated(Temp n) {
        return false;
    }

    public boolean canSimplify() {
        return true;
    }

    /**
     * Simply to graph by removing nodes that are less that degree
     * and then looking for further oppurtunities to simplify.
     */
    public void simpify() {
        /*
        for(var i = this.simplifyWorklist.first(); i != this.simplifyWorklist.TRAILER; i = i.next) {
            var prev = i.prev;
            var next = i.next;
            prev.next= i.next;
            next.prev = i.prev;
            this.selectStack.push(this.interferenceGraph.gtemp(i.t));
            for(var m = this.adjacent(i.t).first(); m != this.adjacent(i.t).TRAILER; m = m.next) {
                this.decrementDegree(m.t);
            }
        }*/
    }

    private void decrementDegree(Node t) {
        int degree = 0;
        degree--;
        if(degree == this.K) {
            //enable moves[n] union adjacent[m]
            //spillWorklist = spillWorkList sub m
            //if moveRelated(n) then
            //freezeWorkList = freezeWorkList union m
            //else
            //simpligyWorklist = simplifyWoklist union m
        }
    }

    private DoubleLinkedList<Node> adjacent(Node t) {
        return null;
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

    public Node getAlias(Node n) {
        //if n is element in coalesce list, then  return getAlias(alias[n]) else
        return n;
    }

    public void assignColours() {
        /*
        while(!this.selectStack.isEmpty()) {
            var n = this.selectStack.pop();
            for(var adj = this.interferenceGraph.tnode(n).adj(); adj != null; adj = adj.tail) {
            }
        }
        */
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