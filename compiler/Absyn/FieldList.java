package Absyn;

import Symbol.Symbol;

/**
 * Represents a list of fields in a record. This can be in a record type
 * declarion or as the formal arguments for a function call.
 * By default this ast escapes, unless the FindEscape phase marks this item as
 * not escaping.
 */
public class FieldList extends Absyn {
   public final Symbol name;
   public final Symbol typ;
   public final FieldList tail;
   public boolean escape = true;
   public Absyn def;

   public FieldList(int p, Symbol n, Symbol t, FieldList x) {
      pos = p;
      name = n;
      typ = t;
      tail = x;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }

   public void def(Absyn exp) {
      this.def = exp;
   }

}
