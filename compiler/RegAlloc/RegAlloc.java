package RegAlloc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Hashtable;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import Assem.InstrList;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Frame.Access;
import Frame.Frame;
import Graph.GraphvisRenderer;
import Temp.CombineMap;
import Temp.DefaultMap;
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
	public NoSpillColor color;
	public int iterations;
	private TempList spillTemps;
	private IGBackwardControlEdges baig;
	private Liveness liveness;

	private void build() {
		FlowGraph fg = new AssemFlowGraph(instrList);
		this.liveness = new Liveness(fg);
		this.baig = new IGBackwardControlEdges(fg, this.liveness);
		this.baig.show(System.out);
		this.colour = new PotentialSpillColour(baig, this.frame, this.frame.registers());
		try {
			PrintStream ps = new PrintStream(new FileOutputStream("./colour-graph" + this.iterations + ".txt"));
			new GraphvisRenderer().render(ps, baig, new CombineMap(this, this.frame));
			ps.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			PrintStream ps = new PrintStream(new FileOutputStream("./flow-graph" + this.iterations + ".txt"));
			new GraphvisRenderer().render(ps, fg, new CombineMap(this, this.frame));
			ps.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean hasSpills() {
		return this.colour.spills() != null;
	}

	private TempList selectSpill() {
	//	System.out.println("Spill Select");
		TempList originalTemps = TempList.andNot(new TempList(this.colour.spills().head), this.spillTemps);
		for(TempList tl = originalTemps; tl != null; tl = tl.tail) {
			System.out.println(tl.head + " cost: " + this.baig.spillCost(this.baig.tnode(tl.head)));
		}
		return originalTemps;
	}

	private void rewrite() {
		Hashtable<Temp, Access> accessHash = new Hashtable<Temp, Access>(); 
		TempList spills = this.selectSpill();
		for(TempList spill = spills; spill != null; spill = spill.tail) {
			Access access = this.frame.allocLocal(true);
			accessHash.put(spill.head, access);
			break;
		}
		InstrList newList = null;
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
				Access access = accessHash.get(spilledDefs.head);
				Temp spillTemp = Temp.create();
				this.spillTemps = TempList.append(this.spillTemps, spillTemp);
				InstrList tempToMemory = frame.tempToMemory(spilledDefs.head, spillTemp, access);
				newList = InstrList.append(newList, tempToMemory);
			}
		}
		this.instrList = newList;
	}

	private void allocate() {
		if(this.iterations > 6) 
		throw new Error("No many iterations");
		this.iterations++;
		this.build();
		this.dumpUsesAndDefs();
		this.dumpLiveness();
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

	public void dumpUsesAndDefs() {
		System.out.println("### Uses and Defs");
		for(InstrList instrList = this.instrList; instrList != null; instrList = instrList.tail) {
			System.out.println(instrList.head.format(new DefaultMap())+ " : def => " + instrList.head.def() + ", use => " + instrList.head.use());
			//System.out.println(instrList.head.format(new DefaultMap()));
		}
	}

	public void dumpLiveness() {
		System.out.println("### Liveness");
		for(InstrList instrList = this.instrList; instrList != null; instrList = instrList.tail) {
			System.out.println(instrList.head.format(new DefaultMap()) + " => " + this.liveness.liveMap(instrList.head));
		}

		System.out.print("Assembly");
		int maxChars = 0;
		for(InstrList instrList = this.instrList; instrList != null; instrList = instrList.tail) {
			String assem = instrList.head.format(new DefaultMap());
			maxChars = Math.max(maxChars, assem.length());
		}
		for(int i = 0; i < maxChars - 8; i++) System.out.print(" ");
		for(TempList tl =Temp.all(); tl != null; tl = tl.tail) {
				System.out.print(tl.head);
				System.out.print(" ");
		}
		System.out.print("rc");
		System.out.print(" ");
		System.out.print("def");
		System.out.println();
		for(InstrList instrList = this.instrList; instrList != null; instrList = instrList.tail) {
			String assem = instrList.head.format(new DefaultMap());
			System.out.print(assem);
			TempList live = this.liveness.liveMap(instrList.head);
			for(int i = 0; i < maxChars - assem.length(); i++) System.out.print(" ");
			int regCount = 0;
			for(TempList tl =Temp.all(); tl != null; tl = tl.tail) {
				for(int i = 0; i < tl.head.toString().length() - 1; i++) System.out.print(" ");
				System.out.print((TempList.contains(live, tl.head)) ? "|" : " ");
				if(TempList.contains(live, tl.head))
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

	@Override
	public String tempMap(Temp t) {
		String maps = this.colour.tempMap(t);
		//if (maps == null) {
//			maps = this.frame.tempMap(t);
		//}
	//	if(maps == null) {
			//throw new Error("No map for " + t);
	//	}
		return maps;
	}
}