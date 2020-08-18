package Intel;

import Temp.Temp;

public class IndirectWithDisplacementAndScaleExpression {

    final Temp base;
    final BinopOffsetExpression binopOffsetExpression;

	public IndirectWithDisplacementAndScaleExpression(Temp base, BinopOffsetExpression binopOffsetExpression) {
        this.base = base;
        this.binopOffsetExpression = binopOffsetExpression;
	}
}
