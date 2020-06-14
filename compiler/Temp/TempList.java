package Temp;

import java.util.HashSet;
import java.util.Hashtable;

public class TempList {

   public static boolean contains(TempList s, Temp n) {
      for (; s != null; s = s.tail) {
         if (s.head == n) {
            return true;
         }
      }
      return false;
   }

   public static TempList sort(TempList tempList) {
      if (tempList == null || tempList.tail == null) {
         return tempList;
      }
      /*
       * 2) Else divide the linked list into two halves. FrontBackSplit(head, &a, &b);
       * a and b are two halves 3) Sort the two halves a and b. MergeSort(a);
       * MergeSort(b); 4) Merge the sorted a and b (using SortedMerge() discussed
       * here) and update the head pointer using headRef. headRef = SortedMerge(a, b);
       */

      return tempList;
   }

   /**
    * Return this set in reverse.
    * 
    * @return a new linked list with this lists elements in reverse.
    */
   public static TempList reverse(TempList me) {
      if (me.tail == null) {
         return new TempList(me.head);
      }
      return TempList.append(TempList.reverse(me.tail), me.head);
   }

   /**
    * Appends Temp t onto the end of TempList me. If TempList me is null and Temp t
    * is not, a new TempList with t as head is created a returned to the caller.
    * 
    * @param me
    * @param t
    * @return
    */
   public static TempList append(TempList me, Temp t) {
      if (me == null && t == null) {
         return null;
      }
      if (me == null && t != null) {
         return new TempList(t);
      }
      if (me.tail == null) {
         return new TempList(me.head, new TempList(t));
      }
      return new TempList(me.head, TempList.append(me.tail, t));
   }

   public static TempList append(TempList me, TempList end) {
      if (me == null && end == null) {
         return null;
      }
      if (me == null && end != null) {
         return end;
      }
      if (me != null && end == null) {
         return me;
      }
      TempList res = me;
      for (; end != null; end = end.tail) {
         res = TempList.append(res, end.head);
      }
      return res;
   }

   /**
    * Performs a set intersection operation and returns a new set. This assumes
    * boths sets are ordered using the same ordering. Uses merge sort.
    * 
    * @param me
    * @param tempList
    * @return
    */
   public static TempList and(TempList me, TempList tempList) {
      me = TempList.sort(me);
      tempList = TempList.sort(tempList);
      if (me == null || tempList == null)
         return null;
      TempList and = null;
      do {
         if (me.head == tempList.head) {
            and = TempList.append(and, me.head);
         }
         me = me.tail;
         tempList = tempList.tail;
      } while (me != null && tempList != null);
      return and;
   }

   /**
    * Performs a set union on me and templist, returns a new set. This assumes
    * boths sets are ordered using the same ordering. Uses merge sort.
    * 
    * @param me
    * @param tempList
    * @return
    */
   public static TempList or(TempList me, TempList tempList) {
      if (me == null && tempList == null)
         return null;
      me = TempList.sort(me);
      tempList = TempList.sort(tempList);
      TempList or = null;
      while (me != null && tempList != null) {
         if (me.head.compareTo(tempList.head) > 0) {
            or = TempList.append(or, me.head);
            or = TempList.append(or, tempList.head);
         } else if (me.head.compareTo(tempList.head) < 0) {
            or = TempList.append(or, tempList.head);
            or = TempList.append(or, me.head);
         } else {
            or = TempList.append(or, tempList.head);
         }
         me = me.tail;
         tempList = tempList.tail;
      }
      ;
      while (me != null) {
         or = TempList.append(or, me.head);
         me = me.tail;
      }
      while (tempList != null) {
         or = TempList.append(or, tempList.head);
         tempList = tempList.tail;
      }
      return or;
   }

   public static TempList andNot(TempList me, TempList tempList) {
      if (me == null)
         return null;
      if (tempList == null) {
         return me;
      }
      me = TempList.sort(me);
      tempList = TempList.sort(tempList);
      TempList andNot = null;
      for (; me != null; me = me.tail) {
         if (!tempList.contains(me)) {
            andNot = TempList.append(andNot, me.head);
         }
      }
      return andNot;
   }

   public static TempList create(Temp h) {
      return new TempList(h, null);
   }

   public static TempList create(Temp[] h) {
      TempList tl = null;
      for (int i = 1; i < h.length; i++) {
         tl = TempList.append(tl, h[i]);
      }
      return tl;
   }

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

   public boolean contains(TempList spills) {
      for (TempList s = spills; s != null; s = s.tail) {
         if (this.contains(s.head)) {
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
         builder.append(s.head + "=>" + Temp.name(s.head));
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