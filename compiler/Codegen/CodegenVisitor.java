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
                emit(new Assem.MOVE("movl %`s0, %`d0", finalPos, argTemp));
            } else {
                emit(new Assem.MOVE("movl %`s0, " + ((i - 5) * frame.wordSize()) + "(%`d0)", IntelFrame.rsp, argTemp));
            }
            if (args.tail == null) {
                return L(argTemp, null);
            }
            return L(argTemp, munchArgs(i + 1, args.tail));
        }

        
        public CodegenVisitor(Frame frame) {
            this.frame = frame;
        }

        @Override
        public void visit(BINOP op) {
            // Order of visitation is important.
            // If reversed, the wrong temp is used
            // in the following instructiom 
            // ( set test arraySet.tig )
            // The right temp is the item that
            // contains our result, hence it is
            // evaluated last.
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
                    emit(new Assem.MOVE("movl %`s0, %`d0", IntelFrame.rax, leftTemp));
                    emit(new OPER("div %`s0", L(IntelFrame.rax, L(IntelFrame.rdx, null)), L(rightTemp, L(IntelFrame.rax, null))));
                    emit(new Assem.MOVE("movl %`s0, %`d0", rightTemp, IntelFrame.rax));
                    break;
                case BINOP.LSHIFT:
                    break;
                case BINOP.MINUS:
                    emit(new OPER("sub %`s0, %`d0", L(leftTemp, null), L(rightTemp, L(leftTemp, null))));
                    break;
                case BINOP.MUL:
                    //multiply rax by value in right temp, place value in rax:rdx
                    emit(new Assem.MOVE("movl %`s0, %`d0", IntelFrame.rax, leftTemp));
                    emit(new OPER("mul %`s0", L(IntelFrame.rax, L(IntelFrame.rdx, null)), L(rightTemp, L(IntelFrame.rax, null))));
                    emit(new Assem.MOVE("movl %`s0, %`d0", rightTemp, IntelFrame.rax));
                    break;
                case BINOP.OR:
                    emit(new OPER("or %`s0, %`d0", L(leftTemp, null), L(rightTemp, L(leftTemp, null))));
                    break;
                case BINOP.PLUS:
                    emit(new OPER("add %`s0, %`d0", L(leftTemp, null), L(rightTemp, L(leftTemp, null))));
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
            temp = IntelFrame.rax; //ensures rax is used by the parent instuction.
            emit(new OPER("call " + name.label + " # default call",  calldefs, l));
        }

        @Override
        public void visit(ESEQ op) {
            throw new Error("Not implemented.");
        }

        @Override
        public void visit(EXP exp) {
            TilePatternMatcher tilePatternMatcher = new TilePatternMatcher(exp);
            if(tilePatternMatcher.isMatch(TilePatterns.EXP_CALL)) {
                CALL call = (CALL)tilePatternMatcher.getCapture("call");
                var name = (NAME) call.func;
                TempList l = munchArgs(0, call.args);
                emit(new OPER("call " + name.label + " # exp call ( no return value )", calldefs, l));
            } else {
                exp.exp.accept(this);
                var expTemp = temp;
                temp = Temp.create();
                emit(new Assem.MOVE("movl %`s0, %`d0 # default exp", temp, expTemp));
            }
        }

        @Override
        public void visit(MEM op) {
            op.exp.accept(this);
            var mem = temp;
            temp = Temp.create();
            emit(new Assem.MOVE("movl %`s0, (%`d0) # default load", temp, mem));
        }
    
        @Override
        public void visit(MOVE op) {
            TilePatternMatcher tilePatternMatcher = new TilePatternMatcher(op);
            // notice that store to memory operations only use and dont define variables.
            if (tilePatternMatcher.isMatch(TilePatterns.STORE_ARRAY)) {
                Exp dst = (Exp) tilePatternMatcher.getCapture("base");
                dst.accept(this);
                Temp dstTemp = temp;
                Exp srcExp = (Exp) tilePatternMatcher.getCapture("src");
                srcExp.accept(this);
                Temp srcTemp = temp;
                Exp indexExp = (Exp) tilePatternMatcher.getCapture("index");
                indexExp.accept(this);
                Temp indexTemp = temp;
                int wordSize = (Integer) tilePatternMatcher.getCapture("wordSize");
                emit(new Assem.OPER("movl $`s0, (%`s1, %`s2, " + wordSize +") # store array", 
                        null, 
                        new TempList(srcTemp, new TempList(indexTemp, new TempList(dstTemp)))
                        ));
                return;
            }
            if (tilePatternMatcher.isMatch(TilePatterns.STORE)) {
                Exp dst = (Exp) tilePatternMatcher.getCapture("dst");
                dst.accept(this);
                Temp dstTemp = this.temp;
                Exp src = (Exp) tilePatternMatcher.getCapture("src");
                src.accept(this);
                Temp srcTemp = this.temp;
                emit(new Assem.OPER("movl %`s0, (%`s1) # store", null, new TempList(srcTemp, new TempList(dstTemp))));
                return;
            } 

            // Unmatched tile case.
            op.dst.accept(this);
            var dst = temp;
            op.src.accept(this);
            var src = temp;
            emit(new Assem.MOVE("movl %`s0, %`d0 # default move", dst, src));

        }

        @Override
        public void visit(SEQ seq) {
            seq.left.accept(this);
            seq.right.accept(this);
        }

        @Override
        public void visit(CJUMP cjump) {
            cjump.left.accept(this);
            var leftTemp = temp;
            cjump.right.accept(this);
            var rightTemp = temp;
            emit(new OPER("cmp %`s0, %`s1", null, L(rightTemp, L(leftTemp, null))));
            String op = "";
            switch(cjump.relop) {
                case CJUMP.EQ:
                    op = "je";
                break;
                case CJUMP.GE:
                    op = "jge";
                break;
                case CJUMP.GT:
                    op = "jg";
                break;
                case CJUMP.LE:
                    op = "jle";
                break;
                case CJUMP.LT:
                    op = "jl";
                break;
                case CJUMP.NE:
                    op = "jne";
                break;
                case CJUMP.UGE:
                    op = "jae";
                break;
                case CJUMP.UGT:
                    op = "ja";
                break;
                case CJUMP.ULE:
                    op = "jbe";
                break;
                case CJUMP.ULT:
                    op = "jb";
                break;
            }
            emit(new OPER(op + " `j0", null, null, new LabelList(cjump.iftrue, new LabelList(cjump.iffalse, null))));
        }

        @Override
        public void visit(CONST cnst) {
            temp = Temp.create();
            emit(new OPER("movl $" + cnst.value + ", %`d0 # const", L(temp, null), null));
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
        public void visit(NAME op) {
            temp = Temp.create();
            emit(new Assem.OPER("movl $" + op.label + ", %`d0 # default name", L(temp, null), null));
        }

        @Override
        public void visit(TEMP op) {
            this.temp = op.temp;
        }
    }