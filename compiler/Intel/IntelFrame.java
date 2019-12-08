package Intel;

import Temp.Label;
import Temp.Temp;
import Util.BoolList;
import Frame.*;
/**
 * Intel activation frame
 * The formals field contains a list of K accesses
 * denoting the locations where the formal paramters 
 * will be kept at runtime as seen from inside the callee
 * This is likely to be at an offset from the base pointer
 * ( the function being called )
 * 
 * On current frame out going arguments for functions
 * from arg0 -> arg5 into registers
 * from arg6 -> into the stack
 * 
 * From callee, the extra args are at the following locations
 * Formals
 * RBP + 8 return address
 * RBP + 16 arg6
 * RBP + 24 arg7
 * 
 * From the caller, the extra args are at the following locations
 * Caller adds them in to the stack, thats just locals
 * RSP - 8 return address
 * RSP - 16 arg6
 * RSP - 24 arg7
 */
public class IntelFrame extends Frame {

    private static int id = 0;
    private int off = 0;
    private static final int WORD_SIZE = 8;

    public IntelFrame(Label nm, BoolList frml) {
        //need to save any registers we are going to use on the stack before we do anything
        //once the function has written to the output register, we can deallocate locals
        //and the restore the registers
        //formals are placed by the caller in the out going arguments of previous stack from
        //reachable as ebp + 8, ebp + 16
        name = nm;
        BoolList tmp = frml;
        AccessList al = null;
        AccessList prev =  null;
        //should be allocated from right to left
        while(tmp != null){
            //var escape =  i++ > 5 || tmp.head;
            var escape =  tmp.head;
            Access local;
            if(!escape) {
               local = new InReg(new Temp());
            } else {
                local = new InFrame((off));
                off = off + WORD_SIZE;
            }
            al = new AccessList(local, prev);
            prev = al;
            tmp = tmp.tail;
        }
        formals = al;
    }


    @Override
    public Access allocLocal(boolean escape) {
        System.out.println("allocate local variable in current stack frame:" + this.toString());
        var ret = escape ? new InFrame(off) : new InReg(new Temp());
        off = off - WORD_SIZE;
        return ret;

    }

    @Override
    public Frame newFrame(Label name, BoolList formals) {
        System.out.println("create new stack frame " + this.toString());
        return new IntelFrame(name, formals);
    }

    public String toString(){
        return "stack frame " + id++;
    }
}

class InFrame extends Access {

    int offset;

    InFrame(int os) {
        System.out.println("bp[" + os+ "]");
        offset = os;
    }
}

class InReg extends Access {
    Temp temp;

    InReg(Temp tmp) {
        System.out.println("reg:" + tmp);
        temp = tmp;
    }
}