package RegAlloc;

import Assem.Instr;
import Assem.InstrList;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Frame.Frame;
import Graph.Node;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempList;

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

	public void processAll(InterferenceGraph interferenceGraph, RegisterAllocator registerAllocator) {
		// build the interference graph
		for (CodeFrag loop = this; loop != null; loop = loop.next) {
			//create a new flow graph for function fragment
			FlowGraph flowGraph = new AssemFlowGraph(loop.instrList);
			// calculate live out for function
			LiveOut liveOut = new LiveOut(flowGraph);
			// add interference edges
			for (NodeList nodeList = flowGraph.nodes().reverse(); nodeList != null; nodeList = nodeList.tail) {
				Node node = nodeList.head;
				if (flowGraph.isMove(node)) {
					// need to indicate that the edge is move based.
					// MoveList moveList = new MoveList(s, d, null);
					TempList defs = flowGraph.def(node);
					for (TempList uses = flowGraph.use(node); uses != null; uses = uses.tail) {
						// we can assume moves only have 1 src and 1 dest
						for (TempList lo = liveOut.liveOut(node); lo != null; lo = lo.tail) {
							if (uses.head != lo.head && defs.head != lo.head) {
								Node from = this.getOrCreate(interferenceGraph, defs.head);
								Node to = this.getOrCreate(interferenceGraph, lo.head);
								interferenceGraph.addEdge(from, to, true);
							}
						}
					}
				} else {
					for (TempList defs = flowGraph.def(node); defs != null; defs = defs.tail) {
						for (TempList lo = liveOut.liveOut(node); lo != null; lo = lo.tail) {
							Node from = this.getOrCreate(interferenceGraph, lo.head);
							Node to = this.getOrCreate(interferenceGraph, defs.head);
							interferenceGraph.addEdge(from, to, false);
						}
					}
				}
			}
		}
		TempList spills = registerAllocator.allocate(this.frame, interferenceGraph);
		if(spills != null) {
			SpillSelectStrategy spillSelectStrategy = new DefaultSpillSelectStrategy();
			Temp spilledTemp = spillSelectStrategy.spill(spills);
			for (CodeFrag loop = this; loop != null; loop = loop.next) {
				InstrList previous = null;
				for(InstrList instrList = loop.instrList; instrList != null; instrList = instrList.tail) {
					if(instrList.head.def().contains(spilledTemp)) {
						InstrList defSpill = loop.frame.tempToMemory(spilledTemp);
						//set current item to defSpill
						InstrList oldTail = instrList.tail;
						instrList.tail = defSpill;
						defSpill.tail = oldTail;
						//move loop to next item
						instrList = instrList.tail;
					}
					if(instrList.head.use().contains(spilledTemp)) {
						InstrList useSpill = loop.frame.memoryToTemp(spilledTemp);
						//set previous item
						InstrList oldTail = previous.tail;
						previous.tail = useSpill;
						useSpill.tail = oldTail;

					}
					previous = instrList;

				}

			}

		}
		System.out.println("done");
	}

	private Node getOrCreate(InterferenceGraph interferenceGraph, Temp temp) {
		if(interferenceGraph.tnode(temp) != null) {
			return interferenceGraph.tnode(temp);
		} else {
			Node node = interferenceGraph.newNode();
			interferenceGraph.bind(node, temp);
			return node;
		}
    }

	public void append(CodeFrag process) {
		CodeFrag processedFrag = this;
		for (; processedFrag.next != null; processedFrag = processedFrag.next)
			;
		processedFrag.next = process;
	}
}