package Main;

import Assem.InstrList;
import FlowGraph.AssemFlowGraph;
import Frame.Frame;
import Graph.Node;
import Graph.NodeList;
import RegAlloc.BitArrayInterferenceGraphImpl;
import RegAlloc.InterferenceGraph;
import RegAlloc.RegisterAllocator;
import RegAlloc.SimpleGraphColouring;
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

	public void processAll(RegisterAllocator registerAllocator) {
		for (CodeFrag me = this; me != null; me = me.next) {
			me.process(registerAllocator);
		}
		registerAllocator.allocate();
	}

	private NodeList getPrecoloured(InterferenceGraph graph) {
		NodeList nodeList = null;
		for(TempList tempList = this.frame.precoloured(); tempList != null; tempList = tempList.tail) {
			Node node = graph.tnode(tempList.head);
			if(node == null) {
				continue;
			}
			if(nodeList == null) {
				nodeList = new NodeList(node, null);
			} else {
				nodeList = new NodeList(node, nodeList);
			}
		}
		return nodeList;
	}

	private NodeList getColours(InterferenceGraph graph) {
		NodeList nodeList = null;
		for(TempList tempList = this.frame.registers(); tempList != null; tempList = tempList.tail) {
			Node node = graph.tnode(tempList.head);
			if(node == null) {
				continue;
			}
			if(nodeList == null) {
				nodeList = new NodeList(node, null);
			} else {
				nodeList = new NodeList(node, nodeList);
			}
		}
		return nodeList;
	}

	public void process(RegisterAllocator registerAllocator) {
		AssemFlowGraph assemFlowGraph = new AssemFlowGraph(this.instrList.reverse());
		InterferenceGraph interferenceGraph = new BitArrayInterferenceGraphImpl(assemFlowGraph);
		SimpleGraphColouring simpleGraphColouring = new SimpleGraphColouring(this.getPrecoloured(interferenceGraph), this.getColours(interferenceGraph));
		simpleGraphColouring.allocate(interferenceGraph);
		
	}

	public void append(CodeFrag process) {
		CodeFrag processedFrag = this;
		for (; processedFrag.next != null; processedFrag = processedFrag.next)
			;
		processedFrag.next = process;
	}
}
