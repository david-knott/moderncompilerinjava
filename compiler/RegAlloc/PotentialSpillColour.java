package RegAlloc;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Stack;

import Core.CompilerEventType;
import Core.Component;
import Graph.Node;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;

public class PotentialSpillColour extends Component implements TempMap {

	public static CompilerEventType NON_SIG_PUSH_STACK = new CompilerEventType("Start");
    public static CompilerEventType SIG_PUSH_STACK = new CompilerEventType("Start");
    public static CompilerEventType REMOVE_AVAILABLE_COLOUR = new CompilerEventType("Start");
    public static CompilerEventType REMOVE_AVAILABLE_COLOUR_PRECOLOURED = new CompilerEventType("Start");
    public static CompilerEventType ASSIGN_COLOUR = new CompilerEventType("Start");

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
		// phase 'select'
		// phase 'select < k'
		// add nodes with degree less than k to stack
		// if we only have nodes of signigicant degree left
		// select one, mark as potential spill and continue with 'select < k'
		TempList sk = null;
		do {
			boolean ilk = false;
			for (NodeList nodeList = initialNodes; nodeList != null; nodeList = nodeList.tail) {
				Node node = nodeList.head;
				int nodeDegree =  degrees.get(node);
				Temp tfn = graph.gtemp(node);
				if (nodeDegree < k) {
					this.trigger(NON_SIG_PUSH_STACK, node); //TODO
					this.reduceDegree(node);
					this.simpleStack.push(node);
					sk = TempList.append(sk, new TempList(tfn));
					initialNodes = initialNodes.remove(node);
					ilk = true;
				}
			}
			if (!ilk && initialNodes != null) { /* only significant degree nodes left */
				Node node = initialNodes.head;
				this.trigger(SIG_PUSH_STACK, node); //TODO
				initialNodes = initialNodes.tail;
				this.reduceDegree(node);
				Temp tfn = graph.gtemp(node);
				sk = TempList.append(sk, new TempList(tfn));
				this.simpleStack.push(node); // mark as potential for spilling.
			}

		} while (initialNodes != null);

		System.out.println(sk);
		// select phase
		while (!this.simpleStack.isEmpty()) {
			Node node = this.simpleStack.pop();
			Temp tempForNode = graph.gtemp(node);
			HashSet<Temp> colours = new HashSet<Temp>();
			for (var c = this.registers; c != null; c = c.tail) {
				colours.add(c.head);
			}
			for (var adj = node.adj(); adj != null; adj = adj.tail) {
				if (null != this.precoloured.tempMap(graph.gtemp(adj.head))) { // is the node precoloured ?
					colours.remove(graph.gtemp(adj.head));
					this.trigger(REMOVE_AVAILABLE_COLOUR_PRECOLOURED, graph.gtemp(adj.head));
					continue;
				}
				if (coloured.containsKey(graph.gtemp(adj.head))) { // an adjacent node is already coloured
					var c = coloured.get(graph.gtemp(adj.head));
					colours.remove(c);
					this.trigger(REMOVE_AVAILABLE_COLOUR, graph.gtemp(adj.head));
					continue;
				}
			}
			// assign colour to node
			if (colours.isEmpty()) {
				System.out.println("No colour for " + tempForNode);
				addSpill(tempForNode);
			} else {
				Temp colour = colours.iterator().next();
				this.trigger(ASSIGN_COLOUR, colour);
				coloured.put(tempForNode, colour);
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