package Absyn;

public class NilExp extends Exp {
  public NilExp(int p) {
    pos = p;
  }

  @Override
  public void accept(AbsynVisitor visitor) {
    visitor.visit(this);

  }
}
