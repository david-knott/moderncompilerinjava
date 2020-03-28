package RegAlloc;

public class MoveList {
   public Graph.Node src, dst;
   public MoveList tail;

   public MoveList(Graph.Node s, Graph.Node d, MoveList t) {
      if (s == null)
         throw new Error("s");
      if (d == null)
         throw new Error("d");
      if (t == null)
         throw new Error("t");
      src = s;
      dst = d;
      tail = t;
   }
}
