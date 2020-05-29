package Intel;

import Temp.Label;
import Temp.Temp;
import Temp.TempList;
import Tree.BINOP;
import Tree.CALL;
import Tree.CONST;
import Tree.EXP;
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
import Util.GenericLinkedList;
import Assem.Instr;
import Assem.InstrList;
import Assem.OPER;
import Codegen.Codegen;
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

    private int localOffset = 0;
    private StmList callingConventions;
    private Codegen codege;
    private static final int WORD_SIZE = 8;
    public static Temp rax = Temp.create("rax");
    public static Temp rsp = Temp.create("rsp");
    public static Temp rbx = Temp.create("rbx");// callee save
    public static Temp rcx = Temp.create("rcx");// 4th argu
    public static Temp rdx = Temp.create("rdx");// 3rd argu
    public static Temp rsi = Temp.create("rsi");// 2lrd argu
    public static Temp rdi = Temp.create("rdi");// 1st argu
    public static Temp rbp = Temp.create("rbp");// callee saved
    public static Temp r8 = Temp.create("r8");// 5th argup
    public static Temp r9 = Temp.create("r9");// 6th argup
    public static Temp r10 = Temp.create("r10");// scratch
    public static Temp r11 = Temp.create("r11");// scratch
    public static Temp r12 = Temp.create("r12");// callee
    public static Temp r13 = Temp.create("r13");// callee
    public static Temp r14 = Temp.create("r14");// callee
    public static Temp r15 = Temp.create("r15");// callee
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

    /**
     * Registers that are available as colours for the register allocator.
     */
    private TempList registers = TempList.create(new Temp[]{
        rax,
        rbx,
        rcx,
        rdx,
        rdi,
        r8,
        r9,
        r10,
        r11,
        r12,
        r13,
        r14,
        r15
    });

    /**
     * Registers that are available as colours for the register allocator.
     */
    private TempList precoloured = TempList.create(new Temp[]{
        rax,
        rsp,
        rbx,
        rcx,
        rdx,
        rdi,
        rbp,
        r8,
        r9,
        r10,
        r11,
        r12,
        r13,
        r14,
        r15
    });


    // return sink used to indicate that certain values are live at function exit
    private static TempList returnSink = new TempList(rsp, calleeSaves);
    // map to store spilled temps and their related inframe accesses
    private Hashtable<Temp, InFrame> spillMap = new Hashtable<Temp, InFrame>();
    // map to store callee registers and the temp created to store them.
    private Hashtable<Temp, Temp> calleeTempMap = new Hashtable<Temp, Temp>();

    private void addCallingConvention(Stm stm) {
        if (this.callingConventions == null) {
            this.callingConventions = new StmList(stm);
        } else {
            this.callingConventions = this.callingConventions.append(stm);
        }
    }

    /**
     * Generates statements that move from argument registers or frame locations
     * into a dest temporary.
     */
    private void moveRegArgsToTemp(Temp dest, int i) {
        Temp src;
        switch (i) {
            case 0:
                src = rdi;
                break;
            case 1:
                src = rsi;
                break;
            case 2:
                src = rdx;
                break;
            case 3:
                src = rcx;
                break;
            case 4:
                src = r8;
                break;
            case 5:
                src = r9;
                break;
            default:
                // allocate space on frame
                InFrame inFrame = (InFrame) this.allocLocal(true);
                // move item at frame location into temp
                var memLocation = new MEM(new BINOP(BINOP.PLUS, new CONST(inFrame.offset), new TEMP(this.FP())));
                this.addCallingConvention(new Tree.MOVE(new Tree.TEMP(dest), memLocation));
                return;
        }
        this.addCallingConvention(new Tree.MOVE(new Tree.TEMP(dest), new Tree.TEMP(src)));
    }

    /*
     * Generates statements that move from argument registers or frame locations
     * into a frame location.
     */
    private void moveFrameToFormal(int offset, int i) {
        Temp src;
        var memDest = new MEM(new BINOP(BINOP.PLUS, new CONST(offset), new TEMP(this.FP())));
        switch (i) {
            case 0:
                src = rdi;
                break;
            case 1:
                src = rsi;
                break;
            case 2:
                src = rdx;
                break;
            case 3:
                src = rcx;
                break;
            case 4:
                src = r8;
                break;
            case 5:
                src = r9;
                break;
            default:
                // allocate space on frame
                InFrame inFrame = (InFrame) this.allocLocal(true);
                var srcLocation = new MEM(new BINOP(BINOP.PLUS, new CONST(inFrame.offset), new TEMP(this.FP())));
                var destLocation = new MEM(new BINOP(BINOP.MINUS, new CONST(offset), new TEMP(this.FP())));
                this.addCallingConvention(new Tree.MOVE(destLocation, srcLocation));
                return;
        }
        this.addCallingConvention(new Tree.MOVE(memDest, new Tree.TEMP(src)));
    }


    /**
     * Initialises a new instance of an Intel Frame activation record. The frml
     * argument is passed in from the abstract synax and represents the formal
     * arguments defined by the function header. These arguments do not escape by
     * default, meaning they are stored in temporaries. If an argument does escape
     * it is stored in the frame. The codegen visitor munch args, called by the call
     * codegen, puts the actual arguments into their argument passing registers, or
     * into the caller frame, before the CALL command.
     * 
     * The callee is expected to move the temporaries from the argument passing
     * registers into the frame temporaries, or ensure that we access frame
     * variables using the correct offset relative to the new base pointer.
     * 
     * The frml list is by default non escaping, except for variables that are
     * marked as must escape.
     * 
     * @param nm   the label for the related function
     * @param frml the formal list, where true indicates the argument escapes
     */
    public IntelFrame(Label nm, BoolList frml) {
        this.codege = new Codegen(this);
        int i = 0;
        while (frml != null) {
            // first arg is static link
            var escape = i > 5 || frml.head;
            Access local;
            if (!escape) {
                // create a new temp for the variable
                Temp temp = Temp.create();
                // moves calling convention register value into our temp
                this.moveRegArgsToTemp(temp, i);
                local = new InReg(temp);
            } else {
                // create a location in the frame for the item
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

    /**
     * Allocates a local for storage in this frame.
     */
    @Override
    public Access allocLocal(boolean escape) {
        localOffset = localOffset - WORD_SIZE;
        return escape ? new InFrame(localOffset) : new InReg(new Temp());
    }

    /**
     * Creates a new Frame instance. Where name is
     * 
     * @param name    the name of the function.
     * @param formals the formal argument list, where a true indicates the variable
     *                escapes.
     */
    @Override
    public Frame newFrame(Label name, BoolList formals) {
        return new IntelFrame(name, formals);
    }

    /**
     * Returns the precoloured temporary that maps to the frame pointer or base
     * pointer.
     */
    @Override
    public Temp FP() {
        return rbp;
    }

    /**
     * Returns the precoloured temporary that maps the the register that is the
     * return value from a function is placed in.
     */
    @Override
    public Temp RV() {
        return rax;
    }

    /**
     * The word size for this architecture.
     */
    @Override
    public int wordSize() {
        return WORD_SIZE;
    }

    /**
     * Returns a linked list of statements that move the callee precoloured
     * registers into new temporaries. These may be spilled by the register
     * allocator, or coloured to a free register. 
     * 
     * This moves callee saves to temporaries regardless of whether they are
     * used or not. It would be better to only move items that are already in
     * use.
     * 
     * @return a linked list of move statements.
     */
    Stm calleeSaveList() {
        StmList list = null;
        for (TempList callee = IntelFrame.calleeSaves; callee != null; callee = callee.tail) {
            Temp calleeTemp = new Temp();
            calleeTempMap.put(callee.head, calleeTemp);
            MOVE move = new MOVE(new TEMP(calleeTemp), new TEMP(callee.head));
            if (list == null) {
                list = new StmList(move);
            } else {
                list = list.append(move);
            }
        }
        return list.toSEQ();
    }

    /**
     * Returns a linked list of statements that restore the precoloured callee
     * registers to their values before the function was invoked.
     * 
     * @return a linked list of move statements.
     */
    Stm calleeRestoreList() {
        StmList list = null;
        for (TempList callee = IntelFrame.calleeSaves; callee != null; callee = callee.tail) {
            Temp calleeTemp = calleeTempMap.get(callee.head);
            MOVE move = new MOVE(new TEMP(callee.head), new TEMP(calleeTemp));
            if (list == null) {
                list = new StmList(move);
            } else {
                list = list.append(move);
            }
        }
        return list.toSEQ();
    }

    /**
     * Returns a list of statements that move formal arguments from their calling
     * convention registers or frame locations to the temporaries that are used in
     * the function body.
     * 
     * @return a linked list of move statements.
     */
    Stm moveArgs() {
        if (this.callingConventions == null) {
            return new EXP(new CONST(0));
        }
        return this.callingConventions.toSEQ();
    }

    /**
     * The idea here is that the register allocator will spill the callee Temps if
     * it needs to. The precoloured temps ( callee ) cannot be spilled as they are
     * precoloured so we move them into new temps that are not coloured.
     */
    @Override
    public Stm procEntryExit1(Stm body) {
       return new SEQ(calleeSaveList(), new SEQ(moveArgs(), new SEQ(body, calleeRestoreList())));
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

    /**
     * The return sink is an empty operation added to the end of a function. It is
     * used by the flow analysis to ensure that certain precoloured temporaries are
     * marked as live on exit from the function.
     */
    public Assem.InstrList procEntryExit2(Assem.InstrList body) {
        return append(body, new Assem.InstrList(new Assem.OPER("", null, returnSink), null));
    }

    /**
     * Adds the procedure prolog and epilog.
     */
    @Override
    public Proc procEntryExit3(Assem.InstrList body) {
        InstrList prolog = new InstrList(
            new OPER("pushq %`d0", new TempList(IntelFrame.rbp), null), 
            new InstrList(
                new OPER("movq %`s0 %`d0", new TempList(IntelFrame.rbp), new TempList(IntelFrame.rsp)),
                new InstrList(
                    new OPER("sub %`d0, " + 0, new TempList(IntelFrame.rsp), null),
                    null
                ) 
            )
        );
        InstrList epilog = new InstrList(
            new OPER("movq %`s0 %`d0", new TempList(IntelFrame.rsp), new TempList(IntelFrame.rbp)),
            new InstrList(
                new OPER("pop %`d0", new TempList(IntelFrame.rbp), null),
                new InstrList(
                    new OPER("ret", null,  null)
                )
            )
        );
        return new Proc(prolog, body, epilog);
    }

    @Override
    public Exp externalCall(String func, ExpList args) {
        return new CALL(new NAME(Label.create(func)), args);
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
        return this.codege.codegen(head);
    }

    /**
     * Temporary map for the intel frame.
     */
    @Override
    public String tempMap(Temp t) {
        return this.precoloured.toTempMap().get(t);
    }

    /**
     * A linked list of all Intel registers except for the stack pointer and base
     * pointer. This is used for colours in the register allocation phase.
     */
    @Override
    public TempList registers() {
        return registers;
    }
    

    /**
     * Returns a string representation of this frame.
     * 
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("### Intel Stack Frame ###");
        stringBuilder.append("-> RBP ");
        stringBuilder.append("RBP - 8 ");
        stringBuilder.append("RBP - 16 ");
        stringBuilder.append("RBP - 24 ");
        stringBuilder.append("-> RSP");
        return stringBuilder.toString();
    }

    private InFrame getInFrameAccess(Access access) {
        if(access instanceof InFrame) {
            return (InFrame)access;
        } else {
            throw new Error("Access is not of type InFrame");
        }
    }

    @Override
    public InstrList tempToMemory(Temp temp, Temp spillTemp) {
        InFrame inFrame = this.getInFrameAccess(this.allocLocal(true));
        spillMap.put(temp, inFrame);
        Instr moveTempToNewTemp = new Assem.MOVE("movq %`s0, %`d0;\tspill - move temp to new temp\n", spillTemp, temp);
        Instr moveNewTempToFrame = new OPER("offset = " + inFrame.offset + "; Reg Allocator tempToMemory", null, new TempList(spillTemp, null));
        return new InstrList(moveTempToNewTemp, new InstrList(moveNewTempToFrame, null)); 
    }

    @Override
    public InstrList memoryToTemp(Temp temp, Temp spillTemp) {
        InFrame inFrame = this.spillMap.get(temp);
        Instr moveFrameToNewTemp = new OPER("offset = " + inFrame.offset + "; Reg Allocator memory to temp", new TempList(spillTemp, null), null);
        Instr moveNewTempToTemp = new Assem.MOVE("movq %`s0, %`d0;\tspill - move new temp to temp\n", temp, spillTemp);
        return new InstrList(moveFrameToNewTemp, new InstrList(moveNewTempToTemp, null)); 
    }
}