package Frame;

import Temp.Label;

public abstract class Frame {
    public Label name;
    public AccessList formals;

    abstract public Frame newFrame(Label name, Util.BoolList formals);

    abstract public Access allocLocal(boolean escape);
}