package Absyn;

import java.io.OutputStream;

abstract public class Absyn {
  public int pos;

  public abstract void accept(AbsynVisitor visitor);

}
