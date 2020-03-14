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

    private void registerTreePatterns(){
        var tb = new TreePatternBuilder();
        /*
        tpl.add(
            tb.addRoot(
                new ExpNode()
            ).addChild(
                new CallNode("call1")   
            ).build(), treePattern -> {
                System.out.println("ggs");
            //emit(new Assem.MOVE("movq `s0, (%`d0)\t;\n", dstTemp, srcTemp));
        });
        tpl.add(
            tb.addRoot(
                new MoveNode("move")
            ).build(), treePattern -> {
            var move = (MOVE)treePattern.getNamedMatch("move");
            move.src.accept(this);
            var srcTemp = temp;
            move.dst.accept(this);
            var dstTemp = temp;
            emit(new Assem.MOVE("movq `s0, (%`d0)\t;\n", dstTemp, srcTemp));
        });

        tpl.add(
            tb.addRoot(
                new MoveNode("move")
            ).addChild(
                new TempNode("temp")
            ).build(), treePattern -> {
            var move = (MOVE)treePattern.getNamedMatch("move");
            var tmp = (MOVE)treePattern.getNamedMatch("tmp");
            //emit move instruction
        });
*/
        tpl.add(
            tb.addRoot(
                new MoveNode("move")
            ).addChild(
                new TempNode("t1")
            ).getParent().addChild(
                new MemNode("m1")
            ).addChild(
                new BinopNode("b1", x -> {return x.binop == 1;})
            ).addChild(
                new TempNode("t2")
            ).addSibling(
                new ExpNode("exp")
            )
            .build(), treePattern -> {
            //emit move instruction
            var temp1 = (TEMP)treePattern.getNamedMatch("t1");
            var temp2 = (TEMP)treePattern.getNamedMatch("t2");
            var const1 = (CONST)treePattern.getNamedMatch("c1");
            emit(new Assem.MOVE("movq " + const1.value + "(`s0), %`d0\t;\n", temp1.temp, temp2.temp));
        }
    );

/*

        var tt = tb.addRoot(new TempNode("temp")).build();
        tpl.add(tt, treePattern -> {
            var temp = (TEMP)treePattern.getNamedMatch("temp");
            this.visit(temp);
        });
        */
    }

    public CodegenVisitor2(Frame frame) {
        this.frame = frame;
        tpl = new TreePatternList();
        registerTreePatterns();
    }

    @Override
    public void visit(BINOP op) {
        op.left.accept(this);
        var leftTemp = temp;
        op.right.accept(this);
        var rightTemp = temp;
        switch (op.binop) {
            case BINOP.AND:
                emit(new OPER("and %`s0, %`d0 ; \n", L(leftTemp, null), L(rightTemp, L(leftTemp, null)), null));
                break;
            case BINOP.ARSHIFT:
                break;
            case BINOP.DIV:
                emit(new Assem.MOVE("movq %`s0, %`d0\t; move left into rax \n", leftTemp, IntelFrame.rv));
                emit(new OPER("div  %`s0\t; divide rax by value in right \n", L(IntelFrame.rv, L(IntelFrame.rdx, null)), L(rightTemp, null), null));
                emit(new Assem.MOVE("movq %`s0, %`d0\t; move rax into right\n", rightTemp, IntelFrame.rv));
                break;
            case BINOP.LSHIFT:
                break;
            case BINOP.MINUS:
                emit(new OPER("sub %`s0 %`d0 \n", L(rightTemp, null), L(leftTemp, L(rightTemp, null)), null));
                break;
            case BINOP.MUL:
                emit(new Assem.MOVE("movq %`s0, %`d0\t; move left into rax\n", leftTemp, IntelFrame.rv));
                emit(new OPER("mul %`s0\t; multiple rax by value in right; \n", L(IntelFrame.rv, L(IntelFrame.rdx, null)), L(rightTemp, L(IntelFrame.rv, null)), null));
                emit(new Assem.MOVE("movq %`s0, %`d0\t; move rax into right\n", rightTemp, IntelFrame.rv));
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
        throw new Error();
    }

    @Override
    public void visit(EXP exp) {
        exp.exp.accept(this);
    }

    @Override
    public void visit(JUMP op) {
        emit(new OPER("jmp `j0\n", null, null, op.targets));

    }

    @Override
    public void visit(LABEL op) {
        emit(new Assem.LABEL(op.label.toString() + ":\n", op.label));
    }

    @Override
    public void visit(MEM op) {
        if(!tpl.match(op)) {
            op.exp.accept(this);
            var mem = temp;
            temp = new Temp();
            emit(new Assem.MOVE("movq `s0, %`d0\t;\n", mem, temp));
        }
    }

    @Override
    public void visit(MOVE op) {
        if(!tpl.match(op)) {
            op.dst.accept(this);
            var mem = temp;
            op.src.accept(this);
            var exp = temp;
            emit(new Assem.MOVE("movq `s0, %`d0\t;\n", mem, exp));
        }
    }

    @Override
    public void visit(NAME op) {
        throw new Error("Name should not be called directly");

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