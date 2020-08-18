package Intel;

import Temp.Temp;

public class IndirectWithDisplacementAndScaleExpression {
    final Temp base;
    final Temp index;
    final Integer value;

	public IndirectWithDisplacementAndScaleExpression(Temp arg0, Temp arg1, Integer arg2) {
        this.base = arg0;
        this.index = arg1;
        this.value = arg2;
	}
}
