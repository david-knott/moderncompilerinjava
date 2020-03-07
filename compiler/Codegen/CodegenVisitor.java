package Codegen;

import Assem.Instr;
import Assem.OPER;
import Frame.Frame;
import Intel.IntelFrame;
import Temp.LabelList;
import Temp.Temp;
import Temp.TempList;
import Tree.BINOP;
import Tree.CALL;
import Tree.CJUMP;
import Tree.CONST;
import Tree.ESEQ;
import Tree.EXP;
import Tree.ExpList;
import Tree.JUMP;
import Tree.LABEL;
import Tree.MEM;
import Tree.MOVE;
import Tree.NAME;
import Tree.SEQ;
import Tree.TEMP;
import Tree.TreeVisitor;

class CodegenVisitor implements TreeVisitor {

    Assem.InstrList iList = null, last = null;
    private Temp temp;
    private Frame frame;
    private TempList calldefs = new TempList(IntelFrame.rv, IntelFrame.callerSaves);

    public CodegenVisitor(Frame frame) {
        this.frame = frame;
	}

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

    private TempList munchArgs(int i, ExpList args) {
        args.head.accept(this);
        var argTemp = temp;
        Temp finalPos = null;
        switch (i) {
            case 0:
                finalPos = IntelFrame.rdi;
                break;
            case 1:
                finalPos = IntelFrame.rsi;
                break;
            case 2:
                finalPos = IntelFrame.rdx;
                break;
            case 3:
                finalPos = IntelFrame.rcx;
                break;
            case 5:
                finalPos = IntelFrame.r8;
                break;
            case 6:
                finalPos = IntelFrame.r9;
                break;
            default:
                // item is pushed onto the stack
                


                break;
        }
        if (finalPos != null) {
            emit(new Assem.MOVE("movq %`s0, %`d0\n", finalPos, argTemp));
        } else {
            emit(new Assem.MOVE("push %`s0\n", null, argTemp));
        }
        if (args.tail == null) {
            return L(argTemp, null);
        }
        return L(argTemp, munchArgs(i + 1, args.tail));
    }

    @Override
    public void visit(BINOP op) {
        op.left.accept(this);
        var leftTemp = temp;
        op.right.accept(this);
        switch (op.binop) {
            case BINOP.AND:
                break;
            case BINOP.ARSHIFT:
                break;
            case BINOP.DIV:
                emit(new OPER("div %`s0 %`d0 \n", L(temp, null), L(leftTemp, null), null));
                break;
            case BINOP.LSHIFT:
                break;
            case BINOP.MINUS:
                emit(new OPER("sub %`s0 %`d0 \n", L(temp, null), L(leftTemp, null), null));
                break;
            case BINOP.MUL:
                emit(new OPER("mul %`s0 %`d0 \n", L(temp, null), L(leftTemp, null), null));
                break;
            case BINOP.OR:
                break;
            case BINOP.PLUS:
                emit(new OPER("add %`s0 %`d0 \n", L(temp, null), L(leftTemp, null), null));
                break;
            case BINOP.RSHIFT:
                break;
            case BINOP.XOR:
                break;
            default:
                throw new Error("Unsupported operation");

        }
    }

    @Override
    public void visit(CALL call) {
        call.func.accept(this);
        var callFuncTemp = temp;
        TempList l = munchArgs(0, call.args);
        emit(new OPER("call `s0\n", calldefs, L(callFuncTemp, l)));
    }

    @Override
    public void visit(CONST cnst) {
        temp = new Temp();
        emit(new OPER("movq $" + cnst.value + ", %`d0 \n", L(temp, null), null, null));
    }

    @Override
    public void visit(ESEQ op) {

    }

    @Override
    public void visit(EXP exp) {
        // handle procedure call
        if (exp.exp instanceof CALL) {
            exp.exp.accept(this);
            return;
        }
        // other exp expression
        exp.exp.accept(this);
    }

    @Override
    public void visit(JUMP op) {
        emit(new OPER("jmp `j0\n", null, null, op.targets));
    }

    @Override
    public void visit(LABEL label) {
        emit(new Assem.LABEL(label.label.toString() + ":\n", label.label));
    }

    @Override
    public void visit(MEM op) {
        op.exp.accept(this);
    }

    @Override
    public void visit(MOVE move) {
        if (move.dst instanceof TEMP && move.src instanceof CALL) {
            move.src.accept(this);
            TEMP dstTemp = (TEMP) move.dst;
            // move function temp result into dst temp
            emit(new Assem.MOVE("movq %`s0, %`d0\n", dstTemp.temp, temp));
            return;
        }
        // move src exp to memory exp with offset
        if (move.dst instanceof MEM) {
            MEM memDst = (MEM) move.dst;
            memDst.exp.accept(this);
            var memDstTemp = temp;
            if (memDst.exp instanceof BINOP) {
                BINOP memDstBinop = (BINOP) memDst.exp;
                if (memDstBinop.binop == 0) {
                    if (memDstBinop.right instanceof CONST) {
                        memDstBinop.left.accept(this);
                        var leftTemp = temp;
                        var memDstBinopRight = (CONST) memDstBinop.right;
                        emit(new Assem.MOVE("movq (`s0) " + memDstBinopRight.value + ", %`d0\n", memDstTemp, leftTemp));
                        return;
                    }
                    if (memDstBinop.left instanceof CONST) {
                        memDstBinop.right.accept(this);
                        var rightTemp = temp;
                        var memDstBinopLeft = (CONST) memDstBinop.left;
                        emit(new Assem.MOVE("movq (`s0) " + memDstBinopLeft.value + ", %`d0\n", memDstTemp, rightTemp));
                        return;
                    }
                }
            }
            move.src.accept(this);
            var srcTemp = temp;
            emit(new Assem.MOVE("movq (`s0) %`d0\n", memDstTemp, srcTemp));
            return;
        }
        if (move.dst instanceof TEMP && move.src instanceof MEM) {
            var t1 = (TEMP) (move.dst);
            move.src.accept(this);
            emit(new Assem.MOVE("movq (`s0), %`d0\n", t1.temp, temp));
            return;
        }
        if (move.dst instanceof TEMP) {
            var t1 = (TEMP) (move.dst);
            move.src.accept(this);
            emit(new Assem.MOVE("movq %`s0, %`d0\n", t1.temp, temp));
            return;
        }
        throw new Error();
    }

    @Override
    public void visit(NAME name) {
        this.temp = new Temp();
        emit(new Assem.MOVE("movq " + name.label + ", %`d0\n", temp, null));
    }

    @Override
    public void visit(SEQ seq) {
        seq.left.accept(this);
        seq.right.accept(this);
    }

    @Override
    public void visit(TEMP op) {
        this.temp = op.temp;
    }

    @Override
    public void visit(CJUMP cjump) {
        cjump.left.accept(this);
        var leftTemp = temp;
        cjump.right.accept(this);
        var rightTemp = temp;
        switch(cjump.relop) {
            case CJUMP.EQ:
            emit(new OPER("cmp `s0, `d0\n", L(leftTemp, null), L(rightTemp, null), null));
            break;
            case CJUMP.GE:
            break;
            case CJUMP.GT:
            break;
            case CJUMP.LE:
            break;
            case CJUMP.LT:
            break;
            case CJUMP.NE:
            break;
            case CJUMP.UGE:
            break;
            case CJUMP.UGT:
            break;
            case CJUMP.ULE:
            break;
            case CJUMP.ULT:
            break;
        }
        emit(new OPER("jmp `j0\n", null, null, new LabelList(cjump.iffalse, null)));
        // emit(new OPER("jmpy `j0\n", null, null, null));
    }
}