package Absyn;

import Symbol.Symbol;
import Util.Assert;

public class SimpleVar extends Var  {
   public Symbol name;

   public SimpleVar(int p, Symbol n) {
      pos = p;
      name = n;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }

   public void setDef(Absyn exp) {
      Assert.assertNotNull(exp);
      this.def = exp;
   }
}
