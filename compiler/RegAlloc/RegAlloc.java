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
			if (instrList.head.def() != null) {
				TempList spilledDefs = instrList.head.def().and(spills);
				for (; spilledDefs != null; spilledDefs = spilledDefs.tail) {
					if (newList == null) {
						newList = new InstrList(instrList.head, null);
					} else {
						newList.append(instrList.head);
					}
					System.out.println(
							"rewrite def instruction - move to frame " + instrList.head + " frame " + this.frame);
					InstrList instrList = frame.tempToMemory(spilledDefs.head);
					newList.append(instrList);
				}
			}
			if (instrList.head.use() != null) {
				TempList spilledUses = instrList.head.use().and(spills);
				for (; spilledUses != null; spilledUses = spilledUses.tail) {
					System.out.println(
							"rewrite use instruction - move to temp " + instrList.head + " frame " + this.frame);
					InstrList instrList = frame.memoryToTemp(spilledUses.head);
					if (newList == null) {
						newList = new InstrList(instrList.head, null);
					} else {
						newList.append(instrList);
					}
					newList.append(instrList.head);
				}
			}
		}
		this.instrList = newList;
	}

	private void allocate() {
		this.build();
		if (this.hasSpills()) {
			this.rewrite();
			this.allocate();
		}
		this.instrList.dump();
	}

	public RegAlloc(Frame frame, InstrList instrList, boolean dumpGraphs /* dump graphs */) {
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