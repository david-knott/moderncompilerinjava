package RegAlloc;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import Assem.InstrList;
import FlowGraph.AssemFlowGraph;
import Frame.Frame;
import Graph.GraphvisRenderer;
import Temp.Temp;
import Temp.TempMap;

public class RegAlloc implements TempMap {
	public InstrList instrList;
	public Frame frame;
	public Colour colour;

	public RegAlloc(Frame frame, InstrList instrList, boolean dumpGraphs /* dump graphs */) {
		this.instrList = instrList;
		this.frame = frame;
		var fg = new AssemFlowGraph(instrList);
		
		var baig = new InterferenceGraphImpl(fg);
		this.colour = new Colour(baig, this.frame, this.frame.registers(), false /* dump graph */);
		try {
			PrintStream ps = new PrintStream(new FileOutputStream("./colour-graph.txt"));
			new GraphvisRenderer().render(ps, baig, this);
			ps.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			PrintStream ps = new PrintStream(new FileOutputStream("./flow-graph.txt"));
			new GraphvisRenderer().render(ps, fg, this);
			ps.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String tempMap(Temp t) {
		String maps = this.colour.tempMap(t);
		if (maps == null) {
			return this.frame.tempMap(t);
		}
		return maps;
	}
}