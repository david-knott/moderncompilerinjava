package Absyn;

import Symbol.Symbol;

public class ArrayTy extends Ty {
   public Symbol typ;

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
}
