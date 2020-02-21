package Codegen;

import Assem.Instr;
import Frame.Frame;
import Tree.BINOP;
import Tree.CONST;
import Tree.Exp;
import Tree.JUMP;
import Tree.LABEL;
import Tree.MEM;
import Tree.MOVE;
import Tree.SEQ;
import Tree.Stm;
import Tree.TEMP;

public class Codegen {

    Frame frame;

    public Codegen(Frame f) {
        frame = f;
    }

    private Assem.InstrList iList = null, last = null;

    private void emit(Instr instr) {
        if (last != null) {
            last = last.tail = new Assem.InstrList(instr, null);
        } else {
            last = iList = new Assem.InstrList(instr, null);
        }
    }

    public Assem.InstrList codegen(Tree.Stm stm) {
        Assem.InstrList l;
        munchStm(stm);
        l = iList;
        iList = last = null;
        return l;
    }

    void munchStm(Stm stm) {
        if (stm instanceof LABEL) {
            munchStm((LABEL) stm);
        } else if (stm instanceof MOVE) {
            munchStm((MOVE) stm);
        } else if (stm instanceof JUMP) {
            munchStm((JUMP) stm);
        } else
            throw new RuntimeException(stm + " unsupported");
    }

    void munchStm(JUMP jmp) {
        munchExp(jmp.exp);
    }

    private void munchExp(Exp exp) {
    }

    void munchStm(SEQ seq) {

    }

    void munchStm(MOVE move) {
        munchExp(move.dst);
        munchExp(move.src);
    }

    void munchStm(LABEL label) {

    }

    void munchExp(MEM mem) {

    }

    void munchExp(BINOP binop) {

    }

    void munchExp(CONST cnst) {

    }

    void munchExp(TEMP temp) {

    }

}