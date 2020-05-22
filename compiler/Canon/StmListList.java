package Canon;

import Tree.StmList;

class StmListList {
  public Tree.StmList head;
  public StmListList tail;

  public StmListList(Tree.StmList h, StmListList t) {
    head = h;
    tail = t;
  }

public void append(StmList sl) {
  throw new Error("stmllistlist append not implemented");
}
}
