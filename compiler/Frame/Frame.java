package Frame;
import Temp.Label;

public abstract class Frame {

    abstract public Frame newFrame(Label name, Util.BoolList formals);
    public Label name;
    public AccessList formals;

    abstract public Access allocLocal(boolean escape);
}