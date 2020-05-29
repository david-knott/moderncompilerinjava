package RegAlloc;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Stack;

import Graph.Node;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;

public class PotentialSpillColour implements TempMap {

	private Hashtable<Node, Integer> degrees = new Hashtable<Node, Integer>();
	private Stack<Node> simpleStack = new Stack<Node>();
	private TempMap precoloured;
	private TempList registers;
	private TempList spills;
	private Hashtable<Temp, Temp> coloured = new Hashtable<Temp, Temp>();

	private void addSpill(Temp temp) {
		if (spills == null) {
			spills = new TempList(temp);
		} else {
			spills.append(temp);
		}
	}

	private void reduceDegree(Node node) {
		for (var adj = node.adj(); adj != null; adj = adj.tail) {
			var d = degrees.get(adj.head);
			degrees.put(adj.head, d - 1);
		}
	}

	public PotentialSpillColour(InterferenceGraph graph, TempMap precoloured, TempList registers) {
		this.precoloured = precoloured;
		this.registers = registers;
		// graph.show(System.out);
		// store each nodes degree
		NodeList initialNodes = null;
		for (var nodes = graph.nodes(); nodes != null; nodes = nodes.tail) {
			degrees.put(nodes.head, nodes.head.degree());
			Temp temp = graph.gtemp(nodes.head);
			// add all non precoloured nodes to our node list.
			if (this.precoloured.tempMap(temp) == null) {
				initialNodes = new NodeList(nodes.head, initialNodes);
			}
		}
		// k colours for our graph
		int k = registers.size();
		// phase 'select'
		// phase 'select < k'
		// add nodes with degree less than k to stack
		// if we only have nodes of signigicant degree left
		// select one, mark as potential spill and continue with 'select < k'
		do {
			boolean ilk = false;
			for (NodeList nodeList = initialNodes; nodeList != null; nodeList = nodeList.tail) {
				Node node = nodeList.head;
				if (degrees.get(node) < k) {
					// System.out.println("Less than significant degree left");
					this.reduceDegree(node);
					this.simpleStack.push(node);
					initialNodes = initialNodes.remove(node);
					ilk = true;
				}
			}
			if (!ilk && initialNodes != null) { /* only significant degree nodes left */
				Node node = initialNodes.head;
				initialNodes = initialNodes.tail;
				this.reduceDegree(node);
				// System.out.println("Only significant degree left");
				this.simpleStack.push(node); // mark as potential spill
			}

		} while (initialNodes != null);

		// select phase
		while (!this.simpleStack.isEmpty()) {
			Node node = this.simpleStack.pop();
			// is this node a potential spill ?
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
			// assign colour to node
			if (colours.isEmpty()) {
				// System.out.println("No colours to assign");
				addSpill(graph.gtemp(node));
			} else {
				Temp color = colours.iterator().next();
				// System.out.println("Assigning color " + color);
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
		if (colour == null)
			return null;
		return this.precoloured.tempMap(colour);
	}
}