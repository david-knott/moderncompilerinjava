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

        try {
            new Intel.CodeGen().burm(stm);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ;
        return maximumMunch.iList;
    }
}