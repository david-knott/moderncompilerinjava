package RegAlloc;

import Assem.InstrList;
import FlowGraph.AssemFlowGraph;
import Frame.Frame;
import Temp.Temp;
import Temp.TempMap;

public class RegAlloc implements TempMap {
	public InstrList instrList;
	public Frame frame;
	public Colour colour;

	public RegAlloc(Frame frame, InstrList instrList) {
		this.instrList = instrList;
		this.frame = frame;
		var fg = new AssemFlowGraph(instrList);
		var baig = new BitArrayInterferenceGraphImpl(fg);
		this.colour = new Colour(baig, this.frame, this.frame.registers());
	}

	@Override
	public String tempMap(Temp t) {
		String maps = this.colour.tempMap(t);
		if(maps == null) {
			return this.frame.tempMap(t);
		}
		return maps;
	}
}