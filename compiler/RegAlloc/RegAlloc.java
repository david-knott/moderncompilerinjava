package RegAlloc;

import Assem.InstrList;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Frame.Frame;
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

	private void build() {
		FlowGraph fg = new AssemFlowGraph(instrList);
		InterferenceGraph baig = new IGBackwardControlEdges(fg);
		this.colour = new PotentialSpillColour(baig, this.frame, this.frame.registers());
	}

	private boolean hasSpills() {
		return this.colour.spills() != null;
	}

	private void rewrite() {
		TempList spills = this.colour.spills();
		InstrList newList = null;
		for (; instrList != null; instrList = instrList.tail) {
			TempList spilledDefs = TempList.and(instrList.head.def(), spills);
			TempList spilledUses = TempList.and(instrList.head.use(), spills);
			if (TempList.or(spilledDefs, spilledUses) == null) {
				newList = InstrList.append(newList, instrList.head);
				continue;
			}
			for (; spilledDefs != null; spilledDefs = spilledDefs.tail) {
				InstrList tempToMemory = frame.tempToMemory(spilledDefs.head);
				newList = InstrList.append(newList, tempToMemory);
			}
			newList = InstrList.append(newList, instrList.head);
			for (; spilledUses != null; spilledUses = spilledUses.tail) {
				InstrList memoryToTemp = frame.memoryToTemp(spilledUses.head);
				newList = InstrList.append(newList, memoryToTemp);
			}
			
		}
		this.instrList = newList;
	}

	private void allocate() {
		this.iterations++;
		System.out.println(this.iterations);
		this.instrList.dump();
		this.build();
		if (this.hasSpills()) {
			this.rewrite();
			// this.allocate();
		}
		this.instrList.dump();
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