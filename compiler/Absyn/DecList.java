package Absyn;

public class DecList {
   public Dec head;
   public DecList tail;

   public DecList(Dec h, DecList t) {
      head = h;
      tail = t;
   }

   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
