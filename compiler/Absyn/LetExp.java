package Absyn;

public class LetExp extends Exp {
   public DecList decs;
   public Exp body;

   public LetExp(int p, DecList d, Exp b) {
      pos = p;
      decs = d;
      body = b;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      // TODO Auto-generated method stub
      visitor.visit(this);
      decs.accept(visitor);
      body.accept(visitor);
   }
}
