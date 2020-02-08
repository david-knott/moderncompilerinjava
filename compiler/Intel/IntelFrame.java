package Intel;

import Temp.Label;
import Temp.Temp;
import Tree.BINOP;
import Tree.CONST;
import Tree.ESEQ;
import Tree.Exp;
import Tree.ExpList;
import Tree.LABEL;
import Tree.MEM;
import Tree.NAME;
import Tree.Stm;
import Tree.TEMP;
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

    private int formalOffset = 0;
    private int localOffset = 0;
    private static final int WORD_SIZE = 8;
    private static Temp rv = new Temp();
    private static Temp fp = new Temp();

    public IntelFrame(Label nm, BoolList frml) {
        // need to save any registers we are going to use on the stack before we do
        // anything
        // once the function has written to the output register, we can deallocate
        // locals
        // and the restore the registers
        // formals are placed by the caller in the out going arguments of previous stack
        // from
        // reachable as ebp + 8, ebp + 16
        // System.out.println("Intel frame created " + this.toString());
        BoolList tmp = frml;
        AccessList al = null;
        AccessList prev = null;
        int i = 0;
        // should be allocated from right to left
        while (tmp != null) {
            var escape = i++ > 5 || tmp.head;
           // escape = true;
            // var escape = tmp.head;
            Access local;
            if (!escape) {
                local = new InReg(new Temp());
            } else {
                formalOffset = formalOffset + WORD_SIZE;
                local = new InFrame((formalOffset));
            }
            al = new AccessList(local, prev);
            prev = al;
            tmp = tmp.tail;
        }
        formals = al;
    }

    @Override
    public Access allocLocal(boolean escape) {
        //System.out.println("Allocate local variable in frame " + this.toString());
        localOffset = localOffset - WORD_SIZE;
        var ret = escape ? new InFrame(localOffset) : new InReg(new Temp());
        return ret;
    }

    @Override
    public Frame newFrame(Label name, BoolList formals) {
        return new IntelFrame(name, formals);
    }

    @Override
    public Temp FP() {
        return fp;
    }

    @Override
    public int wordSize() {
        return WORD_SIZE;
    }

    @Override
    public Temp RV() {
        return rv;
    }

    @Override
    public Stm procEntryExit1(Stm body) {
        // TODO Auto-generated method stub
        return body;
    }

    @Override
    public Exp externalCall(String func, ExpList args) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Exp string(Label l, String literal) {
        //builds a string with a label definition
        //and IR code to make a word containing the string lenght
        //and code to emit character data
        return new NAME(l);
    }

    @Override
    public Stm procEntryExit3(Stm body) {
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
        return new MEM(new BINOP(BINOP.PLUS, framePtr, new CONST(offset)));
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
        return new TEMP(temp);
    }
}