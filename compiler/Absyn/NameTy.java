package Absyn;

import Symbol.Symbol;

public class NameTy extends Ty {
   public Symbol name;
   public Absyn def;

   public NameTy(int p, Symbol n) {
      pos = p;
      name = n;
   }

   public String toString() {
      return "[NameTy: {name=" + name + "}]";
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);

   }


   public void def(Absyn exp) {
      this.def = exp;
   }

}
