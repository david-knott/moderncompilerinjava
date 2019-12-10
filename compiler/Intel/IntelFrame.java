package Intel;

import Temp.Label;
import Temp.Temp;
import Tree.Exp;
import Tree.ExpList;
import Tree.Stm;
import Util.BoolList;
import Frame.*;

/**
 * Intel activation frame The formals field contains a list of K accesses
 * denoting the locations where the formal paramters will be kept at runtime as
 * seen from inside the callee This is likely to be at an offset from the base
 * pointer ( the function being called )
 * 
 * On current frame out going arguments for functions from arg0 -> arg5 into
 * registers from arg6 -> into the stack
 * 
 * From callee, the extra args are at the following locations Formals RBP + 8
 * return address RBP + 16 arg6 RBP + 24 arg7
 * 
 * From the caller, the extra args are at the following locations Caller adds
 * them in to the stack, thats just locals RSP - 8 return address RSP - 16 arg6
 * RSP - 24 arg7
 */
public class IntelFrame extends Frame {

    private int formalOffset = 8;
    private int localOffset = -8;
    private static final int WORD_SIZE = 8;

    public IntelFrame(Label nm, BoolList frml) {
        // need to save any registers we are going to use on the stack before we do
        // anything
        // once the function has written to the output register, we can deallocate
        // locals
        // and the restore the registers
        // formals are placed by the caller in the out going arguments of previous stack
        // from
        // reachable as ebp + 8, ebp + 16
        System.out.println("intel frame created");
        BoolList tmp = frml;
        AccessList al = null;
        AccessList prev = null;
        int i = 0;
        // should be allocated from right to left
        while (tmp != null) {
            var escape = i++ > 5 || tmp.head;
            escape = true;
            // var escape = tmp.head;
            Access local;
            if (!escape) {
                local = new InReg(new Temp());
            } else {
                local = new InFrame((formalOffset));
                formalOffset = formalOffset + WORD_SIZE;
            }
            al = new AccessList(local, prev);
            prev = al;
            tmp = tmp.tail;
        }
        formals = al;
        System.out.println("end constructor");

    }

    @Override
    public Access allocLocal(boolean escape) {
        System.out.println("allocate local variable " + this.toString());
        var ret = escape ? new InFrame(localOffset) : new InReg(new Temp());
        localOffset = localOffset - WORD_SIZE;
        return ret;

    }

    @Override
    public Frame newFrame(Label name, BoolList formals) {
        System.out.println("creating new stack frame " + this.toString());

        return new IntelFrame(name, formals);
    }

    @Override
    public Temp FP() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int wordSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Temp RV() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Stm procEntryExit1(Stm body) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Exp externalCall(String func, ExpList args) {
        // TODO Auto-generated method stub
        return null;
    }
}

class InFrame extends Access {

    int offset;

    InFrame(int os) {
        System.out.println("bp[" + os + "]");
        offset = os;
    }

    @Override
    public Exp exp(Exp framePtr) {
        // TODO Auto-generated method stub
        return null;
    }
}

class InReg extends Access {
    Temp temp;

    InReg(Temp tmp) {
        System.out.println("reg:" + tmp);
        temp = tmp;
    }

    @Override
    public Exp exp(Exp framePtr) {
        // TODO Auto-generated method stub
        return null;
    }
}