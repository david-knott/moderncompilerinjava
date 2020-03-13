package Codegen;

import Assem.Instr;
import Assem.OPER;
import Frame.Frame;
import Intel.IntelFrame;
import Temp.Temp;
import Temp.TempList;
import Tree.BINOP;
import Tree.CALL;
import Tree.CJUMP;
import Tree.CONST;
import Tree.EXP;
import Tree.Exp;
import Tree.ExpList;
import Tree.JUMP;
import Tree.LABEL;
import Tree.MEM;
import Tree.MOVE;
import Tree.NAME;
import Tree.SEQ;
import Tree.Stm;
import Tree.TEMP;



public class Codegen {

    Frame frame;

    public Codegen(Frame f) {
        frame = f;
    }

    private Assem.InstrList iList = null, last = null;

    public Assem.InstrList codegen(Tree.Stm stm) {
        Assem.InstrList l;
       // munchStm(stm);
       var cg = new CodegenVisitor(frame);
       stm.accept(cg);
        l = iList;
        iList = last = null;
        return cg.iList;
    }
}