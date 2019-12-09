package Util;

public class BoolList {
  public boolean head;
  public BoolList tail;

  public BoolList(boolean h, BoolList t) {
    head = h;
    tail = t;
  }

  public BoolList last() {
    BoolList result = this;
    for (; result.tail != null; result = result.tail)
      ;
    return result;
  }
}

