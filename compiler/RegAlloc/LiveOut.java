package RegAlloc;

import java.util.BitSet;
import java.util.Hashtable;

import FlowGraph.FlowGraph;
import Graph.Node;
import Temp.Temp;
import Temp.TempList;

class LiveOut {

	private Hashtable<Node, TempList> liveMap;
	private Hashtable<Integer, Temp> tempMap;
	private Hashtable<Node, BitArraySet> liveInMap = new Hashtable<Node, BitArraySet>();
	private Hashtable<Node, BitArraySet> liveOutMap = new Hashtable<Node, BitArraySet>();

	private Temp getTemp(Integer i) {
		if (tempMap.containsKey(i)) {
			return tempMap.get(i);
		}
		return null;
	}

	public LiveOut(FlowGraph flowGraph) {
		liveMap = new Hashtable<Node, TempList>();
		tempMap = new Hashtable<Integer, Temp>();
		int capacity = 1;
		for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
			var node = nodes.head;
			for (var tl = flowGraph.def(node); tl != null; tl = tl.tail) {
				 System.out.println("adding " + tl.head.hashCode() + " to tempMap");
				tempMap.put(tl.head.hashCode(), tl.head);
				capacity = Math.max(capacity, tl.head.hashCode());
			}
		}
		capacity++;
		System.out.println("Capacity = " + capacity);
		// initialise maps with empty bit sets
		for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
			liveInMap.put(nodes.head, new BitArraySet(capacity));
			liveOutMap.put(nodes.head, new BitArraySet(capacity));
		}
		// calculate live ranges using liveness equations
		do {
			boolean changed = false;
			for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
				var node = nodes.head;
				System.out.println("live range for " + node);
				BitArraySet liveInPrev = liveInMap.get(node);
				BitArraySet liveOutPrev = liveOutMap.get(node);
				BitArraySet liveIn = new BitArraySet(flowGraph.use(node), capacity)
						.union(liveOutPrev.difference(new BitArraySet(flowGraph.def(node), capacity)));
				BitArraySet liveOut = new BitArraySet(capacity);
				for (var succ = node.succ(); succ != null; succ = succ.tail) {
					BitArraySet liveInSucc = liveInMap.get(succ.head);
					liveOut = liveOut.union(liveInSucc);
				}
				// save liveIn and liveOut in hash map for node
				liveInMap.put(node, liveIn);
				liveOutMap.put(node, liveOut);
				changed = changed || (!liveIn.equals(liveInPrev) || !liveOut.equals(liveOutPrev));
			}
			if (!changed) {
				break;
			}
		} while (true);
		// add live ranges as tempLists to liveOutmap
		for (Node n : liveOutMap.keySet()) {
			var bitMap = liveOutMap.get(n);
			System.out.print(n + " => " );
			for (int i = 0; i < capacity; i++) {
				if (bitMap.getBit(i)) {
					TempList tempList = liveMap.get(n);
					Temp temp = getTemp(i);
					if (temp != null) {
						if (tempList != null) {
							tempList = new TempList(temp, tempList);
							liveMap.put(n, tempList);
						} else {
							tempList = new TempList(temp);
							liveMap.put(n, tempList);
						}
						System.out.print(temp + ",");
					}
				}
			}
			System.out.println();
		}
	}

	public TempList liveOut(Node node) {
		return this.liveMap.get(node);
	}
}