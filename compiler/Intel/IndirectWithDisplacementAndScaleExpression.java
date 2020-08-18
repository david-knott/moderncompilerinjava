package Intel;

import Temp.Temp;

public class IndirectWithDisplacementAndScaleExpression {
    final Temp base;
    final Temp index;
    final IntegerConstantExpression integerConstantExpression;

	public IndirectWithDisplacementAndScaleExpression(Temp arg0, Temp arg1, IntegerConstantExpression arg2) {
        this.base = arg0;
        this.index = arg1;
        this.integerConstantExpression = arg2;
	}
}
