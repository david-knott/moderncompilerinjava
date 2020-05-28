package Temp;

import java.util.Hashtable;

public class TempList {
   public Temp head;
   public TempList tail;

   public static TempList create(Temp h) {
      return new TempList(h, null);
   }

   public static TempList create(Temp[] h) {
      TempList tl = new TempList(h[0]);
      for (int i = 1; i < h.length; i++) {
         tl.append(h[i]);
      }
      return tl;
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

   public void append(Temp t) {
      TempList end = this;
      for (; end.tail != null; end = end.tail)
         ;
      end.tail = new TempList(t);
   }

   public TempList and(TempList tempList) {
      System.out.println(this);
      System.out.println(tempList);
      TempList and = null;
      TempList me = this;
      do
      {
         if(me.head == tempList.head) {
            if(and == null) {
               and = new TempList(me.head);
            } else {
               and.append(me.head);
            }
         }
         me = me.tail;
         tempList = tempList.tail;
      }while(me != null && tempList != null);
      System.out.println(and);
      return and;
   }

   public boolean contains(Temp n) {
      for (TempList s = this; s != null; s = s.tail) {
         if (s.head == n) {
            return true;
         }
      }
      return false;
   }

   public boolean contains(TempList spills) {
      for (TempList s = spills; s != null; s = s.tail) {
         if(this.contains(s.head)) {
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

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("[");
      for (TempList s = this; s != null; s = s.tail) {
         builder.append(s.head);
         if (s.tail != null) {
            builder.append(",");
         }
      }
      builder.append("]");
      return builder.toString();

   }

   public Hashtable<Temp, String> toTempMap() {
      Hashtable<Temp, String> ht = new Hashtable<Temp, String>();
      TempList start = this;
      for (; start != null; start = start.tail) {
         ht.put(start.head, Temp.name(start.head));
      }
      return ht;
   }

}