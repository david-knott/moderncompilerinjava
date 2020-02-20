package Translate;

public class AccessList {
    public Access head;
    public AccessList tail;

    public AccessList(Access hd) {
        head = hd;
    }

    public void append(Access access) {
        var end = this;
        for (; end.tail != null; end = end.tail)
            ;
        end.tail = new AccessList(access);

    }
}
