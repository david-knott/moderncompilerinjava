package Codegen;

import Frame.Frame;

public class Codegen {

    Frame frame;

    public Codegen(Frame f) {
        frame = f;
    }

    public Assem.InstrList codegen(Tree.Stm stm) {
        var cg = new CodegenVisitor(frame);
        stm.accept(cg);
        return cg.iList;
    }
}