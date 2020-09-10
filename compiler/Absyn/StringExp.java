package Absyn;

import Types.Type;

public class StringExp extends Exp implements Typable {
   public String value;
   private Type type;

   public StringExp(int p, String v) {
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
