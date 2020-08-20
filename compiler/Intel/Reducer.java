package Intel;

import java.util.Collections;
import java.util.Vector;

import Assem.Instr;
import Assem.OPER;
import Temp.LabelList;
import Temp.Temp;
import Temp.TempList;
import Tree.BINOP;
import Tree.CALL;
import Tree.CJUMP;
import Tree.IR;
import Tree.JUMP;
import Tree.LABEL;
import Tree.NAME;

public class Reducer {

	final Emitter emitter;
	Assem.InstrList iList = null, last = null;

	private  static TempList L(Temp h, TempList t) {
		return new TempList(h, t);
	}

	private void emit(Instr instr) {
		if (last != null) {
			last = last.tail = new Assem.InstrList(instr, null);
		} else {
			last = iList = new Assem.InstrList(instr, null);
		}
	}

    public Reducer(Emitter emitter) {
		this.emitter = emitter;
	}

	public Temp temp(IR __p) {
		return ((Tree.TEMP)__p).temp;
	}

	public Integer integerConstant(IR __p) {
		return ((Tree.CONST)__p).value;
	}

	public BinopOffsetExpression binopOffsetExpression(IR __p, Temp base, Integer offset) {
		return new BinopOffsetExpression((BINOP)__p, base, offset);
	}

	public IndirectExpression indirect(IR __p, Temp arg) {
		return new IndirectExpression(arg);
	}

	public IndirectWithDisplacementExpression indirectWithDisplacement(IR __p, BinopOffsetExpression arg0) {
		return new IndirectWithDisplacementExpression(arg0);
	}

	public IndirectWithDisplacementAndScaleExpression indirectWithDisplacementAndScale(IR __p, Temp arg0,
			BinopOffsetExpression arg1) {
		return new IndirectWithDisplacementAndScaleExpression(arg0, arg1);
	}

	public IR loadindirectWithDisplacement(IR __p, Temp dst, IndirectWithDisplacementExpression arg1) {
		emit(
			new Assem.OPER("movq " + arg1.displacement() + "(%`s0), %`d0 # load to offset", 
				new TempList(dst), 
				new TempList(arg1.temp())
			)
		);
		return null;
	}

	public IR loadindirectWithDisplacementAndScale(IR __p, Temp dst,
			IndirectWithDisplacementAndScaleExpression src) {
		//TODO : Check the binop operator !
		emit(new Assem.OPER("movq (%`s0, %`s1, " + src.wordSize() +"), %`d0 # load array", 
			new TempList(dst), 
			new TempList(src.base, new TempList(src.index()))
		));
		return null;
	}

	public IR storeIndirect(IR __p, IndirectExpression dst, Temp src) {
		emit(new Assem.OPER("movq %`s0, (%`s1) # store", null, new TempList(src, new TempList(dst.temp))));
		return null;
	}

	public Temp storeindirectWithDisplacement(IR __p, IndirectWithDisplacementExpression dst, Temp src) {
		emit(new Assem.OPER("movq %`s0, " + dst.displacement() + "(%`s1) # store to offset", 
			null, 
			new TempList(src, new TempList(dst.temp()))
		));
		return null;
	}

	public Temp storeindirectWithDisplacementAndScale(IR __p, IndirectWithDisplacementAndScaleExpression dst, Temp src) {
		//TODO : Check the binop operator !
		emit(new Assem.OPER("movq %`s0, (%`s1, %`s2, " + dst.wordSize() + ") # store array", 
			null, 
			new TempList(src, new TempList(dst.base, new TempList(dst.index())))
		));
		return null;
	}

	public IR loadindirect(IR __p, Temp dst, IndirectExpression src) {
		emit(new Assem.MOVE("movq %`s0, %`d0 # load 0", dst, src.temp));
		return null;
	}

	public IR cjumpStatement(IR __p, Temp leftTemp, Temp rightTemp) {
		CJUMP cjump = (CJUMP)__p;
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
		return null;
	}

	public IR sxpStatement(IR __p, Temp arg) {
		//TODO : What to do here ?
		return null;
	}

	public IR labelStatement(IR __p) {
		LABEL op = (LABEL)__p;
		emit(new Assem.LABEL(op.label.toString() + ":", op.label));
		return null;
	}

	public Object seqStatement(IR __p, Object left, Object right) {
		//TODO : What to do here ?
		return null;
	}

	public IR jumpStatement(IR __p, Temp arg0) {
		JUMP op = (JUMP)__p;
		emit(new OPER("jmp `j0", null, null, op.targets));
		return null;
	}

	public Temp nameExpression(IR __p) {
		NAME op = (NAME)__p;
		Temp temp = Temp.create();
     //   emit(new Assem.OPER("movq $" + op.label + ", %`d0 # default name", L(temp, null), null));
		return temp;
	}

