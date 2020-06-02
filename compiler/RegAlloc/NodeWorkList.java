package RegAlloc;

import Graph.Node;

class NodeWorkList {

    public Node me;
    public NodeWorkList next;
    public NodeWorkList prev;


    NodeWorkList(Node me) {
    }

	public static NodeWorkList or(NodeWorkList spillWorkList, Object instructionWorkList) {
		return null;
	}

	public static NodeWorkList andOr(NodeWorkList spillWorkList, NodeWorkList nodeWorkList) {
		return null;
	}

	public static boolean contains(NodeWorkList coalescedNodes, Node node) {
		return false;
	}

	public static NodeWorkList append(NodeWorkList precoloured, Node tnode) {
		return null;
	}

}