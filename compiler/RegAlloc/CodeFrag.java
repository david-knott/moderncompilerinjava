package RegAlloc;

import Assem.InstrList;
import FlowGraph.AssemFlowGraph;
import FlowGraph.FlowGraph;
import Frame.Frame;
import Graph.Node;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempList;

class CodeFragWithTemps {
	private CodeFrag codeFrag;
	private InstrList instrList;
}

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
			// get flow graph for function fragment
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
		//full interference graph has been built.
		//pass in the base frame to get precoloured temps
		//and colours
		registerAllocator.allocate(this.frame, interferenceGraph);
		if(registerAllocator.hasSpills()) {
			//rewrite and call again
			//CodeFrag newCodeFrags = instructionRewriter.rewrite(this);
			//newCodeFrags.processAll(flowGraphBuilder, interferenceGraph, registerAllocator);
		}
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