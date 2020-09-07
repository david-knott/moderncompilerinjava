package Absyn;

import Symbol.Symbol;

/**
 * Represents a list of fields in a record. A field
 * list always escapes as it is stored on the heap.
 */
public class FieldList extends Absyn {
   public Symbol name;
   public Symbol typ;
   public FieldList tail;
   public boolean escape = true;

   public FieldList(int p, Symbol n, Symbol t, FieldList x) {
      pos = p;
      name = n;
      typ = t;
      tail = x;
   }

   public String toString() {
      return "[FieldList:{name=" + name + ",typ=" + typ + ",tail=" + tail + ",escape=" + escape + "}]";
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
