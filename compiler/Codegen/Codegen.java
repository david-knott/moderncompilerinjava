package Codegen;

import Frame.Frame;

public class Codegen {

    Frame frame;

    public Codegen(Frame f) {
        frame = f;
    }

    public Assem.InstrList codegen(Tree.Stm stm) {
        MaximumMunch maximumMunch = new MaximumMunch(frame);
        stm.accept(maximumMunch);
        return maximumMunch.iList;
    }
}