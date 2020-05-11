package RegAlloc;

import Frame.Frame;
import Graph.Node;
import Graph.NodeList;
import Temp.TempList;

public class RegisterAllocator {

	public void allocate(Frame frame, InterferenceGraph interferenceGraph) {
        System.out.println("Allocating registers.");
        var precolouredNodes = this.getPrecoloured(frame, interferenceGraph);
        var colours = frame.registers();
        var initial = this.getInitial(frame, interferenceGraph);
        SimpleGraphColouring2 simpleGraphColouring2 = new SimpleGraphColouring2(precolouredNodes, colours);
        simpleGraphColouring2.allocate(interferenceGraph, initial);
	}

	public TempList getSpills() {
		return null;
	}
	
	public boolean hasSpills() {
		return false;
	}
    
	/**
	 * Returns all the precoloured nodes that are present in the interference graph
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
	 * @param frame argument provides the registers and the precoloured temps
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
}