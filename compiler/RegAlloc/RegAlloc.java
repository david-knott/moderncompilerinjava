package RegAlloc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Hashtable;

import Assem.InstrList;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Frame.Access;
import Frame.AccessList;
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
	public int iterations;
	private TempList spillTemps;
	private InterferenceGraph baig;

	private void build() {
		FlowGraph fg = new AssemFlowGraph(instrList);
		baig = new IGBackwardControlEdges(fg);
		this.colour = new PotentialSpillColour(baig, this.frame, this.frame.registers());
		/*
		try {
			PrintStream ps = new PrintStream(new FileOutputStream("./colour-graph" + this.iterations + ".txt"));
			new GraphvisRenderer().render(ps, baig, this);
			ps.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			PrintStream ps = new PrintStream(new FileOutputStream("./flow-graph" + this.iterations + ".txt"));
			new GraphvisRenderer().render(ps, fg, this);
			ps.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	private boolean hasSpills() {
		return this.colour.spills() != null;
	}

	private TempList selectSpill() {
		System.out.println("Spill Select");
		TempList originalTemps = TempList.andNot(new TempList(this.colour.spills().head), this.spillTemps);
		for(TempList tl = originalTemps; tl != null; tl = tl.tail) {
			System.out.println(tl.head + " cost: " + this.baig.spillCost(this.baig.tnode(tl.head)));
		}
		return originalTemps;
	}

	private void rewrite() {
		TempList spills = this.selectSpill();
		InstrList newList = null;
		Hashtable<Temp, Access> accessHash = new Hashtable<Temp, Access>(); 
		for (; instrList != null; instrList = instrList.tail) {
			TempList spilledDefs = TempList.and(instrList.head.def(), spills);
			TempList spilledUses = TempList.and(instrList.head.use(), spills);
			if (TempList.or(spilledDefs, spilledUses) == null) {
				newList = InstrList.append(newList, instrList.head);
				continue;
			}
			for (; spilledUses != null; spilledUses = spilledUses.tail) {
				Access access = accessHash.get(spilledUses.head);
				Temp spillTemp = Temp.create();
				this.spillTemps = TempList.append(this.spillTemps, spillTemp);
				InstrList memoryToTemp = frame.memoryToTemp(spilledUses.head, spillTemp, access);
				newList = InstrList.append(newList, memoryToTemp);
			}
			newList = InstrList.append(newList, instrList.head);
			for (; spilledDefs != null; spilledDefs = spilledDefs.tail) {
				Temp spillTemp = Temp.create();
				Access access = this.frame.allocLocal(false);
				accessHash.put(spilledDefs.head, access);
				this.spillTemps = TempList.append(this.spillTemps, spillTemp);
				InstrList tempToMemory = frame.tempToMemory(spilledDefs.head, spillTemp, access);
				newList = InstrList.append(newList, tempToMemory);
			}
		}
		this.instrList = newList;
	}

	private void allocate() {
		this.iterations++;
		this.build();
		if (this.hasSpills()) {
			this.rewrite();
			this.allocate();
		}
	}

	public RegAlloc(Frame frame, InstrList instrList, boolean dumpGraphs /* dump graphs */) {
		this.iterations = 0;
		this.instrList = instrList;
		this.frame = frame;
		this.allocate();
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