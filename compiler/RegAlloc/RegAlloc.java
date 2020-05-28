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
import Temp.TempList;
import Temp.TempMap;

/**
 * RegAlloc class manages the spilling.
 */
public class RegAlloc implements TempMap {
	public InstrList instrList;
	public Frame frame;
	public PotentialSpillColour colour;

	public RegAlloc(Frame frame, InstrList instrList, boolean dumpGraphs /* dump graphs */) {
		this.instrList = instrList;
		this.frame = frame;
		var fg = new AssemFlowGraph(instrList);
		var baig = new IGBackwardControlEdges(fg);
		//this.colour = new Colour(baig, this.frame, this.frame.registers(), false /* dump graph */);
		this.colour = new PotentialSpillColour(baig, this.frame, this.frame.registers());
		
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
		TempList spills = this.colour.spills();
		if(spills != null) {
			//rewrite the instructions
			for(; instrList != null; instrList = instrList.tail) {
				if(instrList.head.def() != null && instrList.head.def().contains(spills)) {

					System.out.println("rewrite instruction " + instrList.head + " frame " + this.frame);
				}
				if(instrList.head.use() != null && instrList.head.use().contains(spills)) {

					System.out.println("rewrite instruction " + instrList.head);
				}



				

			}
		//	RegAlloc alloc = new RegAlloc(frame, instrList, dumpGraphs);
			
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