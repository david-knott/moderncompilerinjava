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
import Tree.Exp;
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

    private TreePatternList tpl;

    Assem.InstrList iList = null, last = null;
    private Temp temp;
    private Frame frame;

    /**
     * A linked list of temporaries that are used in liveness
     * analysis to flag caller saved registers ( registers that caller must manage )
     * as being destination nodes or defs. This means they are defined at the point 
     * the callee is invoked, this in turn means they are live across the function call.
     * Which means they will interfere with all other temporaries within that function.
     */
    private TempList calldefs = new TempList(IntelFrame.rax, IntelFrame.callerSaves);

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
            emit(new Assem.MOVE("movq %`s0, %`d0", finalPos, argTemp));
        } else {
            emit(new Assem.MOVE("movq %`s0, " + ((i - 5) * frame.wordSize()) + "(%`d0)", IntelFrame.rsp, argTemp));
        }
        if (args.tail == null) {
            return L(argTemp, null);
        }
        return L(argTemp, munchArgs(i + 1, args.tail));
    }

    private void registerMoveTreePatterns(){
        var tb = new TreePatternBuilder();
        tpl.add(
            tb.addRoot(
                new MoveNode("move")
            ).addChild(
                new MemNode("m1")
            ).addChild(
                new BinopNode("b1", x -> {return x.binop == BINOP.PLUS;})
            ).addChild(
                new TempNode("temp")
            ).addSibling(
                new ConstNode("c1")
            ).getParent().getParent().getParent().addChild(
                new ConstNode("c2")
            )
            .build(), treePattern -> {
                var temp = (TEMP)treePattern.getNamedMatch("temp");
                var cnst = (CONST)treePattern.getNamedMatch("c1");
                var cnst2 = (CONST)treePattern.getNamedMatch("c2");
                //special move where there is no src register
                emit(new Assem.OPER("movq %" + cnst2.value + ", " + cnst.value + "(%`d0)", L(temp.temp, null), null));
            }
        );
        tpl.add(
            tb.addRoot(
                new MoveNode("move")
            ).addChild(
                new TempNode("temp1")
            ).getParent().addChild(
                new MemNode("m1")
            ).addChild(
                new BinopNode("b1", x -> {return x.binop == BINOP.PLUS;})
            ).addChild(
                new TempNode("temp2")
            ).addSibling(
                new ConstNode("c1")
            )
            .build(), treePattern -> {
                var temp1 = (TEMP)treePattern.getNamedMatch("temp1");
                var temp2 = (TEMP)treePattern.getNamedMatch("temp2");
                var cnst = (CONST)treePattern.getNamedMatch("c1");
                emit(new Assem.MOVE("movq " + cnst.value + "(%`s0), %`d0", temp1.temp, temp2.temp));
            }
        );
    }

    private void registerMemTreePatterns(){
        var tb = new TreePatternBuilder();
        tpl.add(
            tb.addRoot(
                new MemNode("mem")
            ).addChild(
                new BinopNode("b1", x -> {return x.binop == BINOP.PLUS;})
            ).addChild(
                new ConstNode("cnst")
            ).addSibling(
                new ExpNode("exp")
            )
            .build(), treePattern -> {
                var exp = (Exp)treePattern.getNamedMatch("exp");
                var cnst = (CONST)treePattern.getNamedMatch("cnst");
                exp.accept(this);
                var src = temp;
                temp = new Temp();
                var dst = temp;
                emit(new Assem.MOVE("movq " + cnst.value + "(%`s0), %`d0", 
                    dst, src));
            }
        );
        tpl.add(
            tb.addRoot(
                new MemNode("mem")
            ).addChild(
                new BinopNode("b1", x -> {return x.binop == BINOP.PLUS;})
            ).addChild(
                new ExpNode("exp")
            ).addSibling(
                new ConstNode("cnst")
            )
            .build(), treePattern -> {
                var exp = (Exp)treePattern.getNamedMatch("exp");
                var cnst = (CONST)treePattern.getNamedMatch("cnst");
                exp.accept(this);
                var src = temp;
                temp = new Temp();
                var dst = temp;
                emit(new Assem.MOVE("movq " + cnst.value + "(%`s0), %`d0", 
                    dst, src));
            }
        );
    }
     
    private void registerBinopTreePatterns(){
        var tb = new TreePatternBuilder();
        /*
        tpl.add(
            tb.addRoot(
                new BinopNode("b1", x -> {return x.binop == BINOP.PLUS;})
            ).addChild(
                new ConstNode("c1")
            ).addSibling(
                new ExpNode("exp")
            )
            .build(), treePattern -> {
                var cnst1 = (CONST)treePattern.getNamedMatch("c1");
                var exp = (Exp)treePattern.getNamedMatch("exp");
                exp.accept(this);
                var expR = temp;
                emit(new Assem.OPER("add $" + cnst1.value + ", %`d0\t;add literal\n", L(expR, null), L(expR, null)));
            }
        );
        */
        /*
        tpl.add(
            tb.addRoot(
                new BinopNode("b1", x -> {return x.binop == BINOP.PLUS;})
            ).addChild(
                new MemNode("m1")
            ).addChild(
                new BinopNode("b2", x -> {return x.binop == BINOP.PLUS;})
            ).addChild(
                new ConstNode("c1")
            ).addSibling(
                new ExpNode("exp")
            ).getParent().getParent().getParent().addChild(
                new ConstNode("c2")
            )
            .build(), treePattern -> {
                var cnst1 = (CONST)treePattern.getNamedMatch("c1");
                var cnst2 = (CONST)treePattern.getNamedMatch("c2");
                var exp = (Exp)treePattern.getNamedMatch("exp");
                exp.accept(this);
                var expR = temp;
                emit(new Assem.OPER("add $" + cnst1.value + " " + cnst2.value + ", %`d0\t;add memory offset\n", L(expR, null), L(expR, null)));
            }
        );
        */
    }

    private void registerExpTreePatterns(){
        var tb = new TreePatternBuilder();
        //handle call
        tpl.add(
            tb.addRoot(
                new EXPNode("exp")
            ).addChild(
                new CallNode("call1")   
            ).build(), treePattern -> {
            var call = (CALL)treePattern.getNamedMatch("call1");
            call.accept(this);
        });
    }
    
    public CodegenVisitor(Frame frame) {
        this.frame = frame;
        tpl = new TreePatternList();
        registerExpTreePatterns();
        registerMoveTreePatterns();
        registerMemTreePatterns();
        registerBinopTreePatterns();
    }

    @Override
    public void visit(BINOP op) {
        if(tpl.match(op)) {
            return;
        }
        op.left.accept(this);
        var leftTemp = temp;
        op.right.accept(this);
        var rightTemp = temp;
        switch (op.binop) {
            case BINOP.AND:
                emit(new OPER("and %`s0, %`d0", L(leftTemp, null), L(rightTemp, L(leftTemp, null))));
                break;
            case BINOP.ARSHIFT:
                break;
            case BINOP.DIV:
                emit(new Assem.MOVE("movq %`s0, %`d0", IntelFrame.rax, leftTemp));
                emit(new OPER("div  %`s0", L(IntelFrame.rax, L(IntelFrame.rdx, null)), L(rightTemp, null) ));
                emit(new Assem.MOVE("movq %`s0, %`d0", rightTemp, IntelFrame.rax));
                break;
            case BINOP.LSHIFT:
                break;
            case BINOP.MINUS:
                emit(new OPER("sub %`s0 %`d0", L(rightTemp, null), L(leftTemp, L(rightTemp, null))));
                break;
            case BINOP.MUL:
                emit(new OPER(";comment\n", null, null));
                emit(new Assem.MOVE("movq %`s0, %`d0", IntelFrame.rax, leftTemp));
                emit(new OPER("mul %`s0", L(IntelFrame.rax, L(IntelFrame.rdx, null)), L(rightTemp, L(IntelFrame.rax, null))));
                emit(new Assem.MOVE("movq %`s0, %`d0", rightTemp, IntelFrame.rax));
                break;
            case BINOP.OR:
                emit(new OPER("or %`s0, %`d0", L(leftTemp, null), L(rightTemp, L(leftTemp, null))));
                break;
            case BINOP.PLUS:
                emit(new OPER("add %`s0 %`d0", L(rightTemp, null), L(leftTemp, L(rightTemp, null))));
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
        temp = IntelFrame.rax;
        emit(new OPER("call " + name.label,  calldefs, l));
    }

    @Override
    public void visit(CONST cnst) {
        temp = new Temp();
        emit(new OPER("movq $" + cnst.value + ", %`d0", L(temp, null), null));
    }

    @Override
    public void visit(ESEQ op) {
        throw new Error("Not implemented.");
    }

    @Override
    public void visit(EXP exp) {
        if(!tpl.match(exp)) {
            exp.exp.accept(this);
            var expTemp = temp;
            temp = new Temp();
            emit(new Assem.MOVE("movq %`s0, %`d0", temp, expTemp));
        }
    }

    @Override
    public void visit(JUMP op) {
        emit(new OPER("jmp `j0", null, null, op.targets));
    }

    @Override
    public void visit(LABEL op) {
        emit(new Assem.LABEL(op.label.toString() + ":", op.label));
    }

    @Override
    public void visit(MEM op) {
        if(!tpl.match(op)) {
            op.exp.accept(this);
            var mem = temp;
            temp = new Temp();
            emit(new Assem.MOVE("movq (%`s0), %`d0", temp, mem));
        }
    }

    @Override
    public void visit(MOVE op) {
        if(!tpl.match(op)) {
            op.dst.accept(this);
            var mem = temp;
            op.src.accept(this);
            var exp = temp;
            emit(new Assem.MOVE("movq %`s0, %`d0", mem, exp));
        }
    }

    @Override
    public void visit(NAME op) {
        temp = new Temp();
        emit(new Assem.OPER("movq " + op.label + ", %`d0", L(temp, null), null));
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
        emit(new OPER("cmp %`s0, %`s1", null, L(leftTemp, L(rightTemp, null))));
        switch(cjump.relop) {
            case CJUMP.EQ:
                emit(new OPER("je `j0", null, null, new LabelList(cjump.iftrue, new LabelList(cjump.iffalse, null))));
            break;
            case CJUMP.GE:
                emit(new OPER("jge `j0", null, null, new LabelList(cjump.iftrue, new LabelList(cjump.iffalse, null))));
            break;
            case CJUMP.GT:
                emit(new OPER("jg `j0", null, null, new LabelList(cjump.iftrue, new LabelList(cjump.iffalse, null))));
            break;
            case CJUMP.LE:
                emit(new OPER("jle `j0", null, null, new LabelList(cjump.iftrue, new LabelList(cjump.iffalse, null))));
            break;
            case CJUMP.LT:
                emit(new OPER("jl `j0", null, null, new LabelList(cjump.iftrue, new LabelList(cjump.iffalse, null))));
            break;
            case CJUMP.NE:
                emit(new OPER("jne `j0", null, null, new LabelList(cjump.iftrue, new LabelList(cjump.iffalse, null))));
            break;
            case CJUMP.UGE:
                emit(new OPER("jae `j0", null, null, new LabelList(cjump.iftrue, new LabelList(cjump.iffalse, null))));
            break;
            case CJUMP.UGT:
                emit(new OPER("ja `j0", null, null, new LabelList(cjump.iftrue, new LabelList(cjump.iffalse, null))));
            break;
            case CJUMP.ULE:
                emit(new OPER("jbe `j0", null, null, new LabelList(cjump.iftrue, new LabelList(cjump.iffalse, null))));
            break;
            case CJUMP.ULT:
                emit(new OPER("jb `j0", null, null, new LabelList(cjump.iftrue, new LabelList(cjump.iffalse, null))));
            break;
        }
    }
}