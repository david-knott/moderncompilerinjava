package Frame;
import Temp.Temp;
import Temp.Label;

public abstract class AccessList {
    public Access head;
    public AccessList tail;
    public AccessList(Access h, AccessList t){
        head = h;
        tail = t;
    }
}