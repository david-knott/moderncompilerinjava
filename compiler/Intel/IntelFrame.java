package Intel;

import Temp.Label;
import Temp.Temp;
import Temp.TempList;
import Tree.BINOP;
import Tree.CALL;
import Tree.CONST;
import Tree.Exp;
import Tree.ExpList;
import Tree.MEM;
import Tree.MOVE;
import Tree.NAME;
import Tree.SEQ;
import Tree.Stm;
import Tree.StmList;
import Tree.TEMP;
import Util.BoolList;
import Assem.Instr;
import Assem.InstrList;
import Assem.OPER;
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
    public static Temp rax = new Temp();
    public static Temp rsp = new Temp();
    public static Temp rbx = new Temp();// callee save
    public static Temp rcx = new Temp();// 4th argu
    public static Temp rdx = new Temp();// 3rd argu
    public static Temp rsi = new Temp();// 2lrd argu
    public static Temp rdi = new Temp();// 1st argu
    public static Temp rbp = new Temp();// callee saved
    public static Temp r8 = new Temp();// 5th argup
    public static Temp r9 = new Temp();// 6th argup
    public static Temp r10 = new Temp();// scratch
    public static Temp r11 = new Temp();// scratch
    public static Temp r12 = new Temp();// callee
    public static Temp r13 = new Temp();// callee
    public static Temp r14 = new Temp();// callee
    public static Temp r15 = new Temp();// callee
    public static TempList specialRegs = new TempList(rbp, new TempList(rsp, null));
    /**
     * A linked list of temporaries that represent registers that are saved upon
     * entry and restored upon exit by the callee function. The caller can be
     * guaranteed that these registers will contain the same values when the callee
     * returns.
     */
    public static TempList calleeSaves = new TempList(rbx,
            new TempList(r12, new TempList(r13, new TempList(r14, new TempList(r15, null)))));

    /**
     * A linked list of temporaries that are used to pass arguments to functions.
     */
    public static TempList argRegs = new TempList(rdi,
            new TempList(rsi, new TempList(rdx, new TempList(rcx, new TempList(r8, new TempList(r9, null))))));

    /**
     * A linked list of temporaries that represent registers that are saved by the
     * caller and restored by the caller. The callee is free to clobber the values
     * in these registers and not worry about restoring them.
     */
    public static TempList callerSaves = new TempList(rcx, new TempList(rdx, new TempList(rdi,
            new TempList(rsi, new TempList(r8, new TempList(r9, new TempList(r10, new TempList(r11, null))))))));

    private static TempList precoloured = new TempList(rax, new TempList(rcx, new TempList(rdx, new TempList(rdi,
            new TempList(rsi, new TempList(r8, new TempList(r9, new TempList(r10, new TempList(r11, new TempList(rbx,
                    new TempList(r12, new TempList(r13, new TempList(r14, new TempList(r15, specialRegs))))))))))))));

    private static TempList registers = new TempList(rax,
            new TempList(rcx, new TempList(rdx, new TempList(rdi, new TempList(rsi, new TempList(
                    // rsp, new TempList(
                    r8, new TempList(r9, new TempList(r10, new TempList(r11, new TempList(rbx, new TempList(
                            // rbp, new TempList(
                            r12, new TempList(r13, new TempList(r14, new TempList(r15, null))))
                    // )
                    ))))
            // )
            ))))));

    //precoloured temps that map to the actual machine registers
    private static Hashtable<Temp, String> tmap = new Hashtable<Temp, String>();
    //mapping from string to an external function label
    private static Hashtable<String, Label> externalCalls = new Hashtable<String, Label>();
    //return sink used to indicate that certain values are live at function exit
    private static TempList returnSink = new TempList(rsp, calleeSaves);
    //map to store spilled temps and their related inframe accesses
    private Hashtable<Temp, InFrame> spillMap = new Hashtable<Temp, InFrame>();
    //map to store callee registers and the temp created to store them.
    private Hashtable<Temp, Temp> calleeTempMap = new Hashtable<Temp, Temp>();

    static {
        tmap.put(rax, "rax");
        tmap.put(rsp, "rsp");
        tmap.put(rbp, "rbp");
        tmap.put(rbx, "rbx");
        tmap.put(rcx, "rcx");
        tmap.put(rdx, "rdx");
        tmap.put(rsi, "rsi");
        tmap.put(rdi, "rdi");
        tmap.put(r8, "r8");
        tmap.put(r9, "r9");
        tmap.put(r10, "r10");
        tmap.put(r11, "r11");
        tmap.put(r12, "r12");
        tmap.put(r13, "r13");
        tmap.put(r14, "r14");
        tmap.put(r15, "r15");
    }

    private StmList callingConventions;

    private void addCallingConvention() {

    }
    /**
     * Moves function arguments into the registers or frame location
     * where they will be accessed within the function body. 
     */
    private void moveTempToFormal(Temp src, int i) {
        Temp dest;
        switch (i) {
            case 0:
                dest = rdi;
                break;
            case 1:
                dest = rsi;
                break;
            case 2:
                dest = rdx;
                break;
            case 3:
                dest = rcx;
                break;
            case 4:
                dest = r8;
                break;
            case 5:
                dest = r9;
                break;
            default:
                //allocate space on frame
                InFrame inFrame = (InFrame)this.allocLocal(true);
                var memLocation = new MEM(new BINOP(BINOP.PLUS, new CONST(inFrame.offset), new TEMP(this.FP())));
                this.callingConventions = new StmList(new Tree.MOVE(memLocation, new Tree.TEMP(src)), this.callingConventions);
                return;
        }
        this.callingConventions = new StmList(new Tree.MOVE(new Tree.TEMP(dest), new Tree.TEMP(src)),
                this.callingConventions);
    }

    /*
     * Moves function arguments into the registers or frame location
     * where they will be accessed within the function body. 
     */
    private void moveFrameToFormal(int offset, int i) {
        Temp dest;
        var memSrc = new MEM(new BINOP(BINOP.PLUS, new CONST(offset), new TEMP(this.FP())));
        switch (i) {
            case 0:
                dest = rdi;
                break;
            case 1:
                dest = rsi;
                break;
            case 2:
                dest = rdx;
                break;
            case 3:
                dest = rcx;
                break;
            case 4:
                dest = r8;
                break;
            case 5:
                dest = r9;
                break;
            default:
                //allocate space on frame
                InFrame inFrame = (InFrame)this.allocLocal(true);
                var memLocation = new MEM(new BINOP(BINOP.PLUS, new CONST(inFrame.offset), new TEMP(this.FP())));
                this.callingConventions = new StmList(new Tree.MOVE(memLocation, memSrc), this.callingConventions);
                return;
        }
        this.callingConventions = new StmList(new Tree.MOVE(new Tree.TEMP(dest), memSrc),
                this.callingConventions);
    }



    /**
     * Initialises a new instance of an Intel Frame activation record.
     * The frml argument is passed in from the abstract synax and represents
     * the formal arguments defined by the function header. These arguments
     * do not escape by default, meaning they are stored in temporaries. If 
     * an argument does escape it is stored in the frame. The codegen visitor
     * munch args, called by the call codegen, puts the actual arguments into
     * their argument passing registers, or into the caller frame, before the CALL command.
     * 
     * The callee is expected to move the temporaries from the argument passing registers
     * into the frame temporaries, or ensure that we access frame variables using
     * the correct offset relative to the new base pointer.
     * 
     * The frml list is by default non escaping, except for variables that are marked 
     * as must escape. 
     * 
     * This confuses me a bit ! The formals, which are taken
     * 
     * @param nm   the label for the related function
     * @param frml the formal list, where true indicates the argument escapes
     */
    public IntelFrame(Label nm, BoolList frml) {
        int i = 0;
        while (frml != null) {
            // first arg is static link
            var escape = /* i == 0 || */ i > 5 || frml.head;
            Access local;
            if (!escape) {
                //create a new temp for the variable
                Temp temp = new Temp();
                //moves calling convention register value into our temp
                this.moveTempToFormal(temp, i);
                local = new InReg(temp);
            } else {
                //create a location in the frame for the item 
                localOffset = localOffset - WORD_SIZE;
                this.moveFrameToFormal(localOffset, i);
                local = new InFrame((localOffset));
            }
            if (super.formals == null)
                super.formals = new AccessList(local, null);
            else
                super.formals.append(local);
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
        return rbp;
    }

    @Override
    public Temp RV() {
        return rax;
    }

    @Override
    public int wordSize() {
        return WORD_SIZE;
    }

    SEQ calleeSaveList() {
        SEQ seq = null;
        for(TempList callee = IntelFrame.calleeSaves; callee != null; callee = callee.tail) {
            Temp calleeTemp = new Temp();
            calleeTempMap.put(callee.head, calleeTemp);
            seq = new SEQ(seq, new MOVE(new TEMP(calleeTemp), new TEMP(callee.head)));
        }
        return seq;
    }

    SEQ calleeRestoreList() {
        SEQ seq = null;
        for(TempList callee = IntelFrame.calleeSaves; callee != null; callee = callee.tail) {
            Temp calleeTemp = calleeTempMap.get(callee.head);
            seq = new SEQ(seq, new MOVE(new TEMP(calleeTemp), new TEMP(callee.head)));
        }
        return seq;
    }

    SEQ moveArgs() {
        return null;
    }

    SEQ prepend(SEQ seqa, SEQ b) {
        return new SEQ(seqa, b);
    }

    SEQ prepend(SEQ seqa, Stm b) {
        return new SEQ(seqa, b);
    }

    SEQ append(SEQ seqa, SEQ b) {
        return new SEQ(b, seqa);
    }

    @Override
    public Stm procEntryExit1(Stm body) {
        //the idea here is that the register allocator will spill
        //the callee Temps if it needs to. The precoloured temps ( callee )
        //cannot be spilled as they are precoloured so we move them into new
        //temps that are not coloured.
        /*
        for(TempList callee = IntelFrame.calleeSaves; callee != null; callee = callee.tail) {
            Temp calleeTemp = new Temp();
            onEntry = new SEQ(new MOVE(new TEMP(calleeTemp), new TEMP(callee.head)), onEntry);
            onExit = new SEQ(new MOVE(new TEMP(callee.head), new TEMP(calleeTemp)), onExit);
        }
       // return new SEQ(onEntry, new SEQ(buildSeq(this.callingConventions), new SEQ(body, onExit)));
        return new SEQ(onEntry, onExit);
        */
        return append(
            prepend(
                prepend(
                    moveArgs(), 
                    calleeSaveList()
                ), 
                body
            ), 
            calleeRestoreList()
        );
    }

    /**
     * The return sink is an empty operation added to the end of a function. It is
     * used by the flow analysis to ensure that certain precoloured temporaries are
     * marked as live on exit from the function
     */
    public Assem.InstrList procEntryExit2(Assem.InstrList body) {
        return append(body, new Assem.InstrList(new Assem.OPER("; SINK INSTRUCTION", null, returnSink), null));
    }

    @Override
    public Proc procEntryExit3(Assem.InstrList body) {
        return new Proc("PROC " + "name", body, "END" + "name");
    }

    @Override
    public Exp externalCall(String func, ExpList args) {
        Label l = externalCalls.containsKey(func) ? externalCalls.get(func) : null;
        if (l == null) {
            l = new Label(func);
            externalCalls.put(func, l);
        }
        return new CALL(new NAME(l), args);
    }

    /**
     * Returns assembly for a string literal.
     */
    @Override
    public String string(Label l, String literal) {
        return l + "  db " + literal.length() + ",'" + literal + "'";
    }

    /**
     * Returns assembly for the supplied statement.
     */
    @Override
    public InstrList codegen(Stm head) {
        return (new Codegen.Codegen(this)).codegen(head);
    }

    @Override
    public String tempMap(Temp t) {
        return tmap.containsKey(t) ? tmap.get(t) : t.toString();
    }

    @Override
    public TempList registers() {
        return registers;
    }

    @Override
    public TempList precoloured() {
        return precoloured;
    }

    private Assem.InstrList append(Assem.InstrList a, Assem.InstrList b) {
        if (a == null)
            return b;
        else {
            Assem.InstrList p;
            for (p = a; p.tail != null; p = p.tail)
                ;
            p.tail = b;
            return a;
        }
    }



    @Override
    public Instr tempToMemory(Temp temp) {
        InFrame inFrame = (InFrame)this.allocLocal(true);
        spillMap.put(temp, inFrame);
        return new OPER("offset = " + inFrame.offset + "; Reg Allocator tempToMemory", null, new TempList(temp, null));
    }

    @Override
    public Instr memoryToTemp(Temp temp) {
        InFrame inFrame = this.spillMap.get(temp);
        return new OPER("offset = " + inFrame.offset + "; Reg Allocator memoryToTemp", new TempList(temp, null), null);
    }
}