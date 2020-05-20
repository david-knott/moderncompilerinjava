package RegAlloc;

import java.util.Hashtable;
import java.util.Stack;

import Assem.InstrList;
import FlowGraph.AssemFlowGraph;
import Frame.Frame;
import Graph.Node;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;

class RegAlloc implements TempMap {
	public InstrList instrList;
	public Frame frame;
	public Colour colour;

	public RegAlloc(Frame frame, InstrList instrList) {
		this.instrList = instrList;
		this.frame = frame;
		var fg = new AssemFlowGraph(instrList);
		var baig = new BitArrayInterferenceGraphImpl(fg);
		this.colour = new Colour(baig, this.frame, this.frame.registers());
	}

	@Override
	public String tempMap(Temp t) {
		return this.colour.tempMap(t);
	}
}

class Colour implements TempMap {

	private Hashtable<Node, Integer> degrees = new Hashtable<Node, Integer>();
	private Stack<Node> simpleStack = new Stack<Node>();
	private TempMap precoloured;
	private TempList registers;
	private TempList spills;
	private Hashtable<Temp, String> coloured = new Hashtable<Temp, String>();

	public Colour(InterferenceGraph graph, TempMap precoloured, TempList registers) {
		this.precoloured = precoloured;
		graph.show(System.out);
		this.registers = registers;
		this.spills = null;
		//store each nodes degree
		NodeList initialNodes = null;
		NodeList potentialSpills = null;
		for (var nodes = graph.nodes(); nodes != null; nodes = nodes.tail) {
			degrees.put(nodes.head, nodes.head.degree());
			Temp temp = graph.gtemp(nodes.head);
			// add non precoloured temps to the worklist
			if(this.precoloured.tempMap(temp) == null) {
				initialNodes = new NodeList(nodes.head, initialNodes);
			}
		}
		// keep adding nodes to stack until we only
		// nodes of K + 1 degree list
		int k = registers.size();
		// first process nodes with degree less than or 
		// equal to K
		/*
		while(true) {
			var node = initialNodes.head;
			if(degrees.get(node) <= k) {
				for (var adj = node.adj(); adj != null; adj = adj.tail) {
					var d = degrees.get(adj.head);
					degrees.put(adj.head, d - 1);
				}
				this.simpleStack.push(node);
			} else {
				//node with more than K adjacent nodes
				potentialSpills = new NodeList(node, potentialSpills);
			}
			initialNodes = initialNodes.tail;
			//if only nodes of higher degree left
			if(initialNodes == null)
				break;
		}
		*/
		while (initialNodes != null) {
			var node = initialNodes.head;
			if(degrees.get(node) <= k) {
				for (var adj = node.adj(); adj != null; adj = adj.tail) {
					var d = degrees.get(adj.head);
					degrees.put(adj.head, d - 1);
				}
				this.simpleStack.push(initialNodes.head);
			} else {
				throw new Error("Cannot spill");
			}
			initialNodes = initialNodes.tail;
		}
		while (!this.simpleStack.isEmpty()) {
			Node node = this.simpleStack.pop();
			TempList colours = this.registers;
			for (var adj = node.adj(); adj != null; adj = adj.tail) {
				if (null != this.precoloured.tempMap(graph.gtemp(adj.head))) { // is the node precoloured ?
					System.out.println("Precoloured." + this.precoloured.tempMap(graph.gtemp(adj.head)));
					colours = colours.tail;
					continue;
				}
				if (coloured.containsKey(graph.gtemp(adj.head))) { // an adjacent node is already coloured
					// remove colour from our list
					System.out.println("Removing " + colours.head);
					colours = colours.tail;
					continue;
				}
			}
			if (colours == null) {
				System.out.println("Spilling node " + node);
				spills = new TempList(graph.gtemp(node), spills);
			} else {
				Temp color = colours.head;
				System.out.println("Assiging:" + graph.gtemp(node) + " => " + precoloured.tempMap(color));
				coloured.put(graph.gtemp(node), precoloured.tempMap(color));
			}
		}
	}

	public TempList spills() {
		return spills;
	}

	@Override
	public String tempMap(Temp t) {
		return coloured.get(t);
	}
}

public class RegisterAllocator implements TempMap {

	public TempList allocate(Frame frame, InterferenceGraph interferenceGraph) {
		// System.out.println("Allocating registers.");
		var precolouredNodes = this.getPrecoloured(frame, interferenceGraph);
		var colours = frame.registers();
		var initial = this.getInitial(frame, interferenceGraph);
		SimpleGraphColouring simpleGraphColouring2 = new SimpleGraphColouring(precolouredNodes, colours);
		simpleGraphColouring2.allocate(interferenceGraph, initial);
		TempList spills = null;
		for (Node node : simpleGraphColouring2.getSpilledNodes()) {
			if (spills == null) {
				spills = new TempList(interferenceGraph.gtemp(node));
			} else {
				spills = new TempList(interferenceGraph.gtemp(node), spills);
			}
		}
		return spills;
	}

	/**
	 * Returns all the precoloured nodes that are present in the interference graph
	 * 
	 * @param frame argument provides the registers and the precoloured temps
	 * @param graph the graph
	 * @return a linked list of precoloured nodes
	 */
	private PrecolouredNode getPrecoloured(Frame frame, InterferenceGraph graph) {
		PrecolouredNode precolouredNode = null;
		for (TempList tempList = frame.precoloured(); tempList != null; tempList = tempList.tail) {
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
	 * Returns a linked list of nodes in the interference graph that are not
	 * precoloured
	 * 
	 * @param frame             argument provides the registers and the precoloured
	 *                          temps
	 * @param interferenceGraph the interference graph
	 * @return a linked list of nodes
	 */
	private NodeList getInitial(Frame frame, InterferenceGraph interferenceGraph) {
		NodeList initialList = null;
		PrecolouredNode precoloured = this.getPrecoloured(frame, interferenceGraph);
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
			// node is in the precoloured list so skip it.
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

	@Override
	public String tempMap(Temp t) {
		// TODO Auto-generated method stub
		return null;
	}
}