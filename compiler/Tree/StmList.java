package Tree;

import Frame.Frame;

public class StmList {

  public Stm head;

  public StmList tail;

  public Frame frame;

  public StmList(Stm h, StmList t) {
    if (h == null)
      throw new IllegalArgumentException("Stm");
    head = h;
    tail = t;
  }
}
