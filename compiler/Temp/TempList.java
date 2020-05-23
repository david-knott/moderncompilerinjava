package Temp;

import Util.GenericLinkedList;

public class TempList extends GenericLinkedList<Temp> {
   public Temp head;
   public TempList tail;

   public TempList(Temp h) {
      super(h);
   }

   public TempList(Temp h, TempList t) {
      super(h, t);
   }
}