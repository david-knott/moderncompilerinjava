package RegAlloc;

import Graph.Node;
import Temp.Temp;

public class PrecolouredNode {

    public Node node;
    public Temp temp;
    public PrecolouredNode tail;

    public PrecolouredNode(Node node, Temp temp) {
        this.node = node;
        this.temp = temp;
        this.tail = null;
    }

    public PrecolouredNode append(Node node, Temp temp) {
        PrecolouredNode end = this;
        for(;end.tail != null; end = end.tail);
        end.tail = new PrecolouredNode(node, temp);
        return this;
    }
}