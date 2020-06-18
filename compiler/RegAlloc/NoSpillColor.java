package RegAlloc;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Stack;

import Graph.Node;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;

/**
 * Graph Colouring implementation.
 */
class NoSpillColor implements TempMap {

	private Hashtable<Node, Integer> degrees = new Hashtable<Node, Integer>();
	private Stack<Node> simpleStack = new Stack<Node>();
	private TempMap precoloured;
	private TempList registers;
	private Hashtable<Temp, Temp> coloured = new Hashtable<Temp, Temp>();

	public NoSpillColor(InterferenceGraph graph, TempMap precoloured, TempList registers, boolean dumpGraph) {
		this.precoloured = precoloured;
		this.registers = registers;
		//graph.show(System.out);
		// store each nodes degree
		NodeList initialNodes = null;
		for (var nodes = graph.nodes(); nodes != null; nodes = nodes.tail) {
			degrees.put(nodes.head, nodes.head.degree());
			Temp temp = graph.gtemp(nodes.head);
			if (this.precoloured.tempMap(temp) == null) {
				initialNodes = new NodeList(nodes.head, initialNodes);
			}
		}
		// k colours for our graph
		int k = registers.size();
		while (initialNodes != null) {
			var node = initialNodes.head;
			if (degrees.get(node) <= k) {
				for (var adj = node.adj(); adj != null; adj = adj.tail) {
					var d = degrees.get(adj.head);
					degrees.put(adj.head, d - 1);
				}
				this.simpleStack.push(initialNodes.head);
			} else {
				throw new Error("Cannot spill " + degrees.get(node) + " > " + k);
			}
			initialNodes = initialNodes.tail;
		}
		while (!this.simpleStack.isEmpty()) {
			Node node = this.simpleStack.pop();
			HashSet<Temp> colours = new HashSet<Temp>();
			for (var c = this.registers; c != null; c = c.tail) {
				colours.add(c.head);
			}
			for (var adj = node.adj(); adj != null; adj = adj.tail) {
				if (null != this.precoloured.tempMap(graph.gtemp(adj.head))) { // is the node precoloured ?
					colours.remove(graph.gtemp(adj.head));
					continue;
				}
				if (coloured.containsKey(graph.gtemp(adj.head))) { // an adjacent node is already coloured
					var c = coloured.get(graph.gtemp(adj.head));
					colours.remove(c);
					continue;
				}
			}
			if (colours.isEmpty()) {
				throw new Error("Spill not implemented");
			} else {
				Temp color = colours.iterator().next();
				coloured.put(graph.gtemp(node), color);
			}
		}
	}

	public TempList spills() {
		throw new Error("Spill not implemented");
	}

	@Override
	public String tempMap(Temp t) {
		var colour = this.coloured.get(t);
		if (colour == null)
			return null;
		return this.precoloured.tempMap(colour);
	}
}
