package Intel;

import java.util.Vector;

import Temp.Temp;
import Tree.BINOP;
import Tree.IR;

public class Reducer {

    final Emitter emitter;

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

	public IndirectWithDisplacementAndScaleExpression indirectWithDisplacementAndScale(IR __p, Temp arg0,
			BinopOffsetExpression arg1) {
		return new IndirectWithDisplacementAndScaleExpression(arg0, arg1);
	}

	public IndirectWithDisplacementExpression indirectWithDisplacement(IR __p, BinopOffsetExpression arg0) {
		return new IndirectWithDisplacementExpression(arg0);
	}


	public IR loadindirectWithDisplacement(IR __p, Temp dst, IndirectWithDisplacementExpression arg1) {
		return null;
	}

	public IR loadindirectWithDisplacementAndScale(IR __p, Temp dst,
			IndirectWithDisplacementAndScaleExpression src) {
		return null;
	}

	public IR storeIndirect(IR __p, IndirectExpression dst, Temp src) {
		return null;
	}

	public Temp storeindirectWithDisplacement(IR __p, IndirectWithDisplacementExpression dst, Temp src) {
		return null;
	}

	public Temp storeindirectWithDisplacementAndScale(IR __p, IndirectWithDisplacementAndScaleExpression dst, Temp src) {
		return null;
	}

	public IR loadindirect(IR __p, Temp arg0, IndirectExpression arg1) {
		return null;
	}

	public IR cjumpStatement(IR __p, Temp left, Temp right) {
		return null;
	}

	public IR sxpStatement(IR __p, Temp arg) {
		return null;
	}

	public IR labelStatement(IR __p) {
		return null;
	}

	public Object seqStatement(IR __p, Object left, Object right) {
		return null;
	}

	public IR jumpStatement(IR __p, Temp arg0) {
		return null;
	}

	public IR nameExpression(IR __p) {
		return null;
	}

	public Temp binopExpression(IR __p, Temp left, Temp right) {
		return Temp.create();
	}

	public Temp binopExpression(IR __p, Temp left, IR right) {
		return Temp.create();
	}

	public Temp binopExpression(IR __p, IR left, Temp right) {
		return Temp.create();
	}

	public Temp binopExpression(IR __p, IR left, IR right) {
		return Temp.create();
	}

	public Temp mem(BinopOffsetExpression boe) {
		return Temp.create();
	}

	public Temp mem(Integer ic) {
		return Temp.create();
	}

	public Temp mem(Temp e) {
		return Temp.create();
	}

	public IR call(IR __p, Vector<Temp> args) {
		//reduced args ??
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
		return null;
	}

	public IR move(IR __p, Temp dst, Temp src) {
		return null;
	}
}