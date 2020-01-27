package Absyn;

public class DecList {
   public Dec head;
   public DecList tail;

   public DecList(Dec h, DecList t) {
      head = h;
      tail = t;
   }

   public void accept(AbsynVisitor visitor) {
      head.accept(visitor);
      if (tail != null)
         tail.accept(visitor);
   }

}
