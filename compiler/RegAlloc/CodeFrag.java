package RegAlloc;

import Assem.Instr;
import Assem.InstrList;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Frame.Frame;
import Graph.Node;
import Graph.NodeList;
import Temp.CombineMap;
import Temp.DefaultMap;
import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;

/**
 * Represents a fragment of assembly code.
 */
public class CodeFrag {

	CodeFrag next;
	private InstrList instrList;
	private Frame frame;

	public CodeFrag(InstrList instrList, Frame frame) {
		this.instrList = instrList;
		this.frame = frame;
	}

	public void processAll() {
		for (CodeFrag loop = this; loop != null; loop = loop.next) {
			TempMap tempMap = new RegAlloc(loop.frame, loop.instrList);
			for(InstrList il = loop.instrList; il != null; il = il.tail) {
				System.out.print(il.head.format(new CombineMap(tempMap, loop.frame)));
				//System.out.print(il.head.format(new DefaultMap()));
			}
		}
	}

	public void append(CodeFrag process) {
		CodeFrag processedFrag = this;
		for (; processedFrag.next != null; processedFrag = processedFrag.next)
			;
		processedFrag.next = process;
	}
}