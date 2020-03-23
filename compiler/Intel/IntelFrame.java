package Intel;

import Temp.Label;
import Temp.Temp;
import Temp.TempList;
import Tree.CALL;
import Tree.Exp;
import Tree.ExpList;
import Tree.NAME;
import Tree.Stm;
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
    public static Temp rv = new Temp();
    public static Temp fp = new Temp();
    public static Temp sp = new Temp();
    public static Temp rbx = new Temp();//callee save
    public static Temp rcx = new Temp();//4th argu
    public static Temp rdx = new Temp();//3rd argu
    public static Temp rsi = new Temp();//2lrd argu
    public static Temp rdi = new Temp();//1st argu
    public static Temp rbp = new Temp();//callee saved
    public static Temp r8= new Temp();//5th argup
    public static Temp r9= new Temp();//6th argup
    public static Temp r10= new Temp();//scratch
    public static Temp r11= new Temp();//scratch
    public static Temp r12= new Temp();//callee
    public static Temp r13= new Temp();//callee
    public static Temp r14= new Temp();//callee
    public static Temp r15= new Temp();//callee
    public static TempList specialRegs = new TempList(
        rv, new TempList(
            fp, new TempList(
                sp, null
            )
        )
    );
    public static TempList calleeSaves = new TempList(
        rbx, new TempList(
            rbp, new TempList(
                r12, new TempList(
                    r13, new TempList(
                        r14, new TempList(
                            r15, null
                        )
                    )
                )
            )
        )
    );
    public static TempList argRegs = new TempList(
        rdi, new TempList(
            rsi, new TempList(
                rdx, new TempList(
                    rcx, new TempList(
                        r8, new TempList(
                            r9, null
                        )
                    )
                )
            )
        )
    );
    public static TempList callerSaves = new TempList(
        rcx, new TempList(
            rdx, new TempList(
                rdi, new TempList(
                    rsi, new TempList(
                        sp, new TempList(
                            r8, new TempList(
                                r9, new TempList(
                                    r10, new TempList(
                                        r11, null
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    );
    private static Hashtable<Temp, String> tmap = new Hashtable<Temp, String>();
    private static Hashtable<String, Label> externalCalls = new Hashtable<String, Label>();
    private static TempList returnSink = new TempList(sp, calleeSaves);

    static {
        tmap.put(rv, "rax");
        tmap.put(fp, "rbp");
        tmap.put(sp, "sp");
        tmap.put(rbx, "rbx");
        tmap.put(rcx, "rcx");
        tmap.put(rdx, "rdx");
        tmap.put(rsi, "rsi");
        tmap.put(rdi, "rdi");
        tmap.put(rbp, "rbp");
        tmap.put(r8, "r8");
        tmap.put(r9, "r9");
        tmap.put(r10, "r10");
        tmap.put(r11, "r11");
        tmap.put(r12, "r12");
        tmap.put(r13, "r13");
        tmap.put(r14, "r14");
        tmap.put(r15, "r15");
    }

    public IntelFrame(Label nm, BoolList frml) {
       int i = 0;
        while (frml != null) {
            // first arg is static link 
            var escape = /*i == 0 ||*/ i > 6 || frml.head;
            Access local;
            if (!escape) {
                Temp temp = new Temp();
                local = new InReg(temp);
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
        localOffset = localOffset - WORD_SIZE;
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
    public Temp RV() {
        return rv;
    }

    @Override
    public int wordSize() {
        return WORD_SIZE;
    }

    @Override
    public Stm procEntryExit1(Stm body) {
        return body;
    }


    @Override
    public Exp externalCall(String func, ExpList args) {
        Label l = externalCalls.containsKey(func) ? externalCalls.get(func) : null;
        if(l == null) {
            l = new Label(func);
            externalCalls.put(func, l);
        }
        return new CALL(
            new NAME(
                l
            ), 
            args
        );
    }

    /**
     * Returns a string of assembly
     */
    @Override
    public String string(Label l, String literal) {
        return l + "  db " + literal.length() +  ",'" + literal + "'";
    }

    public Assem.InstrList procEntryExit2(Assem.InstrList body){
        return append(
            body, 
            new Assem.InstrList(new Assem.OPER("", null, returnSink), null));
    }

    @Override
    public Proc procEntryExit3(Assem.InstrList body) {
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
        return tmap.containsKey(t) 
        ? tmap.get(t) 
        : t.toString();
    }

    private Assem.InstrList append(Assem.InstrList a, Assem.InstrList b){
        if(a == null) 
            return b;
        else {
            Assem.InstrList p;
            for(p = a; p.tail != null; p = p.tail);
            p.tail = b;
            return a;
        }
    }
}