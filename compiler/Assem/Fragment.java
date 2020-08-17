package Assem;

import Frame.Frame;

public abstract class Fragment {

    public Fragment next;

    public abstract Frame getFrame();

    public abstract void accept(FragmentVisitor fragmentVisitor);
}