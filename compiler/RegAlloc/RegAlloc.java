package RegAlloc;

import java.util.Hashtable;

import Assem.InstrList;
import Codegen.Assert;
import Core.CompilerEventType;
import Core.Component;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Frame.Access;
import Frame.Frame;
import Temp.DefaultMap;
import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;

class RegAllocInfor {

	public static void dumpUsesAndDefs(InstrList instrList) {
		System.out.println("### Uses and Defs");
		for (; instrList != null; instrList = instrList.tail) {
			System.out.println(instrList.head.format(new DefaultMap()) + " : def => " + instrList.head.def()
					+ ", use => " + instrList.head.use());
		}
	}

	public static void dumpLiveness(InstrList il, int iterations, Liveness liveness) {
		System.out.println("### Liveness");
		for (InstrList instrList = il; instrList != null; instrList = instrList.tail) {
			System.out.println(instrList.head.format(new DefaultMap()) + " => " + liveness.liveMap(instrList.head));
		}

		System.out.println("Iteration:" + iterations + ")");
		System.out.print("Assembly");
		int maxChars = 0;
		for (InstrList instrList = il; instrList != null; instrList = instrList.tail) {
			String assem = instrList.head.format(new DefaultMap());
			maxChars = Math.max(maxChars, assem.length());
		}
		for (int i = 0; i < maxChars - 8; i++)
			System.out.print(" ");
		for (TempList tl = Temp.all(); tl != null; tl = tl.tail) {
			System.out.print(tl.head);
			System.out.print(" ");
		}
		System.out.print("rc");
		System.out.print(" ");
		System.out.print("ic");
		System.out.print(" ");
		System.out.print("def");
		System.out.println();
		/*
		 * System.out.print("ic"); for(int i = 0; i < maxChars - 2; i++)
		 * System.out.print(" "); for(TempList tl = Temp.all(); tl != null; tl =
		 * tl.tail) { Node n = this.baig.tnode(tl.head); int d = n != null ? n.degree()
		 * : 0; //System.out.print(Integer.toString(d)); System.out.print(" "); }
		 * System.out.println();
		 */
		for (InstrList instrList = il; instrList != null; instrList = instrList.tail) {
			String assem = instrList.head.format(new DefaultMap());
			System.out.print(assem);
			TempList live = liveness.liveMap(instrList.head);
			for (int i = 0; i < maxChars - assem.length(); i++)
				System.out.print(" ");
			int regCount = 0;
			for (TempList tl = Temp.all(); tl != null; tl = tl.tail) {
				for (int i = 0; i < tl.head.toString().length() - 1; i++)
					System.out.print(" ");
				System.out.print((TempList.contains(live, tl.head)) ? "|" : " ");
				if (TempList.contains(live, tl.head))
					regCount++;
				System.out.print(" ");
			}
			System.out.print(" ");
			System.out.print(Integer.toString(regCount));
			System.out.print(" ");
			System.out.print(instrList.head.def());
			System.out.println();
		}
	}

}

/**
 * RegAlloc class manages the spilling.
 */
public class RegAlloc extends Component implements TempMap {

	private static CompilerEventType FLOW_GRAPH_COMPLETE = new CompilerEventType("ONE");
	private static CompilerEventType LIVENESS_COMPLETE = new CompilerEventType("ONE");
	private static CompilerEventType INTERFERENCE_COMPLETE = new CompilerEventType("ONE");
	private static CompilerEventType COLOURING_COMPLETE = new CompilerEventType("ONE");
	private static CompilerEventType REWRITE_EVENT = new CompilerEventType("ONE");
	private static CompilerEventType SPILL_SELECT = new CompilerEventType("ONE");
	public InstrList instrList;
	public Frame frame;
	public PotentialSpillColour colour;
	public int iterations;
	private TempList spillTemps;
	private IGBackwardControlEdges baig;
	private Liveness liveness;
	private static int MAX_ITERATIONS = 7;

	private void build() {
		FlowGraph flowGraph = new AssemFlowGraph(instrList);
		this.trigger(FLOW_GRAPH_COMPLETE, flowGraph);
		this.liveness = new Liveness(flowGraph);
		this.trigger(LIVENESS_COMPLETE, this.liveness);
		this.baig = new IGBackwardControlEdges(flowGraph, this.liveness);
		this.trigger(INTERFERENCE_COMPLETE, this.baig);
		this.colour = new PotentialSpillColour(baig, this.frame, this.frame.registers());
		this.trigger(COLOURING_COMPLETE, this.colour);
	}

	private boolean hasSpills() {
		return this.colour.spills() != null;
	}

	private TempList selectSpill() {
		TempList originalTemps = TempList.andNot(this.colour.spills(), this.spillTemps);
		TempList lowestSpillCost = null;
		int sp = 0;
		for (TempList tl = originalTemps; tl != null; tl = tl.tail) {
			int vsp = this.baig.spillCost(this.baig.tnode(tl.head));
			if (lowestSpillCost == null || vsp < sp) {
				lowestSpillCost = tl;
				sp = vsp;
			}
		}
		this.trigger(SPILL_SELECT, lowestSpillCost);
		return new TempList(lowestSpillCost.head, null);
	}

	private void rewrite() {
		Hashtable<Temp, Access> accessHash = new Hashtable<Temp, Access>();
		TempList spills = this.selectSpill();
		System.out.println("Spilling " + spills);
		Access access = this.frame.allocLocal(true);
		accessHash.put(spills.head, access);
		InstrList newList = null;
		for (; instrList != null; instrList = instrList.tail) {
			TempList spilledDefs = TempList.and(instrList.head.def(), spills);
			TempList spilledUses = TempList.and(instrList.head.use(), spills);
			if (TempList.or(spilledDefs, spilledUses) == null) {
				newList = InstrList.append(newList, instrList.head);
				continue;
			}
			for (; spilledUses != null; spilledUses = spilledUses.tail) {
				access = accessHash.get(spilledUses.head);
				Temp spillTemp = Temp.create();
				this.spillTemps = TempList.append(this.spillTemps, spillTemp);
				InstrList memoryToTemp = frame.memoryToTemp(spilledUses.head, spillTemp, access);
				newList = InstrList.append(newList, memoryToTemp);
			}
			newList = InstrList.append(newList, instrList.head);
			for (; spilledDefs != null; spilledDefs = spilledDefs.tail) {
				access = accessHash.get(spilledDefs.head);
				Temp spillTemp = Temp.create();
				this.spillTemps = TempList.append(this.spillTemps, spillTemp);
				InstrList tempToMemory = frame.tempToMemory(spilledDefs.head, spillTemp, access);
				newList = InstrList.append(newList, tempToMemory);
			}
		}
		this.trigger(REWRITE_EVENT, newList);
		this.instrList = newList;
	}

	private void allocate() {
		this.build();
		this.instrList.dump();
		if (this.hasSpills() && this.iterations++ < MAX_ITERATIONS) {
			this.rewrite();
			this.allocate();
		}
		Assert.assertLE(this.iterations, MAX_ITERATIONS);
	}

	public RegAlloc(Frame frame, InstrList instrList) {
		this.iterations = 0;
		this.instrList = instrList;
		this.frame = frame;
		this.allocate();
	}

	@Override
	public String tempMap(Temp t) {
		return this.colour.tempMap(t);
	}
}