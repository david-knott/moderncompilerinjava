package Intel;

import Temp.Temp;

public class IndirectWithDisplacementExpression {

    final Temp temp;
    final IntegerConstantExpression arg1;

	public IndirectWithDisplacementExpression(Temp arg0, IntegerConstantExpression arg1) {
        this.temp = arg0;
        this.arg1 = arg1;
	}
}
