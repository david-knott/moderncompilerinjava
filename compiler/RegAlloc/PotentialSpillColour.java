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
		spills = TempList.append(spills, temp);
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
		System.out.println("registers:" + k);
		// phase 'select'
		// phase 'select < k'
		// add nodes with degree less than k to stack
		// if we only have nodes of signigicant degree left
		// select one, mark as potential spill and continue with 'select < k'
		do {
			boolean ilk = false;
			for (NodeList nodeList = initialNodes; nodeList != null; nodeList = nodeList.tail) {
				Node node = nodeList.head;
				int nodeDegree =  degrees.get(node);
				if (nodeDegree < k) {
		//			System.out.println(graph.gtemp(node) + " :less than significant degree left (" + nodeDegree + ")");
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
//				System.out.println(graph.gtemp(node) +" :only significant degree left");
				this.simpleStack.push(node); // mark as potential for spilling.
			}

		} while (initialNodes != null);

		// select phase
		while (!this.simpleStack.isEmpty()) {
			Node node = this.simpleStack.pop();
			Temp tempForNode = graph.gtemp(node);
			// is this node a potential spill ?
			System.out.print("Adding colours:");
			HashSet<Temp> colours = new HashSet<Temp>();
			for (var c = this.registers; c != null; c = c.tail) {
				System.out.print(c.head + " ");
				colours.add(c.head);
			}
			System.out.println();;
			for (var adj = node.adj(); adj != null; adj = adj.tail) {
				if (null != this.precoloured.tempMap(graph.gtemp(adj.head))) { // is the node precoloured ?
					colours.remove(graph.gtemp(adj.head));
		//			System.out.println(tempForNode + " :removing colour " + graph.gtemp(adj.head) + " as this is a precoloured node.");
					continue;
				}
				if (coloured.containsKey(graph.gtemp(adj.head))) { // an adjacent node is already coloured
					var c = coloured.get(graph.gtemp(adj.head));
					colours.remove(c);
				//	System.out.println(tempForNode + " :removing colour " + c + " as already assigned.");
					continue;
				}
			}
			// assign colour to node
			if (colours.isEmpty()) {
		//		System.out.println("--->" + tempForNode + ": no colours to assign, need to spill");
				addSpill(tempForNode);
			} else {
				Temp color = colours.iterator().next();
			//	System.out.println(tempForNode + ":Assigning color " + color);
				coloured.put(tempForNode, color);
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