package Absyn;


import Symbol.Symbol;

public class SimpleVar extends Var implements Typable {
   public Symbol name;
   public Absyn def;

   public SimpleVar(int p, Symbol n) {
      pos = p;
      name = n;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }

   public void def(Absyn exp) {
      this.def = exp;
   }
}
