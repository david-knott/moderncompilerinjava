package RegAlloc;

import FlowGraph.AssemFlowGraph;
import Temp.Temp;
import Temp.TempMap;

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