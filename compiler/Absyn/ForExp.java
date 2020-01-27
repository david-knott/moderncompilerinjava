package Absyn;

public class ForExp extends Exp {
   public VarDec var;
   public Exp hi, body;

   public ForExp(int p, VarDec v, Exp h, Exp b) {
      pos = p;
      var = v;
      hi = h;
      body = b;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      // TODO Auto-generated method stub
      visitor.visit(this);
      var.accept(visitor);
      hi.accept(visitor);
      body.accept(visitor);
   }
}
