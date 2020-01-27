package Absyn;

import Symbol.Symbol;

public class ArrayExp extends Exp {
   public Symbol typ;
   public Exp size, init;

   public ArrayExp(int p, Symbol t, Exp s, Exp i) {
      pos = p;
      typ = t;
      size = s;
      init = i;
   }

   public String toString() {
      return "[ArrayExp:{typ=" + typ + ",size=" + size + ",init=" + init + "}]";
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
      size.accept(visitor);
      init.accept(visitor);
   }
}
