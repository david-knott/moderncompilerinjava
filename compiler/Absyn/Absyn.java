package Absyn;

abstract public class Absyn {
  public int pos;

  public abstract void accept(AbsynVisitor visitor);
}
