package RegAlloc;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Stack;

import javax.swing.text.AttributeSet.ColorAttribute;

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
	private Hashtable<Temp, Temp> coloured = new Hashtable<Temp, Temp>();

	public Colour(InterferenceGraph graph, TempMap precoloured, TempList registers) {
		this.precoloured = precoloured;
		graph.show(System.out);
		this.registers = registers;
		this.spills = null;
		//store each nodes degree
		NodeList initialNodes = null;
		for (var nodes = graph.nodes(); nodes != null; nodes = nodes.tail) {
			degrees.put(nodes.head, nodes.head.degree());
			Temp temp = graph.gtemp(nodes.head);
			if(this.precoloured.tempMap(temp) == null) {
				initialNodes = new NodeList(nodes.head, initialNodes);
			}
		}
		int k = registers.size();
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
			//TempList colours = this.registers;
			HashSet<Temp> colours = new HashSet<Temp>();
			for(var c = this.registers; c != null; c = c.tail) {
				colours.add(c.head);
			}
			for (var adj = node.adj(); adj != null; adj = adj.tail) {
				if (null != this.precoloured.tempMap(graph.gtemp(adj.head))) { // is the node precoloured ?
					System.out.println("Precoloured." + this.precoloured.tempMap(graph.gtemp(adj.head)));
					//colours = colours.tail;
					colours.remove(graph.gtemp(adj.head));
					continue;
				}
				if (coloured.containsKey(graph.gtemp(adj.head))) { // an adjacent node is already coloured
					var c= coloured.get(graph.gtemp(adj.head));
					//colours = colours.tail;
					colours.remove(c);
					continue;
				}
			}
			if (colours.isEmpty()) {
				throw new Error("Splill not implemented");
			} else {
				Temp color = colours.iterator().next();
				System.out.println("Assiging:" + graph.gtemp(node) + " => " + precoloured.tempMap(color));
				coloured.put(graph.gtemp(node), color);
			}
		}
	}

	public TempList spills() {
		return spills;
	}

	@Override
	public String tempMap(Temp t) {
		var colour = this.coloured.get(t);
		if(colour == null)
		return null;
		return this.precoloured.tempMap(colour);
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