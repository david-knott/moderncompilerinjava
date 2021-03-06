package Absyn;

import Symbol.Symbol;

public class FieldVar extends Var {
   public Var var;
   public Symbol field;

   public FieldVar(int p, Var v, Symbol f) {
      pos = p;
      var = v;
      field = f;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
