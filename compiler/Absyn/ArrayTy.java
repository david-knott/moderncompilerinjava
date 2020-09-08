package Absyn;

import Symbol.Symbol;

/**
 * This class defines the type of an array. The typ member represents the name
 * of the type.
 */
public class ArrayTy extends Ty {
   public Symbol typ;
   public Absyn def;

   public ArrayTy(int p, Symbol t) {
      pos = p;
      typ = t;
   }

   public String toString() {
      return "[ArrayTy:{typ=" + typ + "}]";
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }

   public void def(Absyn exp) {
      this.def = exp;
   }
}
