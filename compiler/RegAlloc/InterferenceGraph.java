package RegAlloc;

import Graph.Node;
import Temp.Temp;
import Graph.Graph;

abstract public class InterferenceGraph extends Graph {

   abstract public Node tnode(Temp temp);

   abstract public Temp gtemp(Node node);

   abstract public void bind(Node node, Temp temp);

   abstract public MoveList moves();

   public void addEdge(Node from, Node to, boolean move) {
      if (from == to)
         return;
      this.addEdge(from, to);
   }

   public int spillCost(Node node) {
      return 1;
   }
}
