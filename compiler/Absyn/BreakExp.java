package Absyn;

public class BreakExp extends Exp {
   public BreakExp(int p) {
      pos = p;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
   }
}
