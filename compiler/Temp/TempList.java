package Temp;

public class TempList {
   public Temp head;
   public TempList tail;

   public TempList(Temp h, TempList t) {
      if (h == null)
         throw new Error("h cannot be null");
      head = h;
      tail = t;
   }
}
