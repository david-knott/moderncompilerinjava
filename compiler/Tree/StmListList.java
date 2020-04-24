package Tree;

public class StmListList {

    public StmList head;

    public StmListList tail;

    public StmListList(StmList h, StmListList t) {
        if (h == null)
            throw new IllegalArgumentException("StmList");
        head = h;
        tail = t;
    }

    public void append(StmList stmList) {
        var end = this;
        for(; end.tail != null; end = end.tail);
        end.tail = new StmListList(stmList, null);
    }
}