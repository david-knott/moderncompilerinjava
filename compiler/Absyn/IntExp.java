package Absyn;

import Types.Type;

public class IntExp extends Exp implements Typable {
   public int value;
   private Type type;

   public IntExp(int p, int v) {
      pos = p;
      value = v;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
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
