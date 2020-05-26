package Temp;

public class TempList {
   public Temp head;
   public TempList tail;

   public static TempList create(Temp h) {
      return new TempList(h, null);
   }

   public static TempList create(Temp[] h) {
      TempList tl = new TempList(h[0]);
      for(int i = 1; i < h.length; i++) {
         tl.append(h[i]);
      }
      return tl;
   }

   public void append(Temp t) {
      TempList end = this;
      for(; end.tail != null; end = end.tail);
      end.tail = new TempList(t);
   }

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