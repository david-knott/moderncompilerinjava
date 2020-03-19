package Intel;

import Frame.Access;
import Temp.Temp;
import Tree.Exp;
import Tree.TEMP;

class InReg extends Access {
    Temp temp;

    InReg(Temp tmp) {
        temp = tmp;
    }

    @Override
    public Exp exp(Exp framePtr) {
        return new TEMP(temp);
    }
}