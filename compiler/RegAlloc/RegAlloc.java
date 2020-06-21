package RegAlloc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import Assem.InstrList;
import Codegen.Assert;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Frame.Access;
import Frame.Frame;
import Graph.GraphvisRenderer;
import Graph.Node;
import Temp.CombineMap;
import Temp.DefaultMap;
import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;

interface RegAllocEventListener {
	public void handle(RegAlloc regAlloc);

}
/**
 * RegAlloc class manages the spilling.
 */
public class RegAlloc implements TempMap {
	public InstrList instrList;
	public Frame frame;
	public PotentialSpillColour colour;
//	public NoSpillColor color;
	public int iterations;
	private TempList spillTemps;
	private IGBackwardControlEdges baig;
	private Liveness liveness;
	private List<RegAllocEventListener> listeners;
	private static int MAX_ITERATIONS = 3;

	public void add(RegAllocEventListener listener) {
		Assert.assertNotNull(listener);
		if(this.listeners == null) {
			this.listeners = new ArrayList<RegAllocEventListener>();
		}
		this.listeners.add(listener);
	}

	public void buildComplete() {
		if(this.listeners == null) {
			return;
		}
		for(RegAllocEventListener listener : this.listeners) {
			listener.handle(this);
		}
	}

	private void build() {
		FlowGraph fg = new AssemFlowGraph(instrList);
		this.liveness = new Liveness(fg);
		this.baig = new IGBackwardControlEdges(fg, this.liveness);
		this.colour = new PotentialSpillColour(baig, this.frame, this.frame.registers());
		this.buildComplete();
		/*
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
		}*/
	}

	private boolean hasSpills() {
		return this.colour.spills() != null;
	}

	private TempList selectSpill() {
		TempList originalTemps = TempList.andNot(this.colour.spills(), this.spillTemps);
		TempList lowestSpillCost = null;
		int sp = 0;
		for(TempList tl = originalTemps; tl != null; tl = tl.tail) {
			int vsp = this.baig.spillCost(this.baig.tnode(tl.head));
			System.out.println("Potential Spills: " + tl.head + " cost:" + vsp);
			if(lowestSpillCost == null || vsp < sp) {
				lowestSpillCost = tl;
				sp = vsp;
			}
		}
		return new TempList(lowestSpillCost.head, null);
	}

	private void rewrite() {
		Hashtable<Temp, Access> accessHash = new Hashtable<Temp, Access>(); 
		TempList spills = this.selectSpill();
		Access access = this.frame.allocLocal(true);
		accessHash.put(spills.head, access);
		System.out.println("Spilling " + spills.head);
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
				System.out.println("spill:" + instrList.head.format(new DefaultMap()) + " insert before:" + spillTemp);
				newList = InstrList.append(newList, memoryToTemp);
			}
			newList = InstrList.append(newList, instrList.head);
			for (; spilledDefs != null; spilledDefs = spilledDefs.tail) {
				access = accessHash.get(spilledDefs.head);
				Temp spillTemp = Temp.create();
				this.spillTemps = TempList.append(this.spillTemps, spillTemp);
				InstrList tempToMemory = frame.tempToMemory(spilledDefs.head, spillTemp, access);
				System.out.println("spill:" + instrList.head.format(new DefaultMap()) + " insert after:" + spillTemp);
				newList = InstrList.append(newList, tempToMemory);
			}
		}
		this.instrList = newList;
	}

	private void allocate() {
		this.build();
		this.dumpUsesAndDefs();
		this.dumpLiveness();
		if (this.hasSpills() && this.iterations++ < MAX_ITERATIONS) {
			this.rewrite();
			this.allocate();
		}
		Assert.assertLE(this.iterations, MAX_ITERATIONS);
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
		}
	}

	public void dumpLiveness() {
		System.out.println("### Liveness");
		for(InstrList instrList = this.instrList; instrList != null; instrList = instrList.tail) {
			System.out.println(instrList.head.format(new DefaultMap()) + " => " + this.liveness.liveMap(instrList.head));
		}

		System.out.println("Iteration:" + this.iterations + ")");
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
		System.out.print("ic");
		System.out.print(" ");
		System.out.print("def");
		System.out.println();
		/*
		System.out.print("ic");
		for(int i = 0; i < maxChars - 2; i++) System.out.print(" ");
		for(TempList tl = Temp.all(); tl != null; tl = tl.tail) {
			Node n  = this.baig.tnode(tl.head);
				int d = n != null ? n.degree() : 0;
				//System.out.print(Integer.toString(d));
				System.out.print(" ");
		}
		System.out.println();
		*/
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