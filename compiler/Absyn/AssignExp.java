package Absyn;


public class AssignExp extends Exp {
   public Var var;
   public Exp exp;

   public AssignExp(int p, Var v, Exp e) {
      pos = p;
      var = v;
      exp = e;
   }

   @Override
   public void accept(AbsynVisitor visitor) {
      visitor.visit(this);
      var.accept(visitor);
      exp.accept(visitor);
   }
}
