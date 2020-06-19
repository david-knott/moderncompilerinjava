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

    class MaximumMunch implements TreeVisitor {

        Assem.InstrList iList = null, last = null;
        private Temp temp;
        private Frame frame;

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
            TempList tl = new TempList(argTemp);
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
                emit(new Assem.MOVE("movq %`s0, %`d0 # move arg " + i + " to temp", finalPos, argTemp));
               tl = TempList.append(tl, finalPos);
            } else {
                emit(new Assem.OPER("pushq %`s1 # move arg " + i + " to stack", null, L(IntelFrame.rsp, L(argTemp, null))));
            }
            if (args.tail == null) {
              // return L(argTemp, null);
               return tl;
            }
           // return L(argTemp, munchArgs(i + 1, args.tail));
            return TempList.append(tl,munchArgs(i - 1, args.tail));
        }

        
        public MaximumMunch(Frame frame) {
            this.frame = frame;
        }

        @Override
        public void visit(BINOP op) {
            op.left.accept(this);
            var leftTemp = temp;
            Validator.assertNotNull(leftTemp);
            op.right.accept(this);
            var rightTemp = temp;
            Validator.assertNotNull(rightTemp);
            switch (op.binop) {
                case BINOP.AND:
                    this.temp = Temp.create();
                    emit(new Assem.MOVE("movq %`s0, %`d0 # and lexp -> r", this.temp, leftTemp));
                    emit(new OPER("and %`s0, %`d0", L(this.temp, null), L(rightTemp, L(this.temp, null))));
                    break;
                case BINOP.ARSHIFT:
                    break;
                case BINOP.DIV:
                    this.temp = Temp.create();
                    emit(new Assem.MOVE("movq %`s0, %`d0 # div lexp -> r", this.temp, leftTemp));
                    emit(new Assem.MOVE("movq %`s0, %`d0 # div r -> rax", IntelFrame.rax, this.temp));
                    emit(new OPER("xor %`s0, %`d0 # div clear bits rdx ", L(IntelFrame.rdx, null), L(IntelFrame.rdx, null)));
                    emit(new OPER("idiv %`s0 # div rax * rexp ", L(IntelFrame.rax, L(IntelFrame.rdx, null)), L(rightTemp, L(IntelFrame.rax, null))));
                    emit(new Assem.MOVE("movq %`s0, %`d0 # div rax -> r", this.temp, IntelFrame.rax));
                    break;
                case BINOP.LSHIFT:
                    break;
                case BINOP.MINUS:
                    this.temp = Temp.create();
                    emit(new Assem.MOVE("movq %`s0, %`d0 # minus lexp -> r", this.temp, leftTemp));
                    emit(new OPER("sub %`s0, %`d0", L(this.temp, null), L(rightTemp, L(this.temp, null))));
                    break;
                case BINOP.MUL:
                    this.temp = Temp.create();
                    emit(new Assem.MOVE("movq %`s0, %`d0 # mul lexp -> r", this.temp, leftTemp));
                    emit(new Assem.MOVE("movq %`s0, %`d0 # mul r -> rax", IntelFrame.rax, this.temp));
                    emit(new OPER("imul %`s0 # mul rax * rexp ", L(IntelFrame.rax, L(IntelFrame.rdx, null)), L(rightTemp, L(IntelFrame.rax, null))));
                    emit(new Assem.MOVE("movq %`s0, %`d0 # mul rax -> r", this.temp, IntelFrame.rax));
                    break;
                case BINOP.OR:
                    this.temp = Temp.create();
                    emit(new Assem.MOVE("movq %`s0, %`d0 # or lexp -> r", this.temp, leftTemp));
                    emit(new OPER("or %`s0, %`d0", L(this.temp, null), L(rightTemp, L(this.temp, null))));
                    break;
                case BINOP.PLUS:
                    this.temp = Temp.create();
                    emit(new Assem.MOVE("movq %`s0, %`d0 # add lexp -> r", this.temp, leftTemp));
                    emit(new OPER("add %`s0, %`d0", L(this.temp, null), L(rightTemp, L(this.temp, null))));
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
            TempList l = munchArgs(ExpList.size(call.args) - 1, ExpList.reverse(call.args));
            temp = IntelFrame.rax; //ensures rax is used by the parent instuction.
            emit(new OPER("call " + name.label + " # default call",  L(temp, IntelFrame.callerSaves), l));
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
                TempList l = munchArgs(ExpList.size(call.args) - 1, ExpList.reverse(call.args));
                emit(new OPER("call " + name.label + " # exp call ( no return value )", L(frame.RV(), IntelFrame.callerSaves), l));
               // emit(new Assem.MOVE("movq %`s0, %`d0 # move rax -> rax", frame.RV(), frame.RV()));
            } else {
                exp.exp.accept(this);
                var expTemp = temp;
                temp = Temp.create();
                emit(new Assem.MOVE("movq %`s0, %`d0 # default exp", temp, expTemp));
            }
        }

        @Override
        public void visit(MEM op) {
            op.exp.accept(this);
            var mem = temp;
            Validator.assertNotNull(mem);
            temp = Temp.create();
            emit(new Assem.OPER("movq (%`s0), %`d0 # default load", new TempList(temp), new TempList(mem)));
        }
    
        @Override
        public void visit(MOVE op) {
            TilePatternMatcher tilePatternMatcher = new TilePatternMatcher(op);
            if (tilePatternMatcher.isMatch(TilePatterns.LOAD_ARRAY)) {
                Exp src = (Exp) tilePatternMatcher.getCapture("base");
                src.accept(this);
                Temp srcTemp = temp;
                Exp dstExp = (Exp) tilePatternMatcher.getCapture("dst");
                dstExp.accept(this);
                Temp dstTemp = temp;
                Exp indexExp = (Exp) tilePatternMatcher.getCapture("index");
                indexExp.accept(this);
                Temp indexTemp = temp;
                int wordSize = (Integer) tilePatternMatcher.getCapture("wordSize");
                emit(new Assem.OPER("movq (%`s0, %`s1, " + wordSize +"), %`d0 # load array", 
                        new TempList(dstTemp), 
                        new TempList(indexTemp, new TempList(srcTemp))
                        ));
                return;
            }
            // notice that store to memory operations only use and dont define variables.
            /*
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
                emit(new Assem.OPER("movq %`s0, (%`s1, %`s2, " + 8 +") # store array", 
                        null, 
                        new TempList(srcTemp, new TempList(dstTemp, new TempList(indexTemp)))
                        ));
                return;
            }*/
            if (tilePatternMatcher.isMatch(TilePatterns.LOAD_2)) {
                int offset = (Integer) tilePatternMatcher.getCapture("offset");
                Exp dst = (Exp) tilePatternMatcher.getCapture("dst");
                dst.accept(this);
                Temp dstTemp = this.temp;
                Exp src = (Exp) tilePatternMatcher.getCapture("src");
                src.accept(this);
                Temp srcTemp = this.temp;
                emit(new Assem.OPER("movq " + offset + "(%`s0), %`d0 # load to offset", new TempList(dstTemp), new TempList(srcTemp)));
                return;
            }
            if (tilePatternMatcher.isMatch(TilePatterns.STORE_2)) {
                int offset = (Integer) tilePatternMatcher.getCapture("offset");
                Exp dst = (Exp) tilePatternMatcher.getCapture("dst");
                dst.accept(this);
                Temp dstTemp = this.temp;
                Exp src = (Exp) tilePatternMatcher.getCapture("src");
                src.accept(this);
                Temp srcTemp = this.temp;
                emit(new Assem.OPER("movq %`s0, " + offset + "(%`s1) # store to offset", null, new TempList(srcTemp, new TempList(dstTemp))));
                return;
            }
            if (tilePatternMatcher.isMatch(TilePatterns.STORE)) {
                Exp dst = (Exp) tilePatternMatcher.getCapture("dst");
                dst.accept(this);
                Temp dstTemp = this.temp;
                Exp src = (Exp) tilePatternMatcher.getCapture("src");
                src.accept(this);
                Temp srcTemp = this.temp;
                emit(new Assem.OPER("movq %`s0, (%`s1) # store", null, new TempList(srcTemp, new TempList(dstTemp))));
                return;
            } 
            
            if (tilePatternMatcher.isMatch(TilePatterns.MOVE_CALL)) {
                Exp dst = (Exp) tilePatternMatcher.getCapture("dst");
                dst.accept(this);
                Temp dstTemp = this.temp;
                CALL call = (CALL) tilePatternMatcher.getCapture("call");
                TempList l = munchArgs(ExpList.size(call.args) - 1, ExpList.reverse(call.args));
                this.temp = IntelFrame.rax; //ensures rax is used by the parent instuction.
                emit(new OPER("call " + ((NAME)call.func).label +  " # move call",  L(temp, IntelFrame.callerSaves), l));
                emit(new Assem.MOVE("movq %`s0, %`d0 # rax to temp ", dstTemp, IntelFrame.rax));
                return;
            } 


            // Unmatched tile case.
            op.dst.accept(this);
            var dst = temp;
            op.src.accept(this);
            var src = temp;
            emit(new Assem.MOVE("movq %`s0, %`d0 # default move", dst, src));

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
            Validator.assertNotNull(leftTemp);
            cjump.right.accept(this);
            var rightTemp = temp;
            Validator.assertNotNull(rightTemp);
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
            emit(new OPER("movq $" + cnst.value + ", %`d0 # const", L(temp, null), null));
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
            emit(new Assem.OPER("movq $" + op.label + ", %`d0 # default name", L(temp, null), null));
        }

        @Override
        public void visit(TEMP op) {
            this.temp = op.temp;
        }
    }