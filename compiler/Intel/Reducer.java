package Intel;

import Temp.Temp;
import Tree.IR;
import Tree.TEMP;

public class Reducer {

    final Emitter emitter;

    public Reducer(Emitter emitter) {
		this.emitter = emitter;

    }
	public Temp temp(IR __p) {
		return ((Tree.TEMP)__p).temp;
	}

	public IntegerConstantExpression integerConstant(IR __p) {
		return new IntegerConstantExpression(((Tree.CONST)__p).value);
	}

	public BinopOffsetExpression binopOffsetExpression(IR __p, Temp arg0, IntegerConstantExpression arg1) {
		return null;
	}
	public IndirectWithDisplacementExpression indirectWithDisplacement(IR __p, Temp arg0) {
		return null;
	}
	public IndirectWithDisplacementAndScaleExpression indirectWithDisplacement(IR __p, Temp arg0,
			BinopOffsetExpression arg1) {
		return null;
	}
	public IndirectExpression indirect(IR __p, Temp arg) {
		return null;
	}
	public Temp loadindirect(IR __p, Temp arg0, IndirectExpression arg1) {
		return null;
	}
	public Temp loadindirectWithDisplacement(IR __p, Temp arg0, IndirectWithDisplacementExpression arg1) {
		return null;
	}
	public Temp loadindirectWithDisplacementAndScale(IR __p, Temp arg0,
			IndirectWithDisplacementAndScaleExpression arg1) {
		return null;
	}
	public IndirectWithDisplacementAndScaleExpression indirectWithDisplacementAndScale(IR __p, Temp arg0,
			BinopOffsetExpression arg1) {
		return null;
	}
	public IndirectWithDisplacementExpression indirectWithDisplacement(IR __p, BinopOffsetExpression arg0) {
		return null;
	}


   
}