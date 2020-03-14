package Codegen;

import Frame.Frame;


public class Codegen {

    Frame frame;

    public Codegen(Frame f) {
        frame = f;
    }

    private Assem.InstrList iList = null, last = null;

    public Assem.InstrList codegen(Tree.Stm stm) {
        Assem.InstrList l;
       // munchStm(stm);
       var cg = new CodegenVisitor2(frame);
       stm.accept(cg);
        l = iList;
        iList = last = null;
        return cg.iList;
    }
}