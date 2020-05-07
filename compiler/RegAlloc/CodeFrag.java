package RegAlloc;

import Assem.InstrList;
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

	public void processAll(FlowGraphBuilder flowGraphBuilder, InterferenceGraph interferenceGraph) {
		//construct a liveness analysis using all the fragments
		//Interference graph uses the following items
		// LiveOut
		// Instruction List ( is it a move or not )
		// moveList
		// workListMoves

		//build the interference graph
		for(CodeFrag loop = this; loop != null; loop = loop.next) {
			//get flow graph for function fragment
			FlowGraph flowGraph = flowGraphBuilder.create(loop.instrList);
			//if there are any cycles, remember then for spilling heuristic
			Cycles cycles = new Cycles(flowGraph);
			//calculate live out for function
			LiveOut liveOut = new LiveOut(flowGraph);
			//add interference edges
			for(NodeList nodeList = flowGraph.nodes().reverse(); nodeList != null; nodeList = nodeList.tail) {
				Node node = nodeList.head;
				if(flowGraph.isMove(node)) {
					//need to indicate that the edge is move based.
//					MoveList moveList = new MoveList(s, d, null);

				} else {
					for(TempList defs = flowGraph.def(node); defs != null; defs = defs.tail) {
						for(TempList lo = liveOut.liveOut(node); lo != null; lo = lo.tail) {
							Node from = interferenceGraph.newNode();
							interferenceGraph.bind(from, lo.head);
							Node to = interferenceGraph.newNode();
							interferenceGraph.bind(to, defs.head);
							interferenceGraph.addEdge(from, to, false);
						}
					}
				}
			}
		}
	}

	/**
	 * Returns all the precoloured nodes that are present in the
	 * interference graph
	 * @param graph the graph
	 * @return a linked list of precoloured nodes
	 */
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

	/**
	 * Returns a linked list of nodes in the interference graph 
	 * that are not precoloured
	 * @param interferenceGraph the interference graph
	 * @return a linked list of nodes
	 */
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
			//node is in the precoloured list so skip it.
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