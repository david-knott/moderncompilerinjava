package Main;

import Assem.Instr;
import Assem.InstrList;
import FlowGraph.AssemFlowGraph;
import Frame.Frame;
import Graph.Node;
import Graph.NodeList;
import RegAlloc.BitArrayInterferenceGraphImpl;
import RegAlloc.DefaultSpillSelectStrategy;
import RegAlloc.InterferenceGraph;
import RegAlloc.PrecolouredNode;
import RegAlloc.RegisterAllocator;
import RegAlloc.RegisterSpiller;
import RegAlloc.SimpleGraphColouring2;
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

	public void processAll(RegisterAllocator registerAllocator) {
		for(CodeFrag loop = this; loop != null; loop = loop.next) {
			registerAllocator.addCodeFrag();
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