package RegAlloc;

import Assem.Instr;
import Assem.InstrList;
import Assem.MOVE;
import FlowGraph.FlowGraph;
import Frame.Frame;
import Graph.Node;
import Graph.NodeList;
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

	public void processAll(RegisterAllocator registerAllocator, FlowGraphBuilder flowGraphBuilder, InterferenceGraphBuilder interferenceGraphBuilder) {
		//construct a liveness analysis using all the fragments
		//Interference graph uses the following items
		// LiveOut
		// Instruction List ( is it a move or not )
		// moveList
		// workListMoves

		InterferenceGraph interferenceGraph = interferenceGraphBuilder.create();
		for(CodeFrag loop = this; loop != null; loop = loop.next) {
			//get flow graph for function fragment
			FlowGraph flowGraph = flowGraphBuilder.create(loop.instrList);
			//if there are any cycles, remember then for spilling heuristic
			Cycles cycles = new Cycles(flowGraph);
			//calculate live out for function
			LiveOut liveOut = new LiveOut(flowGraph);
			//add interference edges
			for(InstrList instrList = loop.instrList.reverse(); instrList != null; instrList = instrList.tail) {
				Instr instr = instrList.head;
				//need to know what node maps to the current instruction.
				//flowGraph.isMove(node);
				if(instr instanceof MOVE) {
					//handle move instructions.
					TempList useI = instr.use();
					TempList defI = instr.def();
					//for each item n in union of useI and defI add to moveList
					//add instr to workListMove
				} else {
					for(TempList defs = instr.def(); defs != null; defs = defs.tail) {

						//liveOut.liveOut(flowGraph.)
						Node from = interferenceGraph.newNode();
						//bind from to live temp 
						Node to = interferenceGraph.newNode();
						//Bind to to def temp 
						interferenceGraph.addEdge(from, to);
					}
				}
			}

		}

		registerAllocator.allocate();

	}

	private PrecolouredNode getPrecoloured(InterferenceGraph graph) {
		PrecolouredNode precolouredNode = null;
		for (TempList tempList = this.frame.precoloured(); tempList != null; tempList = tempList.tail) {
			Node node = graph.tnode(tempList.head);
			// precoloured node has not been seen in the interference graph
			if (node == null) {
				continue;
			}
			if (precolouredNode == null) {
				precolouredNode = new PrecolouredNode(node, tempList.head);
			} else {
				precolouredNode = precolouredNode.append(node, tempList.head);
			}
		}
		return precolouredNode;
	}

	private NodeList getInitial(InterferenceGraph interferenceGraph) {
		NodeList initialList = null;
		PrecolouredNode precoloured = this.getPrecoloured(interferenceGraph);
		for (NodeList nodeList = interferenceGraph.nodes(); nodeList != null; nodeList = nodeList.tail) {
			Node node = nodeList.head;
			// is node in precoloured list
			boolean skip = false;
			for (PrecolouredNode pc = precoloured; pc != null; pc = pc.tail) {
				if (node.equals(pc.node)) {
					skip = true;
					break;
				}
			}
			if (skip) {
				continue;
			}
			if (initialList == null) {
				initialList = new NodeList(node, null);
			} else {
				initialList = new NodeList(node, initialList);
			}
		}
		return initialList;
	}

	public void append(CodeFrag process) {
		CodeFrag processedFrag = this;
		for (; processedFrag.next != null; processedFrag = processedFrag.next)
			;
		processedFrag.next = process;
	}
}