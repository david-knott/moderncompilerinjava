package Tree;

public class StmList {

  public Stm head;

  public StmList tail;

  public StmList(Stm h) {
    if (h == null)
      throw new IllegalArgumentException("Stm");
    head = h;
    tail = null;
  }

  public StmList(Stm h, StmList t) {
    if (h == null)
      throw new IllegalArgumentException("Stm");
    head = h;
    tail = t;
  }

  /**
   * Returns this statement list with a 
   * new statement added to its tail.
   * @param stmList the statement list to add.
   * @return this statement list.
   */
  public StmList append(StmList stmList) {
    StmList end = this;
    for(; end.tail != null; end = end.tail);
    end.tail = stmList;
    return this;
  }

  /**
   * Returns the parameter stmlist with this appened to its end.
   * @param stmList the statement list to prepend to this.
   * @return the statment list.
   */
  public StmList prepend(StmList stmList) {
    return stmList.append(this);
  }

  /**
   * Returns the same statement list with a
   * new statement list added to the tail.
   * @param stm the statement to append.
   * @return this statement list.
   */
  public StmList append(Stm stm) {
    StmList end = this;
    for(; end.tail != null; end = end.tail);
    end.tail = new StmList(stm);
    return this;
  }

  /**
   * Returns a new statement list, with the newly create
   * item as the head of the list.
   * @param stm the statement to prepend.
   * @return a new statement list.
   */
  public StmList prepend(Stm stm) {
    return new StmList(stm, this);
  }

  public Tree.Stm toSEQ() {
    if (this.tail != null) {
      return new SEQ(this.head, this.tail.toSEQ());
    }
    return this.head;
  }

}
