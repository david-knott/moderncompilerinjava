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
    private TempList calldefs = new TempList(IntelFrame.rax, IntelFrame.callerSaves);

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
        if(args == null)
         return null;
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
            case 4:
                finalPos = IntelFrame.r8;
                break;
            case 5:
                finalPos = IntelFrame.r9;
                break;
            default:
                // item is pushed onto the stack
                break;
        }
        if (finalPos != null) {
            emit(new Assem.MOVE("movq %`s0, %`d0\n", finalPos, argTemp));
        } else {
            //0, 7, 8, 9... => sp + 0, sp + 8, sp + 16
            emit(new Assem.MOVE("movq %`s0, " + (i * frame.wordSize()) + "(%`d0)\t; " + i + " argument\n", IntelFrame.sp, argTemp));
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
        var rightTemp = temp;
        switch (op.binop) {
            case BINOP.AND:
                emit(new OPER("and %`s0, %`d0 ; \n", L(leftTemp, null), L(rightTemp, null), null));
                break;
            case BINOP.ARSHIFT:
                break;
            case BINOP.DIV:
                emit(new Assem.MOVE("movq %`s0, %`d0\t; move left into rax \n", leftTemp, IntelFrame.rax));
                emit(new OPER("div  %`s0\t; divide rax by value in right \n", L(IntelFrame.rax, L(IntelFrame.rdx, null)), L(rightTemp, null), null));
                emit(new Assem.MOVE("movq %`s0, %`d0\t; move rax into right\n", rightTemp, IntelFrame.rax));
                break;
            case BINOP.LSHIFT:
                break;
            case BINOP.MINUS:
                emit(new OPER("sub %`s0 %`d0 \n", L(rightTemp, null), L(leftTemp, L(rightTemp, null)), null));
                break;
            case BINOP.MUL:
                emit(new Assem.MOVE("movq %`s0, %`d0\t; move left into rax\n", leftTemp, IntelFrame.rax));
                emit(new OPER("mul %`s0\t; multiple rax by value in right; \n", L(IntelFrame.rax, L(IntelFrame.rdx, null)), L(rightTemp, L(IntelFrame.rax, null)), null));
                emit(new Assem.MOVE("movq %`s0, %`d0\t; move rax into right\n", rightTemp, IntelFrame.rax));
                break;
            case BINOP.OR:
                emit(new OPER("or %`s0, %`d0\n", L(leftTemp, null), L(rightTemp, L(leftTemp, null)), null));
                break;
            case BINOP.PLUS:
                emit(new OPER("add %`s0 %`d0 \n", L(rightTemp, null), L(leftTemp, L(rightTemp, null)), null));
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
//        call.func.accept(this);
        var name = (NAME)call.func;
        TempList l = munchArgs(0, call.args);
        emit(new OPER("call " + name.label + "\n", calldefs, l));
    }

    @Override
    public void visit(CONST cnst) {
        temp = new Temp();
        emit(new OPER("movq $" + cnst.value + ", %`d0\t;\n", L(temp, null), null, null));
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

    private void munchMove(TEMP dst, Tree.Exp src){
        //default case
        src.accept(this);
        var mem = temp;
        emit(new Assem.MOVE("movq %`s0, %`d0\t; Exp src -> TEMP dst\n", dst.temp, mem));
    }

    private void munchMove(TEMP dst, MEM src){
        if(src.exp instanceof BINOP){
            var binop = (BINOP)src.exp;
            if(binop.left instanceof CONST && binop.binop == BINOP.PLUS){
                var cons = (CONST)binop.left;
                //process right element
                binop.right.accept(this);
                var right = temp;
                emit(new Assem.MOVE("movq " + cons.value + "(`s0), %`d0\t;\n", dst.temp, right));
                return;
            }
            if(binop.right instanceof CONST && binop.binop == BINOP.PLUS){
                var cons = (CONST)binop.right;
                //process right element
                binop.left.accept(this);
                var left = temp;
                emit(new Assem.MOVE("movq " + cons.value + "(`s0), %`d0\t;\n", dst.temp, left));
                return;
            }
        }
        //default case
        src.accept(this);
        var mem = temp;
        emit(new Assem.MOVE("movq (`s0), %`d0\t;\n", dst.temp, mem));
    }

    private void munchMove(MEM dst, Tree.Exp src){
        if(dst.exp instanceof BINOP){
            var binop = (BINOP)dst.exp;
            if(binop.left instanceof CONST && binop.binop == BINOP.PLUS){
                var cons = (CONST)binop.left;
                //process right element
                binop.right.accept(this);
                var right = temp;
                src.accept(this);
                var exp = temp;
                emit(new Assem.MOVE("movq `s0, " + cons.value + "(%`d0)\t;mem 1\n", right, exp));
                return;
            }
            if(binop.right instanceof CONST && binop.binop == BINOP.PLUS){
                var cons = (CONST)binop.right;
                //process right element
                binop.left.accept(this);
                var left = temp;
                src.accept(this);
                var exp = temp;
                emit(new Assem.MOVE("movq `s0, " + cons.value + "(%`d0)\t;mem 2\n", left, exp));
                return;
            }

        }
        //default case
        dst.accept(this);
        var mem = temp;
        src.accept(this);
        var exp = temp;
        emit(new Assem.MOVE("movq `s0, (%`d0)\t;\n", mem, exp));
    }

    private void munchMove(MEM dst, MEM src){
        if(dst.exp instanceof BINOP && src.exp instanceof BINOP) {
            var dstBinop = (BINOP)dst.exp;
            var srcBinop = (BINOP)src.exp;
            if(dstBinop.left instanceof CONST && dstBinop.binop == BINOP.PLUS 
            && srcBinop.left instanceof CONST && srcBinop.binop == BINOP.PLUS
            ){
                var dstCons = (CONST)dstBinop.left;
                var srcCons = (CONST)srcBinop.left;
                //process right element
                dstBinop.right.accept(this);
                var dstRight = temp;
                srcBinop.right.accept(this);
                var srcRight = temp;
                emit(new Assem.MOVE("movq " + srcCons + "(%`s0), " + dstCons.value + "(%`d0)\t;\n", dstRight, srcRight));
                return;
            }
            if(dstBinop.right instanceof CONST && dstBinop.binop == BINOP.PLUS
            && srcBinop.right instanceof CONST && srcBinop.binop == BINOP.PLUS
            ){
                var dstCons = (CONST)dstBinop.right;
                var srcCons = (CONST)srcBinop.right;
                //process right element
                dstBinop.left.accept(this);
                var dstLeft = temp;
                srcBinop.left.accept(this);
                var srcLeft = temp;
                emit(new Assem.MOVE("movq " + srcCons + "(%`s0), " + dstCons.value + "(%`d0)\t;\n", dstLeft, srcLeft));
                return;
            }
        }
        //default case
        dst.accept(this);
        var mem = temp;
        src.accept(this);
        var exp = temp;
        emit(new Assem.MOVE("movq `s0, (%`d0)\t;\n", mem, exp));

    }

    @Override
    public void visit(MOVE move) {
        if (move.dst instanceof TEMP && move.src instanceof CALL) {
            move.src.accept(this);
            TEMP dstTemp = (TEMP) move.dst;
            // move function temp result into dst temp
            emit(new Assem.MOVE("movq %`s0, %`d0\t;\n", dstTemp.temp, temp));
            return;
        }
        // move src exp to memory exp with offset
        if (move.dst instanceof MEM && move.src instanceof TEMP) {
            munchMove((MEM)move.dst, (TEMP)move.src);
            return;
        }
        if (move.dst instanceof TEMP && move.src instanceof MEM) {
            munchMove((TEMP)move.dst, (MEM)move.src);
            return;
        }
        if (move.dst instanceof MEM && move.src instanceof MEM) {
            munchMove((MEM)move.dst, (MEM)move.src);
            return;
        }
        if (move.dst instanceof MEM) {
            munchMove((MEM)move.dst, move.src);
            return;
        }
        if (move.dst instanceof TEMP) {
            munchMove((TEMP)move.dst, move.src);
            return;
        }
        throw new Error("Unable to handle" + move.dst + " " + move.src);
    }

    @Override
    public void visit(NAME name) {
        //this.temp = new Temp();
        //emit(new Assem.MOVE("movq " + name.label + ", %`d0\t; move label into temp \n", temp, null));
        throw new Error();
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
        emit(new OPER("cmp `s0, `d0\n", L(leftTemp, null), L(rightTemp, null), null));
        switch(cjump.relop) {
            case CJUMP.EQ:
                emit(new OPER("je `j0\n", null, null, new LabelList(cjump.iftrue, null)));
            break;
            case CJUMP.GE:
                emit(new OPER("jge `j0\n", null, null, new LabelList(cjump.iftrue, null)));
            break;
            case CJUMP.GT:
                emit(new OPER("jg `j0\n", null, null, new LabelList(cjump.iftrue, null)));
            break;
            case CJUMP.LE:
                emit(new OPER("jle `j0\n", null, null, new LabelList(cjump.iftrue, null)));
            break;
            case CJUMP.LT:
                emit(new OPER("jl `j0\n", null, null, new LabelList(cjump.iftrue, null)));
            break;
            case CJUMP.NE:
                emit(new OPER("jne `j0\n", null, null, new LabelList(cjump.iftrue, null)));
            break;
            case CJUMP.UGE:
                emit(new OPER("jae `j0\n", null, null, new LabelList(cjump.iftrue, null)));
            break;
            case CJUMP.UGT:
                emit(new OPER("ja `j0\n", null, null, new LabelList(cjump.iftrue, null)));
            break;
            case CJUMP.ULE:
                emit(new OPER("jbe `j0\n", null, null, new LabelList(cjump.iftrue, null)));
            break;
            case CJUMP.ULT:
                emit(new OPER("jb `j0\n", null, null, new LabelList(cjump.iftrue, null)));
            break;
        }
    }
}