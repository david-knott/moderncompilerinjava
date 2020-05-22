package Graph;

public class NodeList {
  public Node head;
  public NodeList tail;

  public NodeList(Node h, NodeList t) {
    head = h;
    tail = t;
  }

  public boolean contains(Node n) {
    for (NodeList s = this; s != null; s = s.tail) {
      if (s.head == n) {
        return true;
      }
    }
    return false;
  }

  public int size() {
    int size = 0;
    for (NodeList s = this; s != null; s = s.tail) {
      size++;
    }
    return size;
  }

  public NodeList reverse() {
    NodeList reversed = null;
    for (NodeList il = this; il != null; il = il.tail) {
      if (reversed == null) {
        reversed = new NodeList(il.head, null);
      } else {
        reversed = new NodeList(il.head, reversed);
      }
    }
    return reversed;
  }

}