	private Temp binOp(BINOP op, Temp leftTemp, Temp rightTemp) {
		Temp temp = Temp.create();
		switch (op.binop) {
			case BINOP.AND:
				emit(new Assem.MOVE("movq %`s0, %`d0 # and lexp -> r", temp, leftTemp));
				emit(new OPER("and %`s0, %`d0", L(temp, null), L(rightTemp, L(temp, null))));
				break;
		//	case BINOP.ARSHIFT:
		//		break;
			case BINOP.DIV:
				emit(new Assem.MOVE("movq %`s0, %`d0 # div r -> rax", IntelFrame.rax, leftTemp));
				emit(new OPER("xor %`s0, %`d0 # div clear bits rdx ", L(IntelFrame.rdx, null), L(IntelFrame.rdx, null)));
				emit(new OPER("idiv %`s0 # div rax * rexp ", L(IntelFrame.rax, L(IntelFrame.rdx, null)), L(rightTemp, L(IntelFrame.rax, null))));
				emit(new Assem.MOVE("movq %`s0, %`d0 # div rax -> r", temp, IntelFrame.rax));
				break;
			case BINOP.LSHIFT:
				break;
			case BINOP.MINUS:
				emit(new Assem.MOVE("movq %`s0, %`d0 # minus lexp -> r", temp, leftTemp));
				emit(new OPER("sub %`s0, %`d0", L(temp, null), L(rightTemp, L(temp, null))));
				break;
			case BINOP.MUL:
				emit(new Assem.MOVE("movq %`s0, %`d0 # imul l -> rax", IntelFrame.rax, leftTemp));
				emit(new OPER("imul %`s0 # imul rax * r ", L(IntelFrame.rax, L(IntelFrame.rdx, null)), L(rightTemp, L(IntelFrame.rax, null))));
				emit(new Assem.MOVE("movq %`s0, %`d0 # imul rax -> t", temp, IntelFrame.rax));
				break;
			case BINOP.OR:
				emit(new Assem.MOVE("movq %`s0, %`d0 # or lexp -> r", temp, leftTemp));
				emit(new OPER("or %`s0, %`d0", L(temp, null), L(rightTemp, L(temp, null))));
				break;
			case BINOP.PLUS:
				emit(new Assem.MOVE("movq %`s0, %`d0 # add lexp -> r", temp, leftTemp));
				emit(new OPER("add %`s0, %`d0", L(temp, null), L(rightTemp, L(temp, null))));
				break;
		//	case BINOP.RSHIFT:
		//		break;
		//	case BINOP.XOR:
		//		break;
			default:
				throw new Error("Unsupported operation");
		}
		return temp;
	}

	public Temp binopExpression(IR __p, Temp left, Temp right) {
		return binOp((BINOP)__p, left, right);
	}

	public Temp binopExpression(IR __p, Temp left, Integer right) {
		Temp temp = Temp.create();
		emit(new Assem.OPER("movq $" + right + ", %`d0 # add lexp -> r", new TempList(temp), null));
		return binOp((BINOP)__p, temp, left);
	}

	public Temp binopExpression(IR __p, Integer left, Temp right) {
		Temp temp = Temp.create();
		emit(new Assem.OPER("movq $" + left + ", %`d0 # add lexp -> r", new TempList(temp), null));
		return binOp((BINOP)__p, temp, right);
	}

	public Temp binopExpression(IR __p, Integer left, Integer right) {
		Temp leftTemp = Temp.create();
		emit(new Assem.OPER("movq $" + left + ", %`d0 # bin(i,i)", new TempList(leftTemp), null));
		Temp rightTemp = Temp.create();
		emit(new Assem.OPER("movq $" + right + ", %`d0 # bin(i,i)", new TempList(rightTemp), null));
		return binOp((BINOP)__p, leftTemp, rightTemp);
	}

	public Temp mem(BinopOffsetExpression boe) {
		Temp temp = Temp.create();
		//TODO: Apply binop operator.
		emit(new Assem.OPER("movq " + boe.offset +  "(%`s0), %`d0 # add lexp -> r", new TempList(temp), new TempList(boe.base)));
		return temp;
	}

	public Temp mem(Integer ic) {
		Temp temp = Temp.create();
		emit(new Assem.OPER("movq (%" + ic + "), %`d0 # mem(int)", new TempList(temp), null));
		return temp;
	}

	public Temp mem(Temp mem) {
        Temp temp = Temp.create();
		emit(new Assem.OPER("movq (%`s0), %`d0 # mem(temp)", new TempList(temp), new TempList(mem)));
		return temp;
	}

	public Object call(IR __p, Vector<Temp> args) {
		int i = 0;
		TempList tl = null;
		TempList pr = IntelFrame.paramRegs;
		//TODO : Yuck !
		Collections.reverse(args);
		for(Temp arg : args) {
			if (pr != null) {
				emit(new Assem.MOVE("movq %`s0, %`d0 # move reg arg " + i + " to temp", pr.head, arg));
				tl = TempList.append(tl, pr);
				pr = pr.tail;
			} else {
				emit(new Assem.OPER("pushq %`s1 # move reg arg " + i + " to stack", null,
						L(IntelFrame.rsp, L(arg, null))));
			}
			++i;
		}
		CALL call = (CALL)__p;
		var name = (NAME) call.func;
		emit(new OPER("call " + name.label + "", IntelFrame.callDefs, tl));
		return null;
	}

	public void setUpFunctionExpression(IR p) {
		System.out.println("prolog");
	}

	public IR storeIndirectWithDisplacement(IR __p, IndirectWithDisplacementExpression arg0, Temp arg1) {
		return null;
	}

	public IR storeIndirectWithDisplacementAndScale(IR __p, IndirectWithDisplacementAndScaleExpression arg0,
			Temp arg1) {
		return null;
	}

	public IR move(IR __p, Temp dst, Integer src) {
		emit(new Assem.OPER("movq $" + src + ", %`d0 # move(t, i)", new TempList(dst), null));
		return null;
	}

	public IR move(IR __p, Temp dst, Temp src) {
		emit(new Assem.MOVE("movq %`s0, %`d0 # move(t, t)", dst, src));
		return null;
	}

	public Temp integerExpression(Integer integerConstant) {
		Temp dst = Temp.create();
		emit(new Assem.OPER("movq $" + integerConstant + ", %`d0 # integerExpression", new TempList(dst), null));
		return dst;
	}
	public Object expCall() {
		return null;
	}

	public Object moveCall() {
		Temp dstTemp = Temp.create();
		emit(new Assem.MOVE("movq %`s0, %`d0 # rax to temp ", dstTemp, IntelFrame.rax));
		return null;
	}
}