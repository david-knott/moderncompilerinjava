package Intel;

import Temp.Label;
import Temp.Temp;
import Temp.TempList;
import Tree.BINOP;
import Tree.CALL;
import Tree.CONST;
import Tree.Exp;
import Tree.ExpList;
import Tree.LABEL;
import Tree.MEM;
import Tree.MOVE;
import Tree.NAME;
import Tree.SEQ;
import Tree.Stm;
import Tree.TEMP;
import Util.BoolList;
import Assem.InstrList;
import Frame.*;
import java.util.Hashtable;

/**
 * Intel activation frame The formals field contains a list of K accesses
 * denoting the locations where the formal paramters will be kept at runtime as
 * seen from inside the callee.
 * 
 * For caller on intel procs, for outgoing function arguments for functions from
 * arg0 -> arg5 into registers from arg6 -> into the stack
 * 
 * For callee, function arguments are placed into the expected place in the
 * translate code. The frame assumes that arguments will be placed correctly.
 */
public class IntelFrame extends Frame {

    private int localOffset = WORD_SIZE;
    private static final int WORD_SIZE = 8;
    private static Temp rv = new Temp();
    private static Temp fp = new Temp();
    private Hashtable<Temp, String> tmap = new Hashtable<Temp, String>();
    private static TempList returnSink = new TempList(new Temp(), new TempList(new Temp(), null));

    public IntelFrame(Label nm, BoolList frml) {
        tmap.put(rv, "rv");
        tmap.put(fp, "fp");
        int i = 0;
        while (frml != null) {
            // first arg is static link in frame, net 6 in registers,
            var escape = i == 0 || i > 6 || frml.head;
            Access local;
            if (!escape) {
                local = new InReg(new Temp());
            } else {
                localOffset = localOffset - WORD_SIZE;
                local = new InFrame((localOffset));
            }
            if (formals == null)
                formals = new AccessList(local, null);
            else
                formals.append(local);
            frml = frml.tail;
            i++;
        }
    }

    @Override
    public Access allocLocal(boolean escape) {
        localOffset = -WORD_SIZE;
        return escape ? new InFrame(localOffset) : new InReg(new Temp());
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
        return body;
    }

    @Override
    public Exp externalCall(String func, ExpList args) {
        return new CALL(new NAME(new Label(func)), args);
    }

    @Override
    public Exp string(Label l, String literal) {
        // builds a string with a label definition
        // and IR code to make a word containing the string lenght
        // and code to emit character data
        return new NAME(l);
    }

    public Assem.InstrList procEntryExit2(Assem.InstrList body){
        return append(body, new Assem.InstrList(new Assem.OPER("", null, returnSink), null));
    }

    @Override
    public Proc procEntryExit3(Stm body) {
        return new Proc(
            "PROC " + "name", body, "END" + "name"
        );
    }

    @Override
    public InstrList codegen(Stm head) {
        return (new Codegen.Codegen(this)).codegen(head);
    }

    @Override
    public String tempMap(Temp t) {

        boolean f = tmap.containsKey(t);
        if (f)
            return tmap.get(t);
        else
            return t.toString();
    }

    private Assem.InstrList append(Assem.InstrList a, Assem.InstrList b){
        if(a == null) return b;
        else {
            Assem.InstrList p;
            for(p = a; p.tail != null; p = p.tail);
            p.tail = b;
            return a;
        }
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