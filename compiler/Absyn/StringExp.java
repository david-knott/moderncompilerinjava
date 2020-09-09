package Absyn;

public class StringExp extends Exp implements Typable {
   public String value;

   public StringExp(int p, String v) {
      pos = p;
      value = v;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
