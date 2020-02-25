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
    //registers that call will trash. The caller
    //saves are expected to be saved by the caller
    //and the return value
    private TempList calldefs = new TempList(
        IntelFrame.rv, IntelFrame.callerSaves
    );

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
        //write out label
        if (stm instanceof LABEL) {
            var label = ((LABEL) stm);
            emit(new Assem.LABEL(label.label.toString() + ":\n", label.label));
            return;
        }
        //move -> dest = temp, src = munched expression
        if (stm instanceof MOVE 
        && ((MOVE)stm).dst instanceof TEMP
        ) {
            var t1 = (TEMP)(((MOVE)stm).dst);
            var t2 = munchExp(((MOVE)stm).src);
            emit(new Assem.MOVE("movq %`s0, %`d0\n", t1.temp, t2));
            return;
        }
        //jump to label
        if (stm instanceof JUMP) {
            emit(new OPER("jmp `j0\n",null, null, ((JUMP)stm).targets));
            return;
        }
        if(stm instanceof EXP){
            if(((EXP)stm).exp instanceof CALL){
                //Temp t = munchExp(((EXP)stm).exp);
            }
        }




//        emit(new OPER("movq `d0 <- M[`s0]\n", L(new Temp(), null), L(new Temp(), null)));


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

    void munchStm(CJUMP jmp) {
      
    }

    void munchStm(SEQ seq) {
        munchStm(seq.left);
        munchStm(seq.right);
    }

    void munchStm(MOVE move) {
        throw new RuntimeException("unsupported");
        /*
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
      */
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
        emit(new OPER("movq $" + cnst.value + ", %`d0 \n", L(r, null), null, null));
        return r;
    }

    Temp munchExp(NAME cnst) {
        Temp r = new Temp();
        return r;
    }
}