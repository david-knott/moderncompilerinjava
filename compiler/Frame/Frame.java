package Frame;

import Temp.Label;
import Temp.Temp;
import Tree.ExpList;
import Tree.Stm;

public abstract class Frame {
    public Label name;
    public AccessList formals;

    abstract public Temp FP();

    abstract public Temp RV();

    abstract public int wordSize();

    abstract public Frame newFrame(Label name, Util.BoolList formals);

    abstract public Access allocLocal(boolean escape);

    abstract public Tree.Stm procEntryExit1(Tree.Stm body);

    abstract public Tree.Exp externalCall(String func, Tree.ExpList args);
}