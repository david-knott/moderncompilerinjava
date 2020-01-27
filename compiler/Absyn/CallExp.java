package Absyn;

import Symbol.Symbol;

public class CallExp extends Exp {
   public Symbol func;
   public ExpList args;

   public CallExp(int p, Symbol f, ExpList a) {
      pos = p;
      func = f;
      args = a;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
      args.accept(visitor);
   }
}
