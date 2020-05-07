package RegAlloc;

import java.util.Hashtable;

import Graph.Node;
import Temp.Temp;

class InterferenceGraphImpl extends InterferenceGraph {

	private Hashtable<Temp, Node> tnode = new Hashtable<Temp, Node>();
	private Hashtable<Node, Temp> ntemp = new Hashtable<Node, Temp>();

	public InterferenceGraphImpl() {
    }
    
	@Override
	public Node tnode(Temp temp) {
		return tnode.get(temp);
	}

	@Override
	public Temp gtemp(Node node) {
		return ntemp.get(node);
	}

	@Override
	public MoveList moves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bind(Node node, Temp temp) {
		this.tnode.put(temp, node);
		this.ntemp.put(node, temp);
	}
}