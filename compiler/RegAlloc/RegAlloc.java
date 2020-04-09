package RegAlloc;

import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Temp.Temp;
import Temp.TempMap;

/**
 * Represents a basic implementation of the register allocation
 * algorithm that does not support spilling or coalescing
 */
class RegAllocImpl {

    //precoloured - all machine registers pre assigned a colour
    //initial = temporary registers, not precoloured and not yet processed
    //simplifyWorklist = list of low degree non move related nodes
    //freezeWorklist = low degree move related nodes
    //spillWorklist - high degree modes
    //spilledNodes - nodes marked for spilling during this round, initially empty
    //coalescedNodes - registers that have been coalesed, when u <- v is coalesced
    //v is added to this set and u put back on some work list ( or vice versa )
    //colouredNodes - nodes succesfully coloured
    //selectStack - stack containing temporaries removed from graph.
    public Assem.InstrList instrs;
    public Frame.Frame frame;
    private FlowGraph flowGraph;
    private InterferenceGraph interferenceGraph;


    public RegAllocImpl() {
        super();
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
     * Builds flow graph
     */
    public void liveness() {
        this.flowGraph = new AssemFlowGraph(this.instrs);
        this.flowGraph.show(System.out);
    }

    /**
     * Builds the interference graph using liveness
     * analysis
     */
    public void build() {
        this.interferenceGraph = new BitArrayInterferenceGraphImpl(this.flowGraph);
        this.interferenceGraph.show(System.out);
    }

    public void makeWorklist() {

    }

    public boolean canSimplify() {
        return true;
    }

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