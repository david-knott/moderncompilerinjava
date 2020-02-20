package Frame;

public class AccessList {
    public Access head;
    public AccessList tail;

    public AccessList(Access h, AccessList t) {
        head = h;
        tail = t;
    }

    public void append(Access access){
        var end = this;
        for(; end.tail != null; end = end.tail );
        end.tail = new AccessList(access, null);
    }
}