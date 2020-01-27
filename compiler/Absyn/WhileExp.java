package Absyn;

public class WhileExp extends Exp {
   public Exp test, body;

   public WhileExp(int p, Exp t, Exp b) {
      pos = p;
      test = t;
      body = b;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
      test.accept(visitor);
      body.accept(visitor);
   }
}
