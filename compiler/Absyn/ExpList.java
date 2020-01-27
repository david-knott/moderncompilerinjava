package Absyn;

public class ExpList {
   public Exp head;
   public ExpList tail;

   public ExpList(Exp h, ExpList t) {
      head = h;
      tail = t;
   }

   public void accept(AbsynVisitor visitor) {
      head.accept(visitor);
      if (tail != null)
         tail.accept(visitor);
   }
}
