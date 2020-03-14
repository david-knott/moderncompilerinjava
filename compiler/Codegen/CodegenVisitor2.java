package Codegen;

import Assem.Instr;
import Frame.Frame;
import Intel.IntelFrame;
import Temp.Temp;
import Temp.LabelList;
import Temp.TempList;
import Tree.BINOP;
import Assem.OPER;
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

class CodegenVisitor2 implements TreeVisitor {

    private TreePatternList tpl;

    Assem.InstrList iList = null, last = null;
    private Temp temp;
    private Frame frame;
    private TempList calldefs = new TempList(IntelFrame.rv, IntelFrame.callerSaves);

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

    public CodegenVisitor2(Frame frame) {
        this.frame = frame;
        var tb = new TreePatternBuilder();
        var tp = tb.addRoot(new MoveNode("move")).build();
        tpl = new TreePatternList();
        tpl.add(tp, treePattern -> {
            var move = (MOVE)treePattern.getNamedMatch("move");
            //emit move instruction
            this.visit(move);
        });
        var tt = tb.addRoot(new TempNode("temp")).build();
        tpl.add(tt, treePattern -> {
            var temp = (TEMP)treePattern.getNamedMatch("temp");
            this.visit(temp);
        });
    }

    @Override
    public void visit(BINOP op) {
    }

    @Override
    public void visit(CALL op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CONST op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ESEQ op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(EXP op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(JUMP op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(LABEL op) {
        emit(new Assem.LABEL(op.label.toString() + ":\n", op.label));
    }

    @Override
    public void visit(MEM op) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(MOVE op) {
        //look for matches and proceed using best match
        tpl.match(op);
    }

    @Override
    public void visit(NAME op) {
        throw new Error("Name should not be called");

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