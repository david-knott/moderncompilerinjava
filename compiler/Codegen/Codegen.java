package Codegen;

import Assem.Instr;
import Assem.OPER;
import Frame.Frame;
import Temp.Temp;
import Temp.TempList;
import Tree.BINOP;
import Tree.CALL;
import Tree.CJUMP;
import Tree.CONST;
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
    private TempList calldefs;

    private TempList L(Temp h, TempList t) {
        return new TempList(h, t);
    }

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
            return;
}
if (stm instanceof MOVE && ((MOVE)stm).dst instanceof TEMP) {
        //    munchStm((MOVE) stm);
        emit(new OPER("mov `d0 <- M[`s0]\n", L(new Temp(), null), L(new Temp(), null)));
            return;
}
//if (stm instanceof JUMP && ((JUMP)stm).exp instanceof TEMP) {
if (stm instanceof JUMP) {
        //    munchStm((MOVE) stm);
        
        emit(new OPER("jmp `j0\n",null, null, ((JUMP)stm).targets));
            return;
}



        emit(new OPER("movq `d0 <- M[`s0]\n", L(new Temp(), null), L(new Temp(), null)));


        return;

        /*
        if (stm instanceof LABEL) {
            munchStm((LABEL) stm);
        } else if (stm instanceof MOVE) {
            munchStm((MOVE) stm);
        } else if (stm instanceof SEQ) {
            munchStm((SEQ) stm);
        } else if (stm instanceof JUMP) {
            munchStm((JUMP) stm);
        } else if (stm instanceof CJUMP) {
            munchStm((CJUMP) stm);

        } else
            throw new RuntimeException(stm + " unsupported");
            */
    }

    void munchStm(JUMP jmp) {
        munchExp(jmp.exp);
    }

    void munchStm(CJUMP jmp) {
      
    }

    void munchStm(SEQ seq) {
        munchStm(seq.left);
        munchStm(seq.right);
    }

    void munchStm(MOVE move) {

        emit(new OPER("STORE `d0 <- M[`s0]\n", L(new Temp(), null), L(new Temp(), null)));
        if (move.dst instanceof MEM && move.src instanceof Exp) {
      //      emit(new OPER("STORE `d0 <- M[`s0]", null, null));
        }
        if (move.dst instanceof MEM && move.src instanceof MEM) {
        //    emit(new OPER("STORE", null, null));
        }
        if (move.dst instanceof TEMP && move.src instanceof CONST) {
         //   emit(new OPER("ADD", L(((TEMP) move.dst).temp, null), L(munchExp((CONST)(move.src)), null)));
        }

       // if (move.dst instanceof TEMP && move.src instanceof Exp) {
         //   emit(new OPER("ADD", L(((TEMP) move.dst).temp, null), L(munchExp(move.src), null)));
      //  }
    }

    void munchStm(LABEL label) {
        emit(new Assem.LABEL(label.label.toString() + ":\n", label.label));
    }

    void munchStm(CALL call) {
        Temp r = munchExp(call.func);
        TempList l = munchArgs(0, call.args);
        emit(new OPER("CALL `s0\n", calldefs, L(r, l)));
    }

    private TempList munchArgs(int i, ExpList args) {
        if(i == 0) {
            return L(new Temp(), null);
        }
        return L(new Temp(), munchArgs(i - 1, args));
    }

    Temp munchExp(Exp exp) {
        if (exp instanceof TEMP)
            return munchExp((TEMP) exp);
        if (exp instanceof CONST)
            return munchExp((CONST) exp);
        if (exp instanceof NAME)
            return munchExp((NAME) exp);
        if (exp instanceof MEM)
            return munchExp((MEM) exp);
        throw new RuntimeException(exp + " unsupported");
    }

    Temp munchExp(TEMP temp) {
        return temp.temp;
    }

    Temp munchExp(MEM mem) {
        Temp r = new Temp();
        emit(new OPER("LOAD `d0 <- M[`s0]\n", L(new Temp(), null), L(munchExp(mem.exp), null)));
        return r;
    }

    Temp munchExp(BINOP binop) {
        Temp r = new Temp();
        return r;
    }

    Temp munchExp(CONST cnst) {
        Temp r = new Temp();
        return r;
    }

    Temp munchExp(NAME cnst) {
        Temp r = new Temp();
        return r;
    }
}