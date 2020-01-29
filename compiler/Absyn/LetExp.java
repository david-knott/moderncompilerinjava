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
      if (decs != null)
         decs.accept(visitor);
      if (body != null)
         body.accept(visitor);
      visitor.visit(this);
   }
}
