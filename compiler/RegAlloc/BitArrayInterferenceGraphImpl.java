package RegAlloc;

import java.util.Hashtable;

import FlowGraph.FlowGraph;
import Graph.Node;
import Temp.Temp;
import Temp.TempList;

public class BitArrayInterferenceGraphImpl extends InterferenceGraph {

    private Hashtable<Node, TempList> liveMap;

    public BitArrayInterferenceGraphImpl(FlowGraph flowGraph) {
        liveMap = new Hashtable<Node, TempList>();
        //initialise the liveMap
        for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {
            liveMap.put(nodes.head, null);
        }
        do {
            for (var nodes = flowGraph.nodes(); nodes != null; nodes = nodes.tail) {

            }
        } while (true);

    }

    @Override
    public Node tnode(Temp temp) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Temp gtemp(Node node) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MoveList moves() {
        // TODO Auto-generated method stub
        return null;
    }

}