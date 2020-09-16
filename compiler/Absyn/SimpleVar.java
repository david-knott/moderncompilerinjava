package Absyn;

import Symbol.Symbol;
import Types.Type;

public class SimpleVar extends Var implements Typable {
   public Symbol name;
   private Type type;

   public SimpleVar(int p, Symbol n) {
      pos = p;
      name = n;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }

   public void setDef(Absyn exp) {
      this.def = exp;
   }

   @Override
   public Type getType() {
      return this.type;
   }

   @Override
   public void setType(Type type) {
      this.type = type;
   }
}
