package Temp;

public class TempList {
   public Temp head;
   public TempList tail;

   public TempList(Temp h) {
      if (h == null)
         throw new Error("h cannot be null");
      head = h;
      tail = null;
   }

   public TempList(Temp h, TempList t) {
      if (h == null)
         throw new Error("h cannot be null");
      head = h;
      tail = t;
   }

   public boolean contains(Temp n) {
      for (TempList s = this; s != null; s = s.tail) {
         if (s.head == n) {
            return true;
         }
      }
      return false;
   }

   public int size() {
      int size = 0;
      for (TempList s = this; s != null; s = s.tail) {
         size++;
      }
      return size;
   }
}